package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Full academic transcript for a student")
public class TranscriptResponse {
    @Schema(description = "Student number", example = "STU-2026-0001")
    private String studentNumber;

    @Schema(description = "Student full name", example = "Jane Doe")
    private String fullName;

    @Schema(description = "Student email", example = "jane@example.com")
    private String email;

    @Schema(description = "Cumulative GPA", example = "3.75")
    private Double cumulativeGpa;

    @Schema(description = "Total credits completed", example = "9")
    private Integer totalCredits;

    @Schema(description = "Total courses completed", example = "3")
    private Integer totalCoursesCompleted;

    @Schema(description = "Total courses dropped", example = "1")
    private Integer totalCoursesDropped;

    @Schema(description = "All enrollment records")
    private List<TranscriptEntryResponse> entries;

    @Schema(description = "Generated at timestamp")
    private LocalDateTime generatedAt;
}
