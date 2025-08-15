package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.constant.user.*;
import com.ska.dto.user.*;
import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.ResourceNotFoundException;
import com.ska.vo.user.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ska.model.user.User;
import com.ska.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Transactional()
    public final User createUser(final UserCreateRequest request) {
        log.info("Creating user with email: {}", request.email());

        log.debug("Email validation - start");
        Email email = new Email(request.email());

        log.debug("Email uniqueness validation - start");
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException(
                    String.format("User with email=%s already exists", email.toString())
            );

        log.debug("Raw password validation - start");
        String rawPassword = request.password();
        validateRawPassword(rawPassword);

        log.debug("Password encoding - start");
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

    @Transactional(readOnly = true)
    public final Optional<User> getUserById(final Long id) {
        log.info("Getting user with ID: {}", id);

        log.debug("ID validation - start");
        validateId(id);

        log.debug("Database query - start");
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

    private static void validateId(final Long id) {
        if (id == null)
            throw new BusinessRuleViolationException("User id is <null>");
        if (id <= 0)
            throw new BusinessRuleViolationException("User id < 1");
    }

    @Transactional(readOnly = true)
    public final List<User> getAllUsers() {
    log.info("Getting all users");

    List<User> users = userRepository.findAll();
    log.info("Retrieved {} users", users.size());

    return users;
    }

    @Transactional
    public final User updateUserEmail(final UserUpdateEmailRequest request) {
        Long id = request.id();
        log.info("Updating user email for ID: {}", id);
        
        log.debug("ID validation - start");
        validateId(id);

        log.debug("ID existing check - start");
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User id=%d not found to update email", id)
            )
        );

        log.debug("Email validation - start");
        Email newEmail = new Email(request.newEmail());

        log.debug("Email uniqueness validation - start");
        boolean isUnique = !userRepository.existsByEmail(newEmail);
        user.changeEmail(newEmail, isUnique);

        User updatedUser = userRepository.save(user);
        log.info(
                "User email updated successfully for ID: {}, new email: {}",
                id, newEmail
        );

        return updatedUser;
    }

    @Transactional
    public final User updateUserPassword(final UserUpdatePasswordRequest request) {
        Long id = request.id();
        log.info("Updating user password for ID: {}", id);

        log.debug("ID validation - start");
        validateId(id);

        log.debug("ID existing check - start");
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User id=%d not found to update password", id)
            )
        );

        log.debug("Raw password validation - start");
        String rawPassword = request.newPassword();
        validateRawPassword(rawPassword);

        log.debug("Password encoding - start");
        Password newPassword = encodePassword(rawPassword);

        user.changePassword(newPassword);
        User updatedUser = userRepository.save(user);
        log.info("User password for ID: {} updated successfully", id);

        return updatedUser;
    }

    @Transactional
    public final void deleteUserById(final Long id) {
        log.info("Deleting user with ID: {}", id);

        log.debug("User ID validation - start");
        validateId(id);

        log.debug("ID existing check - start");
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException(
                    String.format("User id=%d not found to delete", id)
            );

        userRepository.deleteById(id);
        log.info("User with ID: {} deleted successfully", id);
    }

}
