package com.api.classes.exception;

import com.api.users.utils.ErrorCode;
import lombok.Getter;

/**
 * NotFoundException is used to throw a exception to show what object
 * is null with its identifier.<br>
 * ** Advised to use with entities.
 *
 * @author gustavo.pickler
 *
 */
public class UserTypeException extends Exception {

    @Getter
    private final int code;

    public UserTypeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
