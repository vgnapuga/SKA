package com.ska.vo.user;

import com.ska.vo.ValueObject;


public final class Password extends ValueObject<String> {

    public Password(final String value) {
        super(value);
    }


    @Override
    public final void checkValidation(final String value) {
        validateNotBlank(value);

        if (!value.startsWith("$2a$") && !value.startsWith("$2b$"))
            throw new IllegalArgumentException("Password value is not a valid BCrypt hash");
    }

    
    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
