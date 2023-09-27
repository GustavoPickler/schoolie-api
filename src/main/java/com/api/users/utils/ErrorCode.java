package com.api.users.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INCORRECT_OLD_PASSWORD(902, "Senha atual incorreta"),
    NEW_PASSWORD_DOES_NOT_MATCH(903, "A nova senha não corresponde"),
    NEW_PASSWORD_SAME_AS_OLD(904, "A nova senha é igual à antiga"),
    INVALID_PASSWORD(905, "Senha inválida"),
    USER_ALREADY_IN_CLASS(906, "Usuário já está na turma."),
    STUDENT_NOT_FOUND_IN_THIS_CLASS(907, "Estudante não encontrado nesta turma"),
    INVALID_USER_TYPE(908, "Tipo de usuário inválido"),
    EMAIL_ALREADY_REGISTERED(909, "E-mail já registrado"),
    PHONE_ALREADY_REGISTERED(910, "Telefone já registrado"),
    USER_NOT_FOUND(911, "Usuário não encontrado"),
    CLASS_NOT_FOUND(912, "Turma não encontrada"),
    NO_CLASSES_FOUND(913, "Nenhuma turma encontrada"),
    TEACHER_NOT_THE_OWNER_OF_CLASS(914, "Professor não é o proprietário da turma"),
    STUDENT_NOT_FOUND(915, "Estudante não encontrado"),
    TEACHER_NOT_FOUND(916, "Professor não encontrado"),
    TEACHER_NOT_FOUND_IN_THIS_CLASS(917, "Professor não encontrado nesta turma"),
    USER_NOT_A_TEACHER(918, "Usuário não é um professor"),
    USER_NOT_A_STUDENT(919, "Usuário não é um estudante"),
    TEACHER_TO_REMOVE_NOT_FOUND_IN_CLASS(920, "Professor para remover não encontrado nesta turma"),
    POST_NOT_FOUND(921, "Postagem não encontrada"),
    COMMENT_NOT_FOUND(922, "Comentário não encontrado"),
    USER_NOT_FOUND_IN_CLASS(923, "Usuário não encontrado na turma"),
    OWNER_NOT_FOUND(924, "Proprietário não encontrado"),
    USER_NOT_THE_CLASS_OWNER(925, "Usuário não é o proprietário da turma"),
    TEACHER_ALREADY_IN_CLASS(926, "Professor já está na turma"),
    RESPONSIBLE_NOT_FOUND(927, "Responsável não encontrado"),
    TOO_MANY_RESPONSIBLES(928, "Limite de 3 responsáveis já foi alcançado");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
