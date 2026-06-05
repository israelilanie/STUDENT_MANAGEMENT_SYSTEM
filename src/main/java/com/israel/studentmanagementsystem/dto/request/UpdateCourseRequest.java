package com.israel.studentmanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Request body for updating a course")
public class UpdateCourseRequest {

    @Schema(description = "Course title", example = "Advanced Programming")
    private String title;

    @Schema(description = "Course description")
    private String description;

    @Min(value = 1, message = "Credits must be at least 1")
    @Schema(description = "Number of credits", example = "4")
    private Integer credits;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Schema(description = "Max capacity", example = "40")
    private Integer maxCapacity;

    @Schema(description = "Semester", example = "Spring 2027")
    private String semester;
}
