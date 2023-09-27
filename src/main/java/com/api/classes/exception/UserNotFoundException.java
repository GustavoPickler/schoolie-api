package com.api.classes.exception;

import com.api.users.utils.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{

    private final int code;

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}



