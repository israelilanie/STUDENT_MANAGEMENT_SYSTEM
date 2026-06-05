package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Course information")
public class CourseResponse {

    @Schema(description = "Course ID", example = "1")
    private Long id;

    @Schema(description = "Unique course code", example = "CS101")
    private String code;

    @Schema(description = "Course title", example = "Introduction to Programming")
    private String title;

    @Schema(description = "Course description")
    private String description;

    @Schema(description = "Number of credits", example = "3")
    private Integer credits;

    @Schema(description = "Max capacity", example = "30")
    private Integer maxCapacity;

    @Schema(description = "Current enrollment count", example = "15")
    private Integer currentEnrollment;

    @Schema(description = "Course status", example = "ACTIVE")
    private CourseStatus status;

    @Schema(description = "Semester", example = "Fall 2026")
    private String semester;

    @Schema(description = "Assigned teacher")
    private TeacherProfileResponse teacher;

    @Schema(description = "Available seats")
    private Integer availableSeats;

    @Schema(description = "Whether course is full")
    private Boolean full;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
