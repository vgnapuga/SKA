package com.ska.vo.user;

import com.ska.exceptions.DomainValidationException;
import com.ska.vo.ValueObject;
import com.ska.constants.user.EmailConstants;


public final class Email extends ValueObject<String> {

    public Email(String value) {
        super(value);
    }


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
