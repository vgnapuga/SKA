package com.ska.model.syncable.note.vo;


import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObject;
import com.ska.util.constant.note.NoteContentConstants;


public final class EncryptedNoteContent extends EncryptedValueObject {

    public EncryptedNoteContent(final byte[] encryptedValue) {
        super(encryptedValue);
    }

    @Override
    protected void validateReasonableSize(final byte[] encryptedValue) {
        if (encryptedValue.length > NoteContentConstants.Format.MAX_ENCRYPTED_DATA_SIZE)
            throw new DomainValidationException(NoteContentConstants.Messages.INVALID_ENCRYPTED_DATA_SIZE);
    }

}
