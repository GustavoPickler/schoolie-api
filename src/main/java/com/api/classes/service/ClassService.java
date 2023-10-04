package com.api.classes.service;

import com.api.auth.utils.SecurityUtil;
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
    private final CodeGeneratorService codeGeneratorService;

    public ClassEntity createClass(ClassDTO classDTO) {
        Teacher teacher = (Teacher) SecurityUtil.getCurrentUser();

        ClassEntity classEntity = new ClassEntity();
        classEntity.setDescription(classDTO.getDescription());
        classEntity.setName(classDTO.getName());
        classEntity.setOwner(teacher);
        classEntity.setCode(codeGeneratorService.generateUniqueCode());

        if (classDTO.getPassword() != null) {
            classEntity.setPassword(classDTO.getPassword());
        }

        classRepository.save(classEntity);

        return classEntity;
    }

    public ClassEntity getClass(Long classId) throws NotFoundException {
        return classRepository.findById(classId).orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
    }

    public Page<ClassInfoDTO> getUserClasses(String searchValue, Pageable pageable) {
        User user = SecurityUtil.getCurrentUser();

        Page<ClassEntity> classesPage = switch (user.getUserType()) {
            case TEACHER -> getClassesByTeacherPage(pageable, searchValue);
            case STUDENT -> getClassesByStudentPage(pageable, searchValue);
            case RESPONSIBLE -> getClassesByResponsiblePage(pageable, searchValue);
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

    private Page<ClassEntity> getClassesByTeacherPage(Pageable pageable, String searchValue) {
        User teacher = SecurityUtil.getCurrentUser();
        List<ClassEntity> classes = classRepository.findByTeacherId(teacher.getId(), searchValue, pageable);
        Long count = classRepository.countClassesByTeacherId(teacher.getId());
        return new PageImpl<>(classes, pageable, count);
    }

    private Page<ClassEntity> getClassesByResponsiblePage(Pageable pageable, String searchValue) {
        User responsible = SecurityUtil.getCurrentUser();
        List<Student> students = studentResponsibleRepository.findStudentsByResponsibleId(responsible.getId());

        Set<ClassEntity> uniqueClasses = students.stream()
                .flatMap(student -> getClassesByStudentPage(pageable, searchValue).getContent().stream())
                .collect(Collectors.toSet());

        List<ClassEntity> classes = new ArrayList<>(uniqueClasses);
        long count = uniqueClasses.size();

        return new PageImpl<>(classes, pageable, count);
    }

    private Page<ClassEntity> getClassesByStudentPage(Pageable pageable, String searchValue) {
        Student student = (Student) SecurityUtil.getCurrentUser();
        List<ClassEntity> classes = classRepository.findByStudentId(student.getId(), searchValue, pageable);
        Long count = classRepository.countClassesByStudentId(student.getId());
        return new PageImpl<>(classes, pageable, count);
    }

    private boolean userIsOwner(Teacher owner, ClassEntity classEntity) {
        return Objects.equals(classEntity.getOwner().getId(), owner.getId());
    }

    public ClassEntity deleteClass(Long classId) throws NotFoundException {
        Teacher owner = (Teacher) SecurityUtil.getCurrentUser();
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        if (!userIsOwner(owner, classEntity))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        ClassEntity pClass = getClass(classId);
        teacherClassRepository.deleteByClass(classId);
        studentClassRepository.deleteByClass(classId);
        classRepository.delete(pClass);
        return pClass;
    }
}
