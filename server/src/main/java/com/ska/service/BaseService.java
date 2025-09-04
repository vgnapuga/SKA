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

    private static final String NULL_MESSAGE = "ID is <null>";
    private static final String LESS_THAN_ONE_MESSAGE = "ID is less than 1";

    /**
     * ID validation method with validation log.
     * 
     * @param id the entity identifier
     * @throws BusinessRuleViolationException if ID is null or less than 1
     */
    protected static void validateId(final Long id) {
        log.debug(LogTemplates.validationStartLog("ID"));

        if (id == null)
            throw new BusinessRuleViolationException( NULL_MESSAGE);
        if (id <= 0)
            throw new BusinessRuleViolationException(LESS_THAN_ONE_MESSAGE);
    }

}
