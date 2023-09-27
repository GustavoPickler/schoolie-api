package com.api.classes.repository.impl;

import com.api.classes.model.ClassEntity;
import com.api.classes.repository.ClassRepositoryCustom;
import com.api.users.model.Teacher;
import com.api.users.repository.UserRepository;
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

@Repository
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TEACHER_ID_PARAM = "teacherId";
    @Autowired
    public ClassRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String COUNT_CLASSES_BY_TEACHER_ID = "SELECT  c.id, COALESCE(totalStudents, 0) " +
            "FROM classes c " +
            "LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
            "LEFT JOIN users u_owner ON c.owner_id = u_owner.id " +
            "LEFT JOIN (" +
            "    SELECT class_id, COUNT(student_id) AS totalStudents " +
            "    FROM students_classes " +
            "    GROUP BY class_id " +
            ") sc ON c.id = sc.class_id "+
            "WHERE tc.teacher_id = :teacherId OR u_owner.id = :teacherId " +
            "group by c.id, sc.totalStudents";

    private static final String COUNT_CLASSES_BY_STUDENT_ID = "SELECT COUNT(c.id) AS totalClasses " +
            "FROM classes c " +
            "INNER JOIN students_classes sc ON c.id = sc.class_id " +
            "WHERE sc.student_id = :studentId";

    private static final String COUNT_STUDENTS_BY_CLASS_ID = "SELECT DISTINCT c.id, COUNT(c.id) AS totalStudents " +
            "FROM classes c " +
            "INNER JOIN students_classes sc ON c.id = sc.class_id " +
            "WHERE c.id IN (:classIds) " +
            "group by c.id";

    @Override
    public List<ClassEntity> findByTeacherId(Long teacherId, String searchValue, Pageable pageable) {
        final String FIND_BY_TEACHER_ID = "SELECT DISTINCT c.*, u.username AS ownerName " +
                "FROM classes c " +
                "LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
                "LEFT JOIN users u_teacher ON tc.teacher_id = u_teacher.id " +
                "LEFT JOIN users u ON c.owner_id = u.id " +
                "WHERE ((tc.teacher_id = :teacherId) OR (c.owner_id = :teacherId)) " +
                "AND (:searchValue IS NULL OR c.name LIKE :searchValue) " +
                "GROUP BY c.id, u.username " +
                "LIMIT :limit OFFSET :offset";

        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TEACHER_ID_PARAM, teacherId, Types.NUMERIC)
                .addValue("searchValue", searchValue, Types.VARCHAR)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        return namedParameterJdbcTemplate.query(FIND_BY_TEACHER_ID, params, (rs, row) -> {
            final ClassEntity classEntity = new ClassEntity();
            classEntity.setId(rs.getLong("id"));
            classEntity.setName(rs.getString("name"));
            classEntity.setDescription(rs.getString("description"));
            Teacher teacher = new Teacher();
            teacher.setUsername(rs.getString("ownerName"));
            classEntity.setOwner(teacher);
            return classEntity;
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
            Long classId = ((Number) row.get("id")).longValue();
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
    public List<ClassEntity> findByStudentId(Long studentId, String searchValue, Pageable pageable) {
        final String FIND_BY_STUDENT_ID = "SELECT DISTINCT c.*, u.username AS ownerName " +
                "FROM classes c " +
                "LEFT JOIN students_classes sc ON c.id = sc.class_id " +
                "LEFT JOIN users u_student ON sc.student_id = u_student.id " +
                "LEFT JOIN users u ON c.owner_id = u.id " +
                "WHERE (sc.student_id = :studentId) " +
                "AND (:searchValue IS NULL OR c.name LIKE :searchValue) " +
                "GROUP BY c.id, u.username " +
                "LIMIT :limit OFFSET :offset";

        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("studentId", studentId, Types.NUMERIC)
                .addValue("searchValue", searchValue, Types.VARCHAR)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        return namedParameterJdbcTemplate.query(FIND_BY_STUDENT_ID, params, (rs, row) -> {
            final ClassEntity classEntity = new ClassEntity();
            classEntity.setId(rs.getLong("id"));
            classEntity.setName(rs.getString("name"));
            classEntity.setDescription(rs.getString("description"));
            Teacher teacher = new Teacher();
            teacher.setUsername(rs.getString("ownerName"));
            classEntity.setOwner(teacher);
            return classEntity;
        });
    }

    @Override
    public Long countClassesByStudentId(Long studentId) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("studentId", studentId, Types.NUMERIC);

        AtomicReference<Long> count = new AtomicReference<>(0L);

        namedParameterJdbcTemplate.query(COUNT_CLASSES_BY_STUDENT_ID, params, (rs, row) -> {
            count.set(rs.getLong("totalClasses"));
            return null;
        });

        return count.get();
    }

}
