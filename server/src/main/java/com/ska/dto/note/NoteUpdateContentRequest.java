package com.ska.dto.note;


import com.ska.constant.note.NoteContentConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteUpdateContentRequest(
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE) String encryptedNewContent) {
}
