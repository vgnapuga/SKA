package com.ska.dto.validation.user;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ska.util.constant.UserConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = UserConstants.Email.DTO_REQUIRED_MESSAGE)
@Size(max = UserConstants.Email.LENGTH_MAX, message = UserConstants.Email.DTO_INVALID_LENGTH_MESSAGE)
@Email(message = UserConstants.Email.DTO_INVALID_FORMAT_MESSAGE)
public @interface ValidEmail {
}
