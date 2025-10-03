package com.ska.dto.note.request;


import com.ska.util.constant.note.NoteContentConstants;
import com.ska.util.constant.note.NoteTitleConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteCreateRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE) String encryptedTitle,
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE) String encryptedContent) {
}
