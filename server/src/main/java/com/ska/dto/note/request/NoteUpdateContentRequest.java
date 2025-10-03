package com.ska.dto.note.request;


import com.ska.util.constant.note.NoteContentConstants;

import jakarta.validation.constraints.NotBlank;


public record NoteUpdateContentRequest(
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE) String encryptedNewContent) {
}
