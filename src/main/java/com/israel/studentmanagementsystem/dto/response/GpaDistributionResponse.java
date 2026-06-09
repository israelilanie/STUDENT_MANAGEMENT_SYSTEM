package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "GPA distribution across all students")
public class GpaDistributionResponse {
    @Schema(description = "Students with GPA 3.5 - 4.0", example = "45")
    private Long excellentCount;

    @Schema(description = "Students with GPA 3.0 - 3.49", example = "30")
    private Long goodCount;

    @Schema(description = "Students with GPA 2.0 - 2.99", example = "25")
    private Long averageCount;

    @Schema(description = "Students with GPA below 2.0", example = "10")
    private Long belowAverageCount;

    @Schema(description = "Students with no grades yet", example = "10")
    private Long noGradesCount;

    @Schema(description = "Total students", example = "120")
    private Long totalStudents;
}
