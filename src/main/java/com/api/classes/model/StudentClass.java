package com.api.classes.model;

import com.api.users.model.Student;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "students_classes")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity pClass;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}