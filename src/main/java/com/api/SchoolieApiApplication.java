package com.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Schoolie API",
				version = "1.0",
				description = "Esta API foi desenvolvida para fornecer endpoints para a criação de contas de usuários (professores, alunos ou responsáveis), " +
						"a criação e gerenciamento de classes, a postagem de conteúdo em classes e a participação em fóruns"
		),
		servers = @Server(
				description = "Local DEV",
				url = "http://localhost:8080"
		),
		security = {
				@SecurityRequirement(name = "bearerAuth"),
		}
)
@SecurityScheme(name = "bearerAuth",
		description = "JWT auth",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class SchoolieApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolieApiApplication.class, args);
	}

}
