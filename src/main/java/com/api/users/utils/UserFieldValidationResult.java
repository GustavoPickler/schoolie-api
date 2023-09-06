package com.api.users.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFieldValidationResult {
    private boolean exists;
    private String message;
    private int errorCode;

    public UserFieldValidationResult(boolean exists, String message, int errorCode) {
        this.exists = exists;
        this.message = message;
        this.errorCode = errorCode;
    }

}