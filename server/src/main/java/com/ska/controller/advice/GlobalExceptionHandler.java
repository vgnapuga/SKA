package com.ska.controller.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ska.dto.error.*;
import com.ska.exceptions.*;


@RestControllerAdvice
public final class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrorsExceptions(
            final MethodArgumentNotValidException ex,
            final WebRequest request
    ) {
        Map<String, List<String>> errorFields = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errorFields.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ValidationErrorResponse response = ValidationErrorResponse.of(
                "Validation failed at DTO level",
                getPath(request),
                HttpStatus.BAD_REQUEST.value(),
                errorFields
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationExceptions(
            final BusinessRuleViolationException ex,
            final WebRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                getPath(request),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(
            final ResourceNotFoundException ex,
            final WebRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                getPath(request),
                HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsExceptions(
            final ResourceAlreadyExistsException ex,
            final WebRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                "RESOURCE_ALREADY_EXISTS",
                ex.getMessage(),
                getPath(request),
                HttpStatus.CONFLICT.value()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidationExceptions(
            final DomainValidationException ex,
            final WebRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                "DOMAIN_VALIDATION_ERROR",
                ex.getMessage(),
                getPath(request),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(
            final Exception ex,
            final WebRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                getPath(request),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.internalServerError().body(response);
    }


    private static String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

}
