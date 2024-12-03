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

import br.com.fillipeoliveira.coursehub_api.dto.CourseUpdateDTO;
import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UpdateCourseControllerTest {
    
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
  @DisplayName("Should update a course successfully")
  public void shouldUpdateCourseSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();
    Course mockCourse = TestUtils.mockCourse(
        id,
        "NAME_TEST", 
        "CATEGORY_TEST",
        false
      );

    var result = this.courseRepository.saveAndFlush(mockCourse);

    CourseUpdateDTO courseUpdateDTO = CourseUpdateDTO.builder()
        .name("NEW_NAME")
        .category("NEW_CATEGORY")
        .build();

    mvc.perform(MockMvcRequestBuilders.put("/courses/{id}", result.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtils.objectToJson(courseUpdateDTO)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("NEW_NAME"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("NEW_CATEGORY"));
  }

  @Test
  @DisplayName("Should throw CourseNotFoundException when course does not exist")
  public void shouldThrowCourseNotFoundExceptionWhenCourseDoesNotExist() throws Exception {
    CourseUpdateDTO courseUpdateDTO = CourseUpdateDTO.builder()
      .name("TEST_NAME")
      .category("TEST_CATEGORY")
      .build();

    mvc.perform(MockMvcRequestBuilders.put("/courses/{id}", UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectToJson(courseUpdateDTO)))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result -> {
          Exception exception = result.getResolvedException();

          assertTrue(exception instanceof CourseNotFoundException);
          assertEquals("Course not found.", exception.getMessage());
        });
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should return BadRequest when course data is null")
  public void shouldReturnBadRequestWhenCourseDataIsNull() throws Exception {
    UUID id = TestUtils.generateId();
    Course mockCourse = TestUtils.mockCourse(
        id,
        "TEST_NAME",
        "TEST_CATEGORY",
        true
      );

    this.courseRepository.saveAndFlush(mockCourse);

    String messageErroExpected = "Course data for modification cannot be null.";

    mvc.perform(MockMvcRequestBuilders.put("/courses/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(result -> {
        Exception exception = result.getResolvedException();

        assertEquals(messageErroExpected, exception.getMessage());
      });
  }

}
