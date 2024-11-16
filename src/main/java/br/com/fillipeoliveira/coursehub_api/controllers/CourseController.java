package br.com.fillipeoliveira.coursehub_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.services.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/courses")
public class CourseController {
  
  @Autowired
  private CourseService courseService;

  @PostMapping
  public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
    Course result = this.courseService.create(course);
    return ResponseEntity.ok().body(result);
  }

  @GetMapping
  public ResponseEntity<List<Course>> findAllCourses() {
    List<Course> courses = this.courseService.findAll();
    return ResponseEntity.ok().body(courses);
  }
  
}
