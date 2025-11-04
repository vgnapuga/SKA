package com.ska.dto.validation.user;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ska.util.constant.UserConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = UserConstants.Password.DTO_REQUIRED_MESSAGE)
@Size(min = UserConstants.Password.ARGON2_PHC_SIZE_MIN, max = UserConstants.Password.ARGON2_PHC_SIZE_MAX, message = UserConstants.Password.DTO_INVALID_LENGTH_MESSAGE)
@Pattern(regexp = UserConstants.Password.ARGON2_PHC_REGEX, message = UserConstants.Password.DTO_INVALID_FORMAT_MESSAGE)
public @interface ValidPassword {
}
