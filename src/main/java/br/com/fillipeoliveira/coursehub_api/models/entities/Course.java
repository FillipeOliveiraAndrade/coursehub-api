package br.com.fillipeoliveira.coursehub_api.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "O campo [name] não pode estar nulo")
  private String name;

  @NotBlank(message = "O campo [category] não pode estar nulo")
  private String category;

  private boolean active;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
