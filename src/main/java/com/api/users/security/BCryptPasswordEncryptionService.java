package com.api.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordEncryptionService implements PasswordEncryptionService {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = passwordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean validatePassword(String password, String encryptedPassword) {
        BCryptPasswordEncoder passwordEncoder = passwordEncoder();
        return passwordEncoder.matches(password, encryptedPassword);
    }
}