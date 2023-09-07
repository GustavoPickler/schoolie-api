package com.api.users.controller;

import com.api.users.dto.UserDTO;
import com.api.users.dto.request.ChangePasswordRequest;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.model.UserType;
import com.api.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Retrieve user, map to DTO, and build user response
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    // Retrieve all users, map to DTOs, and return response
    @GetMapping
    public ResponseEntity<User> getUserById(@RequestParam Long userId) throws NotFoundException {
        return ResponseEntity.ok(service.getUser(userId));
    }

    // Retrieve user, map to DTO, and build user response
    @GetMapping("/userByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) throws NotFoundException {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    private ResponseEntity<User> createUser(UserDTO userDTO, UserType type) throws BadRequestException {
        return ResponseEntity.ok(service.createUser(userDTO, type));
    }

    // Create teacher, build response map, and return response
    @PostMapping("/teacher")
    public ResponseEntity<User> createTeacher(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, UserType.TEACHER);
    }

    // Create student, build response map, and return response
    @PostMapping("/student")
    public ResponseEntity<User> createStudent(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, UserType.STUDENT);
    }

    // Create responsible, build response map, and return response
    @PostMapping("/responsible")
    public ResponseEntity<User> createResponsible(@RequestBody UserDTO userDTO) throws BadRequestException {
        return createUser(userDTO, UserType.RESPONSIBLE);
    }

    // Delete user by email, build response map, and return response
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam Long userId) throws NotFoundException {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // Reset user password, map to DTO, and build user response
    @PutMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestParam Long userId, @RequestBody String newPassword)
            throws NotFoundException {
        service.resetPassword(userId, newPassword);
        return ResponseEntity.ok().build();
    }

    // Change user password, map to DTO, and build user response
    @PutMapping
    public ResponseEntity<Void> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest changePasswordRequest)
            throws NotFoundException, BadRequestException {
        service.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

}