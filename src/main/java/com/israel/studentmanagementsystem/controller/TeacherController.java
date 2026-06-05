package com.israel.studentmanagementsystem.controller;

import com.israel.studentmanagementsystem.dto.request.UpdateTeacherProfileRequest;
import com.israel.studentmanagementsystem.dto.response.TeacherProfileResponse;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Teachers", description = "Teacher profile endpoints")
public class TeacherController {


    private final TeacherService teacherService;

    @GetMapping("/profile")
    @Operation(summary = "Get my profile — TEACHER only")
    public ResponseEntity<TeacherProfileResponse> getMyProfile(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                teacherService.getMyProfile(user.getEmail())
        );
    }

    @PatchMapping("/profile")
    @Operation(summary = "Update my profile — TEACHER only")
    public ResponseEntity<TeacherProfileResponse> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateTeacherProfileRequest request) {

        return ResponseEntity.ok(
                teacherService.updateMyProfile(
                        user.getEmail(), request)
        );
    }
}
