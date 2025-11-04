package com.ska.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.user.request.UserCreateRequest;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.DomainValidationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.model.user.vo.Email;
import com.ska.model.user.vo.Password;
import com.ska.repository.UserRepository;
import com.ska.util.LogTemplates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for managing system users.
 * 
 * Extends {@link BaseService}. Provides operations for creating, retrieving and
 * deleting users with business rule validation.
 * 
 * @see User - user entity
 * @see Email - email value object
 * @see Password - password value object
 * @see UserRepository - JPA repository
 * @see UserCreateRequest - user creation request
 * @see BusinessRuleViolationException - thrown on business rules violation
 * @see ResourceAlreadyExistsException - thrown if resource already exists
 * @see ResourceNotFoundException - thrown if resource not found
 * @see DomainValidationException - thrown if email or password validation
 * failure
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // ===== Helper methods ===== //

    private final void checkEmailUniqueness(Email email) {
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException(
                    String.format("User with email=%s already exists", email.toString()));
    }

    private final Password encodePassword(String argon2PHC) {
        String hashed = passwordEncoder.encode(argon2PHC);
        return new Password(hashed);
    }

    public final User checkUserExistenceAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User id=%d not found", userId)));
    }

    // ========================== //

    /**
     * Creates a new system user.
     * 
     * @param request data containing email and password
     * @return created user with assigned ID
     * @throws ResourceAlreadyExistsException if user with this email already exists
     * @throws BusinessRuleViolationException if password does not meet the
     * requirements
     * @throws DomainValidationException if email invalid or too long
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserCreateRequest - user creation request
     * @see User - user entity
     * @see Email - email value object
     * @see Password - password value object
     */
    @Transactional()
    public User create(UserCreateRequest request) {
        log.info("Creating user with email: {}", request.email());

        log.debug(LogTemplates.validationStartLog("Email"));
        Email email = new Email(request.email());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        checkEmailUniqueness(email);

        String argon2PHC = request.password();
        log.debug(LogTemplates.startLog("Password encoding"));
        Password password = encodePassword(argon2PHC);

        User user = new User(email, password);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}, email: {}", savedUser.getId(), email.getValue());
        return savedUser;
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
    public User getById(Long id) {
        log.info("Getting user with ID: {}", id);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.UserService.checkUserExistenceStartLog());
        User retrievedUser = checkUserExistenceAndGet(id);

        log.info("User with ID: {} retrieved successfully, email: {}", id, retrievedUser.getEmail().getValue());
        return retrievedUser;
    }

    /**
     * Deletes user by ID.
     * 
     * @param id the user identifier
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);

        log.debug(LogTemplates.UserService.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.UserService.checkUserExistenceStartLog());
        checkUserExistenceAndGet(id);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        userRepository.deleteById(id);

        log.info("User with ID: {} deleted successfully", id);
    }

}
