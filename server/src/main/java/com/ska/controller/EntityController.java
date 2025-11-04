package com.ska.controller;


import java.util.List;
import java.util.UUID;

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

import com.ska.dto.entity.request.EntityCreateRequest;
import com.ska.dto.entity.request.EntityUpdateAllRequest;
import com.ska.dto.entity.request.EntityUpdateContentRequest;
import com.ska.dto.entity.request.EntityUpdateTitleRequest;
import com.ska.dto.entity.response.EntityResponse;
import com.ska.model.syncable.Syncable;
import com.ska.service.EntityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("api/entities")
@RequiredArgsConstructor
public final class EntityController {

    private final EntityService entityService;
    private static final String ROOT = "api/entities";

    @PostMapping("/{userId}/{uuid}")
    public ResponseEntity<EntityResponse> createEntity(
            @PathVariable Long userId,
            @PathVariable UUID uuid,
            @Valid @RequestBody EntityCreateRequest request) {
        log.info("POST - {}/{}", ROOT, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                EntityResponse.of(entityService.create(userId, uuid, request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Syncable>> getAllEntitiesForUser(@PathVariable Long userId) {
        log.info("GET - {}/{}", ROOT, userId);

        List<Syncable> notes = entityService.getAll(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{userId}/{uuid}")
    public ResponseEntity<EntityResponse> getEntityByUuid(@PathVariable Long userId, @PathVariable UUID noteUuid) {
        log.info("GET - {}/{}/{}", ROOT, userId, noteUuid);
        return ResponseEntity.ok(EntityResponse.of(entityService.getByUuid(userId, noteUuid)));
    }

    @PutMapping("/{userId}/{uuid}")
    public ResponseEntity<EntityResponse> updateEntityTitleAndContent(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody EntityUpdateAllRequest request) {
        log.info("PUT - {}/{}/{}", ROOT, userId, noteUuid);
        return ResponseEntity.ok(EntityResponse.of(entityService.updateTitleAndContent(userId, noteUuid, request)));
    }

    @PutMapping("/{userId}/{uuid}/title")
    public ResponseEntity<EntityResponse> updateEntityTitle(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody EntityUpdateTitleRequest request) {
        log.info("PUT - {}/{}/{}/title", ROOT, userId, noteUuid);
        return ResponseEntity.ok(EntityResponse.of(entityService.updateTitle(userId, noteUuid, request)));
    }

    @PutMapping("/{userId}/{uuid}/content")
    public ResponseEntity<EntityResponse> updateEntityContent(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody EntityUpdateContentRequest request) {
        log.info("PUT - {}/{}/{}/content", ROOT, userId, noteUuid);
        return ResponseEntity.ok(EntityResponse.of(entityService.updateContent(userId, noteUuid, request)));
    }

    @DeleteMapping("/{userId}/{uuid}")
    public ResponseEntity<Void> deleteEntityByUuid(@PathVariable final Long userId, @PathVariable final UUID noteUuid) {
        log.info("DELETE - {}/{}/{}", ROOT, userId, noteUuid);

        entityService.delete(userId, noteUuid);
        return ResponseEntity.noContent().build();
    }

}
