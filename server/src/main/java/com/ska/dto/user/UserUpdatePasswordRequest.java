package com.ska.dto.user;


import com.ska.constant.user.PasswordConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserUpdatePasswordRequest(
        @NotBlank(message = PasswordConstants.Messages.REQUIRED_MESSAGE) @Size(min = PasswordConstants.Format.MIN_LENGTH, message = PasswordConstants.Messages.INVALID_LENGTH_MESSAGE) String newPassword) {
}
