package com.israel.studentmanagementsystem.dto.response;

import com.israel.studentmanagementsystem.enums.Role;
import com.israel.studentmanagementsystem.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "User information returned in responses")
public class UserResponse {

    @Schema(description = "Unique user ID", example = "1")
    private Long id;

    @Schema(description = "User's email address", example = "john@example.com")
    private String email;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User role in the system", example = "ROLE_STUDENT")
    private Role role;

    @Schema(description = "Account status", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
}
