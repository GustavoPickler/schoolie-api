package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.dto.ClassInfoDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Student;
import com.api.users.model.Teacher;
import com.api.users.model.User;
import com.api.users.repository.StudentResponsibleRepository;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final StudentClassRepository classUserRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final StudentClassRepository studentClassRepository;
    private final StudentResponsibleRepository studentResponsibleRepository;

    public ClassEntity createClass(ClassDTO classDTO, Long teacherId) throws NotFoundException {
        Teacher teacher = (Teacher) userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));
        ClassEntity classEntity = new ClassEntity();
        classEntity.setDescription(classDTO.getDescription());
        classEntity.setName(classDTO.getName());
        classEntity.setOwner(teacher);
        classEntity.setRegisterDate(new Date());
        classEntity.setLastUpdate(new Date());

        if(classDTO.getPassword() != null)
            classEntity.setPassword(classDTO.getPassword());

        classRepository.save(classEntity);

        return classEntity;
    }

    public ClassEntity getClass(Long classId) throws NotFoundException {
        return classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
    }

    public Page<ClassInfoDTO> getUserClasses(Long userId, String searchValue, Pageable pageable) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Page<ClassEntity> classesPage = switch (user.getUserType()) {
            case TEACHER -> getClassesByTeacherPage(pageable, userId, searchValue);
            case STUDENT -> getClassesByStudentPage(pageable, userId, searchValue);
            case RESPONSIBLE -> getClassesByResponsiblePage(pageable, userId, searchValue);
        };

        List<Long> classIds = classesPage.getContent().stream()
                .map(ClassEntity::getId)
                .toList();

        Map<Long, Long> getClassIdAndStudentsAmount = classRepository.countStudentsByClassId(classIds);

        List<ClassInfoDTO> classInfoDTOs = classesPage.getContent().stream()
                .map(classInfo -> {
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setId(classInfo.getId());
                    classInfoDTO.setName(classInfo.getName());
                    classInfoDTO.setDescription(classInfo.getDescription());
                    classInfoDTO.setTeacherName(classInfo.getOwner().getUsername());
                    Long studentsCount = getClassIdAndStudentsAmount.computeIfAbsent(classInfo.getId(), id -> 0L);
                    classInfoDTO.setTotalStudents(studentsCount);
                    return classInfoDTO;
                })
                .toList();

        return new PageImpl<>(classInfoDTOs, pageable, classesPage.getTotalElements());
    }

    private Page<ClassEntity> getClassesByTeacherPage(Pageable pageable, Long teacherId, String searchValue) {
        List<ClassEntity> classes = classRepository.findByTeacherId(teacherId, searchValue, pageable);
        Long count = classRepository.countClassesByTeacherId(teacherId);
        return new PageImpl<>(classes, pageable, count);
    }

    private Page<ClassEntity> getClassesByResponsiblePage(Pageable pageable, Long responsibleId, String searchValue) {
        List<Student> students = studentResponsibleRepository.findStudentsByResponsibleId(responsibleId);

        Set<ClassEntity> uniqueClasses = students.stream()
                .flatMap(student -> getClassesByStudentPage(pageable, student.getId(), searchValue).getContent().stream())
                .collect(Collectors.toSet());

        List<ClassEntity> classes = new ArrayList<>(uniqueClasses);
        long count = uniqueClasses.size();

        return new PageImpl<>(classes, pageable, count);
    }

    private Page<ClassEntity> getClassesByStudentPage(Pageable pageable, Long studentId, String searchValue) {
        List<ClassEntity> classes = classRepository.findByStudentId(studentId, searchValue, pageable);
        Long count = classRepository.countClassesByStudentId(studentId);
        return new PageImpl<>(classes, pageable, count);
    }

    private boolean userIsOwner(User owner, ClassEntity classEntity) {
        return Objects.equals(classEntity.getOwner().getId(), owner.getId());
    }

    public ClassEntity deleteClass(Long teacherId, Long classId) throws NotFoundException {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        User owner = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));

        if (!userIsOwner(owner, classEntity))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        ClassEntity pClass = getClass(classId);
        teacherClassRepository.deleteByClass(classId);
        studentClassRepository.deleteByClass(classId);
        classRepository.delete(pClass);
        return pClass;
    }
}
