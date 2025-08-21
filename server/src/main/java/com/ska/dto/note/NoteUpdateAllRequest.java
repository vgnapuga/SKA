package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;

import com.ska.constant.note.*;


public record NoteUpdateAllRequest(
        @NotBlank(message = NoteTitleConstants.Messages.REQUIRED_MESSAGE)
        String newTitle,

        @NotBlank(message = NoteContentConstants.Messages.REQUIRED_MESSAGE)
        String encryptedNoteContent
) {}
