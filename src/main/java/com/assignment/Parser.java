package com.assignment;

import com.assignment.entities.Errors;

import java.util.stream.Stream;

public interface Parser {
    Errors parse(Stream<String> logLines, int startingRow);
}
