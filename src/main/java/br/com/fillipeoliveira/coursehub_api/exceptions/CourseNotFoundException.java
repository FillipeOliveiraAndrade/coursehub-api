package br.com.fillipeoliveira.coursehub_api.exceptions;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Course not found.");
  }
}
