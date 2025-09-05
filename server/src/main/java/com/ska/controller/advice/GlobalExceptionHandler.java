package com.ska.controller.advice;

import java.nio.file.AccessDeniedException;
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
import com.ska.dto.error.ErrorResponse;
import com.ska.dto.error.ValidationErrorResponse;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.DomainValidationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrorsExceptions(
            final MethodArgumentNotValidException exception,
            final WebRequest request) {
        Map<String, List<String>> errorFields = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errorFields.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(
                "DTO_VALIDATION_ERROR",
                getPath(request),
                HttpStatus.BAD_REQUEST.value(),
                errorFields);

        log.warn("Validation error at {}: {}", getPath(request), exception.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(
            final AccessDeniedException exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "ACCESS_DENIED",
                exception.getMessage(),
                getPath(request),
                HttpStatus.FORBIDDEN.value());

        log.warn("Access denied at {}: {}", getPath(request), exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationExceptions(
            final BusinessRuleViolationException exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "BUSINESS_RULE_VIOLATION",
                exception.getMessage(),
                getPath(request),
                HttpStatus.BAD_REQUEST.value());

        log.warn("Business rule violation at {}: {}", getPath(request), exception.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(
            final ResourceNotFoundException exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "RESOURCE_NOT_FOUND",
                exception.getMessage(),
                getPath(request),
                HttpStatus.NOT_FOUND.value());

        log.info("Resource not found at {}: {}", getPath(request), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsExceptions(
            final ResourceAlreadyExistsException exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "RESOURCE_ALREADY_EXISTS",
                exception.getMessage(),
                getPath(request),
                HttpStatus.CONFLICT.value());

        log.warn("Resource already exists error at {}: {}", getPath(request),
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidationExceptions(
            final DomainValidationException exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "DOMAIN_VALIDATION_ERROR",
                exception.getMessage(),
                getPath(request),
                HttpStatus.BAD_REQUEST.value());

        log.warn("Domain validation error at {}: {}", getPath(request), exception.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(
            final Exception exception,
            final WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                getPath(request),
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.error("Unexpected error at {}: {}", getPath(request), exception.getMessage(),
                exception);

        return ResponseEntity.internalServerError().body(errorResponse);
    }


    private static String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

}
