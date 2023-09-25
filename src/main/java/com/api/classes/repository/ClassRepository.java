package com.api.classes.repository;

import com.api.classes.model.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    @Query(value = "SELECT DISTINCT c.id AS id, c.name AS name, c.description AS description, c.password AS password " +
            "FROM classes c " +
            "LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
            "LEFT JOIN students_classes sc ON c.id = sc.class_id " +
            "WHERE tc.teacher_id = :userId OR sc.student_id = :userId ORDER BY c.id \\n-- #pageable\\n",
            countQuery = "SELECT count(*) FROM classes c " +
                    "LEFT JOIN teachers_classes tc ON c.id = tc.class_id " +
                    "LEFT JOIN students_classes sc ON c.id = sc.class_id " +
                    "WHERE tc.teacher_id = :userId OR sc.student_id = :userId",
            nativeQuery = true)
    Page<ClassEntity> findClassesByUserId(@Param("userId") Long userId, Pageable pageable);

}
