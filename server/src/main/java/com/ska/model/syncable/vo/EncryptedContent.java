package com.ska.model.syncable.vo;


import com.ska.exception.DomainValidationException;
import com.ska.util.constant.EntityConstants;


public final class EncryptedContent extends BaseEncryptedValueObject {

    public EncryptedContent(final byte[] encryptedValue) {
        super(encryptedValue);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > EntityConstants.Content.ENCRYPTED_DATA_SIZE_MAX)
            throw new DomainValidationException(
                    EntityConstants.Content.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }

}
