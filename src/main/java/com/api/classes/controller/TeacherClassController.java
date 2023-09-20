package com.api.classes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.classes.dto.ClassDTO;
import com.api.classes.exception.UserTypeException;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.service.TeacherClassService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controlador para operações relacionadas a classes de professores.
 * Este controlador oferece endpoints para professores criar classes, gerenciar estudantes em classes e outras operações relacionadas a classes.
 * @author gustavo.pickler
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes/teacher")
@Tag(name = "Classes de Professores", description = "Endpoints relacionados a classes de professores")
public class TeacherClassController {

    private final TeacherClassService teacherClassService;

    /**
     * Cria uma nova classe por um professor.
     * @param teacherId O ID do professor.
     * @param classDTO  As informações da classe a ser criada.
     * @return A classe criada.
     * @throws NotFoundException Se o professor não for encontrado.
     */
    @Operation(
            summary = "Criar Classe",
            description = "Cria uma nova classe por um professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classe criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/{teacherId}")
    public ResponseEntity<ClassEntity> createClass(
            @Parameter(description = "ID do professor", required = true)
            @PathVariable Long teacherId,
            @RequestBody ClassDTO classDTO
    ) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.createClass(classDTO, teacherId));
    }

    /**
     * Adiciona um estudante a uma classe por um professor.
     * @param classId   O ID da classe.
     * @param studentId O ID do estudante.
     * @param teacherId O ID do professor.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException  Se a classe, o estudante ou o professor não forem encontrados.
     * @throws UserTypeException Se o usuário não for um estudante.
     */
    @Operation(
            summary = "Adicionar Estudante em Classe",
            description = "Adiciona um estudante a uma classe por um professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudante adicionado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "404", description = "Classe, estudante ou professor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping("/{teacherId}")
    public ResponseEntity<Void> addStudentIntoClass(
            @Parameter(description = "ID da classe", required = true)
            @RequestParam Long classId,
            @Parameter(description = "ID do estudante", required = true)
            @RequestParam Long studentId,
            @Parameter(description = "ID do professor", required = true)
            @PathVariable Long teacherId
    ) throws NotFoundException, UserTypeException {
        teacherClassService.addStudentInClass(classId, studentId, teacherId);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtém as classes de um professor.
     * @param teacherId O ID do professor.
     * @return Uma lista das classes do professor.
     * @throws NotFoundException Se o professor não for encontrado.
     */
    @Operation(
            summary = "Obter Classes do Professor",
            description = "Obtém as classes de um professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classes do professor encontradas"),
                    @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClassEntity>> getTeacherClasses(
            @Parameter(description = "ID do professor", required = true)
            @RequestParam Long teacherId
    ) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.getTeacherClasses(teacherId));
    }

    /**
     * Remove um estudante de uma classe por um professor.
     * @param teacherId O ID do professor.
     * @param classId   O ID da classe.
     * @param userId    O ID do usuário (estudante) a ser removido.
     * @return Uma resposta vazia em caso de sucesso.
     * @throws NotFoundException Se a classe, o professor ou o estudante não forem encontrados.
     */
    @Operation(
            summary = "Remover Estudante de Classe",
            description = "Remove um estudante de uma classe por um professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudante removido da classe com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Classe, professor ou estudante não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<StudentClass> removeStudentFromClass(
            @Parameter(description = "ID do professor", required = true)
            @PathVariable Long teacherId,
            @Parameter(description = "ID da classe", required = true)
            @RequestParam Long classId,
            @Parameter(description = "ID do usuário (estudante) a ser removido", required = true)
            @RequestParam Long userId
    ) throws NotFoundException {
        teacherClassService.removeStudentFromClass(teacherId, classId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Exclui uma classe por um professor.
     * @param teacherId O ID do professor.
     * @param classId   O ID da classe a ser excluída.
     * @return A classe excluída.
     * @throws NotFoundException Se a classe ou o professor não forem encontrados.
     */
    @Operation(
            summary = "Excluir Classe",
            description = "Exclui uma classe por um professor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classe excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Classe ou professor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{teacherId}/deleteClass")
    public ResponseEntity<ClassEntity> removeClass(
            @Parameter(description = "ID do professor", required = true)
            @PathVariable Long teacherId,
            @Parameter(description = "ID da classe a ser excluída", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.deleteClass(teacherId, classId));
    }
}