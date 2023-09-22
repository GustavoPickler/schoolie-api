package com.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi apiAuth() {
        return GroupedOpenApi.builder()
                .group("auth")
                .packagesToScan("com.api.auth.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi apiClasses() {
        return GroupedOpenApi.builder()
                .group("db/changelog/tables/classes")
                .packagesToScan("com.api.classes.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi apiUsers() {
        return GroupedOpenApi.builder()
                .group("users")
                .packagesToScan("com.api.users.controller")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
    }
}