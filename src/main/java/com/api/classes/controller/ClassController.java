package com.api.classes.controller;

import com.api.classes.dto.ClassDTO;
import com.api.classes.model.request.UserIdRequest;
import com.api.classes.model.response.ClassInfo;
import com.api.classes.service.ClassService;
import com.api.users.exception.NotFoundException;
import com.api.users.utils.MessageUtils;
import com.api.users.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService service;

    @PostMapping("/createClass")
    public ResponseEntity<Map<String, Object>> createClass(@RequestBody ClassDTO classDTO) {
        service.createClass(classDTO);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.CREATED_CLASS_SUCCESS_MESSAGE, 601);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/addUser/{classId}")
    public ResponseEntity<Map<String, Object>> addUser(@PathVariable Long classId, @RequestBody UserIdRequest request) throws NotFoundException {
        Long userId = request.getUserId();
        service.addUser(classId, userId);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.ADDED_USER_SUCCESS_MESSAGE, 602);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userClasses")
    public ResponseEntity<List<ClassInfo>> getUserClasses(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getUserClasses(userId));
    }

    @GetMapping("/class")
    public ResponseEntity<ClassInfo> getClass(@RequestParam Long classId) throws NotFoundException {
        return ResponseEntity.ok(service.getClass(classId));
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<Map<String, Object>> removeUserFromClass(@RequestParam Long classId, @RequestParam Long userId) throws NotFoundException {
        service.deleteUserFromClass(classId, userId);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.REMOVED_USER_SUCCESS_MESSAGE, 603);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/removeClass")
    public ResponseEntity<Map<String, Object>> removeClass(@RequestParam Long classId) throws NotFoundException {
        service.deleteClass(classId);
        Map<String,Object> response = StringUtil.responseMap(MessageUtils.REMOVED_CLASS_SUCCESS_MESSAGE, 604);
        return ResponseEntity.ok(response);
    }
}
