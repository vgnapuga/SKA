package com.ska.dto.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.ska.constant.note.*;


public record NoteCreateRequest(
        @NotBlank(message = TitleConstants.Messages.REQUIRED_MESSAGE)
        @Size(max = TitleConstants.Format.MAX_LENGTH, message = TitleConstants.Messages.INVALID_LENGTH_MESSAGE)
        String title,

        @Size(max = ContentConstants.Format.MAX_LENGTH, message = ContentConstants.Messages.INVALID_LENGTH_MESSAGE)
        String content
) {}
