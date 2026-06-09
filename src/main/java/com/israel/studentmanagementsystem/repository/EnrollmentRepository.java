package com.israel.studentmanagementsystem.repository;

import com.israel.studentmanagementsystem.entity.Enrollment;
import com.israel.studentmanagementsystem.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {


    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.course c
        JOIN FETCH c.teacher t
        JOIN FETCH t.user
        WHERE e.student.id = :studentId
        AND e.status = :status
        """)
    List<Enrollment> findByStudentIdAndStatus(
            @Param("studentId") Long studentId,
            @Param("status") EnrollmentStatus status);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.course c
        JOIN FETCH c.teacher t
        JOIN FETCH t.user
        WHERE e.student.id = :studentId
        """)
    List<Enrollment> findAllByStudentId(
            @Param("studentId") Long studentId);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.student s
        JOIN FETCH s.user
        WHERE e.course.id = :courseId
        AND e.status = :status
        """)
    List<Enrollment> findByCourseIdAndStatus(
            @Param("courseId") Long courseId,
            @Param("status") EnrollmentStatus status);


    Optional<Enrollment> findByStudentIdAndCourseId(
            Long studentId, Long courseId);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.course
        WHERE e.student.id = :studentId
        AND e.status = 'COMPLETED'
        AND e.gradePoints IS NOT NULL
        """)
    List<Enrollment> findCompletedWithGrades(
            @Param("studentId") Long studentId);

    Long countByStatus(EnrollmentStatus status);

    Long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    @Query("""
    SELECT e FROM Enrollment e
    JOIN FETCH e.student s
    JOIN FETCH s.user
    WHERE e.course.id = :courseId
    AND e.status = 'COMPLETED'
    AND e.gradePoints IS NOT NULL
    """)
    List<Enrollment> findCompletedByCourseId(@Param("courseId") Long courseId);

}