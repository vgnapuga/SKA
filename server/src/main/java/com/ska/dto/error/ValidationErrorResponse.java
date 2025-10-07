package com.ska.dto.error;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;


public final record ValidationErrorResponse(
        String error,
        String message,
        String path,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeStamp,
        int status,
        Map<String, List<String>> errorFields) {

    public static ValidationErrorResponse of(
            String message,
            String path,
            int status,
            Map<String, List<String>> errorFields) {
        return new ValidationErrorResponse("VALIDATION_ERROR", message, path, LocalDateTime.now(), status, errorFields);
    }

    public static ValidationErrorResponse of(String message, int status, Map<String, List<String>> errorFields) {
        return new ValidationErrorResponse("VALIDATION_ERROR", message, null, LocalDateTime.now(), status, errorFields);
    }

}
