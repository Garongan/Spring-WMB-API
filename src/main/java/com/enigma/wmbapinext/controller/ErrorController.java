package com.enigma.wmbapinext.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> responseStatusExceptionHandler(HttpStatus httpStatus, ResponseStatusException e) {
        return errorResponse(httpStatus, e);
    }

    private ResponseEntity<?> errorResponse(HttpStatus httpStatus, Exception e) {
        return ResponseEntity.status(httpStatus.value()).body(e.getMessage());
    }
}
