package com.ska.service;

import com.ska.exception.BusinessRuleViolationException;

public abstract class BaseService {

    
    protected static void validateId(final Long id) {
        if (id == null)
            throw new BusinessRuleViolationException( "ID is <null>");
        if (id <= 0)
            throw new BusinessRuleViolationException("ID < 1");
    }

}
