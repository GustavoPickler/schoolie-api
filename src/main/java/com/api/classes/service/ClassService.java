package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.classes.repository.StudentClassRepository;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final StudentClassRepository classUserRepository;

    public ClassEntity getClass(Long classId) throws NotFoundException {
        return classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
    }

}
