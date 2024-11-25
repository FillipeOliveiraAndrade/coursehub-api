package br.com.fillipeoliveira.coursehub_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

  @InjectMocks
  private CourseService courseService;

  @Mock
  private CourseRepository courseRepository;

  private UUID generateId() {
    return UUID.randomUUID();
  }

  private Course mockCourse(UUID id, String name, String category, boolean active) {
    return Course.builder().id(id).name(name).category(category).active(active).build();
  }

  @Test
  @DisplayName("Should not allow creating duplicate courses by name and category")
  public void shouldNotCreateCourseWithDuplicateNameAndCategory() {
    final String COURSE_NAME = "name";
    final String COURSE_CATEGORY = "category";

    when(this.courseRepository.findByNameAndCategoryIgnoreCase(COURSE_NAME, COURSE_CATEGORY))
        .thenReturn(Optional.of(new Course()));

    Course duplicateCourse = Course.builder()
        .name(COURSE_NAME)
        .category(COURSE_CATEGORY)
        .build();

    Exception exception = assertThrows(CourseConflictException.class, () -> {
      this.courseService.create(duplicateCourse);
    });

    assertEquals(
        "This course already exists in our database.",
        exception.getMessage(),
        "The exception message should match the expected conflict message."
    );
  }

  @Test
  @DisplayName("Should create a new course successfully")
  public void shouldCreateNewCourseSuccessfully() {
    UUID id = generateId();
    Course courseCreated = mockCourse(id, "testName", "testCategory", true);

    when(this.courseRepository.save(any(Course.class))).thenReturn(courseCreated);

    Course course = Course.builder().name("testName").category("testCategory").build();
    Course result = this.courseService.create(course);

    assertNotNull(result.getId(), "The created course ID should not be null.");
    assertEquals("testName", result.getName(), "The course name should match the input.");
    assertEquals("testCategory", result.getCategory(), "The course category should match the input.");
    assertTrue(result.isActive(), "The course should be active by default.");
  }

  @Test
  @DisplayName("Should throw an error when updating a non-existent course")
  public void shouldThrowWhenUpdatingNonExistentCourse() {
    Exception exception = assertThrows(CourseNotFoundException.class, () -> {
      this.courseService.update(null, null);
    });

    assertEquals("Course not found.", exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should throw an error when course data for update is null")
  public void shouldThrowWhenUpdateDataIsNull() {
    UUID id = generateId();
    Course mockCourse = mockCourse(id, "test", "category", false);

    when(this.courseRepository.findById(id)).thenReturn(Optional.of(mockCourse));

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      this.courseService.update(id, null);
    });

    assertEquals("Course data for modification cannot be null.", exception.getMessage(), "The exception message should match.");
  }

  @Test
  @DisplayName("Should update course data successfully")
  public void shouldUpdateCourseSuccessfully() {
    UUID id = generateId();
    Course course = mockCourse(id, "old name", "old category", false);
    Course courseUpdated = mockCourse(id, "updated name", "updated category", true);

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
    UUID id = generateId();
    Course course = mockCourse(id, "test", "category", false);
    Course courseUpdated = mockCourse(id, "test", "category", true);

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
}
