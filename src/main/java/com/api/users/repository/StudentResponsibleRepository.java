package com.api.users.repository;

import com.api.classes.model.ClassEntity;
import com.api.users.model.Responsible;
import com.api.users.model.Student;
import com.api.users.model.StudentResponsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StudentResponsibleRepository extends JpaRepository<StudentResponsible, Long> {

    @Query("SELECT sr.responsible FROM StudentResponsible sr WHERE sr.student.id = :studentId")
    List<Responsible> findResponsiblesByStudentId(Long studentId);

    @Query("SELECT sr.student FROM StudentResponsible sr WHERE sr.responsible.id = :responsibleId")
    List<Student> findStudentsByResponsibleId(Long responsibleId);

    @Query("SELECT COUNT(sr) FROM StudentResponsible sr WHERE sr.student.id = :studentId")
    Long countResponsiblesByStudentId(Long studentId);

}