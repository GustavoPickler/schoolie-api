package com.api.posts.controller;

import com.api.posts.model.Comment;
import com.api.posts.model.Post;
import com.api.posts.service.CommentService;
import com.api.posts.service.PostService;
import com.api.users.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Endpoints relacionados a comentários")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    /**
     * Get a comment by its ID.
     *
     * @param commentId The ID of the comment to retrieve.
     * @return The comment.
     * @throws NotFoundException If the comment is not found.
     */
    @Operation(
            summary = "Obter Comentário por ID",
            description = "Recupera informações sobre um comentário com base no ID do comentário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(
            @Parameter(description = "ID do comentário a ser recuperado", required = true)
            @PathVariable Long commentId
    ) throws NotFoundException {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    /**
     * Create a new comment for a specific post.
     *
     * @param postId  The ID of the post to which the comment belongs.
     * @param comment The comment to create.
     * @return The created comment.
     * @throws NotFoundException If the post is not found.
     */
    @Operation(
            summary = "Criar Comentário",
            description = "Cria um novo comentário para um post específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @Secured({"TEACHER", "STUDENT"})
    @PostMapping()
    public ResponseEntity<Comment> createCommentForPost(
            @Parameter(description = "ID do post ao qual o comentário pertence", required = true)
            @RequestParam Long postId,
            @RequestBody Comment comment
    ) throws NotFoundException {
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        Comment createdComment = commentService.createComment(comment, post);
        return ResponseEntity.ok(createdComment);
    }

    /**
     * Update an existing comment.
     *
     * @param commentId     The ID of the comment to update.
     * @param updatedComment The updated comment data.
     * @return The updated comment.
     * @throws NotFoundException If the comment is not found.
     */
    @Operation(
            summary = "Atualizar Comentário",
            description = "Atualiza um comentário existente com base no ID do comentário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @Secured({"TEACHER", "STUDENT"})
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "ID do comentário a ser atualizado", required = true)
            @PathVariable Long commentId,
            @RequestBody Comment updatedComment
    ) throws NotFoundException {
        Comment updated = commentService.updateComment(commentId, updatedComment);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a comment by its ID.
     *
     * @param commentId The ID of the comment to delete.
     * @return A response indicating success.
     * @throws NotFoundException If the comment is not found.
     */
    @Operation(
            summary = "Excluir Comentário",
            description = "Exclui um comentário existente com base no ID do comentário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comentário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @Secured({"TEACHER", "STUDENT"})
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID do comentário a ser excluído", required = true)
            @PathVariable Long commentId
    ) throws NotFoundException {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
