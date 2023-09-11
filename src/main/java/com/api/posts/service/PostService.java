package com.api.posts.service;

import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.posts.model.Post;
import com.api.posts.repository.PostRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public Post createPost(Long classId, Long teacherId, Post post) throws NotFoundException {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        User user = userRepository.findById(teacherId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        post.setAuthor(user);
        post.setPClass(classEntity);

        if(!teacherClassRepository.existsByClassIdAndTeacherId(teacherId, classId))
            throw new BadRequestException(ErrorCode.TEACHER_NOT_FOUND_IN_THIS_CLASS);

        return postRepository.save(post);
    }

}
