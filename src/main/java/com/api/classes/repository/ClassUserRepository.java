package com.api.classes.repository;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.ClassUser;
import com.api.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ClassUserRepository extends JpaRepository<ClassUser, Long> {

    @Query("SELECT cu.pClass FROM ClassUser cu WHERE cu.user.id = :userId")
    List<ClassEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(cu) > 0 THEN true ELSE false END FROM ClassUser cu WHERE cu.pClass.id = :classId AND cu.user.id = :userId")
    boolean existsByClassIdAndUserId(@Param("classId") Long classId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM ClassUser cu WHERE cu.pClass.id = :classId AND cu.user.id = :userId")
    void deleteByClassAndUser(@Param("classId") Long classId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM ClassUser cu WHERE cu.pClass.id = :classId")
    void deleteByClass(@Param("classId") Long classId);

}