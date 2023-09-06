package com.api.classes.dto;

import com.api.classes.model.ClassUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDTO {

    private Long id;
    private String name;
    private String description;
    private String password;

}