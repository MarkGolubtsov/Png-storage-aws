package com.mark.task.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<String> errors = new ArrayList<>();

    public void addError(String s) {
        errors.add(s);
    }

    public List<String> getErrors() {
        return errors;
    }

    public ErrorResponse setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ErrorResponse setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
