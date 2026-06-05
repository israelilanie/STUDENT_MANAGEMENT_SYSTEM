package com.israel.studentmanagementsystem.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Gpa Calculation Response")
@AllArgsConstructor
@NoArgsConstructor
public class GpaResponse {

    @Schema(description = "Student number", example = "STU-2026-0052")
    private String studentNumber;

    @Schema(description = "Cumulative GPA", example = "2.95")
    private Double gpa;

    @Schema(description = "Credits value", example = "120")
    private int totalCredits;

    @Schema(description = "Number of Courses completed", example = "16")
    private int completedCoursesCount;
}
