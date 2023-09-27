package com.api.users.service;

import com.api.classes.model.ClassEntity;
import com.api.classes.repository.StudentClassRepository;
import com.api.users.exception.NotFoundException;
import com.api.users.exception.ResponsiblesException;
import com.api.users.model.Responsible;
import com.api.users.model.Student;
import com.api.users.model.StudentResponsible;
import com.api.users.repository.StudentResponsibleRepository;
import com.api.users.repository.UserRepository;
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
public class StudentResponsibleService {

    private final UserRepository userRepository;
    private final StudentResponsibleRepository studentResponsibleRepository;
    private final StudentClassRepository studentClassRepository;
    public List<Responsible> getStudentBonds(Long studentId) {
        return studentResponsibleRepository.findResponsiblesByStudentId(studentId);
    }

    public List<Student> getResponsibleBonds(Long userId) {
        return studentResponsibleRepository.findStudentsByResponsibleId(userId);
    }

    public void linkResponsibleToStudent(Long studentId, Long responsibleId) throws NotFoundException, ResponsiblesException {
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STUDENT_NOT_FOUND));
        Responsible responsible = (Responsible) userRepository.findById(responsibleId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESPONSIBLE_NOT_FOUND));

        if (studentResponsibleRepository.countResponsiblesByStudentId(studentId) >= 3) {
            throw new ResponsiblesException(ErrorCode.TOO_MANY_RESPONSIBLES);
        }

        StudentResponsible studentResponsible = new StudentResponsible();
        studentResponsible.setResponsible(responsible);
        studentResponsible.setStudent(student);
        studentResponsibleRepository.save(studentResponsible);
    }



}
