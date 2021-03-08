package com.assignment.entities;

import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder
@ToString
public class ErrorLogLine extends LogLine {
    private final Collection<LogLine> previousLogs;
}


