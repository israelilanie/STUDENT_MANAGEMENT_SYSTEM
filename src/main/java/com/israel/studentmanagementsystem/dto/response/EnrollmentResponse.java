package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Enrollment information")
public class EnrollmentResponse {

    @Schema(description = "Enrollment ID", example = "1")
    private Long id;

    @Schema(description = "Course info")
    private CourseResponse course;

    @Schema(description = "Enrollment status", example = "ENROLLED")
    private EnrollmentStatus status;

    @Schema(description = "When the student enrolled")
    private LocalDateTime enrolledAt;

    @Schema(description = "When the student dropped — null if not dropped")
    private LocalDateTime droppedAt;

    @Schema(description = "Final grade — null until completed", example = "A")
    private String finalGrade;

    @Schema(description = "Grade points — null until completed", example = "4.0")
    private Double gradePoints;

}
