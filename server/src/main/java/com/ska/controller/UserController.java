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

import com.ska.dto.user.*;
import com.ska.model.user.User;
import com.ska.service.UserService;


@RestController
@RequestMapping("api/users")
public final class UserController {
    
    private final UserService userService;


    public UserController(final UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody final UserCreateRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable final Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(u)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<User> updateUserEmail(
            @PathVariable final Long id,
            @Valid @RequestBody UserUpdateEmailRequest request
    ) {
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
        if (!id.equals(request.id()))
            return ResponseEntity.badRequest().build();

        User updatedUser = userService.updateUserPassword(request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable final Long id
    ) {
        UserDeleteRequest request = new UserDeleteRequest(id);
        userService.deleteUser(request);

        return ResponseEntity.noContent().build();
    }

}
