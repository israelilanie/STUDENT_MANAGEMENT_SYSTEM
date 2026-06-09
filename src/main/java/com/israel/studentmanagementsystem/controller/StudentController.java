package com.israel.studentmanagementsystem.controller;

import com.israel.studentmanagementsystem.dto.request.UpdateStudentProfileRequest;
import com.israel.studentmanagementsystem.dto.response.StudentProfileResponse;
import com.israel.studentmanagementsystem.dto.response.TranscriptResponse;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.service.ReportService;
import com.israel.studentmanagementsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Students", description = "Student profile endpoints")
public class StudentController {

    private final StudentService studentService;
    private final ReportService reportService;

    @GetMapping("/student/transcript")
    @Operation(summary = "My academic transcript — STUDENT only")
    public ResponseEntity<TranscriptResponse> getMyTranscript(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                reportService.getMyTranscript(userDetails.getUsername()));
    }

    @GetMapping("/student/profile")
    @Operation(summary = "Get my profile — STUDENT only")
    public ResponseEntity<StudentProfileResponse> getMyProfile(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                studentService.getMyProfile(user.getUsername())
        );
    }

    @PatchMapping("/student/profile")
    @Operation(summary = "Update my profile — STUDENT only")
    public ResponseEntity<StudentProfileResponse> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateStudentProfileRequest request) {

        return ResponseEntity.ok(
                studentService.updateMyProfile(
                        user.getEmail(), request)
        );
    }


}
