package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Report for a specific course")
public class CourseReportResponse {
    @Schema(description = "Course code", example = "CS101")
    private String courseCode;

    @Schema(description = "Course title", example = "Introduction to Programming")
    private String courseTitle;

    @Schema(description = "Total enrolled students", example = "28")
    private Long totalEnrolled;

    @Schema(description = "Total completed", example = "20")
    private Long totalCompleted;

    @Schema(description = "Total dropped", example = "3")
    private Long totalDropped;

    @Schema(description = "Available seats", example = "2")
    private Integer availableSeats;

    @Schema(description = "Pass rate percentage", example = "85.5")
    private Double passRate;

    @Schema(description = "Class average GPA", example = "3.10")
    private Double averageGpa;

    @Schema(description = "Highest grade in class", example = "A+")
    private String highestGrade;

    @Schema(description = "Lowest grade in class", example = "D")
    private String lowestGrade;
}
