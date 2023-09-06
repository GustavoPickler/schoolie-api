package com.api.users.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public <T, U> U map(T source, Class<U> destinationType) {
        return modelMapper().map(source, destinationType);
    }
}