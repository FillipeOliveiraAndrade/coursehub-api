package br.com.fillipeoliveira.coursehub_api.services;

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

  public List<Course> findAll() {
    List<Course> courses = this.courseRepository.findAll();
    return courses;
  }
}
