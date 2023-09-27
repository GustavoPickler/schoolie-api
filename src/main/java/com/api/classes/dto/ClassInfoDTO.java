package com.api.classes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClassInfoDTO {

    private Long id;
    private String name;
    private String description;
    private String teacherName;
    private Long totalStudents;

    public ClassInfoDTO(Long id, String name, String description, String teacherName, Long totalStudents) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherName = teacherName;
        this.totalStudents = totalStudents;
    }
}