package com.assignment;

import com.assignment.entities.Errors;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FileSupport {

    private static final String outputPath = "errors.json";

    private final Parser parser;

    public FileSupport(Parser parser) {
        this.parser = parser;
    }

    public Errors readFile(String filename) {
        return readFileInternally(filename, 0);
    }

    public Errors readFile(String filename, int startingRow) {
        return readFileInternally(filename, startingRow);
    }

    public Errors readFileInternally(String filename, int startingRow) {
        return getPath(filename).map(path -> {
            try (var lines = Files.lines(path)) {
                return parser.parse(lines, startingRow);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Couldn't read the file %s", filename), e);
            }
        }).orElseThrow(() -> new RuntimeException(String.format("File %s wasn't found", filename)));
    }

    public void writeToJsonFile(Errors errors) {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME)))
                .create();

        var errorString = gson.toJson(errors);
        try {
            Files.write(Paths.get(outputPath), errorString.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Couldn't write to file %s", outputPath), e);
        }
    }

    private static Optional<Path> getPath(String filename) {
        try {
            return Optional.of(Paths.get(ClassLoader.getSystemResource(filename).toURI()));
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }
}
