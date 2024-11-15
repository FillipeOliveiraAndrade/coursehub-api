package br.com.fillipeoliveira.coursehub_api.models.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;

public interface CourseRepository extends JpaRepository<Course, UUID> {
  
}
