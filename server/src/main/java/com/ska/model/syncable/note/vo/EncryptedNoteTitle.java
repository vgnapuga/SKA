package com.ska.model.syncable.note.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObject;
import com.ska.util.constant.note.NoteTitleConstants;


public final class EncryptedNoteTitle extends EncryptedValueObject {

    public EncryptedNoteTitle(final byte[] value) {
        super(value);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > NoteTitleConstants.Format.MAX_ENCRYPTED_DATA_SIZE)
            throw new DomainValidationException(NoteTitleConstants.Messages.INVALID_ENCRYPTED_DATA_SIZE);
    }
}
