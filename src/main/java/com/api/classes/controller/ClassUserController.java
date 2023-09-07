package com.api.classes.controller;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.ClassUser;
import com.api.classes.service.ClassUserService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classUser")
public class ClassUserController {

    private final ClassUserService classUserService;

    @PutMapping
    public ResponseEntity<ClassUser> addUser(@RequestParam Long classId, @RequestParam Long userId) throws NotFoundException {
        return ResponseEntity.ok(classUserService.addUser(classId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ClassEntity>> getUserClasses(@RequestParam Long userId) throws NotFoundException {
        return ResponseEntity.ok(classUserService.getUserClasses(userId));
    }

    @DeleteMapping
    public ResponseEntity<ClassUser> removeUserFromClass(@RequestParam Long classId, @RequestParam Long userId) throws NotFoundException {
        classUserService.deleteUserFromClass(classId, userId);
        return ResponseEntity.ok().build();
    }
}
