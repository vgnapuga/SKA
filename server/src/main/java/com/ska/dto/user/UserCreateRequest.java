package com.ska.dto.user;

import com.ska.constant.user.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;;


public record UserCreateRequest(
        @NotBlank(message = EmailConstants.REQUIRED_MESSAGE)
        @Size(max = EmailConstants.MAX_LENGTH, message = EmailConstants.INVALID_LENGTH_MESSAGE)
        @Email(message = EmailConstants.INVALID_FORMAT_MESSAGE)
        String email,

        @NotBlank(message = PasswordConstants.REQUIRED_MESSAGE)
        @Size(min = PasswordConstants.MIN_LENGTH, message = PasswordConstants.INVALID_LENGTH_MESSAGE)
        String password
) {}
