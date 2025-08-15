package com.ska.exception;


public final class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(final String message) {
        super(message);
    }

}
