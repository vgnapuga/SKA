package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;

import com.ska.constant.note.*;


public record NoteCreateRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE)
        String encryptedTitle,

        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE)
        String encryptedContent
) {}
