package com.israel.studentmanagementsystem.dto.request;

import com.israel.studentmanagementsystem.enums.TeacherTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for admin to create a teacher account")
public class CreateTeacherRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Teacher email", example = "prof.jones@sms.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Initial password", example = "teacher123")
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "First name", example = "Robert")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name", example = "Jones")
    private String lastName;

    @Schema(description = "Department", example = "Computer Science")
    private String department;

    @Schema(description = "Academic title", example = "PROFESSOR")
    private TeacherTitle title;

    @Schema(description = "Specialization", example = "Databases")
    private String specialization;

    @Schema(description = "Office hours", example = "Tue-Thu 3pm-5pm")
    private String officeHours;
}
