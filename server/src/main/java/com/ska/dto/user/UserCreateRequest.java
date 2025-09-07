package com.ska.dto.user;


import com.ska.constant.user.EmailConstants;
import com.ska.constant.user.PasswordConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserCreateRequest(
        @NotBlank(message = EmailConstants.Messages.REQUIRED_MESSAGE) @Size(max = EmailConstants.Format.MAX_LENGTH, message = EmailConstants.Messages.INVALID_LENGTH_MESSAGE) @Email(message = EmailConstants.Messages.INVALID_FORMAT_MESSAGE) String email,
        @NotBlank(message = PasswordConstants.Messages.REQUIRED_MESSAGE) @Size(min = PasswordConstants.Format.MIN_LENGTH, message = PasswordConstants.Messages.INVALID_LENGTH_MESSAGE) String password) {
}
