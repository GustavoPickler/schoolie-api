package com.api.posts.service;

import com.api.auth.utils.SecurityUtil;
import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.posts.model.Post;
import com.api.posts.repository.PostRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Teacher;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final StudentClassRepository studentClassRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final PostRepository postRepository;

    public Post createPost(Long classId, Post post) throws NotFoundException {
        Teacher teacher = (Teacher) SecurityUtil.getCurrentUser();
        ClassEntity classEntity = classRepository.findById(classId).
                orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        post.setAuthor(teacher);
        post.setPClass(classEntity);

        if (validateTeacherInClass(teacher, classEntity))
            throw new BadRequestException(ErrorCode.TEACHER_NOT_FOUND_IN_THIS_CLASS);

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) throws NotFoundException {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public Post updatePost(Long postId, Post updatedPost, Long classId) throws NotFoundException {
        Teacher teacher = (Teacher) SecurityUtil.getCurrentUser();
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        ClassEntity classEntity = classRepository.findById(classId).
                orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        if (validateTeacherInClass(teacher, classEntity))
            throw new BadRequestException(ErrorCode.TEACHER_NOT_FOUND_IN_THIS_CLASS);

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }

    public void deletePost(Long postId, Long classId) throws NotFoundException {
        Teacher teacher = (Teacher) SecurityUtil.getCurrentUser();
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        ClassEntity classEntity = classRepository.findById(classId).
                orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        if (validateTeacherInClass(teacher, classEntity))
            throw new BadRequestException(ErrorCode.TEACHER_NOT_FOUND_IN_THIS_CLASS);

        postRepository.delete(existingPost);
    }

    private boolean validateTeacherInClass(Teacher teacher, ClassEntity classEntity) {
        return teacherClassRepository.existsByClassIdAndTeacherId(teacher.getId(), classEntity.getId()) &&
                classEntity.getOwner().equals(teacher);
    }

}
