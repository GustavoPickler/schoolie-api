package com.api.classes.repository;

import com.api.classes.dto.ClassInfoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ClassRepositoryCustom {
    List<ClassInfoDTO> findByTeacherId(Long teacherId, String searchValue, Pageable pageable);

    Long countClassesByTeacherId(Long teacherId);

    Map<Long, Long> countStudentsByClassId(List<Long> classId);

    List<ClassInfoDTO> findByResponsibleId(Long responsibleId);

    Long countByResponsibleId(Long responsibleId);

    List<ClassInfoDTO> findByStudentId(Long responsibleId);

    Long countByStudentId(Long responsibleId);
}
