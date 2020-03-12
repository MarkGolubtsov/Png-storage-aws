package com.mark.task.controller.exception;

public class NotValidImageException extends RuntimeException {
    public NotValidImageException(String message) {
        super(message);
    }
}
