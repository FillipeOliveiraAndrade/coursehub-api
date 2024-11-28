package br.com.fillipeoliveira.coursehub_api.utils;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fillipeoliveira.coursehub_api.models.entities.Course;

public class TestUtils {
  public static UUID generateId() {
    return UUID.randomUUID();
  }

  public static Course mockCourse(UUID id, String name, String category, boolean active) {
    return Course.builder().id(id).name(name).category(category).active(active).build();
  }

  public static String objectToJson(Object object){
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(object);
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}
