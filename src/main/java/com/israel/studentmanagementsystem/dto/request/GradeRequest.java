package com.israel.studentmanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request body for grading an enrollment")
public class GradeRequest {
    @NotNull(message = "Enrollment ID is required")
    @Schema(description = "Enrollment ID to grade", example = "1")
    private Long enrollmentId;

    @NotBlank(message = "Final grade is required")
    @Schema(description = "Letter grade", example = "A")
    private String finalGrade;
}
