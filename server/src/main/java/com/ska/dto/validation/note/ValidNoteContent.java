package com.ska.dto.validation.note;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ska.util.constant.NoteConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = NoteConstants.Content.DTO_REQUIRED_MESSAGE)
@Size(max = NoteConstants.Content.MAX_BASE64_SIZE, message = NoteConstants.Content.DTO_INVALID_BASE64_LENGTH_MESSAGE)
public @interface ValidNoteContent {
}
