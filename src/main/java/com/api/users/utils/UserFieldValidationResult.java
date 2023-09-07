package com.api.users.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFieldValidationResult {
    private boolean exists;
    private ErrorCode errorCode;

    public UserFieldValidationResult(boolean exists, ErrorCode errorCode) {
        this.exists = exists;
        this.errorCode = errorCode;
    }

}