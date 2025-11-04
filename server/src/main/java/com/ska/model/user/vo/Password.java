package com.ska.model.user.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.BaseValueObject;
import com.ska.model.user.converter.PasswordConverter;
import com.ska.util.constant.UserConstants;


/**
 * Immutable password value object with BCrypt validation.
 * 
 * Extends {@link BaseValueObject}. Uses
 * {@link PasswordConstants#BCRYPT_HASHED_SIZE} for length validation and
 * {@link PasswordConstants#BCRYPT_PREFIXES} for format checking.
 * 
 * @see PasswordConstants - validation and exception messages constants
 * @see PasswordConverter - JPA persistence converter
 * @see DomainValidationException - thrown on validation failure
 */
public final class Password extends BaseValueObject<String> {

    public Password(String value) {
        super(value);
    }

    /**
     * Validates hashed password format.
     * 
     * @param value the hashed password string to validate
     * @throws DomainValidationException if value is blank, not exactly 60
     * characters, or has invalid BCrypt prefixes
     */
    @Override
    protected void checkValidation(String value) {
        validateNotBlank(value);

        if (value.length() != UserConstants.Password.BCRYPT_HASH_SIZE) {
            throw new DomainValidationException(UserConstants.Password.DOMAIN_INVALID_FORMAT_MESSAGE);
        }

        for (String str : UserConstants.Password.BCRYPT_PREFIXES) {
            if (value.startsWith(str))
                return;
        }
        throw new DomainValidationException(UserConstants.Password.DOMAIN_INVALID_FORMAT_MESSAGE);
    }

    @Override
    public final String toString() {
        return "Password{value=***}";
    }

}
