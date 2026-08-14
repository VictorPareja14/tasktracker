package com.enterprise.tasktracker.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Long id) {
        super(message + " (ID: " + id + ")");
    }

}