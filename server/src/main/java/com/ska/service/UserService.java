package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import com.ska.dto.user.*;
import com.ska.model.user.User;
import com.ska.repository.UserRepository;
import com.ska.vo.user.*;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final int minLength = 6;


    public UserService(
                final UserRepository userRepository,
                final BCryptPasswordEncoder passwordEncoder
                ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public final User createUser(final UserCreateRequest request) {
        if(request == null)
            throw new IllegalArgumentException("Request is <null>");

        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email))
            throw new EntityExistsException("User with email=" + email.toString() + " already exists");

        Password password = encodePassword(request.password());

        User user = new User(email, password);
        return userRepository.save(user);
    }

    private final Password encodePassword(final String rawPassword) {
        if (rawPassword.length() < minLength)
            throw new IllegalArgumentException("Password shorter than " + minLength + " characters");

        String hashed = passwordEncoder.encode(rawPassword);

        return new Password(hashed);
    }

    public final Optional<User> getUserById(final Long id) {
        validateId(id);

        return userRepository.findById(id);
    }

    public final List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public final User updateUserEmail(final UserUpdateEmailRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");

        Long id = request.id();
        validateId(id);

        User user = userRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("User id=" + id + " not found to update email")
        );

        Email newEmail = new Email(request.newEmail());

        boolean isUnique = !userRepository.existsByEmail(newEmail);
        user.changeEmail(newEmail, isUnique);

        return userRepository.save(user);
    }

    public final User updateUserPassword(final UserUpdatePasswordRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");

        Long id = request.id();
        validateId(id);

        User user = userRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("User id=" + id + " not found to update password")
        );

        Password newPassword = encodePassword(request.newPassword());

        user.changePassword(newPassword);
        return userRepository.save(user);
    }

    public final void deleteUser(final UserDeleteRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");
            
        Long id = request.id();
        validateId(id);

        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User id=" + id + " not found to delete");

        userRepository.deleteById(id);
    }

    private static void validateId(final Long id) {
        if (id == null)
            throw new IllegalArgumentException("User id is <null>");
        if (id < 0)
            throw new IllegalArgumentException("User id is negative");
    }

}
