package com.api.auth.service;

import com.api.auth.exceptions.AuthException;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean authenticate(String email, String password) throws NotFoundException, BadRequestException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new NotFoundException(User.class, email, 702);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadRequestException("Invalid password", 701);
        }

        return true;
    }

}