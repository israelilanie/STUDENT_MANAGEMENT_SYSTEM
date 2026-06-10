package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.StudentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Student profile information")
public class StudentProfileResponse {
    @Schema(description = "Profile ID", example = "1")
    private Long id;

    @Schema(description = "Basic user info")
    private UserResponse user;

    @Schema(description = "Unique student number", example = "STU-2026-0001")
    private String studentNumber;

    @Schema(description = "Date of birth", example = "2000-01-15")
    private LocalDate dateOfBirth;

    @Schema(description = "Enrollment date", example = "2026-01-01")
    private LocalDate enrollmentDate;

    @Schema(description = "Current GPA", example = "3.75")
    private Double currentGpa;

    @Schema(description = "Total credits completed", example = "60")
    private Integer totalCredits;

    @Schema(description = "Student status", example = "ACTIVE")
    private StudentStatus status;

    @Schema(description = "Avatar URL from S3", example = "https://bucket.s3.region.amazonaws.com/avatars/...")
    private String avatarUrl;
}
