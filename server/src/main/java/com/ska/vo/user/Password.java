package com.ska.vo.user;


import com.ska.constant.user.PasswordConstants;
import com.ska.exception.DomainValidationException;
import com.ska.model.user.converter.PasswordConverter;
import com.ska.vo.BaseValueObject;


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

    public Password(final String value) {
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
    protected void checkValidation(final String value) {
        validateNotBlank(value);

        if (value.length() != PasswordConstants.Format.BCRYPT_HASHED_SIZE) {
            throw new DomainValidationException(PasswordConstants.Messages.INVALID_BCRYPT_FORMAT_MESSAGE);
        }

        for (String str : PasswordConstants.Format.BCRYPT_PREFIXES) {
            if (value.startsWith(str))
                return;
        }
        throw new DomainValidationException(PasswordConstants.Messages.INVALID_BCRYPT_FORMAT_MESSAGE);
    }

    @Override
    public String toString() {
        return "Password{value=***}";
    }

}
