package com.ska.service.depended;


import java.util.Base64.Decoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ska.dto.note.request.NoteCreateRequest;
import com.ska.dto.note.request.NoteUpdateAllRequest;
import com.ska.dto.note.request.NoteUpdateContentRequest;
import com.ska.dto.note.request.NoteUpdateTitleRequest;
import com.ska.exception.ResourceNotFoundException;
import com.ska.model.syncable.note.Note;
import com.ska.model.syncable.note.vo.NoteContent;
import com.ska.model.syncable.note.vo.NoteTitle;
import com.ska.model.user.User;
import com.ska.repository.NoteRepository;
import com.ska.service.UserService;
import com.ska.util.LogTemplates;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public final class NoteService extends BaseDependedService {

    private final NoteRepository noteRepository;

    public NoteService(UserService userService, Decoder base64Decoder, NoteRepository noteRepository) {
        super(userService, base64Decoder);
        this.noteRepository = noteRepository;
    }

    @Transactional
    public Note createNote(Long userId, NoteCreateRequest request) {
        log.info("Creating note for user with ID: {}", userId);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkUserExistenceStartLog());
        User user = checkUserExistence(userId);

        log.debug(LogTemplates.checkBase64StartLog("Note title"));
        byte[] decodedTitle = decodeBase64(request.encryptedTitle());

        log.debug(LogTemplates.checkBase64StartLog("Note content"));
        byte[] decodedContent = decodeBase64(request.encryptedContent());

        log.debug(LogTemplates.validationStartLog("Note title"));
        NoteTitle title = new NoteTitle(decodedTitle);

        log.debug(LogTemplates.validationStartLog("Note content"));
        NoteContent content = new NoteContent(decodedContent);

        log.debug(LogTemplates.generateUuidStartLog());
        Note note = new Note(user, title, content);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Note savedNote = noteRepository.save(note);

        log.info("Note created successfully for user with ID: {}", userId);
        return savedNote;
    }

    @Transactional(readOnly = true)
    public List<Note> getAllNotesForUser(Long userId) {
        log.info("Getting all notes for user with ID: {}", userId);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        List<Note> retrievedNotes = noteRepository.getAllByUserId(userId);

        log.info("Retrieved {} notes for user with ID: {}", retrievedNotes.size(), userId);
        return retrievedNotes;
    }

    @Transactional(readOnly = true)
    public Optional<Note> getNoteByUuid(Long userId, UUID noteUuid) {
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
    public Note updateNoteTitleAndContent(Long userId, UUID noteUuid, NoteUpdateAllRequest request) {
        log.info("Updating note title and content for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note title"));
        byte[] decodedNewTitle = decodeBase64(request.encryptedNewTitle());

        log.debug(LogTemplates.checkBase64StartLog("New note content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New note title"));
        NoteTitle newTitle = new NoteTitle(decodedNewTitle);

        log.debug(LogTemplates.validationStartLog("New note content"));
        NoteContent newContent = new NoteContent(decodedNewContent);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent()) {
            note = retrievedNote.get();
            note.changeTitle(newTitle);
            note.changeContent(newContent);
        } else {
            throw new ResourceNotFoundException(
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
    public Note updateNoteTitle(Long userId, UUID noteUuid, NoteUpdateTitleRequest request) {
        log.info("Updating note title for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note title"));
        byte[] decodedNewTitle = decodeBase64(request.encryptedNewTitle());

        log.debug(LogTemplates.validationStartLog("New note title"));
        NoteTitle newTitle = new NoteTitle(decodedNewTitle);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent()) {
            note = retrievedNote.get();
            note.changeTitle(newTitle);
        } else {
            throw new ResourceNotFoundException("Note with with uuid=" + noteUuid + " was not found to update title");
        }

        log.debug(LogTemplates.checkPermissionStartLog("Update note title"));
        checkPermissionToAccess(userId, note);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        noteRepository.save(note);

        log.info("Note title was updated for user with ID: {} and note UUID: {}", userId, noteUuid);
        return note;
    }

    @Transactional
    public Note updateNoteContent(Long userId, UUID noteUuid, NoteUpdateContentRequest request) {
        log.info("Updating note content for user with ID: {} and note UUID: {}", userId, noteUuid);

        log.debug(LogTemplates.userIdValidationStartLog());
        validateId(userId);

        log.debug(LogTemplates.checkBase64StartLog("New note content"));
        byte[] decodedNewContent = decodeBase64(request.encryptedNewContent());

        log.debug(LogTemplates.validationStartLog("New note content"));
        NoteContent newContent = new NoteContent(decodedNewContent);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        Optional<Note> retrievedNote = noteRepository.getByUuid(noteUuid);

        Note note;
        if (retrievedNote.isPresent()) {
            note = retrievedNote.get();
            note.changeContent(newContent);
        } else {
            throw new ResourceNotFoundException("Note with with uuid=" + noteUuid + " was not found to update content");
        }

        log.debug(LogTemplates.checkPermissionStartLog("Update note content"));
        checkPermissionToAccess(userId, note);

        log.debug(LogTemplates.dataBaseQueryStartLog());
        noteRepository.save(note);

        log.info("Note content was updated for user with ID: {} and note UUID: {}", userId, noteUuid);
        return note;
    }

    @Transactional
    public void deleteNote(Long userId, UUID noteUuid) {
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
            throw new ResourceNotFoundException("Note with uuid=" + noteUuid + " was not found to delete");
        }

        log.info("Note was deleted successfully for user with ID: {} and note with UUID: {}", userId, noteUuid);
    }

}
