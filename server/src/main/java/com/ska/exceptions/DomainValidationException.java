package com.ska.exceptions;

public final class DomainValidationException extends RuntimeException {
    
    public DomainValidationException(final String message) {
        super(message);
    }

}
