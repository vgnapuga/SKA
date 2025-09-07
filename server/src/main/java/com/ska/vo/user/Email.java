package com.ska.vo.user;


import com.ska.constant.user.EmailConstants;
import com.ska.exception.DomainValidationException;
import com.ska.model.user.converter.EmailConverter;
import com.ska.vo.BaseValueObject;


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

    public Email(final String value) {
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
    protected void checkValidation(final String value) {
        validateNotBlank(value);

        if (value.length() > EmailConstants.Format.MAX_LENGTH)
            throw new DomainValidationException(EmailConstants.Messages.INVALID_LENGTH_MESSAGE);

        if (!value.matches(EmailConstants.Format.REGEX))
            throw new DomainValidationException(EmailConstants.Messages.INVALID_FORMAT_MESSAGE);
    }

}
