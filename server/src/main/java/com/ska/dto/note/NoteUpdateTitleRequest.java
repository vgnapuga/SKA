package com.ska.dto.note;


import com.ska.constant.note.NoteTitleConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteUpdateTitleRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE) String encryptedNewTitle) {
}
