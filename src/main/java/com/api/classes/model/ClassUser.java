package com.api.classes.model;

import com.api.users.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "class_users")
public class ClassUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity pClass;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}