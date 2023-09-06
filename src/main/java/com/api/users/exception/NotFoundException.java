package com.api.users.exception;

import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;

/**
 * NotFoundException is used to throw a exception to show what object
 * is null with its identifier.<br>
 * ** Advised to use with entities.
 *
 * @author gustavo.pickler
 *
 */
@Getter
public class NotFoundException extends Exception {

    private final Class<?> clazz;
    private final transient Object identifier;
    private final int code;

    public NotFoundException(Class<?> pClazz, Object pIdentifier, int code) {
        super(MessageFormat.format("{0} {1} not found", getName(pClazz), pIdentifier));
        this.clazz = pClazz;
        this.identifier = pIdentifier;
        this.code = code;
    }

    private static String getName(Class<?> pClazz) {
        if (Objects.isNull(pClazz)) {
            throw new IllegalArgumentException();
        }

        return pClazz.getSimpleName();
    }
}
