package com.mobalpa.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
      WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("message", "Invalid request: " + ex.getLocalizedMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put("message", "An unexpected error occurred: " + ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}