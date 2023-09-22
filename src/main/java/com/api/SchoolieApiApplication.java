package com.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@OpenAPIDefinition(
		info = @Info(
				title = "Schoolie API",
				version = "1.0",
				description = "Esta API foi desenvolvida para fornecer endpoints para a criação de contas de usuários (professores, alunos ou responsáveis), " +
						"a criação e gerenciamento de classes, a postagem de conteúdo em classes e a participação em fóruns"
		)
)
public class SchoolieApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolieApiApplication.class, args);
	}

}
