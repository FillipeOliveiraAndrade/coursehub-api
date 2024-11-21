package br.com.fillipeoliveira.coursehub_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "CourseHub API",
		description = "API responsável pela gestão dos cursos",
		version = "1"
	)
)
public class CoursehubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursehubApiApplication.class, args);
	}

}
