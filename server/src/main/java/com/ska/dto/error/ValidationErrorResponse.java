package com.ska.dto.error;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;


public record ValidationErrorResponse(
        String error,
        String message,
        String path,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timeStamp,

        int status,
        Map<String, List<String>> errorFields
) {

    public static ValidationErrorResponse of(
            final String message,
            final String path,
            final int status,
            final Map<String, List<String>> errorFields
    ) {
        return new ValidationErrorResponse(
                "VALIDATION_ERROR",
                message,
                path,
                LocalDateTime.now(),
                status,
                errorFields
        );
    }

    public static ValidationErrorResponse of(
            final String message,
            final int status,
            final Map<String, List<String>> errorFields
    ) {
        return new ValidationErrorResponse(
                "VALIDATION_ERROR",
                message,
                null,
                LocalDateTime.now(),
                status,
                errorFields
        );
    }

}
