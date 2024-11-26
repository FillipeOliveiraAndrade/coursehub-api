package br.com.fillipeoliveira.coursehub_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class DeleteCourseServiceTest {

  @InjectMocks
  private CourseService courseService;

  @Mock
  private CourseRepository courseRepository;

  @Test
  @DisplayName("Should throw CourseNotFoundException when the course does not exist")
  public void shouldThrowExceptionWhenCourseDoesNotExist() {
    Exception exception = assertThrows(CourseNotFoundException.class, () -> {
      this.courseService.delete(UUID.randomUUID());
    });

    assertEquals("Course not found.", exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should delete the course when it exists")
  public void shouldDeleteCourseWhenExists() {
    UUID id = TestUtils.generateId();

    when(this.courseRepository.existsById(id)).thenReturn(true);

    this.courseService.delete(id);

    verify(this.courseRepository).deleteById(id);
    verify(this.courseRepository, never()).findById(any());
  }
}
