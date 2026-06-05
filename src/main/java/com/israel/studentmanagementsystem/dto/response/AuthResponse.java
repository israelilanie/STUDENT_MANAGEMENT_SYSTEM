package com.israel.studentmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned after successful login or registration")

public class AuthResponse {

    @Schema(description = "JWT access token to use in future requests", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Token type, always Bearer", example = "Bearer")
    private String tokenType;

    @Schema(description = "Basic info about the authenticated user")
    private UserResponse user;
}
