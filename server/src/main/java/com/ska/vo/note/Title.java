package com.ska.vo.note;

import com.ska.constant.note.TitleConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseValueObject;


public final class Title extends BaseValueObject<String> {
    
    public Title(final String value) {
        super(value);
    }


    @Override
    protected void checkValidation(final String value) {
        validateNotBlank(value);

        if (value.length() > TitleConstants.Numeric.MAX_LENGTH)
            throw new DomainValidationException(TitleConstants.Messages.INVALID_LENGTH_MESSAGE);
    }

}
