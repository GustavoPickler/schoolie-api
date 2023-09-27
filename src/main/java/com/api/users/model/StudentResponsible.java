package com.api.users.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "students_responsibles")
public class StudentResponsible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private Responsible responsible;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}