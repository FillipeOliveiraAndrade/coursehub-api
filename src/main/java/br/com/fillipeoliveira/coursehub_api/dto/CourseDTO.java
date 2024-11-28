package br.com.fillipeoliveira.coursehub_api.dto;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CourseDTO(
  @NotBlank(message = "Course name must not be blank.")
  @Schema(example = "Curso de Java", requiredMode = RequiredMode.REQUIRED)
  String name,

  @NotBlank(message = "Course category must not be blank.")
  @Schema(example = "Back-End", requiredMode = RequiredMode.REQUIRED)
  String category,

  @Schema(example = "true", requiredMode = RequiredMode.REQUIRED)
  Boolean active
) {
  
  public CourseDTO(String name, String category) {
    this(name, category, true);
  }

  public Course toEntity() {
    boolean isActive = active != null ? active : true;

    Course course = Course.builder()
        .name(this.name)
        .category(this.category)
        .active(isActive)
        .build();

    return course;
  }
}