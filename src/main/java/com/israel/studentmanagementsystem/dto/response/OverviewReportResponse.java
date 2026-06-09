package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "System-wide overview report")
public class OverviewReportResponse {
    @Schema(description = "Total registered students", example = "120")
    private Long totalStudents;

    @Schema(description = "Total teachers", example = "15")
    private Long totalTeachers;

    @Schema(description = "Total active courses", example = "30")
    private Long totalActiveCourses;

    @Schema(description = "Total enrollments", example = "340")
    private Long totalEnrollments;

    @Schema(description = "Total completed enrollments", example = "210")
    private Long totalCompletedEnrollments;

    @Schema(description = "Total dropped enrollments", example = "25")
    private Long totalDroppedEnrollments;

    @Schema(description = "System average GPA", example = "3.21")
    private Double averageGpa;
}
