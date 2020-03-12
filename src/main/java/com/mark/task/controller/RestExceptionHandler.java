package com.mark.task.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.mark.task.controller.exception.NotValidImageException;
import com.mark.task.service.exception.RepeatImageException;
import com.mark.task.storage.exception.AmazonResourceStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<ErrorResponse> handleAmazonS3Exception(AmazonS3Exception exception) {
        return new ResponseEntity<>(getObjectErrorResponse(exception.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AmazonResourceStorageException.class)
    public ResponseEntity<ErrorResponse> handleContentException(AmazonResourceStorageException exception) {
        return new ResponseEntity<>(getObjectErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidImageException.class)
    public ResponseEntity<ErrorResponse> handleNotPngException(NotValidImageException exception) {
        return new ResponseEntity<>(getObjectErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepeatImageException.class)
    public ResponseEntity<ErrorResponse> handleRepeatImageException(RepeatImageException exception) {
        return new ResponseEntity<>(getObjectErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    private ErrorResponse getObjectErrorResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addError(message);
        return errorResponse;
    }
}
