package br.com.fillipeoliveira.coursehub_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class FilterCourseServiceTest {
  
  @InjectMocks
  private CourseService courseService;

  @Mock
  private CourseRepository courseRepository;

  @Test
  @DisplayName("Should return all courses when no filters are provided")
  public void shouldReturnAllCoursesWhenNoFiltersProvided() {
    List<Course> mockCourses = List.of(
      TestUtils.mockCourse(TestUtils.generateId(), "course1", "category1", true),
      TestUtils.mockCourse(TestUtils.generateId(), "course2", "category2", false)
    );

    when(this.courseRepository.findAll()).thenReturn(mockCourses);

    List<Course> result = this.courseService.findCoursesWithFiltersOrNot(null, null);

    assertEquals(2, result.size());
    
    verify(this.courseRepository).findAll();
    verifyNoMoreInteractions(this.courseRepository);
  }

  @Test
  @DisplayName("Should filter courses by name only")
  public void shouldFilterCoursesByNameOnly() {
    List<Course> mockCourse = List.of(
      TestUtils.mockCourse(TestUtils.generateId(), "course1", "category1", true)
    );

    when(this.courseRepository.findByNameOrCategoryIgnoreCase("course1", null))
      .thenReturn(mockCourse);

    List<Course> result = this.courseService.findCoursesWithFiltersOrNot("course1", null);

    assertEquals(1, result.size());
    assertEquals("course1", result.get(0).getName());

    verify(this.courseRepository).findByNameOrCategoryIgnoreCase("course1", null);
    verifyNoMoreInteractions(this.courseRepository);
  }

  @Test
  @DisplayName("Should filter courses by category only")
  public void shouldFilterCoursesByCategoryOnly() {
    List<Course> mockCourse = List.of(
      TestUtils.mockCourse(TestUtils.generateId(), "course1", "category1", true)
    );

    when(this.courseRepository.findByNameOrCategoryIgnoreCase(null, "category1"))
      .thenReturn(mockCourse);

    List<Course> result = this.courseService.findCoursesWithFiltersOrNot(null, "category1");

    assertEquals(1, result.size());
    assertEquals("category1", result.get(0).getCategory());

    verify(this.courseRepository).findByNameOrCategoryIgnoreCase(null, "category1");
    verifyNoMoreInteractions(this.courseRepository);
  }

  @Test
  @DisplayName("Should filter courses by both name and category")
  public void shouldFilterCoursesByNameAndCategory() {
    Course mockCourse = TestUtils.mockCourse(
      TestUtils.generateId(), "course1", "category1", true
    );
  
    when(this.courseRepository.findByNameAndCategoryIgnoreCase("course1", "category1"))
      .thenReturn(Optional.of(mockCourse));
  
    List<Course> result = this.courseService.findCoursesWithFiltersOrNot("course1", "category1");
  
    assertEquals(1, result.size(), "The size of the returned list should match.");
    assertEquals("course1", result.get(0).getName(), "The course name should match the filter.");
    assertEquals("category1", result.get(0).getCategory(), "The course category should match the filter.");
    
    verify(this.courseRepository).findByNameAndCategoryIgnoreCase("course1", "category1");
    verifyNoMoreInteractions(this.courseRepository);
  }

  @Test
  @DisplayName("Should return an empty list when no course matches the filters")
  public void shouldReturnEmptyListWhenNoMatchesFound() {
    when(this.courseRepository.findByNameAndCategoryIgnoreCase("nonexistent", "nonexistent"))
      .thenReturn(Optional.empty());

    List<Course> result = this.courseService.findCoursesWithFiltersOrNot("nonexistent", "nonexistent");

    assertTrue(result.isEmpty(), "The returned list should be empty.");
    verify(this.courseRepository).findByNameAndCategoryIgnoreCase("nonexistent", "nonexistent");
    verifyNoMoreInteractions(this.courseRepository);
  }
}
