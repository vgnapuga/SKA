package com.ska.dto.note;


import com.ska.constant.note.NoteContentConstants;
import com.ska.constant.note.NoteTitleConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteUpdateAllRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE) String encryptedNewTitle,
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE) String encryptedNewContent) {
}
