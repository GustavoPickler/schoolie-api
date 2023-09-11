package com.api.classes.controller;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.model.response.PasswordResponse;
import com.api.classes.service.StudentClassService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes/student")
public class StudentClassController {

    private final StudentClassService studentClassService;

    @GetMapping
    public ResponseEntity<List<ClassEntity>> getClasses(@RequestParam Long studentId) throws NotFoundException {
        return ResponseEntity.ok(studentClassService.getStudentClasses(studentId));
    }

    @PutMapping
    public ResponseEntity<Void> enterClass(@RequestParam Long classId, @RequestParam Long studentId, @RequestBody PasswordResponse passwordResponse) throws NotFoundException {
        studentClassService.enterClass(classId, studentId, passwordResponse.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<StudentClass> leaveClass(@PathVariable Long studentId, @RequestParam Long classId) throws NotFoundException {
        studentClassService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok().build();
    }

}
