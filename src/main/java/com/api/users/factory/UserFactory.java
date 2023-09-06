package com.api.users.factory;

import com.api.users.dto.UserDTO;
import com.api.users.exception.BadRequestException;
import com.api.users.model.*;
import com.api.users.repository.UserRepository;
import com.api.users.security.PasswordEncryptionService;
import com.api.users.utils.UserFieldValidationResult;
import com.api.users.utils.ValidationUtils;
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
                return createUser(userDTO, teacher);
            }
            case RESPONSIBLE -> {
                Responsible responsible = new Responsible();
                responsible.setDocument(userDTO.getDocument());
                return createUser(userDTO, responsible);
            }
            case STUDENT -> {
                Student student = new Student();
                return createUser(userDTO, student);
            }
            default -> throw new BadRequestException("Invalid user type", 900);
        }
    }

    private User createUser(UserDTO userDTO, User user) throws BadRequestException {
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());

        UserFieldValidationResult validationResult = ValidationUtils.userExists(user, userRepository);

        if (validationResult.isExists()) {
            throw new BadRequestException(validationResult.getMessage(), validationResult.getErrorCode());
        }

        String encryptedPassword = passwordEncryptionService.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }
}

