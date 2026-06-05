package com.israel.studentmanagementsystem.controller;

import com.israel.studentmanagementsystem.dto.request.CreateCourseRequest;
import com.israel.studentmanagementsystem.dto.request.UpdateCourseRequest;
import com.israel.studentmanagementsystem.dto.response.CourseResponse;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    @Operation(summary = "Search and list active courses — paginated")
    public ResponseEntity<Page<CourseResponse>> searchCourses(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(
                courseService.searchCourses(search, pageable));
    }

    @GetMapping("/courses/{id}")
    @Operation(summary = "Get course details by ID")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }


    @PostMapping("/teacher/courses")
    @Operation(summary = "Create a course — TEACHER only")
    public ResponseEntity<CourseResponse> createCourseAsTeacher(
            @Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.status(201).body(
                courseService.createCourseAsTeacher(
                        request, user.getEmail()));
    }

    // teacher views their own courses
    @GetMapping("/teacher/courses")
    @Operation(summary = "Get my courses — TEACHER only")
    public ResponseEntity<List<CourseResponse>> getMyCourses(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                courseService.getMyCourses(user.getEmail()));
    }

    @PatchMapping("/courses/{id}")
    @Operation(summary = "Update a course — TEACHER or ADMIN")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                courseService.updateCourse(
                        id, request, user.getEmail()));
    }


    @PatchMapping("/admin/courses/{courseId}/assign-teacher/{teacherUserId}")
    @Operation(summary = "Assign teacher to course — ADMIN only")
    public ResponseEntity<CourseResponse> assignTeacher(
            @PathVariable Long courseId,
            @PathVariable Long teacherUserId) {

        return ResponseEntity.ok(
                courseService.assignTeacher(courseId, teacherUserId));
    }


    @PatchMapping("/admin/courses/{id}/archive")
    @Operation(summary = "Archive a course — ADMIN only")
    public ResponseEntity<CourseResponse> archiveCourse(
            @PathVariable Long id) {
        return ResponseEntity.ok(courseService.archiveCourse(id));
    }

}
