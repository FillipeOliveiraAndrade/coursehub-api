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

  public Course update(UUID id, Course courseModified) {
    Course existingCourse = this.courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException("Curso não encontrado."));

    if (courseModified == null) {
        throw new IllegalArgumentException("Dados do curso para modificação não podem ser nulos.");
    }

    if (courseModified.getName() != null) {
        existingCourse.setName(courseModified.getName());
    }

    if (courseModified.getCategory() != null) {
        existingCourse.setCategory(courseModified.getCategory());
    }

    return this.courseRepository.save(existingCourse);
  }

  public Course updateActive(UUID id, boolean active) {
    Course existingCourse = this.courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException("Curso não encontrado."));

    existingCourse.setActive(active);

    return this.courseRepository.save(existingCourse);
  }

  public void delete(UUID id) {
    if (!this.courseRepository.existsById(id)) {
      throw new CourseNotFoundException("Curso não encontrado.");
    }

    this.courseRepository.deleteById(id);
  }
}
