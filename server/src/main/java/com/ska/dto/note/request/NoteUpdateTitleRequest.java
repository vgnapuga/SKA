package com.ska.dto.note.request;


import com.ska.dto.validation.note.ValidNoteTitle;


public final record NoteUpdateTitleRequest(@ValidNoteTitle String encryptedNewTitle) {
}
