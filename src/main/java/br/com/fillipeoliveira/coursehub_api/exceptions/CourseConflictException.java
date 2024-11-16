package br.com.fillipeoliveira.coursehub_api.exceptions;

public class CourseConflictException extends RuntimeException {
  public CourseConflictException(String message) {
    super(message);
  }
}
