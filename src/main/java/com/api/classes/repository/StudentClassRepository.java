package com.api.classes.repository;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StudentClassRepository extends JpaRepository<StudentClass, Long>  {

    @Query("SELECT sc.pClass FROM StudentClass sc WHERE sc.student.id = :studentId")
    List<ClassEntity> findByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Query("DELETE FROM StudentClass sc WHERE sc.pClass.id = :classId AND sc.student.id = :studentId")
    void deleteByClassAndUser(@Param("classId") Long classId, @Param("studentId") Long studentId);

    @Modifying
    @Query("DELETE FROM StudentClass sc WHERE sc.pClass.id = :classId")
    void deleteByClass(@Param("classId") Long classId);

    @Modifying
    @Query("DELETE FROM StudentClass sc WHERE sc.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT CASE WHEN COUNT(sc) > 0 THEN true ELSE false END FROM StudentClass sc WHERE sc.pClass.id = :classId AND sc.student.id = :studentId")
    boolean existsByClassIdAndUserStudent(@Param("classId") Long classId, @Param("studentId") Long studentId);

}