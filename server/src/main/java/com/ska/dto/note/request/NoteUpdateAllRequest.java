package com.ska.dto.note.request;


import com.ska.dto.validation.note.ValidNoteContent;
import com.ska.dto.validation.note.ValidNoteTitle;


public final record NoteUpdateAllRequest(
        @ValidNoteTitle String encryptedNewTitle,
        @ValidNoteContent String encryptedNewContent) {
}
