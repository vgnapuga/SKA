package com.ska.vo.user;

import com.ska.constant.user.PasswordConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.ValueObject;

/**
 * Password value object class with
 * BCrypt format validation.
 * 
 * Ensures hashed password format compliance.
 */
public final class Password extends ValueObject<String> {

    public Password(final String value) {
        super(value);
    }


    /**
     * Validates hashed password format.
     * 
     * @param value the hashed password string to validate
     * @throws DomainValidationException if value is blank, not exactly 60 characters, or has invalid BCrypt prefixes
     */
    @Override
    public final void checkValidation(final String value) {
        validateNotBlank(value);

        if (value.length() != PasswordConstants.BCRYPT_HASHED_SIZE) {
            throw new DomainValidationException(PasswordConstants.INVALID_BCRYPT_FORMAT_MESSAGE);
        }
        
        for (String str : PasswordConstants.BCRYPT_PREFIXES) {
            if (value.startsWith(str))
                return;
        }
        throw new DomainValidationException(PasswordConstants.INVALID_BCRYPT_FORMAT_MESSAGE);
    }
    
    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
