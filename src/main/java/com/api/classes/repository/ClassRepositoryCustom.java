package com.api.classes.repository;

import com.api.classes.model.ClassEntity;

import java.util.List;

public interface ClassRepositoryCustom {
    List<ClassEntity> findByTeacherId(Long teacherId);

    Long countByTeacherId(Long teacherId);
}
