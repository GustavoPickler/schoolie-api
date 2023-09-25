package com.api.classes.repository.impl;

import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ClassRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String FIND_BY_TEACHER_ID = "SELECT c.*, t.name as teacherName, t.id AS teacherId from class c " +
            "INNER JOIN teachers_classes tc ON tc.class_id = c.id " +
            "INNER JOIN teacher t ON t.id = tc.teacher_id " +
            "WHERE tc.teacher_id = :teacherId";

    @Override
    public List<ClassEntity> findByTeacherId(Long teacherId) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("teacherId", teacherId, Types.NUMERIC);

        return namedParameterJdbcTemplate.query(FIND_BY_TEACHER_ID, params, (rs,row) -> {
            final ClassEntity classEntity = new ClassEntity();
            classEntity.setId(rs.getLong("id"));
            classEntity.setName(rs.getString("name"));
            return classEntity;
        });
    }

    @Override
    public Long countByTeacherId(Long teacherId) {
        //TODO: implementar count para paginação
        return null;
    }
}
