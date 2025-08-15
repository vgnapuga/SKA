package com.ska.exception;


public final class BusinessRuleViolationException extends RuntimeException {
    
    public BusinessRuleViolationException(final String message) {
        super(message);
    }

}
