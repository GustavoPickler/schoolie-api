package com.api.classes.dto;

import lombok.Data;

@Data
public class ClassInfoDTO {

    private Long id;
    private String name;
    private String description;
    private String teacherName;
    private Integer studentsCount;

    public ClassInfoDTO(Long id, String name, String description, String teacherName, Integer studentsCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherName = teacherName;
        this.studentsCount = studentsCount;
    }
}