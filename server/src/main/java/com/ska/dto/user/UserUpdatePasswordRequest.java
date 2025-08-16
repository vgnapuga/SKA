package com.ska.dto.user;

import com.ska.constant.user.PasswordConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserUpdatePasswordRequest(
        @NotBlank(message = PasswordConstants.REQUIRED_MESSAGE)
        @Size(min = PasswordConstants.MIN_LENGTH, message = PasswordConstants.INVALID_LENGTH_MESSAGE)
        String newPassword
) {}
