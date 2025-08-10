package com.ska.vo.user;

import com.ska.vo.ValueObject;


public final class Password extends ValueObject<String> {

    private static final int minLength = 6;


    public Password(final String value) {
        super(value);
    }


    @Override
    public final void checkValidation(final String value) {
        validateNotBlank(value);

        if (value.length() < minLength)
            throw new IllegalArgumentException("Password must be at least " + minLength + " characters long");
    }

    
    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
