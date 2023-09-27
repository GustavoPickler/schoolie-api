package com.api.auth.controller;

import com.api.auth.dto.request.LoginRequestDTO;
import com.api.auth.dto.response.LoginResponseDTO;
import com.api.auth.service.AuthService;
import com.api.users.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller para autenticação de login.
 * Este controlador oferece endpoints para autenticar usuários e gerar tokens JWT.
 * @author gustavo.pickler
 */

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "Autenticar Usuário",
            description = "Autentica um usuário com base nas credenciais fornecidas (email e senha) e retorna um token JWT válido.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoginResponseDTO authenticateUser(@Valid @RequestBody LoginRequestDTO authRequest) throws NotFoundException {
        final String token = service.authenticate(authRequest);
        return new LoginResponseDTO(token);
    }
}