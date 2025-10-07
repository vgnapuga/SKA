package com.ska.exception;


public final class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }

}
