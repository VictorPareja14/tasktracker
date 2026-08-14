package com.enterprise.tasktracker.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String tarea, Long id) {
        super(tarea + " (ID: " + id + ")");
    }
}
