package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.repository.UserRepository;
import com.ska.constant.user.*;
import com.ska.dto.user.*;
import com.ska.vo.user.*;
import com.ska.util.LogTemplates;


/**
 * Service for managing system users.
 * 
 * Provides operations for
 * creating, updating, retrieving and deleting users
 * with business rule validation.
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
     * @throws DomainValidationException if password BCrypt hash incorrect
     */
    @Transactional()
    public final User createUser(final UserCreateRequest request) {
        log.info("Creating user with email: {}", request.email());

        log.debug(LogTemplates.validationStartLog("Email"));
        Email email = new Email(request.email());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException(
                    String.format("User with email=%s already exists", email.toString())
            );

        log.debug(LogTemplates.validationStartLog("Raw password"));
        String rawPassword = request.password();
        validateRawPassword(rawPassword);

        log.debug(LogTemplates.startLog("Password encoding"));
        Password password = encodePassword(rawPassword);

        User user = new User(email, password);
        User savedUser = userRepository.save(user);
        log.info(
                "User created successfully with ID: {}, email: {}",
                savedUser.getId(), savedUser.getEmail().getValue()
        );

        return savedUser;
    }

    private static void validateRawPassword(final String rawPassword) {
        if (rawPassword.length() < PasswordConstants.MIN_LENGTH)
            throw new BusinessRuleViolationException(PasswordConstants.INVALID_LENGTH_MESSAGE);
    }

    private final Password encodePassword(final String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);
        return new Password(hashed);
    }

    /**
     * Retrieves user by ID.
     * 
     * @param id the user identifier
     * @return Optional containing user if found, empty otherwise
     * @throws BusinessRuleViolationException if ID is null or less than one
     */
    @Transactional(readOnly = true)
    public final Optional<User> getUserById(final Long id) {
        log.info("Getting user with ID: {}", id);

        log.debug(LogTemplates.validationStartLog("ID"));
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
     * Retrieves all database users.
     * 
     * @return List of database users
     */
    @Transactional(readOnly = true)
    public final List<User> getAllUsers() {
        log.info("Getting all users");

        List<User> users = userRepository.findAll();
        log.info("Retrieved {} users", users.size());

        return users;
    }

    /**
     * Updates user email by ID.
     * 
     * @param request data containing new email
     * @return User with updated email
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exists in database
     * @throws DomainValidationException if email incorrect
     * @throws ResourceAlreadyExistsException if email already exists in database
     */
    @Transactional
    public final User updateUserEmail(final Long id, final UserUpdateEmailRequest request) {
        log.info("Updating user email for ID: {}", id);
        
        log.debug(LogTemplates.validationStartLog("ID"));
        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User id=%d not found to update email", id)
            )
        );

        log.debug(LogTemplates.validationStartLog("Email"));
        Email newEmail = new Email(request.newEmail());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        boolean isUnique = !userRepository.existsByEmail(newEmail);
        user.changeEmail(newEmail, isUnique);

        User updatedUser = userRepository.save(user);
        log.info(
                "User email updated successfully for ID: {}, new email: {}",
                id, newEmail
        );

        return updatedUser;
    }

    /**
     * Updates user password by ID.
     * 
     * @param request data containing new password
     * @return User with updated password
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exists in database
     * @throws BusinessRuleViolationException if password does not meet the requirements
     * @throws DomainValidationException if password BCrypt hash incorrect
     */
    @Transactional
    public final User updateUserPassword(final Long id, final UserUpdatePasswordRequest request) {
        log.info("Updating user password for ID: {}", id);

        log.debug(LogTemplates.validationStartLog("ID"));
        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User id=%d not found to update password", id)
            )
        );

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
     * @throws ResourceNotFoundException if ID does not exists in database
     */
    @Transactional
    public final void deleteUserById(final Long id) {
        log.info("Deleting user with ID: {}", id);

        log.debug(LogTemplates.validationStartLog("ID"));
        validateId(id);

        log.debug(LogTemplates.checkStartLog("ID existing"));
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException(
                    String.format("User id=%d not found to delete", id)
            );

        userRepository.deleteById(id);
        log.info("User with ID: {} deleted successfully", id);
    }

}
