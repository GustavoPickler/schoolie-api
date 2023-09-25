package com.api.classes.service;

import com.api.classes.dto.ClassDTO;
import com.api.classes.dto.ClassInfoDTO;
import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepository;
import com.api.classes.repository.StudentClassRepository;
import com.api.users.exception.NotFoundException;
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

import java.util.List;
import java.util.Map;

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

    public Page<ClassInfoDTO> getUserClasses(Long userId, Pageable pageable) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Page<ClassEntity> classesPage = switch (user.getUserType()) {
            case "Teacher" -> getClassesByTeacherPage(pageable, userId);
            case "Responsible" -> null; //TODO: implementar consultas pageable responsible
            case "Student" -> null; //TODO: implementar consultas pageable student
            default -> throw new NotFoundException(ErrorCode.INVALID_USER_TYPE);
        };

        Map<Long, Long> mapClassIdByStudents = null; //faz um metodo pra retornar um Map<Long, List<Student>> que a chave é o id da classe e o value é a lista de alunos em sala

        //map to classinfodto utilizar o map pra popular o countStudents e usar o content do page classentity pra popular valores da class no dto
        return null;
    }

    private Page<ClassEntity> getClassesByTeacherPage(Pageable pageable, Long userId) {
        List<ClassEntity> classes = classRepository.findByTeacherId(userId);
        Long count = classRepository.countByTeacherId(userId);
        return new PageImpl<>(classes, pageable, count);
    }

}
