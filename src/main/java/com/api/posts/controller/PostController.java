package com.api.posts.controller;

import com.api.posts.model.Post;
import com.api.posts.service.PostService;
import com.api.users.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Endpoints relacionados a posts")
public class PostController {

    private final PostService postService;

    /**
     * Cria um novo post.
     *
     * @param classId O ID da classe relacionada ao post.
     * @param post    O corpo do post a ser criado.
     * @return O post criado.
     * @throws NotFoundException Se a classe ou o usuário não forem encontrados.
     */
    @Operation(
            summary = "Criar Post",
            description = "Cria um novo post relacionado a uma classe com base no ID da classe e no ID do usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post criado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Classe ou usuário não encontrados"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping
    public ResponseEntity<Post> createPost(
            @Parameter(description = "ID da classe relacionada ao post", required = true)
            @RequestParam Long classId,
            @RequestBody Post post
    ) throws NotFoundException {
        return ResponseEntity.ok(postService.createPost(classId, post));
    }

    /**
     * Recupera todos os posts disponíveis.
     *
     * @return Uma lista de todos os posts.
     */
    @Operation(
            summary = "Obter Todos os Posts",
            description = "Recupera todos os posts disponíveis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Posts encontrados com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Recupera informações sobre um post com base no ID do post.
     *
     * @param postId O ID do post a ser recuperado.
     * @return As informações do post.
     * @throws NotFoundException Se o post não for encontrado.
     */
    @Operation(
            summary = "Obter Post por ID",
            description = "Recupera informações sobre um post com base no ID do post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(
            @Parameter(description = "ID do post a ser recuperado", required = true)
            @PathVariable Long postId
    ) throws NotFoundException {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Atualizar Post",
            description = "Atualiza um post existente com base no ID do post, ID da classe e ID do professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @Parameter(description = "ID do post a ser atualizado", required = true)
            @PathVariable Long postId,
            @Parameter(description = "ID da classe à qual o post pertence", required = true)
            @RequestParam Long classId,
            @RequestBody Post updatedPost
    ) throws NotFoundException {
        Post post = postService.updatePost(postId, updatedPost, classId);
        return ResponseEntity.ok(post);
    }
    @Operation(
            summary = "Deletar Post",
            description = "Deleta um post existente com base no ID do post, ID da classe e ID do professor.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID do post a ser deletado", required = true)
            @PathVariable Long postId,
            @Parameter(description = "ID da classe à qual o post pertence", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        postService.deletePost(postId, classId);
        return ResponseEntity.noContent().build();
    }

}
