package com.ska.dto.user;

import com.ska.constant.user.EmailConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserUpdateEmailRequest(
      @NotBlank(message = EmailConstants.REQUIRED_MESSAGE)
      @Size(max = EmailConstants.MAX_LENGTH, message = EmailConstants.INVALID_LENGTH_MESSAGE)
      @Email(message = EmailConstants.INVALID_FORMAT_MESSAGE)
      String newEmail 
) {}
