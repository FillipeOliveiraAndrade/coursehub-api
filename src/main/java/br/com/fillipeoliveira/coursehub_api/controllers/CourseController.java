package br.com.fillipeoliveira.coursehub_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.services.CourseService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/courses")
public class CourseController {
  
  @Autowired
  private CourseService courseService;

  @PostMapping
  public ResponseEntity<Course> saveCourse(@Valid @RequestBody Course course) {
    Course result = this.courseService.create(course);
    return ResponseEntity.ok().body(result);
  }

  @GetMapping()
  public ResponseEntity<List<Course>> findCoursesByNameAndCategory(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String category
  ) {
    List<Course> result = this.courseService.findCoursesWithFiltersOrNot(name, category);
    return ResponseEntity.ok().body(result);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Course> updatingCourseById(@PathVariable UUID id, @RequestBody Course course) {
    Course result = this.courseService.update(id, course);
    return ResponseEntity.ok().body(result);
  }

  @PatchMapping("/{id}/active")
  public ResponseEntity<Course> updatingActiveOfCourseById(
    @PathVariable UUID id,
    @RequestBody boolean active
  ) {
    Course result = this.courseService.updateActive(id, active);
    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable UUID id) {
    this.courseService.delete(id);
    return ResponseEntity.ok().build();
  }
}
