package br.com.fillipeoliveira.coursehub_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;

@Service
public class CourseService {
  
  @Autowired
  private CourseRepository courseRepository;

  public Course create(Course course) {
    return this.courseRepository.save(course);
  }
}
