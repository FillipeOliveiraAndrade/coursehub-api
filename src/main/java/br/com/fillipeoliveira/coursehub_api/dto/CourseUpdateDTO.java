package br.com.fillipeoliveira.coursehub_api.dto;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CourseUpdateDTO(
  @Schema(example = "Curso de React Native")
  String name,

  @Schema(example = "front-end")
  String category
) {
  
  public Course toEntity() {
    Course course = Course.builder()
        .name(this.name)
        .category(this.category)
        .build();

    return course;
  }
}
