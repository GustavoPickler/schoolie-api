package com.api.users.controller;

import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.model.UserType;
import com.api.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar operações relacionadas a usuários.
 * Este controlador oferece endpoints para criar, recuperar, atualizar e excluir usuários.
 * @author gustavo.pickler
 */
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "Usuários", description = "Endpoints relacionados a usuários")
public class UserController {

    private final UserService service;

    /**
     * Recupera todos os usuários.
     * @return Uma lista de todos os usuários.
     */
    @Operation(
            summary = "Obter Todos os Usuários",
            description = "Recupera todos os usuários cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    /**
     * Recupera um usuário pelo ID.
     * @param userId O ID do usuário a ser recuperado.
     * @return As informações do usuário.
     * @throws NotFoundException Se o usuário não for encontrado.
     */
    @Operation(
            summary = "Obter Usuário por ID",
            description = "Recupera um usuário pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID do usuário a ser recuperado", required = true)
            @PathVariable Long userId
    ) throws NotFoundException {
        return ResponseEntity.ok(service.getUser(userId));
    }

    /**
     * Recupera um usuário pelo email.
     * @param email O email do usuário a ser recuperado.
     * @return As informações do usuário.
     * @throws NotFoundException Se o usuário não for encontrado.
     */
    @Operation(
            summary = "Obter Usuário por Email",
            description = "Recupera um usuário pelo email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/userByEmail")
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email do usuário a ser recuperado", required = true)
            @RequestParam String email
    ) throws NotFoundException {
        User user = service.getUserByEmail(email);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        return ResponseEntity.ok(userDTO);
    }

    private ResponseEntity<User> createUser(UserDTO userDTO, UserType userType) throws BadRequestException {
        return ResponseEntity.ok(service.createUser(userDTO, userType));
    }

    /**
     * Cria um professor.
     * @param userDTO As informações do professor a ser criado.
     * @return O professor criado.
     * @throws BadRequestException Se os dados do professor forem inválidos.
     */
    @Operation(
            summary = "Criar Professor",
            description = "Cria um novo professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professor criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/teacher")
    public ResponseEntity<User> createTeacher(
            @RequestBody UserDTO userDTO
    ) throws BadRequestException {
        return createUser(userDTO, UserType.TEACHER);
    }

    /**
     * Cria um estudante.
     * @param userDTO As informações do estudante a ser criado.
     * @return O estudante criado.
     * @throws BadRequestException Se os dados do estudante forem inválidos.
     */
    @Operation(
            summary = "Criar Estudante",
            description = "Cria um novo estudante.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudante criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/student")
    public ResponseEntity<User> createStudent(
            @RequestBody UserDTO userDTO
    ) throws BadRequestException {
        return createUser(userDTO, UserType.STUDENT);
    }

    /**
     * Cria um responsável.
     * @param userDTO As informações do responsável a ser criado.
     * @return O responsável criado.
     * @throws BadRequestException Se os dados do responsável forem inválidos.
     */
    @Operation(
            summary = "Criar Responsável",
            description = "Cria um novo responsável.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsável criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/responsible")
    public ResponseEntity<User> createResponsible(
            @RequestBody UserDTO userDTO
    ) throws BadRequestException {
        return createUser(userDTO, UserType.RESPONSIBLE);
    }

    /**
     * Exclui um usuário pelo ID.
     * @param userId O ID do usuário a ser excluído.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException Se o usuário não for encontrado.
     */
    @Operation(
            summary = "Excluir Usuário",
            description = "Exclui um usuário pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID do usuário a ser excluído", required = true)
            @RequestParam Long userId
    ) throws NotFoundException {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Redefine a senha de um usuário pelo ID.
     * @param userId O ID do usuário.
     * @param newPassword A nova senha.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException Se o usuário não for encontrado.
     */
    @Operation(
            summary = "Redefinir Senha",
            description = "Redefine a senha de um usuário pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "ID do usuário", required = true)
            @RequestParam Long userId,
            @RequestBody String newPassword
    ) throws NotFoundException {
        service.resetPassword(userId, newPassword);
        return ResponseEntity.ok().build();
    }

    /**
     * Altera a senha de um usuário pelo ID.
     * @param userId O ID do usuário.
     * @param changePasswordRequest As informações de alteração de senha.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException  Se o usuário não for encontrado.
     * @throws BadRequestException Se a solicitação de alteração de senha for inválida.
     */
    @Operation(
            summary = "Alterar Senha",
            description = "Altera a senha de um usuário pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) throws NotFoundException, BadRequestException {
        service.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok().build();
    }
}