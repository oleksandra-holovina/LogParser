package com.assignment;

import com.assignment.entities.ErrorLogLine;
import com.assignment.entities.LogLine;
import com.assignment.entities.Errors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.assignment.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultParserTest {

    private final Parser parser = new DefaultParser();

    @Test
    @DisplayName("Logs contain error and previous messages within given time")
    void parseLogsWithError() {
        var logs = Stream.of(NON_ERROR_LOG_EARLIER, NON_ERROR_LOG, NON_ERROR_LOG, ERROR_LOG, NON_ERROR_LOG);
        var expected = createErrors();
        assertThat(parser.parse(logs, 0)).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("Logs contain error but starting with fourth line")
    void parseLogsWithErrorStartingWith() {
        var logs = Stream.of(NON_ERROR_LOG, NON_ERROR_LOG, ERROR_LOG, NON_ERROR_LOG, NON_ERROR_LOG);
        var expected = emptyErrors();
        assertThat(parser.parse(logs, 3)).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("Logs don't contain error")
    void parseLogsWithoutError() {
        var logs = Stream.of(NON_ERROR_LOG, NON_ERROR_LOG, NON_ERROR_LOG);
        var expected = emptyErrors();
        assertThat(parser.parse(logs, 0)).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {LOG_WITHOUT_DATE, LOG_WITHOUT_CLASSNAME, LOG_WITHOUT_MESSAGE})
    @DisplayName("Log line doesn't contain required parameter")
    void parseLogsWhenParamIsMissing() {
        var logs = Stream.of(LOG_WITHOUT_MESSAGE);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(logs, 0));
    }

    @Test
    @DisplayName("Log line contains invalid date format")
    void parseLogsWithInvalidDateFormat() {
        var logs = Stream.of(LOG_INVALID_DATE);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(logs, 0));
    }

    private Errors emptyErrors() {
        return Errors.builder().errors(Collections.emptyList()).build();
    }

    private Errors createErrors() {
        var previousLogs = LogLine.builder()
                .timestamp(LocalDateTime.of(2020, 4, 1, 10, 10, 8))
                .message("Job started")
                .className("ServiceClient")
                .build();

        var details = ErrorLogLine.builder()
                .timestamp(LocalDateTime.of(2020, 4, 1, 10, 10, 9))
                .message("Error while processing row - service returned status code 500")
                .className("ServiceClient")
                .previousLogs(List.of(previousLogs))
                .build();

        return Errors.builder()
                .errorCount(1)
                .errors(List.of(details))
                .build();
    }
}
