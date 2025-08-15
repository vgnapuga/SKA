package com.ska.controller;

import java.util.Optional;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ska.dto.user.*;
import com.ska.model.user.User;
import com.ska.service.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public final class UserController {
    
    private final UserService userService;
    private static final String MAIN_PATH = "api/users";


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserCreateRequest request) {
        log.info("POST {} - email: {}", MAIN_PATH, request.email());

        User createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable final Long id) {
        log.info("GET {}/{}", MAIN_PATH, id);

        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(u)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<User> updateUserEmail(
            @PathVariable final Long id,
            @Valid @RequestBody UserUpdateEmailRequest request
    ) {
        log.info("PUT {}/{}/email - new email: {}", MAIN_PATH, id, request.newEmail());

        if (!id.equals(request.id()))
            return ResponseEntity.badRequest().build();

        User updatedUser = userService.updateUserEmail(request);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<User> updateUserPassword(
            @PathVariable final Long id,
            @Valid @RequestBody UserUpdatePasswordRequest request
    ) {
        log.info("PUT {}/{}/password - new password: ***", MAIN_PATH, id);

        if (!id.equals(request.id()))
            return ResponseEntity.badRequest().build();

        User updatedUser = userService.updateUserPassword(request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable final Long id) {
        log.info("DELETE {}/{}", MAIN_PATH, id);

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
