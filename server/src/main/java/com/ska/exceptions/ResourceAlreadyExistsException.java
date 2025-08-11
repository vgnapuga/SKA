package com.ska.exceptions;


public final class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }

}
