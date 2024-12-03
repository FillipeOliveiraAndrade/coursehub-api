package br.com.fillipeoliveira.coursehub_api.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;

@Service
public class CourseService {
  
  @Autowired
  private CourseRepository courseRepository;

  public Course create(Course newCourse) {
    Optional<Course> course = this.courseRepository.findByNameAndCategoryIgnoreCase(newCourse.getName(), newCourse.getCategory());

    if (course.isPresent()) {
      throw new CourseConflictException("This course already exists in our database.");
    }

    return this.courseRepository.save(newCourse);
  }

  public List<Course> findCoursesWithFiltersOrNot(String name, String category) {
    if (name == null && category == null) return this.courseRepository.findAll();
    if (name == null) return this.courseRepository.findByNameOrCategoryIgnoreCase(null, category); // Filtrando apenas pela category
    if (category == null) return this.courseRepository.findByNameOrCategoryIgnoreCase(name, null); // Filtrando apenas pelo nome

    Optional<Course> courses = this.courseRepository.findByNameAndCategoryIgnoreCase(name, category);
    return courses.map(Collections::singletonList).orElse(Collections.emptyList()); // convertendo o optional para list (reaproveitando a função findByNameAndCategory)
  }

  public Course update(UUID id, Course courseModified) {
    if (
        courseModified.getName() == null && courseModified.getCategory() == null) {
      throw new IllegalArgumentException("Course data for modification cannot be null.");
    }

    Course existingCourse = this.courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException());

    if (courseModified.getName() != null) {
      existingCourse.setName(courseModified.getName());
    }

    if (courseModified.getCategory() != null) {
      existingCourse.setCategory(courseModified.getCategory());
    }

    return this.courseRepository.save(existingCourse);
  }

  public Course updateActive(UUID id, boolean active) {
    Course course = this.courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException());

    if (course.isActive() == active) {
      return course;
    }

    course.setActive(active);

    return this.courseRepository.save(course);
  }

  public void delete(UUID id) {
    if (!this.courseRepository.existsById(id)) {
      throw new CourseNotFoundException();
    }

    this.courseRepository.deleteById(id);
  }
}
