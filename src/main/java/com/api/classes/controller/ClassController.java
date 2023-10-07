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
import org.springframework.security.access.annotation.Secured;
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
    @Secured("TEACHER")
    @PostMapping
    public ResponseEntity<ClassEntity> createClass(
            @RequestBody ClassDTO classDTO
    ) throws NotFoundException {
        return ResponseEntity.ok(classService.createClass(classDTO));
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
    @Secured({"TEACHER", "STUDENT", "RESPONSIBLE"})
    @GetMapping
    public ResponseEntity<ClassEntity> getClass(
            @Parameter(description = "ID da classe a ser recuperada", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        return ResponseEntity.ok(classService.getClass(classId));
    }

    /**
     * Exclui uma classe por um professor.
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
    @Secured("TEACHER")
    @DeleteMapping()
    public ResponseEntity<ClassEntity> removeClass(
            @Parameter(description = "ID da classe a ser excluída", required = true)
            @RequestParam Long classId
    ) throws NotFoundException {
        return ResponseEntity.ok(classService.deleteClass(classId));
    }

    /**
     * Lista todas as classes do usuário paginado.
     *
     * @param pageable   Configurações de paginação.
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
    @Secured({"TEACHER", "STUDENT", "RESPONSIBLE"})
    @GetMapping("/list")
    public ResponseEntity<Page<ClassInfoDTO>> listUserClasses(
            @Parameter(description = "Configurações de paginação", required = true)
            Pageable pageable,
            @Parameter(description = "Nome da Classe")
            @RequestParam(required = false) String searchValue
    ) throws NotFoundException {
        return ResponseEntity.ok(classService.getUserClasses(searchValue, pageable));
    }
}