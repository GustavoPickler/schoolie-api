package com.api.posts.service;

import com.api.auth.utils.SecurityUtil;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.posts.model.Comment;
import com.api.posts.model.Post;
import com.api.posts.repository.CommentRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Student;
import com.api.users.model.Teacher;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Security;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudentClassRepository studentClassRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final UserRepository userRepository;

    public Comment createComment(Comment comment, Post post) {
        User user = SecurityUtil.getCurrentUser();
        validateUserInClass(user, post.getId(), user.getId());
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) throws NotFoundException {
        User user = SecurityUtil.getCurrentUser();
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        if (!existingComment.getAuthor().equals(user))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        existingComment.setContent(updatedComment.getContent());

        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long commentId) throws NotFoundException {
        User user = SecurityUtil.getCurrentUser();
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        if (!existingComment.getAuthor().equals(user))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        commentRepository.delete(existingComment);
    }

    public Comment getCommentById(Long commentId) throws NotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateUserInClass(User author, Long classId, Long userId) {
        boolean userIsInClass = false;

        if (author instanceof Student) {
            userIsInClass = studentClassRepository.existsByClassIdAndUserStudent(classId, userId);
        } else if (author instanceof Teacher) {
            userIsInClass = teacherClassRepository.existsByClassIdAndTeacherId(classId, userId);
        }

        if (!userIsInClass) {
            throw new BadRequestException(ErrorCode.USER_NOT_FOUND_IN_CLASS);
        }
    }
}