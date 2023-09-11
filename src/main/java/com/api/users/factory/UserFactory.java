package com.api.users.factory;

import com.api.users.dto.UserDTO;
import com.api.users.exception.BadRequestException;
import com.api.users.model.*;
import com.api.users.repository.UserRepository;
import com.api.users.security.PasswordEncryptionService;
import com.api.users.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFactory {

    private final PasswordEncryptionService passwordEncryptionService;
    private final UserRepository userRepository;

    public User createUser(UserDTO userDTO, UserType userType) throws BadRequestException {

        switch (userType) {
            case TEACHER -> {
                Teacher teacher = new Teacher();
                teacher.setDocument(userDTO.getDocument());
                return teacher;
            }
            case RESPONSIBLE -> {
                Responsible responsible = new Responsible();
                responsible.setDocument(userDTO.getDocument());
                return responsible;
            }
            case STUDENT -> {
                return new Student();
            }
            default -> throw new BadRequestException(ErrorCode.INVALID_USER_TYPE);
        }
    }
}

