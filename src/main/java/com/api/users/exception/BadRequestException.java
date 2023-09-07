package com.api.users.exception;

import lombok.Getter;
import com.api.users.utils.ErrorCode;

public class BadRequestException extends RuntimeException {

    @Getter
    private final int code;

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}