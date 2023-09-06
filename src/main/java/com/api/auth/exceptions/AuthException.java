package com.api.auth.exceptions;

import com.api.auth.dto.Error;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthException extends RuntimeException {

    private final transient String code;
    private final transient Error error;

    public AuthException(String pCode, String pMessage) {
        super(pMessage);
        this.code = pCode;
        this.error = new Error(pCode, pMessage);
    }
}
