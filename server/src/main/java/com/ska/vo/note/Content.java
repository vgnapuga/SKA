package com.ska.vo.note;

import com.ska.constant.note.ContentConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


public final class Content extends BaseValueObject<String> {
    
    public Content(final String value) {
        super(value);
    }

    
    @Override
    protected final void checkValidation(final String value) {
        if (value.length() > ContentConstants.Numeric.MAX_LENGTH)
            throw new DomainValidationException(ContentConstants.Messages.INVALID_LENGTH_MESSAGE);
    }

    @Override
    public final String toString() {
        return "Content{" +
                "value=" + this.value +
                "}";
    }

}
