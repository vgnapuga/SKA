package com.ska.vo.user;

import com.ska.constant.user.PasswordConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.ValueObject;


public final class Password extends ValueObject<String> {

    public Password(final String value) {
        super(value);
    }


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
