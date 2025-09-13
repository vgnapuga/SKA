package com.ska.service.depended;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.note.NoteCreateRequest;
import com.ska.dto.note.NoteUpdateAllRequest;
import com.ska.dto.note.NoteUpdateContentRequest;
import com.ska.dto.note.NoteUpdateTitleRequest;
import com.ska.model.syncable.note.Note;
import com.ska.model.user.User;
import com.ska.repository.NoteRepository;
import com.ska.service.UserService;
import com.ska.util.LogTemplates;
import com.ska.vo.encrypted.note.EncryptedNoteContent;
import com.ska.vo.encrypted.note.EncryptedNoteTitle;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class NoteService extends DependedService {

    private final NoteRepository noteRepository;

    public NoteService(final UserService userService, final NoteRepository noteRepository) {
        super(userService);
        this.noteRepository = noteRepository;
    }

    @Transactional
    public final Note createNote(final Long userId, final NoteCreateRequest request) {
        log.info("Creating note for user: {}; with title: {}", userId, request.encryptedTitle());

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkUserExistenceStartLog());
        User user = checkUserExistence(userId);

        log.debug(LogTemplates.checkBase64StartLog("Note title"));
        byte[] decodedTitle = decodeBase64(request.encryptedTitle());

        log.debug(LogTemplates.checkBase64StartLog("Note content"));
        byte[] decodedContent = decodeBase64(request.encryptedContent());

        log.debug(LogTemplates.validationStartLog("Note title"));
        EncryptedNoteTitle title = new EncryptedNoteTitle(decodedTitle);

        log.debug(LogTemplates.validationStartLog("Note content"));
        EncryptedNoteContent content = new EncryptedNoteContent(decodedContent);

        log.debug(LogTemplates.generateUuidStartLog());
        Note note = new Note(user, title, content);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Note savedNote = noteRepository.save(note);

        log.info(
                "Note created successfully for user: {}; with ID: {}, title: {}",
                userId,
                savedNote.getId(),
                title.getValue());

        return savedNote;
    }

    @Transactional(readOnly = true)
    public final List<Note> getAllNotesForUser(final Long userId) {
        log.info("Getting all notes for user with ID: {}", userId);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        List<Note> retrievedNotes = noteRepository.getAllByUserId(userId);

        log.info("Retrieved {} notes for user with ID: {}", retrievedNotes.size(), userId);

        return retrievedNotes;
    }

    @Transactional(readOnly = true)
    public final Optional<Note> getNoteByUuid(final Long userId, final UUID noteUuid) {
        log.info("Getting note with UUID: {} for user with ID: {}", noteUuid, userId);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        if (retrievedNote.isPresent()) {
            log.debug(LogTemplates.checkPermissionStartLog("Get"));
            checkPermissionToAccess(userId, retrievedNote.get());

            log.info("Note with UUID: {} for user with ID: {} retrieved successfully", noteUuid, userId);
        } else {
            log.info("Note with UUID: {} not found", noteUuid);
        }

        return retrievedNote;
    }

    @Transactional
    public final Note updateNoteTitleAndContent(
            final Long userId,
            final UUID noteUuid,
            final NoteUpdateAllRequest request) {
        log.info("Updating note title and content for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note title"));
        byte[] decodedNewTitle = decodeBase64(request.encryptedNewTitle());

        log.debug(LogTemplates.checkBase64StartLog("New note content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New note title"));
        EncryptedNoteTitle newTitle = new EncryptedNoteTitle(decodedNewTitle);

        log.debug(LogTemplates.validationStartLog("New note content"));
        EncryptedNoteContent newContent = new EncryptedNoteContent(decodedNewContent);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent()) {
            note = retrievedNote.get();
            note.changeTitle(newTitle);
            note.changeContent(newContent);
        } else {
            throw new EntityNotFoundException(
                    "Note with uuid=" + noteUuid + " was not found to update title and content");
        }

        log.debug(LogTemplates.checkPermissionStartLog("Update note title and content"));
        checkPermissionToAccess(userId, note);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        noteRepository.save(note);

        log.info("Note title and content was updated for user with ID: {} and note UUID: {}", userId, noteUuid);

        return note;
    }

    @Transactional
    public final Note updateNoteTitle(final Long userId, final UUID noteUuid, final NoteUpdateTitleRequest request) {
        log.info("Updating note title for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note title"));
        byte[] decodedNewTitle = decodeBase64(request.encryptedNewTitle());

        log.debug(LogTemplates.validationStartLog("New note title"));
        EncryptedNoteTitle newTitle = new EncryptedNoteTitle(decodedNewTitle);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent())
            note = retrievedNote.get();
        else
            throw new EntityNotFoundException("Note with with uuid=" + noteUuid + " was not found to update title");

        log.debug(LogTemplates.checkPermissionStartLog("Update note title"));
        checkPermissionToAccess(userId, note);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Note updatedNote = new Note(note.getUser(), newTitle, note.getContent());
        noteRepository.save(updatedNote);

        log.info("Note title was updated for user with ID: {} and note UUID: {}", userId, noteUuid);

        return updatedNote;
    }

    @Transactional
    public final Note updateNoteContent(
            final Long userId,
            final UUID noteUuid,
            final NoteUpdateContentRequest request) {
        log.info("Updating note content for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New note content"));
        EncryptedNoteContent newContent = new EncryptedNoteContent(decodedNewContent);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent()) {
            note = retrievedNote.get();
            note.changeContent(newContent);
        } else {
            throw new EntityNotFoundException("Note with with uuid=" + noteUuid + " was not found to update content");
        }

        log.debug(LogTemplates.checkPermissionStartLog("Update note content"));
        checkPermissionToAccess(userId, note);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        noteRepository.save(note);

        log.info("Note content was updated for user with ID: {} and note UUID: {}", userId, noteUuid);

        return note;
    }

    @Transactional
    public final void deleteNote(final Long userId, final UUID noteUuid) {
        log.info("Deleting note for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        if (retrievedNote.isPresent()) {
            log.debug(LogTemplates.checkPermissionStartLog("Delete note"));
            checkPermissionToAccess(userId, retrievedNote.get());

            noteRepository.delete(retrievedNote.get());
        } else {
            throw new EntityNotFoundException("Note with uuid=" + noteUuid + " was not found to delete");
        }

        log.info("Note was deleted successfully for user with ID: {} and note with UUID: {}", userId, noteUuid);
    }

}
