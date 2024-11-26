package br.com.fillipeoliveira.coursehub_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class CreateCourseServiceTest {
  
  @InjectMocks
  private CourseService courseService;

  @Mock
  private CourseRepository courseRepository;

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
    UUID id = TestUtils.generateId();
    Course courseCreated = TestUtils.mockCourse(id, "testName", "testCategory", true);

    when(this.courseRepository.save(any(Course.class))).thenReturn(courseCreated);

    Course course = Course.builder().name("testName").category("testCategory").build();
    Course result = this.courseService.create(course);

    assertNotNull(result.getId(), "The created course ID should not be null.");
    assertEquals("testName", result.getName(), "The course name should match the input.");
    assertEquals("testCategory", result.getCategory(), "The course category should match the input.");
    assertTrue(result.isActive(), "The course should be active by default.");
  }
}
