package com.ska.service;


import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.user.request.UserCreateRequest;
import com.ska.dto.user.request.UserUpdateEmailRequest;
import com.ska.dto.user.request.UserUpdatePasswordRequest;
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
 * Extends {@link BaseService}. Provides operations for creating, updating,
 * retrieving and deleting users with business rule validation.
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
 * @see DomainValidationException - thrown if email or password validation
 * failure
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final void checkEmailUniqueness(Email email) {
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException(
                    String.format("User with email=%s already exists", email.toString()));
    }

    private final Password encodePassword(String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);
        return new Password(hashed);
    }

    public final User checkUserExistence(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User id=%d not found", userId)));
    }

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
    public User createUser(UserCreateRequest request) {
        log.info("Creating user with email: {}", request.email());

        log.debug(LogTemplates.validationStartLog("Email"));
        Email email = new Email(request.email());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        checkEmailUniqueness(email);

        String rawPassword = request.password();
        log.debug(LogTemplates.startLog("Password encoding"));
        Password password = encodePassword(rawPassword);

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
    public Optional<User> getUserById(Long id) {
        log.info("Getting user with ID: {}", id);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<User> retrievedUser = userRepository.findById(id);

        if (retrievedUser.isPresent())
            log.info(
                    "User with ID: {} retrieved successfully, email: {}",
                    id,
                    retrievedUser.get().getEmail().getValue());
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
    public List<User> getAllUsers() {
        log.info("Getting all users");

        log.debug(LogTemplates.dataBaseQueryStartLog());
        List<User> users = userRepository.findAll();

        log.info("Retrieved {} users", users.size());
        return users;
    }

    /**
     * Updates user email by ID using {@link User#changeEmail(Email)}.
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
    public User updateUserEmail(Long id, UserUpdateEmailRequest request) {
        log.info("Updating user email for ID: {}", id);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.checkUserExistenceStartLog());
        User user = checkUserExistence(id);

        log.debug(LogTemplates.validationStartLog("Email"));
        Email newEmail = new Email(request.newEmail());

        log.debug(LogTemplates.checkStartLog("Email uniqueness"));
        checkEmailUniqueness(newEmail);

        user.changeEmail(newEmail);
        log.debug(LogTemplates.dataBaseQueryStartLog());
        User updatedUser = userRepository.save(user);

        log.info("User email updated successfully for ID: {}, new email: {}", id, newEmail);
        return updatedUser;
    }

    /**
     * Updates user password by ID using {@link User#changePassword(Password)}.
     * 
     * @param request data containing new password
     * @return User with updated password
     * @throws BusinessRuleViolationException if ID is null or less than one
     * @throws ResourceNotFoundException if ID does not exist in database
     * @throws BusinessRuleViolationException if password does not meet the
     * requirements
     * @throws DomainValidationException if password BCrypt hash invalid
     * @see UserUpdatePasswordRequest - password update request
     * @see User - user entity
     * @see Password - password value object
     */
    @Transactional
    public User updateUserPassword(Long id, UserUpdatePasswordRequest request) {
        log.info("Updating user password for ID: {}", id);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.checkUserExistenceStartLog());
        User user = checkUserExistence(id);

        String rawPassword = request.newPassword();
        log.debug(LogTemplates.startLog("Password encoding"));
        Password newPassword = encodePassword(rawPassword);

        user.changePassword(newPassword);
        log.debug(LogTemplates.dataBaseQueryStartLog());
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
    public void deleteUserById(Long id) {
        log.info("Deleting user with ID: {}", id);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(id);

        log.debug(LogTemplates.checkUserExistenceStartLog());
        checkUserExistence(id);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        userRepository.deleteById(id);

        log.info("User with ID: {} deleted successfully", id);
    }

}
