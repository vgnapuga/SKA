package com.ska.model.user.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.BaseValueObject;
import com.ska.model.user.converter.EmailConverter;
import com.ska.util.constant.UserConstants;


/**
 * Immutable email value object with comprehensive validation.
 * 
 * Extends {@link BaseValueObject}. Uses {@link EmailConstants#MAX_LENGTH} for
 * length validation and {@link EmailConstants#REGEX} for format checking.
 * 
 * @see EmailConstants - validation and exception messages constants
 * @see EmailConverter - JPA persistence converter
 * @see DomainValidationException - thrown on validation failure
 */
public final class Email extends BaseValueObject<String> {

    public Email(String value) {
        super(value);
    }

    /**
     * Validates email format and length.
     * 
     * @param value the email string to validate
     * @throws DomainValidationException if value is blank, exceeds 254 characters,
     * or has invalid format
     */
    @Override
    protected void checkValidation(String value) {
        validateNotBlank(value);

        if (value.length() > UserConstants.Email.LENGTH_MAX)
            throw new DomainValidationException(UserConstants.Email.getDomainInvalidLengthMessage(value.length()));

        if (!value.matches(UserConstants.Email.REGEX))
            throw new DomainValidationException(UserConstants.Email.DOMAIN_INVALID_FORMAT_MESSAGE);
    }

}
