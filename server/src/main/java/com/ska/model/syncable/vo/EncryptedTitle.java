package com.ska.model.syncable.vo;


import com.ska.exception.DomainValidationException;
import com.ska.util.constant.EntityConstants;


public final class EncryptedTitle extends BaseEncryptedValueObject {

    public EncryptedTitle(final byte[] value) {
        super(value);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > EntityConstants.Title.ENCRYPTED_DATA_SIZE_MAX)
            throw new DomainValidationException(
                    EntityConstants.Title.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }
}
