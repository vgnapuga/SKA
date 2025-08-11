package com.ska.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserCreateRequest(
    @NotBlank(message = "Email is required to create new user")
    @Size(max = 254, message = "Email is longer than 254 characters")
    @Email(message = "Invalid email")
    String email,

    @NotBlank(message = "Password is required to create new user")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password
) {}
