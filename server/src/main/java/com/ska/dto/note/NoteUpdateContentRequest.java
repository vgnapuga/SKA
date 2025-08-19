package com.ska.dto.note;

import jakarta.validation.constraints.Size;

import com.ska.constant.note.ContentConstants;


public record NoteUpdateContentRequest(
        @Size(max = ContentConstants.Format.MAX_LENGTH, message = ContentConstants.Messages.INVALID_LENGTH_MESSAGE)
        String newContent
) {}
