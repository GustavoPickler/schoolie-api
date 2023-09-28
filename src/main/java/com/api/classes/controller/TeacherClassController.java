package com.api.classes.controller;

import com.api.classes.exception.UserTypeException;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.service.TeacherClassService;
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

    @Operation(
            summary = "Adicionar Professor em Classe",
            description = "Adiciona um professor a uma classe por um administrador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professor adicionado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
                    @ApiResponse(responseCode = "404", description = "Classe, professor ou administrador não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PutMapping
    public ResponseEntity<Void> addTeacherToClass(
            @Parameter(description = "ID da classe", required = true)
            @RequestParam Long classId,
            @Parameter(description = "ID do professor", required = true)
            @RequestParam Long teacherId,
            @Parameter(description = "ID do administrador", required = true)
            @RequestParam Long ownerId
    ) throws NotFoundException, UserTypeException {
        teacherClassService.addTeacherToClass(classId, teacherId, ownerId);
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
}