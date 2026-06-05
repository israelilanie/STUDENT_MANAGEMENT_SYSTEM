package com.israel.studentmanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Request body for updating student profile")
public class UpdateStudentProfileRequest {
    @Schema(description = "Date of birth", example = "2000-01-15")
    private LocalDate dateOfBirth;

    @Schema(description = "First name", example = "Jane")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;
}
