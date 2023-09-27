package com.api.auth.service;

import com.api.auth.dto.request.LoginRequestDTO;
import com.api.users.exception.NotFoundException;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService  {

    private final UserDetailsCustomService userDetailsCustomService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public String authenticate(LoginRequestDTO dto) throws NotFoundException {
        final String email = dto.getEmail();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, dto.getPassword()));

        final UserDetails userDetails = userDetailsCustomService.loadUserByUsername(email);

        if (userDetails == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return jwtService.generateToken(email);
    }
}