package com.ska.vo.encryptable;

import java.util.Base64;

import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


public abstract class EncryptedValueObject extends BaseValueObject<String> {

    protected EncryptedValueObject(final String encryptedValue) {
        super(encryptedValue);
    }


    @Override
    protected void checkValidation(final String encryptedValue) {
        validateNotBlank(encryptedValue);
        validateBase64Format(encryptedValue);
        validateReasonableSize(encryptedValue);
    }

    private void validateBase64Format(final String encryptedValue) {
        try {
            Base64.getDecoder().decode(encryptedValue);
        } catch (IllegalArgumentException e) {
            throw new DomainValidationException(this.getClass().getSimpleName() + " has invalid encrypted data format");
        }
    }

    protected abstract void validateReasonableSize(final String encryptedValue);
    
}
