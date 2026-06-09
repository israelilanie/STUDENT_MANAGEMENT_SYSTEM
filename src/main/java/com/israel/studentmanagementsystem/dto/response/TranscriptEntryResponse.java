package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Single entry in a student transcript")
public class TranscriptEntryResponse {
    @Schema(description = "Course code", example = "CS101")
    private String courseCode;

    @Schema(description = "Course title", example = "Introduction to Programming")
    private String courseTitle;

    @Schema(description = "Credits", example = "3")
    private Integer credits;

    @Schema(description = "Semester", example = "Fall 2026")
    private String semester;

    @Schema(description = "Enrollment status", example = "COMPLETED")
    private EnrollmentStatus status;

    @Schema(description = "Final grade", example = "A")
    private String finalGrade;

    @Schema(description = "Grade points", example = "4.0")
    private Double gradePoints;

    @Schema(description = "Enrolled at")
    private LocalDateTime enrolledAt;

    @Schema(description = "Dropped at — null if not dropped")
    private LocalDateTime droppedAt;
}
