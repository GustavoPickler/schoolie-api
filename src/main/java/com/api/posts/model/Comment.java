package com.api.posts.model;

import com.api.users.model.Teacher;
import com.api.users.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id") // Link each comment to a post
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id") // Link each comment to a teacher
    private User author;

    @Column(nullable = false)
    private String content;

    // Add any other necessary fields
}