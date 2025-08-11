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

        if (value.length() != BCRYPT_HASHED_SIZE) {
            throw new DomainValidationException(
                String.format("Password must be exactly %d characters (BCrypt hash), got %d",
                    BCRYPT_HASHED_SIZE, value.length()
                )
            );
        }
        
        if (!value.startsWith("$2a$") && !value.startsWith("$2b$") && !value.startsWith("$2y$")) {
            throw new DomainValidationException(
                "Password must start with $2a$, $2b$ or $2y$ (BCrypt format)"
            );
        }
    }

    
    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
