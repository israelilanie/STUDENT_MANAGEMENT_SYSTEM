package com.israel.studentmanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request body to enroll in a course")
public class EnrollmentRequest {

    @NotNull(message = "Course ID is required")
    @Schema(description = "ID of the course to enroll in", example = "1")
    private Long courseId;

}
