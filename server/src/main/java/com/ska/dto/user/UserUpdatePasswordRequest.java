package com.ska.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record UserUpdatePasswordRequest(
    @NotNull(message = "User's id must not be <null>")
    @Positive(message = "User's id must be positive")
    Long id,

    @NotBlank(message = "Password is required to update user's password")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String newPassword
) {}
