package com.prajjaval.urlshortener.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prajjaval.urlshortener.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        });

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Validation Failed");
        response.put("path", request.getRequestURI());
        response.put("errors", validationErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("Not Found");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(ExpiredUrlException.class)
    public ResponseEntity<ErrorResponse> handleExpiredUrlException(
            ExpiredUrlException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.GONE.value());
        errorResponse.setError("Gone");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(errorResponse);
    }
}