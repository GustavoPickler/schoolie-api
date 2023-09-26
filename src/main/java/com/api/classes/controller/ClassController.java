package com.api.classes.controller;

import com.api.classes.dto.ClassDTO;
import com.api.classes.dto.ClassInfoDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.service.ClassService;
import com.api.classes.service.StudentClassService;
import com.api.classes.service.TeacherClassService;
import com.api.users.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para operações relacionadas a classes.
 * Este controlador oferece endpoints para recuperar informações sobre classes.
 *  @author gustavo.pickler
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes")
@Tag(name = "Classes", description = "Endpoints relacionados a classes")
public class ClassController {

    private final ClassService classService;
    private final TeacherClassService teacherClassService;
    private final StudentClassService studentClassService;

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
        return ResponseEntity.ok(classService.createClass(classDTO, teacherId));
    }

    /**
     * Recupera informações sobre uma classe com base no ID da classe.
     *
     * @param classId O ID da classe a ser recuperada.
     * @return As informações da classe.
     * @throws NotFoundException Se a classe não for encontrada.
     */
    @Operation(
            summary = "Obter Classe",
            description = "Recupera informações sobre uma classe com base no ID da classe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classe encontrada"),
                    @ApiResponse(responseCode = "404", description = "Classe não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public ResponseEntity<ClassEntity> getClass(
            @Parameter(description = "ID da classe a ser recuperada", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        return ResponseEntity.ok(classService.getClass(classId));
    }

    /**
     * Lista todas as classes do usuário paginado.
     *
     * @param pageable   Configurações de paginação.
     * @param userId     O ID do usuário.
     * @return Uma página de classes com informações do professor e total de alunos.
     */
    @Operation(
            summary = "Listar Classes do Usuário Paginado",
            description = "Lista todas as classes do usuário paginado com informações do professor e total de alunos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classes do usuário encontradas"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/list")
    public ResponseEntity<Page<ClassInfoDTO>> listUserClasses(
            @Parameter(description = "Configurações de paginação", required = true)
            Pageable pageable,
            @Parameter(description = "ID do usuário", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Nome da Classe", required = true)
            @RequestParam String searchValue
            ) throws NotFoundException {
        return ResponseEntity.ok(classService.getUserClasses(userId, searchValue, pageable));
    }
}