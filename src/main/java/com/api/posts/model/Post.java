package com.api.posts.model;

import com.api.classes.model.ClassEntity;
import com.api.users.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity pClass;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Lob
    @Column
    private String attachment;

}
