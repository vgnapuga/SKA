package com.ska.exceptions;


public final class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(final String message) {
        super(message);
    }

}
