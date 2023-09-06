package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.config.mapper.ClassInfoMapper;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.ClassUser;
import com.api.classes.model.response.ClassInfo;
import com.api.classes.repository.ClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.User;
import com.api.classes.repository.ClassUserRepository;
import com.api.users.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ClassUserRepository classUserRepository;
    private final ClassInfoMapper classInfoMapper;

    public void createClass(ClassDTO classDTO) throws BadRequestException {
        ClassEntity pClass = new ClassEntity();
        pClass.setDescription(classDTO.getDescription());
        pClass.setName(classDTO.getName());
        pClass.setPassword(classDTO.getPassword());

        if(classDTO.getPassword() != null)
            pClass.setPassword(classDTO.getPassword());

        classRepository.save(pClass);
    }

    public void addUser(Long classId, Long userId) throws NotFoundException {
        ClassEntity pClass = classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ClassEntity.class, classId, 601));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId, 701));

        if (classUserRepository.existsByClassIdAndUserId(classId, userId))
            throw new BadRequestException("User is already in the class.", 703);

        ClassUser classUser = new ClassUser();
        classUser.setPClass(pClass);
        classUser.setUser(user);
        classUserRepository.save(classUser);
    }

    public List<ClassInfo> getUserClasses(Long userId) {
        List<ClassEntity> userClasses = classUserRepository.findByUserId(userId);

        return userClasses.stream()
                .map(ClassEntity::toClassInfo)
                .collect(Collectors.toList());
    }

    public ClassInfo getClass(Long classId) throws NotFoundException{
        ClassEntity pClass =
            classRepository.findById(classId).orElseThrow(() -> new NotFoundException(User.class, classId, 702));

        return pClass.toClassInfo();
    }

    public void deleteUserFromClass(Long classId, Long userId) throws NotFoundException {
        classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ClassEntity.class, classId, 601));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId, 701));

        classUserRepository.deleteByClassAndUser(classId, userId);
    }

    public void deleteClass(Long classId) throws NotFoundException {
        ClassEntity pClass = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ClassEntity.class, classId, 601));

        classUserRepository.deleteByClass(classId);
        classRepository.delete(pClass);
    }

}
