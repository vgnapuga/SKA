package com.ska.exceptions;


public final class BusinessRuleViolationException extends RuntimeException {
    
    public BusinessRuleViolationException(final String message) {
        super(message);
    }

}
