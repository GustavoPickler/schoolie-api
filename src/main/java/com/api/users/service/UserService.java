package com.api.users.service;

import com.api.classes.model.ClassEntity;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.classes.service.StudentClassService;
import com.api.classes.service.TeacherClassService;
import com.api.users.factory.UserFactory;
import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.*;
import com.api.users.repository.UserRepository;
import com.api.users.security.PasswordEncryptionService;
import com.api.users.utils.ErrorCode;
import com.api.users.utils.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible to have all the business rules for users.
 *
 * @author gustavo.pickler
 */
@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncryptionService passwordEncryptionService;
    private final UserRepository userRepository;
    private final StudentClassRepository studentClassRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final UserFactory userFactory;
    private final StudentClassService studentClassService;
    private final TeacherClassService teacherClassService;

    // Retrieve a user by their ID
    public User getUser(Long userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // Retrieve all users from the repository
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieve user by email from the repository and handle potential NotFoundException
    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public User createUser(UserDTO userDTO, UserType userType) throws BadRequestException {
        User user = userFactory.createUser(userDTO, userType);
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        user.setBirthDate(userDTO.getBirthDate());
        user.setUserType(userType);

        String normalizedEmail = StringUtil.normalizeText(user.getEmail());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new BadRequestException(ErrorCode.PHONE_ALREADY_REGISTERED);
        }

        String encryptedPassword = passwordEncryptionService.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    // Retrieve user, update fields, and save changes to the repository
    public User updateUser(Long userId, UserDTO userDTO) throws NotFoundException {
        User userToUpdate = getUser(userId);
        userToUpdate.setEmail(userDTO.getEmail());

        return userRepository.save(userToUpdate);
    }

    // Retrieve user, validate old password, encrypt and update new password, and save to the repository
    public void changePassword(Long userId, ChangePasswordRequest changePasswordRequest)
            throws NotFoundException, BadRequestException {
        User userToUpdate = getUser(userId);

        if (!passwordEncryptionService.validatePassword(changePasswordRequest.getOldPassword(), userToUpdate.getPassword()))
            throw new BadRequestException(ErrorCode.INCORRECT_OLD_PASSWORD);

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword()))
            throw new BadRequestException(ErrorCode.NEW_PASSWORD_DOES_NOT_MATCH);

        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword()))
            throw new BadRequestException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);

        String encryptedPassword = passwordEncryptionService.encryptPassword(changePasswordRequest.getNewPassword());
        userToUpdate.setPassword(encryptedPassword);
        userRepository.save(userToUpdate);
    }

    // Retrieve user, encrypt and update new password, and save to the repository
    public void resetPassword(Long userId, String newPassword) throws NotFoundException {
        User userToReset = getUser(userId);
        String encryptedPassword = passwordEncryptionService.encryptPassword(newPassword);
        userToReset.setPassword(encryptedPassword);
        userRepository.save(userToReset);
    }

    // Retrieve user by email, and delete from the repository
    public void deleteUser(Long userId) throws NotFoundException {
        User userToDelete = getUser(userId);
        UserType userType = userToDelete.getUserType();

        switch (userType) {
            case STUDENT -> {
                List<ClassEntity> studentClassList = studentClassRepository.findByStudentId(userId);
                studentClassList.forEach(student -> studentClassService.removeStudentFromAllClasses(student.getId()));
            }
            case TEACHER -> {
                List<ClassEntity> teacherClassList = teacherClassRepository.findByTeacherId(userId);
                teacherClassList.forEach(teacher -> teacherClassService.removeTeacherFromAllClasses(teacher.getId()));
            }
            default -> userRepository.delete(userToDelete);
        }
    }

}
