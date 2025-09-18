package com.ska.vo.encrypted;


import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


public abstract class EncryptedValueObject extends BaseValueObject<byte[]> {

    protected EncryptedValueObject(final byte[] encryptedBytes) {
        super(encryptedBytes);
    }

    @Override
    protected void checkValidation(final byte[] encryptedBytes) {
        validateNotEmpty(encryptedBytes);
        validateReasonableSize(encryptedBytes);
    }

    private final void validateNotEmpty(final byte[] encryptedBytes) {
        if (encryptedBytes.length == 0)
            throw new DomainValidationException(this.getClass().getSimpleName() + " data is <empty>");
    }

    protected abstract void validateReasonableSize(final byte[] encryptedBytes);

    @Override
    public final String toString() {
        return String.format("%s{value=***}", this.getClass().getSimpleName());
    }

}
