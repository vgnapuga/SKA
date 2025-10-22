package com.ska.dto.note.response;


import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import com.ska.model.syncable.note.Note;
import com.ska.util.constant.NoteConstants;


public record NoteResponse(
        UUID uuid,
        String base64Title,
        String base64Content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static NoteResponse of(final Note note) {
        java.util.Objects.requireNonNull(note, NoteConstants.NULL_MESSAGE);

        String base64Title = Base64.getEncoder().encodeToString(note.getTitle().getValue());
        String base64Content = Base64.getEncoder().encodeToString(note.getContent().getValue());

        return new NoteResponse(
                note.getUuid(),
                base64Title,
                base64Content,
                note.getCreationTime(),
                note.getUpdateTime());
    }

}
