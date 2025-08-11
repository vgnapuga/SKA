package com.ska.vo.user;

import com.ska.exceptions.DomainValidationException;
import com.ska.vo.ValueObject;


public final class Password extends ValueObject<String> {

    private static final int BCRYPT_HASHED_SIZE = 60;


    public Password(final String value) {
        super(value);
    }


    @Override
    public final void checkValidation(final String value) {
        validateNotBlank(value);

        if ((!value.startsWith("$2a$") && !value.startsWith("$2b$")) ||
            value.length() != BCRYPT_HASHED_SIZE
        )
            throw new DomainValidationException("Password value is not a valid BCrypt hash");
    }

    
    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
