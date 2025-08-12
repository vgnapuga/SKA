package com.ska.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.ska.constants.user.PasswordConstants;


public record UserUpdatePasswordRequest(
    @NotNull(message = "User's id must not be <null>")
    @Positive(message = "User's id must be positive")
    Long id,

    @NotBlank(message = PasswordConstants.REQUIRED_MESSAGE)
    @Size(min = PasswordConstants.MIN_LENGTH, message = PasswordConstants.INVALID_LENGTH_MESSAGE)
    String newPassword
) {}
