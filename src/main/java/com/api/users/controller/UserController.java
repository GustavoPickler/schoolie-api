package com.api.users.controller;

import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.mapper.ModelMapperConfig;
import com.api.users.model.User;
import com.api.users.model.response.UserResponse;
import com.api.users.model.response.UsersResponse;
import com.api.users.service.UserService;
import com.api.users.utils.MessageUtils;
import com.api.users.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapperConfig modelMapperConfig;


    // Retrieve user, map to DTO, and build user response
    @GetMapping("/all")
    public ResponseEntity<UsersResponse> getAllUsers() {
        List<User> users = service.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> modelMapperConfig.map(user, UserDTO.class))
                .collect(Collectors.toList());

        UsersResponse usersResponse = new UsersResponse(userDTOs, MessageUtils.getUsersFoundMessage(), 801);
        return ResponseEntity.ok(usersResponse);
    }

    // Retrieve all users, map to DTOs, and return response
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) throws NotFoundException {
        User user = service.getUser(userId);
        UserDTO userDTO = modelMapperConfig.map(user, UserDTO.class);

        UserResponse userResponse = new UserResponse(userDTO, MessageUtils.getFoundUserMessage(), 802);
        return ResponseEntity.ok(userResponse);
    }

    // Retrieve user, map to DTO, and build user response
    @GetMapping("/")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) throws NotFoundException {
        var user = service.getUserByEmail(email);
        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getType());

        UserResponse userResponse = new UserResponse(userDTO, MessageUtils.getFoundUserMessage(), 803);
        return ResponseEntity.ok(userResponse);
    }

    // Create user, build response map, and return response
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserDTO userDTO) throws BadRequestException {
        User createdUser = service.createUser(userDTO);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.getCreatedUserMessage(), 804);

        return ResponseEntity.ok(response);
    }

    // Delete user by email, build response map, and return response
    @DeleteMapping("/")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam("email") String email) throws NotFoundException {
        service.deleteUser(email);
        StringUtil.responseMap(MessageUtils.getDeletedUserMessage(), 811);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.getDeletedUserMessage(), 805);

        return ResponseEntity.ok(response);
    }

    // Reset user password, map to DTO, and build user response
    @PostMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse> resetPassword(@PathVariable Long userId, @RequestBody String newPassword)
            throws NotFoundException {
        User resetUser = service.resetPassword(userId, newPassword);

        UserDTO resetUserDTO = modelMapperConfig.map(resetUser, UserDTO.class);
        UserResponse userResponse = new UserResponse(resetUserDTO, MessageUtils.getPasswordResetMessage(), 806);

        return ResponseEntity.ok(userResponse);
    }

    // Change user password, map to DTO, and build user response
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<UserResponse> changePassword(@PathVariable Long userId,
            @RequestBody ChangePasswordRequest changePasswordRequest) throws NotFoundException, BadRequestException {
        User updatedUser = service.changePassword(userId, changePasswordRequest);
        UserDTO updatedUserDTO = modelMapperConfig.map(updatedUser, UserDTO.class);

        UserResponse userResponse = new UserResponse(updatedUserDTO, MessageUtils.getPasswordChangedMessage(), 807);
        return ResponseEntity.ok(userResponse);
    }

}