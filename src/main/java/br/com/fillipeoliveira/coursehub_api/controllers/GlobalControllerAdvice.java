package br.com.fillipeoliveira.coursehub_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFound;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler({
    CourseConflictException.class,
    CourseNotFound.class
  })
  public ResponseEntity<String> handleExceptionResponse(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
