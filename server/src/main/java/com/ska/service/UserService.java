package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.user.*;
import com.ska.exceptions.BusinessRuleViolationException;
import com.ska.exceptions.ResourceAlreadyExistsException;
import com.ska.exceptions.ResourceNotFoundException;
import com.ska.model.user.User;
import com.ska.repository.UserRepository;
import com.ska.vo.user.*;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final int PASSWORD_MIN_LENGTH = 6;


    public UserService(
                final UserRepository userRepository,
                final BCryptPasswordEncoder passwordEncoder
                ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional()
    public final User createUser(final UserCreateRequest request) {
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistsException("User with email=" + email.toString() + " already exists");

        Password password = encodePassword(request.password());

        User user = new User(email, password);
        return userRepository.save(user);
    }

    private final Password encodePassword(final String rawPassword) {
        if (rawPassword.length() < PASSWORD_MIN_LENGTH)
            throw new BusinessRuleViolationException("Password shorter than " + PASSWORD_MIN_LENGTH + " characters");

        String hashed = passwordEncoder.encode(rawPassword);

        return new Password(hashed);
    }

    @Transactional(readOnly = true)
    public final Optional<User> getUserById(final Long id) {
        validateId(id);

        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public final List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public final User updateUserEmail(final UserUpdateEmailRequest request) {
        Long id = request.id();
        validateId(id);

        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User id=" + id + " not found to update email")
        );

        Email newEmail = new Email(request.newEmail());

        boolean isUnique = !userRepository.existsByEmail(newEmail);
        user.changeEmail(newEmail, isUnique);

        return userRepository.save(user);
    }

    @Transactional
    public final User updateUserPassword(final UserUpdatePasswordRequest request) {
        Long id = request.id();
        validateId(id);

        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User id=" + id + " not found to update password")
        );

        Password newPassword = encodePassword(request.newPassword());

        user.changePassword(newPassword);
        return userRepository.save(user);
    }

    @Transactional
    public final void deleteUser(final UserDeleteRequest request) {
        Long id = request.id();
        validateId(id);

        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User id=" + id + " not found to delete");

        userRepository.deleteById(id);
    }

    private static void validateId(final Long id) {
        if (id == null)
            throw new BusinessRuleViolationException("User id is <null>");
        if (id < 0)
            throw new BusinessRuleViolationException("User id is negative");
    }

}
