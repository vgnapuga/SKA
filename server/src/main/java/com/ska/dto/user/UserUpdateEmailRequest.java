package com.ska.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record UserUpdateEmailRequest(
   @NotNull(message = "User id must not be <null>")
   @Positive(message = "User's id must be positive")
   Long id,

   @NotBlank(message = "Email is required to update user's email")
    @Size(max = 254, message = "Email is longer than 254 characters")
   @Email(message = "Invalid email")
   String newEmail 
) {}