package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Standard structure for application error responses")
public class ErrorResponse {

    @Schema(description = "The precise date and time when the error occurred", example = "2026-06-05T20:02:15")
    private LocalDateTime timestamp;

    @Schema(description = "The HTTP status code integer value", example = "400")
    private int status;

    @Schema(description = "Internal application-specific error classification code", example = "INVALID_INPUT_DATA")
    private String code;

    @Schema(description = "A human-readable detailed explanation of what went wrong", example = "The provided parameter is invalid.")
    private String message;

    @Schema(description = "The context URI path endpoint where the error originated", example = "/api/v1/courses/search")
    private String path;
}
