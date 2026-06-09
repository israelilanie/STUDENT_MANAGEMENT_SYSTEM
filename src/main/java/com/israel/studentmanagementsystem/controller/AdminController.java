package com.israel.studentmanagementsystem.controller;

import com.israel.studentmanagementsystem.dto.request.CreateTeacherRequest;
import com.israel.studentmanagementsystem.dto.response.*;
import com.israel.studentmanagementsystem.service.ReportService;
import com.israel.studentmanagementsystem.service.StudentService;
import com.israel.studentmanagementsystem.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ReportService reportService;

    @GetMapping("/reports/overview")
    @Operation(summary = "System overview report — ADMIN only")
    public ResponseEntity<OverviewReportResponse> getOverview() {
        return ResponseEntity.ok(reportService.getOverview());
    }

    @GetMapping("/reports/courses/{courseId}")
    @Operation(summary = "Course detailed report — ADMIN only")
    public ResponseEntity<CourseReportResponse> getCourseReport(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(reportService.getCourseReport(courseId));
    }

    @GetMapping("/reports/gpa-distribution")
    @Operation(summary = "GPA distribution report — ADMIN only")
    public ResponseEntity<GpaDistributionResponse> getGpaDistribution() {
        return ResponseEntity.ok(reportService.getGpaDistribution());
    }

    @GetMapping("/students/{id}/transcript")
    @Operation(summary = "Get student transcript — ADMIN only")
    public ResponseEntity<TranscriptResponse> getStudentTranscript(
            @PathVariable Long id) {
        return ResponseEntity.ok(reportService.getTranscript(id));
    }

    @PostMapping("/teachers")
    @Operation(summary = "Create a teacher account — ADMIN only")
    public ResponseEntity<TeacherProfileResponse> createTeacher(
            @Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity
                .status(201)
                .body(teacherService.createTeacher(request));
    }

    @GetMapping("/teachers")
    @Operation(summary = "List all teachers — ADMIN only")
    public ResponseEntity<List<TeacherProfileResponse>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/teachers/{id}")
    @Operation(summary = "Get teacher by ID — ADMIN only")
    public ResponseEntity<TeacherProfileResponse> getTeacherById(
            @PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/students")
    @Operation(summary = "List all students — ADMIN only")
    public ResponseEntity<List<StudentProfileResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get student by ID — ADMIN only")
    public ResponseEntity<StudentProfileResponse> getStudentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
