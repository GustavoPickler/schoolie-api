package com.api.auth.utils;

import com.api.auth.exceptions.AuthException;
import com.api.users.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthException("",""); //TODO: retornar exceção 401 ou 403
        }

        if (authentication.getPrincipal() == null) {
            throw new AuthException("",""); //TODO: retornar exceção 401 ou 403
        }

        return (User) authentication.getPrincipal();
    }
}
