package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.dto.ClassInfoDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.TeacherClass;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Teacher;
import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public ClassEntity createClass(ClassDTO classDTO, Long teacherId) throws NotFoundException {
        Teacher teacher = (Teacher) userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));
        ClassEntity classEntity = new ClassEntity();
        classEntity.setDescription(classDTO.getDescription());
        classEntity.setName(classDTO.getName());
        classEntity.setOwnerId(teacher.getId());
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

        Page<ClassInfoDTO> classesPage = switch (user.getUserType()) {
            case "Teacher" -> getClassesByTeacherPage(pageable, searchValue, userId);
            case "Responsible" -> getClassesByResponsiblePage(pageable, userId);
            case "Student" -> getClassesByStudentPage(pageable, userId);
            default -> throw new NotFoundException(ErrorCode.INVALID_USER_TYPE);
        };

        List<Long> classIds = classesPage.getContent().stream()
                .map(ClassInfoDTO::getId)
                .toList();

        Map<Long, Long> getClassIdAndStudentsAmount = classRepository.countStudentsByClassId(classIds);

        List<ClassInfoDTO> classInfoDTOs = classesPage.getContent().stream()
                .map(classInfo -> {
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setId(classInfo.getId());
                    classInfoDTO.setName(classInfo.getName());
                    classInfoDTO.setDescription(classInfo.getDescription());
                    classInfoDTO.setTeacherName(classInfo.getTeacherName());
                    Long studentsCount = getClassIdAndStudentsAmount.computeIfAbsent(classInfo.getId(), id -> 0L);
                    classInfoDTO.setStudentsCount(studentsCount);
                    return classInfoDTO;
                })
                .toList();

        return new PageImpl<>(classInfoDTOs, pageable, classesPage.getTotalElements());
    }

    private Page<ClassInfoDTO> getClassesByTeacherPage(Pageable pageable, String searchValue, Long userId) {
         List<ClassInfoDTO> classes = classRepository.findByTeacherId(userId, searchValue, pageable);
        Long count = classRepository.countClassesByTeacherId(userId);
        return new PageImpl<>(classes, pageable, count);
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

    private Page<ClassInfoDTO> getClassesByResponsiblePage(Pageable pageable, Long userId) {
        List<ClassInfoDTO> classes = classRepository.findByResponsibleId(userId);
        Long count = classRepository.countByResponsibleId(userId);
        return new PageImpl<>(classes, pageable, count);
    }

    private Page<ClassInfoDTO> getClassesByStudentPage(Pageable pageable, Long userId) {
        List<ClassInfoDTO> classes = classRepository.findByStudentId(userId);
        Long count = classRepository.countByStudentId(userId);
        return new PageImpl<>(classes, pageable, count);
    }

    private boolean userIsOwner(User owner, ClassEntity classEntity) {
        return Objects.equals(classEntity.getOwnerId(), owner.getId());
    }

}
