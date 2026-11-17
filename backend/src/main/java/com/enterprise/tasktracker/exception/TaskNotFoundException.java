package com.enterprise.tasktracker.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Tarea no encontrada con id: " + id);
    }
}