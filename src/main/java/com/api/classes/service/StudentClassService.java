package com.api.classes.service;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Student;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final StudentClassRepository studentClassRepository;

    public List<ClassEntity> getStudentClasses(Long studentId) throws NotFoundException {
        List<ClassEntity> classes = studentClassRepository.findByStudentId(studentId);

        if (classes.isEmpty())
            throw new NotFoundException(ErrorCode.NO_CLASSES_FOUND);

        return classes;
    }

    public void enterClass(Long classId, Long studentId, String password) throws NotFoundException {
        ClassEntity pClass = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        if (studentClassRepository.existsByClassIdAndUserStudent(classId, studentId))
            throw new BadRequestException(ErrorCode.USER_ALREADY_IN_CLASS);

        if (StringUtils.isNotBlank(pClass.getPassword()) && !pClass.getPassword().matches(password))
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);

        StudentClass studentClass = new StudentClass();
        studentClass.setPClass(pClass);
        studentClass.setStudent(student);
        studentClassRepository.save(studentClass);
    }

    public void removeStudentFromClass(Long classId, Long studentId) throws NotFoundException {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        if (!studentClassRepository.existsByClassIdAndUserStudent(classId, studentId))
            throw new BadRequestException(ErrorCode.STUDENT_NOT_FOUND_IN_THIS_CLASS);

        studentClassRepository.deleteByClassAndUser(classId, studentId);
    }

    public void removeStudentFromAllClasses(Long studentId) {
        studentClassRepository.deleteByStudentId(studentId);
    }

    public int countStudentsByClassId(Long classId) {
        return studentClassRepository.countStudentsByClassId(classId);
    }
}
