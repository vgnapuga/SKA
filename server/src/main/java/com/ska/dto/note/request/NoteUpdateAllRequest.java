package com.ska.dto.note.request;


import com.ska.util.constant.note.NoteContentConstants;
import com.ska.util.constant.note.NoteTitleConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteUpdateAllRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE) String encryptedNewTitle,
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE) String encryptedNewContent) {
}
