package com.api.users.exception;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

    @Getter
    private int code;

    public BadRequestException(String message, int code) {
        super(message);
        this.code = code;
    }

}