package com.api.users.controller;

import com.api.classes.repository.StudentClassRepository;
import com.api.users.exception.NotFoundException;
import com.api.users.exception.ResponsiblesException;
import com.api.users.model.Responsible;
import com.api.users.model.Student;
import com.api.users.service.StudentResponsibleService;
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
@RequestMapping("/api/classes/studentResponsible")
@Tag(name = "Classes de Estudantes", description = "Endpoints relacionados a classes de estudantes")
public class StudentResponsibleController {

    private final StudentResponsibleService studentResponsibleService;

    /**
     * Recupera as classes em que um estudante está inscrito.
     * @param studentId O ID do estudante.
     * @return Uma lista das classes em que o estudante está inscrito.
     */
    @Operation(
            summary = "Obter responsáveis vínculados a estudante",
            description = "Recupera os responsáveis que estão vínculados com o estudante.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsáveis vinculados encontrados"),
                    @ApiResponse(responseCode = "404", description = "Estudante não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/student")
    public ResponseEntity<List<Responsible>> getStudentBonds(
    ) {
        return ResponseEntity.ok(studentResponsibleService.getStudentBonds());
    }

    @Operation(
            summary = "Obter estudantes vínculados a responsável",
            description = "Recupera os estudantes que estão vínculados com o responsável.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estudantes vinculados encontrados"),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/responsible")
    public ResponseEntity<List<Student>> getResponsibleBonds(
    ) {
        return ResponseEntity.ok(studentResponsibleService.getResponsibleBonds());
    }

    /**
     * Vincula um responsável a um estudante.
     * @param studentId O ID do estudante.
     * @param responsibleId O ID do responsável.
     * @return Uma mensagem de sucesso.
     * @throws NotFoundException Se o estudante ou responsável não forem encontrados.
     */
    @Operation(
            summary = "Vincular um responsável a um estudante",
            description = "Vincula um responsável a um estudante.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsável vinculado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Estudante ou responsável não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping
    public ResponseEntity<Void> linkResponsibleToStudent(
            @Parameter(description = "ID do estudante", required = true)
            @RequestParam Long studentId,
            @Parameter(description = "ID do responsável", required = true)
            @RequestParam Long responsibleId
    ) throws NotFoundException, ResponsiblesException {
        studentResponsibleService.linkResponsibleToStudent(studentId, responsibleId);
        return ResponseEntity.ok().build();
    }

}