package com.api.classes.controller;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.model.response.PasswordResponse;
import com.api.classes.service.StudentClassService;
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

/**
 * Controlador para operações relacionadas a classes de estudantes.
 * Este controlador oferece endpoints para gerenciar a participação de estudantes em classes.
 * @author gustavo.pickler
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes/student")
@Tag(name = "Classes de Estudantes", description = "Endpoints relacionados a classes de estudantes")
public class StudentClassController {

    private final StudentClassService studentClassService;

    /**
     * Recupera as classes em que um estudante está inscrito.
     * @return Uma lista das classes em que o estudante está inscrito.
     * @throws NotFoundException Se o estudante não for encontrado.
     */
    @Operation(
            summary = "Obter Classes do Estudante",
            description = "Recupera as classes em que um estudante está inscrito.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classes do estudante encontradas"),
                    @ApiResponse(responseCode = "404", description = "Estudante não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClassEntity>> getClasses(
    ) throws NotFoundException {
        return ResponseEntity.ok(studentClassService.getStudentClasses());
    }

    /**
     * Inscreve um estudante em uma classe com base na senha fornecida.
     * @param classId        O ID da classe.
     * @param passwordResponse As informações de senha para ingressar na classe.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException Se a classe ou o estudante não forem encontrados.
     */
    @Operation(
            summary = "Ingressar em uma Classe",
            description = "Inscreve um estudante em uma classe com base na senha fornecida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inscrição bem-sucedida"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "404", description = "Classe ou estudante não encontrados"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping
    public ResponseEntity<Void> enterClass(
            @Parameter(description = "ID da classe", required = true)
            @RequestParam Long classId,
            @RequestBody PasswordResponse passwordResponse
    ) throws NotFoundException {
        studentClassService.enterClass(classId, passwordResponse.getPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * Remove um estudante de uma classe.
     * @param studentId O ID do estudante.
     * @param classId   O ID da classe.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException Se a classe ou o estudante não forem encontrados.
     */
    @Operation(
            summary = "Sair de uma Classe",
            description = "Remove um estudante de uma classe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saída da classe bem-sucedida"),
                    @ApiResponse(responseCode = "404", description = "Classe ou estudante não encontrados"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{studentId}")
    public ResponseEntity<StudentClass> leaveClass(
            @Parameter(description = "ID da classe", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        studentClassService.removeStudentFromClass(classId);
        return ResponseEntity.ok().build();
    }
}