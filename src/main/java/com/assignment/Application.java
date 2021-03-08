package com.assignment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {

    private final static Logger LOG = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            var fileSupport = new FileSupport(new DefaultParser());
            var errors = fileSupport.readFile("application.log");
            fileSupport.writeToJsonFile(errors);
        } catch (RuntimeException e) {
            LOG.error("An error was thrown", e);
        }
    }
}
