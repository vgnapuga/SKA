package com.ska.vo.encrypted.note;

import com.ska.constant.note.NoteTitleConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.encrypted.EncryptedValueObject;


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
