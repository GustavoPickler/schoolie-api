package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.exception.UserTypeException;
import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import com.api.classes.model.TeacherClass;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.classes.repository.TeacherClassRepository;
import com.api.users.exception.BadRequestException;
import com.api.users.exception.NotFoundException;
import com.api.users.model.Student;
import com.api.users.model.Teacher;
import com.api.users.model.User;
import com.api.users.model.UserType;
import com.api.users.repository.UserRepository;
import com.api.users.utils.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final StudentClassRepository studentClassRepository;
    private final ClassService classService;

    public List<ClassEntity> getTeacherClasses(Long teacherId) throws NotFoundException {
        userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));
        List<ClassEntity> classes = teacherClassRepository.findByTeacherId(teacherId);

        if (classes.isEmpty())
            throw new NotFoundException(ErrorCode.NO_CLASSES_FOUND);

        return classes;
    }

    public void addStudentInClass(Long classId, Long studentId, Long teacherId) throws NotFoundException, UserTypeException {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        if(!userIsTeacher(teacher)) {
            throw new UserTypeException(ErrorCode.USER_NOT_A_TEACHER);
        }

        if(!userIsStudent(student)) {
            throw new UserTypeException(ErrorCode.USER_NOT_A_TEACHER);
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setPClass(classEntity);
        studentClass.setStudent((Student) student);
        studentClassRepository.save(studentClass);
    }

    public void addTeacherToClass(Long classId, Long teacherId, Long ownerId) throws NotFoundException, UserTypeException {

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.OWNER_NOT_FOUND));

        if (!userIsOwner(owner, classEntity))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));

        if (!userIsTeacher(teacher))
            throw new UserTypeException(ErrorCode.USER_NOT_A_TEACHER);

        if (teacherClassRepository.existsByClassIdAndTeacherId(classId, teacherId))
            throw new BadRequestException(ErrorCode.TEACHER_ALREADY_IN_CLASS);

        TeacherClass teacherClass = new TeacherClass();
        teacherClass.setPClass(classEntity);
        teacherClass.setTeacher((Teacher) teacher);
        teacherClassRepository.save(teacherClass);
    }

    public void removeStudentFromClass(Long teacherId, Long classId, Long studentId) throws NotFoundException {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CLASS_NOT_FOUND));
        userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STUDENT_NOT_FOUND));
        User owner = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TEACHER_NOT_FOUND));

        if (!userIsOwner(owner, classEntity))
            throw new BadRequestException(ErrorCode.USER_NOT_THE_CLASS_OWNER);

        if (!studentClassRepository.existsByClassIdAndUserStudent(classId, studentId))
            throw new BadRequestException(ErrorCode.STUDENT_NOT_FOUND_IN_THIS_CLASS);

        studentClassRepository.deleteByClassAndUser(classId, studentId);
    }

    public void removeTeacherFromAllClasses(Long teacherId) {
        teacherClassRepository.deleteByTeacherId(teacherId);
    }

    public boolean userIsTeacher(User user) {
        String userTypeString = user.getUserType();
        return userTypeString.equalsIgnoreCase(UserType.TEACHER.name());
    }

    public boolean userIsStudent(User user) {
        String userTypeString = user.getUserType();
        return userTypeString.equalsIgnoreCase(UserType.STUDENT.name());
    }

    private boolean userIsOwner(User owner, ClassEntity classEntity) {
        return Objects.equals(classEntity.getOwner().getId(), owner.getId());
    }

}
