package com.ska.model.syncable.vo;


import com.ska.exception.DomainValidationException;
import com.ska.util.constant.EntityConstants;


public final class EncryptedMetadata extends BaseEncryptedValueObject {

    public EncryptedMetadata(final byte[] value) {
        super(value);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > EntityConstants.Metadata.ENCRYPTED_DATA_SIZE_MAX)
            throw new DomainValidationException(
                    EntityConstants.Metadata.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }
}
