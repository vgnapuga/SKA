package com.ska.model.syncable.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.BaseEncryptedValueObject;
import com.ska.util.constant.EntityConstants;


public final class EncryptedContent extends BaseEncryptedValueObject {

    public EncryptedContent(final byte[] encryptedValue) {
        super(encryptedValue);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > EntityConstants.Content.MAX_ENCRYPTED_DATA_SIZE)
            throw new DomainValidationException(
                    EntityConstants.Content.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }

}
