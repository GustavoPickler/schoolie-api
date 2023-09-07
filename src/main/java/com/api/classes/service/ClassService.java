package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.classes.repository.ClassUserRepository;
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
    private final ClassUserRepository classUserRepository;

    public ClassEntity getClass(Long classId) throws NotFoundException {
        return classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
    }

    public ClassEntity createClass(ClassDTO classDTO) throws BadRequestException {
        ClassEntity pClass = new ClassEntity();
        pClass.setDescription(classDTO.getDescription());
        pClass.setName(classDTO.getName());
        pClass.setPassword(classDTO.getPassword());

        if(classDTO.getPassword() != null)
            pClass.setPassword(classDTO.getPassword());

        classRepository.save(pClass);
        return pClass;
    }

    public ClassEntity deleteClass(Long classId) throws NotFoundException {
        ClassEntity pClass = getClass(classId);
        classUserRepository.deleteByClass(classId);
        classRepository.delete(pClass);
        return pClass;
    }

}
