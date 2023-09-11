package com.api.classes.model;

import com.api.users.model.Teacher;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "teachers_classes")
public class TeacherClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity pClass;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}