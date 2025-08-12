package com.ska.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record UserDeleteRequest(
        @NotNull(message = "User id must not be <null>")
        @Positive(message = "User's id must be positive")
        Long id
) {}
