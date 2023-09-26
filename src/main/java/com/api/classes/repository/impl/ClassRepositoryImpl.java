package com.api.classes.repository.impl;

import com.api.Utils.QueryUtils;
import com.api.classes.dto.ClassInfoDTO;
import com.api.classes.repository.ClassRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TEACHER_ID_PARAM = "teacherId";
    @Autowired
    public ClassRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String COUNT_CLASSES_BY_TEACHER_ID = "SELECT COUNT(*) as count " +
            "FROM ( " +
            "    SELECT DISTINCT c.id " +
            "    FROM classes c " +
            "    LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
            "    LEFT JOIN users u_teacher ON tc.teacher_id = u_teacher.id " +
            "    LEFT JOIN users u_owner ON c.owner_id = u_owner.id " +
            "    WHERE tc.teacher_id = :teacherId OR u_owner.id = :ownerId " +
            ") AS subquery";

    private static final String COUNT_STUDENTS_BY_CLASS_ID = "SELECT DISTINCT c.id, COUNT(c.id)\n" +
            "FROM classes c " +
            "INNER JOIN students_classes sc ON c.id = sc.class_id " +
            "WHERE c.id = :classId " +
            "group by c.id";

    @Override
    public List<ClassInfoDTO> findByTeacherId(Long teacherId, String searchValue, Pageable pageable) {
        final String FIND_BY_TEACHER_ID = "SELECT DISTINCT c.*,  u_owner.username AS ownerName " +
                "FROM classes c " +
                "LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
                "LEFT JOIN users u_teacher ON tc.teacher_id = u_teacher.id " +
                "LEFT JOIN users u_owner ON c.owner_id = u_owner.id " +
                "WHERE (tc.teacher_id = :teacherId OR u_owner.id = :ownerId) " +
                "AND (c.name LIKE :searchValue) " +
                "GROUP BY c.id, u_owner.username" + QueryUtils.createQueryPagination(pageable);

        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TEACHER_ID_PARAM, teacherId, Types.NUMERIC)
                .addValue("searchValue", "%" + searchValue + "%"); // Assuming a partial match is desired

        return namedParameterJdbcTemplate.query(FIND_BY_TEACHER_ID, params, (rs,row) -> {
            final ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setId(rs.getLong("id"));
            classInfoDTO.setName(rs.getString("name"));
            return classInfoDTO;
        });
    }

    @Override
    public Map<Long, Long> countStudentsByClassId(List<Long> classIds) {
        if (classIds.isEmpty()) {
            return Collections.emptyMap();
        }

        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("classIds", classIds);

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(COUNT_STUDENTS_BY_CLASS_ID, params);

        Map<Long, Long> resultMap = new HashMap<>();
        for (Map<String, Object> row : results) {
            Long classId = ((Number) row.get("class_id")).longValue();
            Long totalStudents = ((Number) row.get("totalStudents")).longValue();
            resultMap.put(classId, totalStudents);
        }

        return resultMap;
    }

    @Override
    public Long countClassesByTeacherId(Long teacherId) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TEACHER_ID_PARAM, teacherId, Types.NUMERIC);

        AtomicReference<Long> count = new AtomicReference<>(0L);

        namedParameterJdbcTemplate.query(COUNT_CLASSES_BY_TEACHER_ID, params, (rs, row) -> {
            count.set(rs.getLong("id"));
            return null;
        });

        return count.get();
    }

    @Override
    public List<ClassInfoDTO> findByResponsibleId(Long responsibleId) {
        return null;
    }

    @Override
    public Long countByResponsibleId(Long responsibleId) {
        return null;
    }

    @Override
    public List<ClassInfoDTO> findByStudentId(Long responsibleId) {
        return null;
    }

    @Override
    public Long countByStudentId(Long responsibleId) {
        return null;
    }
}
