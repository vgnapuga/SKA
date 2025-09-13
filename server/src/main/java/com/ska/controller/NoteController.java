package com.ska.controller;


import java.util.List;
import java.util.Optional;
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

import com.ska.dto.note.NoteCreateRequest;
import com.ska.dto.note.NoteUpdateAllRequest;
import com.ska.dto.note.NoteUpdateContentRequest;
import com.ska.dto.note.NoteUpdateTitleRequest;
import com.ska.model.syncable.note.Note;
import com.ska.service.depended.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/notes")
public final class NoteController {

    private final NoteService noteService;
    private static final String MAIN_PATH = "api/notes";

    @PostMapping("/{userId}")
    public ResponseEntity<Note> createNote(
            @PathVariable final Long userId,
            @Valid @RequestBody final NoteCreateRequest request) {
        log.info("POST {}/{}", MAIN_PATH, userId);

        Note createdNote = noteService.createNote(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Note>> getAllNotesForUser(@PathVariable final Long userId) {
        log.info("GET {}/{}", MAIN_PATH, userId);

        List<Note> notes = noteService.getAllNotesForUser(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{userId}/{noteUuid}")
    public ResponseEntity<Note> getNote(@PathVariable final Long userId, @PathVariable final UUID noteUuid) {
        log.info("GET {}/{}/{}", MAIN_PATH, userId, noteUuid);

        Optional<Note> retrievedNote = noteService.getNoteByUuid(userId, noteUuid);
        return retrievedNote.map(u -> ResponseEntity.ok(u)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/{noteUuid}")
    public ResponseEntity<Note> updateNoteTitleAndContent(
            @PathVariable final Long userId,
            @PathVariable final UUID noteUuid,
            @Valid @RequestBody NoteUpdateAllRequest request) {
        log.info("PUT {}/{}/{}", MAIN_PATH, userId, noteUuid);

        Note updatedNote = noteService.updateNoteTitleAndContent(userId, noteUuid, request);
        return ResponseEntity.ok(updatedNote);
    }

    @PutMapping("/{userId}/{noteUuid}/title")
    public ResponseEntity<Note> updateNoteTitle(
            @PathVariable final Long userId,
            @PathVariable final UUID noteUuid,
            @Valid @RequestBody NoteUpdateTitleRequest request) {
        log.info("PUT {}/{}/{}/title", MAIN_PATH, userId, noteUuid);

        Note updatedNote = noteService.updateNoteTitle(userId, noteUuid, request);
        return ResponseEntity.ok(updatedNote);
    }

    @PutMapping("/{userId}/{noteUuid}/content")
    public ResponseEntity<Note> updateNoteContent(
            @PathVariable final Long userId,
            @PathVariable final UUID noteUuid,
            @Valid @RequestBody NoteUpdateContentRequest request) {
        log.info("PUT {}/{}/{}/content", MAIN_PATH, userId, noteUuid);

        Note updatedNote = noteService.updateNoteContent(userId, noteUuid, request);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{userId}/{noteUuid}")
    public ResponseEntity<Void> deleteNoteByUuid(@PathVariable final Long userId, @PathVariable final UUID noteUuid) {
        log.info("DELETE {}/{}/{}", MAIN_PATH, userId, noteUuid);

        noteService.deleteNote(userId, noteUuid);
        return ResponseEntity.noContent().build();
    }

}
