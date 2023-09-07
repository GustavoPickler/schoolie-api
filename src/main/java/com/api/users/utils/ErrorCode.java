package com.api.users.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INCORRECT_OLD_PASSWORD(902, "Incorrect old password"),
    NEW_PASSWORD_DOES_NOT_MATCH(903, "New password do not match"),
    NEW_PASSWORD_SAME_AS_OLD(904, "New password is the same as the old one"),
    INVALID_PASSWORD(905, "Invalid password"),
    USER_ALREADY_IN_CLASS(906, "User is already in the class."),
    USER_DONT_EXISTS_IN_CLASS(907, "This class has no user with this ID"),
    INVALID_USER_TYPE(908,"Invalid user type"),
    EMAIL_ALREADY_REGISTERED(909, "Email already registered"),
    PHONE_ALREADY_REGISTERED(910, "Phone already registered"),
    USER_NOT_FOUND(911,"User not found"),
    CLASS_NOT_FOUND(912,"Class not found"),
    NO_CLASSES_FOUND(913,"No classes found");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
