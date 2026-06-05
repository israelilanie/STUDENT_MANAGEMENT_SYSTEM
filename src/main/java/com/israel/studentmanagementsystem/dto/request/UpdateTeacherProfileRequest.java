package com.israel.studentmanagementsystem.dto.request;

import com.israel.studentmanagementsystem.enums.TeacherTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request body for updating teacher profile")
public class UpdateTeacherProfileRequest {

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Smith")
    private String lastName;

    @Schema(description = "Department", example = "Computer Science")
    private String department;

    @Schema(description = "Academic title", example = "PROFESSOR")
    private TeacherTitle title;

    @Schema(description = "Specialization", example = "Machine Learning")
    private String specialization;

    @Schema(description = "Office hours", example = "Mon-Wed 2pm-4pm")
    private String officeHours;
}
