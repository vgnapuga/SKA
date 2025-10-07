package com.ska.model.syncable.note.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.BaseEncryptedValueObject;
import com.ska.util.constant.NoteConstants;


public final class NoteTitle extends BaseEncryptedValueObject {

    public NoteTitle(final byte[] value) {
        super(value);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > NoteConstants.Title.MAX_ENCRYPTED_DATA_SIZE)
            throw new DomainValidationException(
                    NoteConstants.Title.getDomainInvalidDataSizeMessage(encryptedValue.length));
    }
}
