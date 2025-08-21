package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;

import com.ska.constant.note.NoteContentConstants;


public record NoteUpdateContentRequest(
        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE)
        String encryptedNewContent
) {}
