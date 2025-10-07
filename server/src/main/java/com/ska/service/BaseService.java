package com.ska.service;


import com.ska.exception.BusinessRuleViolationException;
import com.ska.exception.DomainValidationException;


/**
 * Base abstract class for all services.
 * 
 * @see BusinessRuleViolationException - thrown if business rules violation
 */
public abstract class BaseService {

    private static final String NULL_MESSAGE = "ID is <null>";
    private static final String LESS_THAN_ONE_MESSAGE = "ID is less than 1";

    /**
     * ID validation method.
     * 
     * @param id the entity identifier
     * @throws BusinessRuleViolationException if ID is null or less than 1
     */
    protected static void validateId(Long id) {
        if (id == null)
            throw new DomainValidationException(NULL_MESSAGE);
        if (id <= 0)
            throw new BusinessRuleViolationException(LESS_THAN_ONE_MESSAGE);
    }

}
