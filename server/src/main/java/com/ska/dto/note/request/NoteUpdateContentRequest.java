package com.ska.dto.note.request;


import com.ska.dto.validation.note.ValidNoteContent;


public final record NoteUpdateContentRequest(@ValidNoteContent String encryptedNewContent) {
}
