package br.com.fillipeoliveira.coursehub_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class UpdateCourseServiceTest {

  @InjectMocks
  private CourseService courseService;

  @Mock
  private CourseRepository courseRepository;

  @Test
  @DisplayName("Should throw an error when updating a non-existent course")
  public void shouldThrowWhenUpdatingNonExistentCourse() {
    Course mockCourse = Course.builder()
        .name("TEST_NAME")
        .category("TEST_CATEGORY")
        .build();

    Exception exception = assertThrows(CourseNotFoundException.class, () -> {
      this.courseService.update(null, mockCourse);
    });

    assertEquals("Course not found.", exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should throw an error when course data for update is null")
  public void shouldThrowWhenUpdateDataIsNull() {
    String messageErrorExpected = "Course data for modification cannot be null.";

    Course courseNull = Course.builder().build();

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      this.courseService.update(null, courseNull);
    });

    assertEquals(messageErrorExpected, exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should update course data successfully")
  public void shouldUpdateCourseSuccessfully() {
    UUID id = TestUtils.generateId();
    Course course = TestUtils.mockCourse(id, "old name", "old category", false);
    Course courseUpdated = TestUtils.mockCourse(id, "updated name", "updated category", true);

    when(this.courseRepository.findById(id)).thenReturn(Optional.of(course));
    when(this.courseRepository.save(any(Course.class))).thenReturn(courseUpdated);

    Course result = this.courseService.update(id, courseUpdated);

    assertNotNull(result, "The returned course should not be null.");
    assertEquals(courseUpdated.getName(), result.getName(), "The course name should be updated.");
    assertEquals(courseUpdated.getCategory(), result.getCategory(), "The course category should be updated.");
    assertEquals(courseUpdated.getId(), result.getId(), "The course ID should remain unchanged.");
  }

  @Test
  @DisplayName("Should throw an error when updating status of non-existent course")
  public void shouldThrowWhenUpdatingStatusOfNonExistentCourse() {
    Exception exception = assertThrows(CourseNotFoundException.class, () -> {
      this.courseService.updateActive(null, false);
    });

    assertEquals("Course not found.", exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should update course status successfully")
  public void shouldUpdateCourseStatusSuccessfully() {
    UUID id = TestUtils.generateId();
    Course course = TestUtils.mockCourse(id, "test", "category", false);
    Course courseUpdated =TestUtils. mockCourse(id, "test", "category", true);

    when(this.courseRepository.findById(id)).thenReturn(Optional.of(course));
    when(this.courseRepository.save(any(Course.class))).thenReturn(courseUpdated);

    Course result = this.courseService.updateActive(id, true);

    assertNotNull(result, "The course returned should not be null.");
    assertTrue(result.isActive(), "The status 'active' should be updated to 'true'.");

    verify(this.courseRepository).save(argThat(updatedCourse -> 
      updatedCourse.getId().equals(id) && updatedCourse.isActive()
    ));

    verify(this.courseRepository).findById(id);
  }

  @Test
  @DisplayName("Should not make changes if the course is already in the desired state")
  public void shouldNotChangeStateIfAlreadyActive() {
    UUID id = TestUtils.generateId();
    Course course = TestUtils.mockCourse(id, "test", "category", true);

    when(this.courseRepository.findById(id)).thenReturn(Optional.of(course));

    Course result = this.courseService.updateActive(id, true);

    assertNotNull(result, "The course should still be returned.");
    assertTrue(result.isActive(), "The course should remain active.");
    
    verify(this.courseRepository, never()).save(any());
  }
}
