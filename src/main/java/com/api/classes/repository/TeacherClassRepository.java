package com.api.classes.repository;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.TeacherClass;
import com.api.users.model.Teacher;
import com.api.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TeacherClassRepository extends JpaRepository<TeacherClass, Long> {

    @Query("SELECT tc.pClass FROM TeacherClass tc WHERE tc.teacher.id = :teacherId")
    List<ClassEntity> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT CASE WHEN COUNT(tc) > 0 THEN true ELSE false END FROM TeacherClass tc WHERE tc.pClass.id = :classId AND tc.teacher.id = :teacherId")
    boolean existsByClassIdAndTeacherId(@Param("classId") Long classId, @Param("teacherId") Long teacherId);

    @Modifying
    @Query("DELETE FROM TeacherClass tc WHERE tc.pClass.id = :classId AND tc.teacher.id = :teacherId")
    void deleteByClassAndTeacher(@Param("classId") Long classId, @Param("teacherId") Long teacherId);

    @Modifying
    @Query("DELETE FROM TeacherClass tc WHERE tc.pClass.id = :classId")
    void deleteByClass(@Param("classId") Long classId);

    @Modifying
    @Query("DELETE FROM TeacherClass tc WHERE tc.teacher.id = :teacherId")
    void deleteByTeacherId(@Param("teacherId") Long teacherId);

    @Query(value = "SELECT u.username FROM classes c " +
            "JOIN teachers_classes tc ON c.id = tc.class_id " +
            "JOIN users u ON tc.teacher_id = u.id " +
            "WHERE tc.teacher_id = :userId", nativeQuery = true)
    String findTeacherByClassId(@Param("userId") Long userId);
}