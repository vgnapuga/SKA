package com.ska.vo.encryptable.note;

import com.ska.constant.note.ContentConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


public final class Content extends BaseValueObject<String> {
    
    public Content(final String value) {
        super(value);
    }

    
    @Override
    protected void checkValidation(final String value) {
        if (value.length() > ContentConstants.Format.MAX_LENGTH)
            throw new DomainValidationException(ContentConstants.Messages.INVALID_LENGTH_MESSAGE);
    }

}
