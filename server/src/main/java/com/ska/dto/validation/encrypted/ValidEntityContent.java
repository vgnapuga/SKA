package com.ska.dto.validation.encrypted;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ska.util.constant.EntityConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = EntityConstants.Content.DTO_REQUIRED_MESSAGE)
@Size(max = EntityConstants.Content.BASE64_SIZE_MAX, message = EntityConstants.Content.DTO_INVALID_BASE64_LENGTH_MESSAGE)
public @interface ValidEntityContent {
}
