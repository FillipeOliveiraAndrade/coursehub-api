package br.com.fillipeoliveira.coursehub_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fillipeoliveira.coursehub_api.exceptions.CourseNotFoundException;
import br.com.fillipeoliveira.coursehub_api.models.entities.Course;
import br.com.fillipeoliveira.coursehub_api.services.CourseService;


@Controller
@RequestMapping("/view/courses")
public class ViewController {

  @Autowired
  private CourseService courseService;
  
  @GetMapping("/create")
  public String create(Model model) {
    model.addAttribute("course", new Course());
    return "create";
  }

  @PostMapping("/create")
  public String save(Model model, Course course) {
    try {
      this.courseService.create(course);
      return "redirect:/view/courses";
    } catch (Exception e) {
      model.addAttribute("error_message", e.getMessage());
      return "create";
    }
  }

  @GetMapping
  public String get(Model model) {
    List<Course> courses = this.courseService.findCoursesWithFiltersOrNot(null, null);
    model.addAttribute("courses", courses);
    return "courses";
  }

  @GetMapping("/update/{id}")
  public String update(Model model, @PathVariable UUID id) {
    Course course = this.courseService.findById(id);
    model.addAttribute("course", course);
    return "update";
  }

  @PutMapping("/update/{id}")
  public String update(@PathVariable UUID id, @ModelAttribute Course course, Model model) {
    try {
      this.courseService.update(id, course);
      this.courseService.updateActive(id, course.isActive());
      return "redirect:/view/courses";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error_message", e.getMessage());
      return "update";
    } catch (CourseNotFoundException e) {
      model.addAttribute("error_message", "Course not found");
      return "update";
    }
  }


  @GetMapping("/details/{id}")
  public String details(Model model, @PathVariable UUID id) {
    Course course = this.courseService.findById(id);
    model.addAttribute("course", course);
    return "details";
  }

  @DeleteMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    try {
      this.courseService.delete(id);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return "redirect:/view/courses";
  }

}
