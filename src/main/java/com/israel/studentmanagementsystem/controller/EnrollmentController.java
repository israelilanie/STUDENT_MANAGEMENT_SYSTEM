package com.israel.studentmanagementsystem.controller;

import com.israel.studentmanagementsystem.dto.request.EnrollmentRequest;
import com.israel.studentmanagementsystem.dto.request.GradeRequest;
import com.israel.studentmanagementsystem.dto.response.EnrollmentResponse;
import com.israel.studentmanagementsystem.dto.response.GpaResponse;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Enrollments", description = "Enrollment and grading endpoints")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/student/enrollments")
    @Operation(summary = "Enroll in a course — STUDENT only")
    public ResponseEntity<EnrollmentResponse> enroll(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody EnrollmentRequest request) {

        return ResponseEntity.status(201).body(
                enrollmentService.enroll(
                        user.getEmail(), request.getCourseId()));
    }

    // student drops a course
    @DeleteMapping("/student/enrollments/{enrollmentId}")
    @Operation(summary = "Drop a course — STUDENT only")
    public ResponseEntity<EnrollmentResponse> drop(
            @AuthenticationPrincipal User user,
            @PathVariable Long enrollmentId) {

        return ResponseEntity.ok(
                enrollmentService.drop(
                        user.getEmail(), enrollmentId));
    }

    // student views active enrollments
    @GetMapping("/student/enrollments")
    @Operation(summary = "My active enrollments — STUDENT only")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                enrollmentService.getMyEnrollments(
                        user.getEmail()));
    }

    // student views full enrollment history
    @GetMapping("/student/enrollments/history")
    @Operation(summary = "My full enrollment history — STUDENT only")
    public ResponseEntity<List<EnrollmentResponse>> getMyHistory(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                enrollmentService.getMyEnrollmentHistory(
                        user.getEmail()));
    }

    // student views their GPA
    @GetMapping("/student/gpa")
    @Operation(summary = "My GPA — STUDENT only")
    public ResponseEntity<GpaResponse> getMyGpa(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                enrollmentService.getMyGpa(user.getEmail()));
    }

    // teacher views students in their course
    @GetMapping("/teacher/courses/{courseId}/enrollments")
    @Operation(summary = "Students enrolled in my course — TEACHER only")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.getCourseEnrollments(
                        user.getEmail(), courseId));
    }

    // teacher grades a student
    @PostMapping("/teacher/grades")
    @Operation(summary = "Grade a student enrollment — TEACHER only")
    public ResponseEntity<EnrollmentResponse> gradeEnrollment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody GradeRequest request) {

        return ResponseEntity.ok(
                enrollmentService.gradeEnrollment(
                        user.getEmail(), request));
    }
}
