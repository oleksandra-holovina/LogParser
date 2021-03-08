package com.assignment.entities;

import lombok.Builder;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
public class Errors {
    private final int errorCount;
    private final List<LogLine> errors;
}
