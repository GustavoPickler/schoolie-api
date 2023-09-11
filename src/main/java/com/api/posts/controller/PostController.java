package com.api.posts.controller;

import com.api.posts.model.Post;
import com.api.posts.service.PostService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PutMapping
    public ResponseEntity<Post> createPost(@RequestParam Long classId, @RequestParam Long userId, @RequestBody Post post) throws NotFoundException {
        return ResponseEntity.ok(postService.createPost(classId, userId, post));
    }

}
