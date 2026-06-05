package com.israel.studentmanagementsystem.repository;

import com.israel.studentmanagementsystem.entity.StudentProfile;
import com.israel.studentmanagementsystem.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserId(Long userId);

    Optional<StudentProfile> findByStudentNumber(String studentNumber);

    boolean existsByUserId(Long userId);

    List<StudentProfile> findAllByStatus(StudentStatus status);

    @Query("""
        SELECT sp FROM StudentProfile sp
        JOIN FETCH sp.user
        WHERE sp.user.id = :userId
        """)
    Optional<StudentProfile> findByUserIdWithUser(Long userId);
}