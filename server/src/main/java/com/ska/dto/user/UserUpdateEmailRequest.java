package com.ska.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.ska.constants.user.EmailConstants;


public record UserUpdateEmailRequest(
      @NotNull(message = "User id must not be <null>")
      @Positive(message = "User's id must be positive")
      Long id,

      @NotBlank(message = EmailConstants.REQUIRED_MESSAGE)
      @Size(max = EmailConstants.MAX_LENGTH, message = EmailConstants.INVALID_LENGTH_MESSAGE)
      @Email(message = EmailConstants.INVALID_FORMAT_MESSAGE)
      String newEmail 
) {}