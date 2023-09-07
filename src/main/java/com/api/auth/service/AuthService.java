package com.api.auth.service;

import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean authenticate(String email, String password) throws NotFoundException, BadRequestException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);

        if (!passwordEncoder.matches(password, user.get().getPassword()))
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);

        return true;
    }

}