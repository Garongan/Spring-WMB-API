package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> responseStatusExceptionHandler(HttpStatus httpStatus, ResponseStatusException e) {
        return errorResponse(e.getMessage(), httpStatus.value());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> handleConflictException() {
        return errorResponse(StatusMessage.CONFLICT, HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler({ConstraintViolationException.class, IOException.class, JsonParseException.class, JsonMappingException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> constraintViolationHandler(ConstraintViolationException e) {
        return errorResponse(StatusMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ SQLException.class, DataAccessException.class, RuntimeException.class })
    public ResponseEntity<?> handleSQLException() {
        return errorResponse(StatusMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredential(){
        return errorResponse(StatusMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDenied(){
        return errorResponse(StatusMessage.ACCESS_DENIED, HttpStatus.FORBIDDEN.value());
    }

    private ResponseEntity<CommonResponse<?>> errorResponse(String message, Integer httpStatus) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(httpStatus)
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
