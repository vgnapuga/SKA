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

import com.ska.dto.note.request.NoteCreateRequest;
import com.ska.dto.note.request.NoteUpdateAllRequest;
import com.ska.dto.note.request.NoteUpdateContentRequest;
import com.ska.dto.note.request.NoteUpdateTitleRequest;
import com.ska.dto.note.response.NoteResponse;
import com.ska.model.syncable.note.Note;
import com.ska.service.depended.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("api/notes")
@RequiredArgsConstructor
public final class NoteController {

    private final NoteService noteService;
    private static final String ROOT = "api/notes";

    @PostMapping("/{userId}")
    public ResponseEntity<NoteResponse> createNote(
            @PathVariable Long userId,
            @Valid @RequestBody NoteCreateRequest request) {
        log.info("POST - {}/{}", ROOT, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.of(noteService.createNote(userId, request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Note>> getAllNotesForUser(@PathVariable Long userId) {
        log.info("GET - {}/{}", ROOT, userId);

        List<Note> notes = noteService.getAllNotesForUser(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{userId}/{noteUuid}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long userId, @PathVariable UUID noteUuid) {
        log.info("GET - {}/{}/{}", ROOT, userId, noteUuid);
        return ResponseEntity.ok(NoteResponse.of(noteService.getNoteByUuid(userId, noteUuid)));
    }

    @PutMapping("/{userId}/{noteUuid}")
    public ResponseEntity<NoteResponse> updateNoteTitleAndContent(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody NoteUpdateAllRequest request) {
        log.info("PUT - {}/{}/{}", ROOT, userId, noteUuid);
        return ResponseEntity.ok(NoteResponse.of(noteService.updateNoteTitleAndContent(userId, noteUuid, request)));
    }

    @PutMapping("/{userId}/{noteUuid}/title")
    public ResponseEntity<NoteResponse> updateNoteTitle(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody NoteUpdateTitleRequest request) {
        log.info("PUT - {}/{}/{}/title", ROOT, userId, noteUuid);
        return ResponseEntity.ok(NoteResponse.of(noteService.updateNoteTitle(userId, noteUuid, request)));
    }

    @PutMapping("/{userId}/{noteUuid}/content")
    public ResponseEntity<NoteResponse> updateNoteContent(
            @PathVariable Long userId,
            @PathVariable UUID noteUuid,
            @Valid @RequestBody NoteUpdateContentRequest request) {
        log.info("PUT - {}/{}/{}/content", ROOT, userId, noteUuid);
        return ResponseEntity.ok(NoteResponse.of(noteService.updateNoteContent(userId, noteUuid, request)));
    }

    @DeleteMapping("/{userId}/{noteUuid}")
    public ResponseEntity<Void> deleteNoteByUuid(@PathVariable final Long userId, @PathVariable final UUID noteUuid) {
        log.info("DELETE - {}/{}/{}", ROOT, userId, noteUuid);

        noteService.deleteNote(userId, noteUuid);
        return ResponseEntity.noContent().build();
    }

}
