package com.assignment.entities;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@ToString
public class LogLine {
    private final LocalDateTime timestamp;
    private final String className;
    private final String message;
}


