package com.ska.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ska.dto.user.request.UserCreateRequest;
import com.ska.dto.user.request.UserUpdateEmailRequest;
import com.ska.dto.user.request.UserUpdatePasswordRequest;
import com.ska.dto.user.response.UserResponse;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.DomainValidationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * REST controller for user management operations.
 * 
 * Provides HTTP endpoints for creating, retrieving, updating, and deleting
 * users. Base path: /api/users
 * 
 * @see UserService - service for managing system users
 * @see User - user entity
 * @see UserCreateRequest - user creation request
 * @see UserUpdateEmailRequest - email update request
 * @see UserUpdatePasswordRequest - password update request
 * @see BusinessRuleViolationException - thrown on business rules violation
 * @see ResourceAlreadyExistsException - thrown if resource already exists
 * @see ResourceNotFoundException - thrown if resource not found
 * @see DomainValidationException - thrown if email or password validation
 * failure
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public final class UserController {

    private final UserService userService;
    private static final String ROOT = "api/users";

    /**
     * Creates a new user in the system using
     * {@link UserService#createUser(UserCreateRequest)}.
     * 
     * @param request data containing email and password
     * @return ResponseEntity with created user and HTTP 201 status
     * @throws ResourceAlreadyExistsException if user with this email already exists
     * @throws BusinessRuleViolationException if password does not meet the
     * requirements
     * @throws DomainValidationException if email invalid or too long
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserCreateRequest - user creation request
     * @see UserService - service for managing system users
     * @see User - user entity
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("POST {} - email: {}", ROOT, request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(userService.createUser(request)));
    }

    /**
     * Retrieves user by ID using {@link UserService#getUserById(Long)}.
     * 
     * @param id the user identifier
     * @return ResponseEntity with user if found (HTTP 200) or HTTP 404 if not found
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @see UserService - service for managing system users
     * @see User - user entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("GET {}/{}", ROOT, id);
        return ResponseEntity.ok(UserResponse.of(userService.getUserById(id)));
    }

    /**
     * Updates user email using
     * {@link UserService#updateUserEmail(Long, UserUpdateEmailRequest)}.
     * 
     * @param id the user identifier
     * @param request data containing new email
     * @return ResponseEntity with updated user and HTTP 200 status
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @throws DomainValidationException if email invalid
     * @throws ResourceAlreadyExistsException if email already exists in database
     * @see UserUpdateEmailRequest - email update request
     * @see UserService - service for managing system users
     * @see User - user entity
     */
    @PutMapping("/{id}/email")
    public ResponseEntity<UserResponse> updateUserEmail(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateEmailRequest request) {
        log.info("PUT {}/{}/email - new email: {}", ROOT, id, request.newEmail());
        return ResponseEntity.ok(UserResponse.of(userService.updateUserEmail(id, request)));
    }

    /**
     * Updates user password using
     * {@link UserService#updateUserPassword(Long, UserUpdatePasswordRequest)}.
     * 
     * @param id the user identifier
     * @param request data containing new password
     * @return ResponseEntity with updated user and HTTP 200 status
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @throws BusinessRuleViolationException if password does not meet the
     * requirements
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserUpdatePasswordRequest - password update request
     * @see UserService - service for managing system users
     * @see User - user entity
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponse> updateUserPassword(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdatePasswordRequest request) {
        log.info("PUT {}/{}/password - new password: ***", ROOT, id);
        return ResponseEntity.ok(UserResponse.of(userService.updateUserPassword(id, request)));
    }

    /**
     * Deletes user from database using {@link UserService#deleteUserById(Long)}.
     * 
     * @param id the user identifier
     * @return ResponseEntity with HTTP 204 status
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @see UserService - service for managing system users
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        log.info("DELETE {}/{}", ROOT, id);

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
