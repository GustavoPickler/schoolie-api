package com.api.users.controller;

import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.config.mapper.ModelMapperConfig;
import com.api.users.model.User;
import com.api.users.model.UserType;
import com.api.users.model.response.UserResponse;
import com.api.users.model.response.UsersResponse;
import com.api.users.service.UserService;
import com.api.users.utils.MessageUtils;
import com.api.users.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing user-related operations.
 *
 * @author gustavo.pickler
 */
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {

    private final UserService service;
    private final ModelMapperConfig modelMapperConfig;

    // Retrieve user, map to DTO, and build user response
    @GetMapping("/all")
    public ResponseEntity<UsersResponse> getAllUsers() {
        List<User> users = service.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> modelMapperConfig.map(user, UserDTO.class))
                .collect(Collectors.toList());

        UsersResponse usersResponse = new UsersResponse(userDTOs, MessageUtils.USERS_FOUND_SUCCESS_MESSAGE, 801);
        return ResponseEntity.ok(usersResponse);
    }

    // Retrieve all users, map to DTOs, and return response
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) throws NotFoundException {
        User user = service.getUser(userId);
        UserDTO userDTO = modelMapperConfig.map(user, UserDTO.class);

        UserResponse userResponse = new UserResponse(userDTO, MessageUtils.USER_FOUND_SUCCESS_MESSAGE, 802);
        return ResponseEntity.ok(userResponse);
    }

    // Retrieve user, map to DTO, and build user response
    @GetMapping("/")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) throws NotFoundException {
        var user = service.getUserByEmail(email);
        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail());

        UserResponse userResponse = new UserResponse(userDTO, MessageUtils.USER_FOUND_SUCCESS_MESSAGE, 803);
        return ResponseEntity.ok(userResponse);
    }

    private ResponseEntity<Map<String, Object>> createUser(UserDTO userDTO, String successMessage, int code, UserType type) throws BadRequestException {
        service.createUser(userDTO, type);
        return ResponseEntity.ok(StringUtil.responseMap(successMessage, code));
    }

    // Create teacher, build response map, and return response
    @PostMapping("/createTeacher")
    public ResponseEntity<Map<String, Object>> createTeacher(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, MessageUtils.CREATED_TEACHER_SUCCESS_MESSAGE, 804, UserType.TEACHER);
    }

    // Create student, build response map, and return response
    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, MessageUtils.CREATED_STUDENT_SUCCESS_MESSAGE, 805, UserType.STUDENT);
    }

    // Create responsible, build response map, and return response
    @PostMapping("/createResponsible")
    public ResponseEntity<Map<String, Object>> createResponsible(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, MessageUtils.CREATED_RESPONSIBLE_SUCCESS_MESSAGE, 806, UserType.RESPONSIBLE);
    }

    // Delete user by email, build response map, and return response
    @DeleteMapping("/")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam("email") String email) throws NotFoundException {
        service.deleteUser(email);
        return ResponseEntity.ok(StringUtil.responseMap(MessageUtils.DELETED_USER_SUCCESS_MESSAGE, 811));
    }

    // Reset user password, map to DTO, and build user response
    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse> resetPassword(@PathVariable Long userId, @RequestBody String newPassword)
            throws NotFoundException {
        User resetUser = service.resetPassword(userId, newPassword);
        UserDTO resetUserDTO = modelMapperConfig.map(resetUser, UserDTO.class);
        UserResponse userResponse = new UserResponse(resetUserDTO, MessageUtils.PASSWORD_RESET_SUCCESS_MESSAGE, 808);

        return ResponseEntity.ok(userResponse);
    }

    // Change user password, map to DTO, and build user response
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<UserResponse> changePassword(@PathVariable Long userId,
            @RequestBody ChangePasswordRequest changePasswordRequest) throws NotFoundException, BadRequestException {
        User updatedUser = service.changePassword(userId, changePasswordRequest);
        UserDTO updatedUserDTO = modelMapperConfig.map(updatedUser, UserDTO.class);

        UserResponse userResponse = new UserResponse(updatedUserDTO, MessageUtils.PASSWORD_CHANGED_SUCCESS_MESSAGE, 809);
        return ResponseEntity.ok(userResponse);
    }

}