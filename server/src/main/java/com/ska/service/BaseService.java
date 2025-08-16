package com.ska.service;

import com.ska.exception.BusinessRuleViolationException;


/**
 * Base abstract class for all services.
 * 
 * Provides common methods to
 * avoid code duplication (DRY principle).
 */
public abstract class BaseService {

    /**
     * ID validation method.
     * 
     * @param id the entity identifier
     * @throws BusinessRuleViolationException if ID is null or less than 1
     */
    protected static void validateId(final Long id) {
        if (id == null)
            throw new BusinessRuleViolationException( "ID is <null>");
        if (id <= 0)
            throw new BusinessRuleViolationException("ID is less than 1");
    }

}
