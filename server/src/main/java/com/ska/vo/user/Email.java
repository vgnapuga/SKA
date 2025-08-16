package com.ska.vo.user;

import com.ska.constant.user.EmailConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.ValueObject;


/**
 * Email value object class with
 * format and length validation.
 * 
 * Ensures email format compliance and length constraints.
 */
public final class Email extends ValueObject<String> {

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
