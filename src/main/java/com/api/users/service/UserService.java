package com.api.users.service;

import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.mapper.ModelMapperConfig;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.security.PasswordEncryptionService;
import com.api.users.utils.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private final PasswordEncryptionService passwordEncryptionService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapperConfig modelMapperConfig;

    // Retrieve a user by their ID
    public User getUser(Long userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException(User.class, userId, 702);
        }

        return optionalUser.get();
    }

    // Retrieve all users from the repository
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieve user by email from the repository and handle potential NotFoundException
    public User getUserByEmail(String email) throws NotFoundException {

        Optional<User> optionalUserContent = userRepository.findByEmail(email);

        if (optionalUserContent.isEmpty()) {
            throw new NotFoundException(User.class, email, 702);
        }

        return optionalUserContent.get();
    }

    // Map and create a new user entity, encrypt password, and save to the repository
    public User createUser(UserDTO userDTO) throws BadRequestException {
        User user = modelMapperConfig.map(userDTO, User.class);

        String normalizedEmail = StringUtil.normalizeText(user.getEmail());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email already registered", 900);
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new BadRequestException("Phone already registered", 901);
        }

        String encryptedPassword = passwordEncryptionService.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    // Retrieve user, update fields, and save changes to the repository
    public User updateUser(Long userId, UserDTO userDTO) throws NotFoundException {
        User userToUpdate = getUser(userId);

        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setType(userDTO.getType());

        return userRepository.save(userToUpdate);
    }

    // Retrieve user, validate old password, encrypt and update new password, and save to the repository
    public User changePassword(Long userId, ChangePasswordRequest changePasswordRequest)
            throws NotFoundException, BadRequestException {
        User userToUpdate = getUser(userId);

        if (!passwordEncryptionService.validatePassword(changePasswordRequest.getOldPassword(), userToUpdate.getPassword())) {
            throw new BadRequestException("Incorrect old password", 902);
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new BadRequestException("New password do not match", 903);
        }

        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new BadRequestException("New password is the same as the old one", 904);
        }

        String encryptedPassword = passwordEncryptionService.encryptPassword(changePasswordRequest.getNewPassword());
        userToUpdate.setPassword(encryptedPassword);

        return userRepository.save(userToUpdate);
    }

    // Retrieve user, encrypt and update new password, and save to the repository
    public User resetPassword(Long userId, String newPassword) throws NotFoundException {
        User userToReset = getUser(userId);
        String encryptedPassword = passwordEncryptionService.encryptPassword(newPassword);
        userToReset.setPassword(encryptedPassword);
        return userRepository.save(userToReset);
    }

    // Retrieve user by email, and delete from the repository
    public void deleteUser(String email) throws NotFoundException {
        User userToDelete = getUserByEmail(email);
        userRepository.delete(userToDelete);
    }

}
