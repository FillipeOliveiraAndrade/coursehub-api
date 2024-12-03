package br.com.fillipeoliveira.coursehub_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fillipeoliveira.coursehub_api.dto.CourseDTO;
import br.com.fillipeoliveira.coursehub_api.dto.CourseUpdateDTO;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(description = "Essa função é responsável por adicionar novos cursos ao banco de dados.")
  public ResponseEntity<Course> saveCourse(@Valid @RequestBody CourseDTO courseDTO) {
    Course result = this.courseService.create(courseDTO.toEntity());
    return ResponseEntity.ok().body(result);
  }

  @GetMapping()
  @Operation(description = "Essa função é responsável por filtrar e buscar os cursos.")
  public ResponseEntity<List<Course>> findCoursesByNameAndCategory(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String category
  ) {
    List<Course> result = this.courseService.findCoursesWithFiltersOrNot(name, category);
    return ResponseEntity.ok().body(result);
  }

  @PutMapping("/{id}")
  @Operation(description = "Essa função é responsável por atualizar um curso baseado em seu ID.")
  public ResponseEntity<Course> updatingCourseById(@PathVariable UUID id, @RequestBody CourseUpdateDTO courseUpdateDTO) {
    Course result = this.courseService.update(id, courseUpdateDTO.toEntity());
    return ResponseEntity.ok().body(result);
  }

  @PatchMapping("/{id}/active")
  @Operation(description = "Essa função é responsável atualizar se um curso está ativo ou não.")
  public ResponseEntity<Course> updatingActiveOfCourseById(
    @PathVariable UUID id,
    @RequestBody boolean active
  ) {
    Course result = this.courseService.updateActive(id, active);
    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping("/{id}")
  @Operation(description = "Essa função é responsável por apagar um curso do banco de dados.")
  public ResponseEntity<Void> deleteCourseById(@PathVariable UUID id) {
    this.courseService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
