package com.ska.dto.note.request;


import com.ska.dto.validation.note.ValidNoteContent;
import com.ska.dto.validation.note.ValidNoteTitle;


public final record NoteCreateRequest(
        @ValidNoteTitle String encryptedTitle,
        @ValidNoteContent String encryptedContent) {
}
