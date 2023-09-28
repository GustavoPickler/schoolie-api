package com.api.classes.repository;

import com.api.classes.model.ClassEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ClassRepositoryCustom {
    List<ClassEntity> findByTeacherId(Long teacherId, String searchValue, Pageable pageable);

    Long countClassesByTeacherId(Long teacherId);

    Map<Long, Long> countStudentsByClassId(List<Long> classId);

    List<ClassEntity> findByStudentId(Long studentId, String searchValue, Pageable pageable);

    Long countClassesByStudentId(Long studentId);

    boolean existsByCode(String codeId);
}
