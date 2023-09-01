package com.api.users.security;

public interface PasswordEncryptionService {
    String encryptPassword(String password);
    boolean validatePassword(String password, String encryptedPassword);
}
