package com.ska.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import com.ska.dto.UserCreateRequest;
import com.ska.dto.UserDeleteRequest;
import com.ska.dto.UserUpdateEmailRequest;
import com.ska.dto.UserUpdatePasswordRequest;
import com.ska.model.User;
import com.ska.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


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

        String email = request.email();
        if (userRepository.existsByEmail(email))
            throw new EntityExistsException("User with email=" + email + " already exists");

        String password = encodePassword(request.password());

        User user = new User(email, password);
        return userRepository.save(user);
    }

    private final String encodePassword(final String password) {
        return passwordEncoder.encode(password);
    }

    public final Optional<User> getUserById(final Long id) {
        if (id == null)
            throw new IllegalArgumentException("User id must not be <null>");

        return userRepository.findById(id);
    }

    public final List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public final User updateUserEmail(final UserUpdateEmailRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");

        Long id = request.id();
        User user = userRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("User id=" + id + " not found to update email")
        );

        String newEmail = request.newEmail();
        if (userRepository.existsByEmail(newEmail))
            throw new EntityExistsException("Email=" + newEmail + " already exists");


        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public final User updateUserPassword(final UserUpdatePasswordRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");

        Long id = request.id();
        User user = userRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("User id=" + id + " not found to update password")
        );

        String newPassword = encodePassword(request.newPassword());

        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public final void deleteUser(final UserDeleteRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request is <null>");
            
        Long id = request.id();
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User id=" + id + " not found to delete");

        userRepository.deleteById(id);
    }

}
