package com.api.posts.service;

import com.api.posts.model.Comment;
import com.api.posts.repository.CommentRepository;
import com.api.users.exception.NotFoundException;
import com.api.users.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) throws NotFoundException {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        // Update the existing comment with the data from updatedComment
        existingComment.setContent(updatedComment.getContent());
        // Add more fields to update as needed

        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long commentId) throws NotFoundException {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        commentRepository.delete(existingComment);
    }

    public Comment getCommentById(Long commentId) throws NotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}