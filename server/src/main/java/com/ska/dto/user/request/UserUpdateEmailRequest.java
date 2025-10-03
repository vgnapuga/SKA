package com.ska.dto.user.request;


import com.ska.util.constant.user.EmailConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserUpdateEmailRequest(
        @NotBlank(message = EmailConstants.Messages.REQUIRED_MESSAGE) @Size(max = EmailConstants.Format.MAX_LENGTH, message = EmailConstants.Messages.INVALID_LENGTH_MESSAGE) @Email(message = EmailConstants.Messages.INVALID_FORMAT_MESSAGE) String newEmail) {
}
