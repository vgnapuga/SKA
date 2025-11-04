package com.ska.model.syncable.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.BaseValueObject;


public abstract class BaseEncryptedValueObject extends BaseValueObject<byte[]> {

    protected BaseEncryptedValueObject(final byte[] encryptedBytes) {
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
