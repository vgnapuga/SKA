package com.ska.vo.user;

import com.ska.constant.user.EmailConstants;
import com.ska.model.user.converter.EmailConverter;
import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


/**
 * Immutable email value object with comprehensive validation.
 * 
 * Extends {@link BaseValueObject}.
 * Uses {@link EmailConstants#MAX_LENGTH} for length validation
 * and {@link EmailConstants#REGEX} for format checking.
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
     * @throws DomainValidationException if value is blank, exceeds 254 characters, or has invalid format
     */
    @Override
    public final void checkValidation(String value) {
        validateNotBlank(value);

        if (value.length() > EmailConstants.MAX_LENGTH)
            throw new DomainValidationException(EmailConstants.INVALID_LENGTH_MESSAGE);

        if (!value.matches(EmailConstants.REGEX))
            throw new DomainValidationException(EmailConstants.INVALID_FORMAT_MESSAGE);
    }
    
    @Override
    public final String toString() {
        return "Email{" +
            "value=" + this.value +
            "}";
    }

}
