package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.TeacherTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Teacher profile information")
public class TeacherProfileResponse {

    @Schema(description = "Profile ID", example = "1")
    private Long id;

    @Schema(description = "Basic user info")
    private UserResponse user;

    @Schema(description = "Unique employee number", example = "EMP-2026-0001")
    private String employeeNumber;

    @Schema(description = "Department name", example = "Computer Science")
    private String department;

    @Schema(description = "Academic title", example = "PROFESSOR")
    private TeacherTitle title;

    @Schema(description = "Area of specialization", example = "Machine Learning")
    private String specialization;

    @Schema(description = "Office hours", example = "Mon-Wed 2pm-4pm")
    private String officeHours;
}
