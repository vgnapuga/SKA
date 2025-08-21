package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;

import com.ska.constant.note.NoteTitleConstants;


public record NoteUpdateTitleRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE)
        String encryptedNewTitle
) {}
