package br.com.fillipeoliveira.coursehub_api.exceptions;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException(String message) {
    super(message);
  }
}
