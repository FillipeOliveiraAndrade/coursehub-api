package br.com.fillipeoliveira.coursehub_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalControllerAdvice {
  public ResponseEntity<String> handleExceptionResponse(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
