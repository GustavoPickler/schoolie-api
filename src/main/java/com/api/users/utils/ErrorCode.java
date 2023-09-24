package com.api.users.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INCORRECT_OLD_PASSWORD(902, "Incorrect old password"),
    NEW_PASSWORD_DOES_NOT_MATCH(903, "New password do not match"),
    NEW_PASSWORD_SAME_AS_OLD(904, "New password is the same as the old one"),
    INVALID_PASSWORD(905, "Invalid password"),
    USER_ALREADY_IN_CLASS(906, "User is already in the class."),
    STUDENT_NOT_FOUND_IN_THIS_CLASS(907, "Student not found in this class"),
    INVALID_USER_TYPE(908,"Invalid user type"),
    EMAIL_ALREADY_REGISTERED(909, "Email already registered"),
    PHONE_ALREADY_REGISTERED(910, "Phone already registered"),
    USER_NOT_FOUND(911,"User not found"),
    CLASS_NOT_FOUND(912,"Class not found"),
    NO_CLASSES_FOUND(913,"No classes found"),
    TEACHER_NOT_THE_OWNER_OF_CLASS(914, "Teacher not the owner of the class"),
    STUDENT_NOT_FOUND(915,"Student not found"),
    TEACHER_NOT_FOUND(916,"Teacher not found"),
    TEACHER_NOT_FOUND_IN_THIS_CLASS(917, "Teacher not found in this class"),
    USER_NOT_A_TEACHER(918, "User is not a teacher"),
    USER_NOT_A_STUDENT(919, "User is not a student"),
    TEACHER_TO_REMOVE_NOT_FOUND_IN_CLASS(920, "Teacher to remove not found in this class"),
    POST_NOT_FOUND(921, "Post not found"),
    COMMENT_NOT_FOUND(922, "Comment not found");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
