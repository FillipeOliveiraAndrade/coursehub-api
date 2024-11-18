package br.com.fillipeoliveira.coursehub_api.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;

@Service
public class CourseService {
  
  @Autowired
  private CourseRepository courseRepository;

  public Course create(Course newCourse) {
    Optional<Course> course = this.courseRepository.findByNameAndCategory(newCourse.getName(), newCourse.getCategory());

    if (course.isPresent()) {
      throw new CourseConflictException("Este curso já existe em nosso banco de dados.");
    }

    return this.courseRepository.save(newCourse);
  }

  public List<Course> findCoursesWithFiltersOrNot(String name, String category) {
    if (name == null && category == null) return this.courseRepository.findAll();
    if (name == null) return this.courseRepository.findByNameOrCategory(null, category); // Filtrando apenas pela category
    if (category == null) return this.courseRepository.findByNameOrCategory(name, null); // Filtrando apenas pelo nome

    Optional<Course> courses = this.courseRepository.findByNameAndCategory(name, category);
    return courses.map(Collections::singletonList).orElse(Collections.emptyList()); // convertendo o optional para list (reaproveitando a função findByNameAndCategory)
  }
}
