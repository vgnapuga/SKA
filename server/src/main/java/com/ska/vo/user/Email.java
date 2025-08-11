package com.ska.vo.user;

import com.ska.exceptions.DomainValidationException;
import com.ska.vo.ValueObject;


public final class Email extends ValueObject<String> {

    private static final int MAX_EMAIL_LENGTH = 254;
        private static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";


    public Email(String value) {
        super(value);
    }


    @Override
    public final void checkValidation(String value) {
        validateNotBlank(value);

        if (value.length() > MAX_EMAIL_LENGTH)
            throw new DomainValidationException("Email longer than 254 characters");

        if (!value.matches(EMAIL_REGEX))
            throw new DomainValidationException("Invalid email format");
    }
    
    @Override
    public final String toString() {
        return "Email{" +
            "value=" + this.value +
            "}";
    }

}
