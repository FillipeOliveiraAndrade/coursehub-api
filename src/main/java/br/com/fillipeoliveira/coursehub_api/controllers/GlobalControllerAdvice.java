package br.com.fillipeoliveira.coursehub_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({
      CourseConflictException.class,
      IllegalArgumentException.class
    })
    public ResponseEntity<String> handleExceptionResponseBadResquest(RuntimeException exception) {
      return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({
      CourseNotFoundException.class,
    })
    public ResponseEntity<String> handleExceptionResponseNotFound(RuntimeException exception) {
      return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach(error -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName, errorMessage);
      });
      
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
