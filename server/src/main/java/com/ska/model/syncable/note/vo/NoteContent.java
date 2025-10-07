package com.ska.model.syncable.note.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.BaseEncryptedValueObject;
import com.ska.util.constant.NoteConstants;


public final class NoteContent extends BaseEncryptedValueObject {

    public NoteContent(final byte[] encryptedValue) {
        super(encryptedValue);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > NoteConstants.Content.MAX_ENCRYPTED_DATA_SIZE)
            throw new DomainValidationException(
                    NoteConstants.Content.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }

}
