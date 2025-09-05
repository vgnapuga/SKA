package com.ska.exception;


public final class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(final String message) {
        super(message);
    }

}
