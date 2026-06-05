package com.israel.studentmanagementsystem.repository;

import com.israel.studentmanagementsystem.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherProfileRepository
        extends JpaRepository<TeacherProfile, Long> {

    Optional<TeacherProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("""
        SELECT tp FROM TeacherProfile tp
        JOIN FETCH tp.user
        WHERE tp.user.id = :userId
        """)
    Optional<TeacherProfile> findByUserIdWithUser(Long userId);
}