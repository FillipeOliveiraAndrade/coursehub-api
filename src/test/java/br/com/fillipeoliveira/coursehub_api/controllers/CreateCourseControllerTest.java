package br.com.fillipeoliveira.coursehub_api.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.fillipeoliveira.coursehub_api.dto.CourseDTO;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseConflictException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateCourseControllerTest {
  
  private MockMvc mvc;
 
  @Autowired
  private WebApplicationContext context;

  @Autowired
  private CourseRepository courseRepository;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  @DisplayName("Should create a new course successfully")
  public void shouldCreateCourseSuccessfully() throws Exception {
    CourseDTO courseDTO = CourseDTO.builder()
        .name("NAME_TEST")
        .category("CATEGORY_TEST")
        .active(true)
        .build();
    
    mvc.perform(MockMvcRequestBuilders.post("/courses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectToJson(courseDTO)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("NAME_TEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("CATEGORY_TEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));
  }

  @Test
  @DisplayName("Should return Bad Request when category is null")
  public void shouldReturnBadRequestWhenCategoryIsNull() throws Exception {
    CourseDTO courseDTO = CourseDTO.builder()
        .name("NAME_TEST")
        .category(null)
        .active(true)
        .build();
  
    mvc.perform(MockMvcRequestBuilders.post("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.objectToJson(courseDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Course category must not be blank."));
  }
  
  @Test
  @DisplayName("Should return Bad Request when name is null")
  public void shouldReturnBadRequestWhenNameIsNull() throws Exception {
    CourseDTO courseDTO = CourseDTO.builder()
        .name(null)
        .category("CATEGORY_TEST")
        .active(true)
        .build();
  
    mvc.perform(MockMvcRequestBuilders.post("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.objectToJson(courseDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Course name must not be blank."));
  }

  @Test
  @DisplayName("Should return Bad Request when trying to create a course that already exists")
  public void shouldReturnBadRequestWhenCourseAlreadyExists() throws Exception {
    Course mockCourse = TestUtils.mockCourse(
        UUID.randomUUID(),
        "NAME_TEST", 
        "CATEGORY_TEST",
        false
      );

    this.courseRepository.saveAndFlush(mockCourse);

    CourseDTO courseDTO = CourseDTO.builder()
        .name("NAME_TEST")
        .category("CATEGORY_TEST")
        .active(true)
        .build();
    
    mvc.perform(MockMvcRequestBuilders.post("/courses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectToJson(courseDTO)))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result -> {
        Exception exception = result.getResolvedException();

        assertTrue(exception instanceof CourseConflictException);
        assertEquals(exception.getMessage(), "This course already exists in our database.");
      });
  }
}
