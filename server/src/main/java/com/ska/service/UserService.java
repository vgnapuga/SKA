package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ska.exception.*;
import com.ska.model.user.User;
import com.ska.repository.UserRepository;
import com.ska.constant.user.*;
import com.ska.dto.user.*;
import com.ska.vo.user.*;
import com.ska.util.LogTemplates;


/**
 * Service for managing system users.
 * 
 * Extends {@link BaseService}.
 * Provides operations for creating, updating, retrieving and deleting users
 * with business rule validation.
 * 
 * @see User - user entity
 * @see Email - email value object
 * @see Password - password value object
 * @see UserRepository - JPA repository
 * @see UserCreateRequest - user creation request
 * @see UserUpdateEmailRequest - email update request
 * @see UserUpdatePasswordRequest - password update request
 * @see BusinessRuleViolationException - thrown on business rules violation
 * @see ResourceAlreadyExistsException - thrown if resource already exists
 * @see ResourceNotFoundException - thrown if resource not found
 * @see DomainValidationException - thrown if email or password validation failure
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    /**
     * Creates a new system user.
     * 
     * @param request data containing email and password
     * @return created user with assigned ID
     * @throws ResourceAlreadyExistsException if user with this email already exists
     * @throws BusinessRuleViolationException if password does not meet the requirements
     * @throws DomainValidationException if email invalid or too long
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserCreateRequest - user creation request
     * @see User - user entity
     * @see Email - email value object
     * @see Password - password value object
     */
    @Transactional()
    public final User createUser(final UserCreateRequest request) {
        log.info("Creating user with email: {}", request.email());

        log.debug(LogTemplates.validationStartLog("Email"));
        Email email = new Email(request.email());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        checkEmailUniqueness(email);

        log.debug(LogTemplates.validationStartLog("Raw password"));
        String rawPassword = request.password();
        validateRawPassword(rawPassword);

        log.debug(LogTemplates.startLog("Password encoding"));
        Password password = encodePassword(rawPassword);

        User user = new User(email, password);
        User savedUser = userRepository.save(user);
        log.info(
                "User created successfully with ID: {}, email: {}",
                savedUser.getId(), email.getValue()
        );

        return savedUser;
    }
    
    private final void checkEmailUniqueness(final Email email) {
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException(
                    String.format("User with email=%s already exists", email.toString())
            );
    }

    private static void validateRawPassword(final String rawPassword) {
        if (rawPassword.length() < PasswordConstants.Format.MIN_LENGTH)
            throw new BusinessRuleViolationException(PasswordConstants.Messages.INVALID_LENGTH_MESSAGE);
    }

    private final Password encodePassword(final String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);
        return new Password(hashed);
    }

    /**
     * (Read only) retrieves user by ID.
     * 
     * @param id the user identifier
     * @return Optional containing user if found, empty otherwise
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @see User - user entity
     */
    @Transactional(readOnly = true)
    public final Optional<User> getUserById(final Long id) {
        log.info("Getting user with ID: {}", id);

        validateId(id);

        log.debug(LogTemplates.startLog("Database query"));
        Optional<User> retrievedUser = userRepository.findById(id);

        if (retrievedUser.isPresent())
            log.info(
                    "User with ID: {} retrieved successfully, email: {}",
                    id, retrievedUser.get().getEmail().getValue()
            );
        else
            log.info("User with ID: {} not found", id);

        return retrievedUser;
    }

    /**
     * (Read only) retrieves all database users.
     * 
     * @return List of database users
     * @see User - user entity
     */
    @Transactional(readOnly = true)
    public final List<User> getAllUsers() {
        log.info("Getting all users");

        List<User> users = userRepository.findAll();
        log.info("Retrieved {} users", users.size());

        return users;
    }

    /**
     * Updates user email by ID using {@link User#changeEmail(Email, boolean)}.
     * 
     * @param request data containing new email
     * @return User with updated email
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @throws DomainValidationException if email invalid
     * @throws ResourceAlreadyExistsException if email already exists in database
     * @see UserUpdateEmailRequest - email update request
     * @see User - user entity
     * @see Email - email value object
     */
    @Transactional
    public final User updateUserEmail(final Long id, final UserUpdateEmailRequest request) {
        log.info("Updating user email for ID: {}", id);
        
        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        User user = checkIdExistence(id);

        log.debug(LogTemplates.validationStartLog("Email"));
        Email newEmail = new Email(request.newEmail());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        checkEmailUniqueness(newEmail);

        user.changeEmail(newEmail);

        User updatedUser = userRepository.save(user);
        log.info(
                "User email updated successfully for ID: {}, new email: {}",
                id, newEmail
        );

        return updatedUser;
    }

    private final User checkIdExistence(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User id=%d not found", userId)
            )
        );

        return user;
    }

    /**
     * Updates user password by ID using {@link User#changePassword(Password)}.
     * 
     * @param request data containing new password
     * @return User with updated password
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @throws BusinessRuleViolationException if password does not meet the requirements
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserUpdatePasswordRequest - password update request
     * @see User - user entity
     * @see Password - password value object
     */
    @Transactional
    public final User updateUserPassword(final Long id, final UserUpdatePasswordRequest request) {
        log.info("Updating user password for ID: {}", id);

        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        User user = checkIdExistence(id);

        log.debug(LogTemplates.validationStartLog("Raw password"));
        String rawPassword = request.newPassword();
        validateRawPassword(rawPassword);

        log.debug(LogTemplates.startLog("Password encoding"));
        Password newPassword = encodePassword(rawPassword);

        user.changePassword(newPassword);
        User updatedUser = userRepository.save(user);
        log.info("User password for ID: {} updated successfully", id);

        return updatedUser;
    }

    /**
     * Deletes user by ID.
     * 
     * @param id the user identifier
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     */
    @Transactional
    public final void deleteUserById(final Long id) {
        log.info("Deleting user with ID: {}", id);

        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        checkIdExistence(id);

        userRepository.deleteById(id);
        log.info("User with ID: {} deleted successfully", id);
    }

}
