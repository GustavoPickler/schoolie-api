package com.api.classes.service;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.ClassUser;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.ClassUserRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.service.UserService;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassUserService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ClassUserRepository classUserRepository;

    public List<ClassEntity> getUserClasses(Long userId) throws NotFoundException {
        List<ClassEntity> classes = classUserRepository.findByUserId(userId);

        if (classes.isEmpty())
            throw new NotFoundException(ErrorCode.NO_CLASSES_FOUND);

        return classes;
    }

    public ClassUser addUser(Long classId, Long userId) throws NotFoundException {
        ClassEntity pClass = classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (classUserRepository.existsByClassIdAndUserId(classId, userId))
            throw new BadRequestException(ErrorCode.USER_ALREADY_IN_CLASS);

        ClassUser classUser = new ClassUser();
        classUser.setPClass(pClass);
        classUser.setUser(user);
        classUserRepository.save(classUser);

        return classUser;
    }

    public void deleteUserFromClass(Long classId, Long userId) throws NotFoundException {
        classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!classUserRepository.existsByClassIdAndUserId(classId, userId))
            throw new BadRequestException(ErrorCode.USER_DONT_EXISTS_IN_CLASS);

        classUserRepository.deleteByClassAndUser(classId, userId);
    }

}
