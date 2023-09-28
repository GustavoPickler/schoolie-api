package com.api.posts.model;

import com.api.classes.model.ClassEntity;
import com.api.users.model.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity pClass;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String attachment;

}
