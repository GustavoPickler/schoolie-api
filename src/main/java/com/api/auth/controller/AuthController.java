package com.api.auth.controller;

import com.api.auth.dto.request.LoginRequestDTO;
import com.api.auth.dto.response.LoginResponseDTO;
import com.api.auth.exceptions.AuthException;
import com.api.auth.service.AuthService;
import com.api.auth.util.JwtGenerator;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Implementation controller for login authentication.
 * @author gustavo.pickler
 */

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("/login")
    public LoginResponseDTO authenticateUser(@Valid @RequestBody LoginRequestDTO authRequest) throws NotFoundException {
        String email = authRequest.getEmail();
        String password = authRequest.getPassword();

        boolean authenticationSuccessful = service.authenticate(email, password);

        if (authenticationSuccessful) {
            String authToken = jwtGenerator.generateToken(email);
            return new LoginResponseDTO(authToken);
        } else {
            throw new AuthException("teste", "Authentication failed");
        }
    }
}