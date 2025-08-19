package com.ska.service;

import lombok.extern.slf4j.Slf4j;

import com.ska.exception.BusinessRuleViolationException;
import com.ska.util.LogTemplates;


/**
 * Base abstract class for all services.
 * 
 * @see BusinessRuleViolationException - thrown if business rules violation
 */
@Slf4j
public abstract class BaseService {

    /**
     * ID validation method.
     * 
     * @param id the entity identifier
     * @throws BusinessRuleViolationException if ID is null or less than 1
     */
    protected static void validateId(final Long id) {
        log.debug(LogTemplates.validationStartLog("ID"));

        if (id == null)
            throw new BusinessRuleViolationException( "ID is <null>");
        if (id <= 0)
            throw new BusinessRuleViolationException("ID is less than 1");
    }

}
