package com.israel.studentmanagementsystem.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request body for creating a course")
public class CreateCourseRequest {

    @NotBlank(message = "Course code is required")
    @Schema(description = "Unique course code", example = "CS101")
    private String code;

    @NotBlank(message = "Title is required")
    @Schema(description = "Course title", example = "Introduction to Programming")
    private String title;

    @Schema(description = "Course description")
    private String description;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Schema(description = "Number of credits", example = "3")
    private Integer credits;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Schema(description = "Maximum number of students", example = "30")
    private Integer maxCapacity;

    @Schema(description = "Semester", example = "Fall 2026")
    private String semester;
}
