package com.api.classes.controller;

import com.api.classes.dto.ClassDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.service.ClassService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassDTO classDTO) {
        return ResponseEntity.ok(classService.createClass(classDTO));
    }

    @GetMapping
    public ResponseEntity<ClassEntity> getClass(@RequestParam Long classId) throws NotFoundException {
        return ResponseEntity.ok(classService.getClass(classId));
    }

    @DeleteMapping
    public ResponseEntity<ClassEntity> removeClass(@RequestParam Long classId) throws NotFoundException {
        return ResponseEntity.ok(classService.deleteClass(classId));
    }
}
