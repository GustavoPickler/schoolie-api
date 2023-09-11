package com.api.classes.controller;

import com.api.classes.dto.ClassDTO;
import com.api.classes.exception.UserTypeException;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.model.TeacherClass;
import com.api.classes.service.StudentClassService;
import com.api.classes.service.TeacherClassService;
import com.api.users.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes/teacher")
public class TeacherClassController {

    private final TeacherClassService teacherClassService;

    @PostMapping("/{teacherId}")
    public ResponseEntity<ClassEntity> createClass(@PathVariable Long teacherId, @RequestBody ClassDTO classDTO) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.createClass(classDTO, teacherId));
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<Void> addStudentIntoClass(@RequestParam Long classId, @RequestParam Long studentId, @PathVariable Long teacherId) throws NotFoundException, UserTypeException {
        teacherClassService.addStudentInClass(classId, studentId, teacherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ClassEntity>> getTeacherClasses(@RequestParam Long teacherId) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.getTeacherClasses(teacherId));
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<StudentClass> removeStudentFromClass(@PathVariable Long teacherId, @RequestParam Long classId, @RequestParam Long userId) throws NotFoundException {
        teacherClassService.removeStudentFromClass(teacherId, classId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}/deleteClass")
    public ResponseEntity<ClassEntity> removeClass(@PathVariable Long teacherId, @RequestParam Long classId) throws NotFoundException {
        return ResponseEntity.ok(teacherClassService.deleteClass(teacherId, classId));
    }
}
