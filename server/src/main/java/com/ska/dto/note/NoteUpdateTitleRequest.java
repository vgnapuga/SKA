package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.ska.constant.note.TitleConstants;


public record NoteUpdateTitleRequest(
        @NotBlank(message = TitleConstants.Messages.REQUIRED_MESSAGE)
        @Size(max = TitleConstants.Format.MAX_LENGTH, message = TitleConstants.Messages.INVALID_LENGTH_MESSAGE)
        String newTitle
) {}
