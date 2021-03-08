package com.assignment;

import com.assignment.entities.ErrorLogLine;
import com.assignment.entities.Errors;
import com.assignment.entities.LogLine;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultParser implements Parser {

    private static final String DATE_FORMAT = "yyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final Duration LOGS_WITHIN_SECONDS = Duration.ofSeconds(5);

    public Errors parse(Stream<String> logLines, int startingRow) {
        TreeMap<LocalDateTime, LogLine> lastLogs = new TreeMap<>();

        var logs = logLines
                .skip(startingRow)
                .map(line -> toLogLine(line, lastLogs))
                .filter(i -> i instanceof ErrorLogLine)
                .collect(Collectors.toList());

        return toErrors(logs);
    }

    private static Errors toErrors(List<LogLine> errorDetails) {
        return Errors.builder()
                .errorCount(errorDetails.size())
                .errors(errorDetails)
                .build();
    }

    private static LogLine toLogLine(String logLine, TreeMap<LocalDateTime, LogLine> lastLogs) {
        var matcher = match(logLine);
        if (matcher.find()) {
            LocalDateTime timestamp = parseDate(matcher.group(1));
            String className = matcher.group(2);
            String message = matcher.group(3);

            Optional<String> isError = containsError(message);
            Collection<LogLine> logLines = isError
                    .map(errorLog -> lastLogs.subMap(timestamp.minus(LOGS_WITHIN_SECONDS), true, timestamp, false).values())
                    .orElse(Collections.emptyList());

            LogLine log;
            if (isError.isEmpty()) {
                log = LogLine.builder()
                        .timestamp(timestamp)
                        .className(className)
                        .message(message)
                        .build();
            } else {
                log = ErrorLogLine.builder()
                        .timestamp(timestamp)
                        .className(className)
                        .message(message)
                        .previousLogs(logLines)
                        .build();
            }

            lastLogs.put(log.getTimestamp(), log);
            return log;
        }

        throw new IllegalArgumentException("Log line doesn't correspond to the given format: timestamp classname - message");
    }

    private static Optional<String> containsError(String logLine) {
        return Stream.of(logLine.split("\\s"))
                .filter(word -> StringUtils.getLevenshteinDistance(word, "error") <= 1)
                .findAny();
    }

    private static LocalDateTime parseDate(String date) {
        try {
            return LocalDateTime.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("Log line contains date (%s) in invalid format", date));
        }
    }

    private static Matcher match(String logLine) {
        var pattern = "^(.*? .*?) (.*?) - (.*?)$";
        var compiled = Pattern.compile(pattern);
        return compiled.matcher(logLine);
    }
}
