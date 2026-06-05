package com.israel.studentmanagementsystem.repository;


import com.israel.studentmanagementsystem.entity.Course;
import com.israel.studentmanagementsystem.enums.CourseStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    Optional<Course> findByCode(String code);

    List<Course> findAllByStatus(CourseStatus status);

    List<Course> findAllByTeacherId(Long teacherId);


    @Query("""
        SELECT c FROM Course c
        JOIN FETCH c.teacher t
        JOIN FETCH t.user
        WHERE c.status = :status
        AND (:search IS NULL
            OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<Course> searchCourses(
            @Param("search") String search,
            @Param("status") CourseStatus status,
            Pageable pageable);

    @Query("""
        SELECT c FROM Course c
        JOIN FETCH c.teacher t
        JOIN FETCH t.user
        WHERE c.id = :id
        """)
    Optional<Course> findByIdWithTeacher(@Param("id") Long id);

    @Query("""
        SELECT c FROM Course c
        JOIN FETCH c.teacher t
        JOIN FETCH t.user
        WHERE t.id = :teacherId
        AND c.status = :status
        """)
    List<Course> findByTeacherIdAndStatus(
            @Param("teacherId") Long teacherId,
            @Param("status") CourseStatus status);
}