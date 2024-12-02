package br.com.fillipeoliveira.coursehub_api.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.models.repositories.CourseRepository;
import br.com.fillipeoliveira.coursehub_api.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeleteCourseControllerTest {
  
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
  @DisplayName("Should delete a course successfully")
  public void shouldDeleteCourseSuccessfully() throws Exception {
    Course mockCourse = TestUtils.mockCourse(
        TestUtils.generateId(),
        "TEST_NAME",
        "TEST_CATEGORY",
        false
      );

    Course result = this.courseRepository.saveAndFlush(mockCourse);

    mvc.perform(MockMvcRequestBuilders.delete("/courses/{id}", result.getId()))
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string(""));
  
      boolean courseExists = courseRepository.existsById(result.getId());
      assertFalse(courseExists, "Curso não foi deletado com sucesso!");
  }

  @Test
  @DisplayName("Should return not found when course not exists")
  public void shouldReturnNotFoundWhenCourseNotFound() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/courses/{id}", TestUtils.generateId()))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(result -> {
        Exception exception = result.getResolvedException();

        assertTrue(exception instanceof CourseNotFoundException);
        assertEquals("Course not found.", exception.getMessage());
      });
  }
}
