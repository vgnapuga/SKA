package com.ska.vo;

import com.ska.exceptions.DomainValidationException;


public abstract class ValueObject<T> {
    
    protected final T value;


    protected ValueObject(final T value) {
        validateNotNull(value);
        checkValidation(value);
        this.value = value;
    }


    protected abstract void checkValidation(final T value);

    private final void validateNotNull(final T value) {
        if (value == null)
            throw new DomainValidationException(this.getClass().getSimpleName() + " value is <null>");
    }

    protected final void validateNotBlank(final String value) {
        if (value.isBlank())
            throw new DomainValidationException(this.getClass().getSimpleName() + " value is <blank>");
    }

    public final T getValue() {
        return this.value;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        return java.util.Objects.equals(this.value, ((ValueObject<?>) obj).value);
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hash(this.value);
    }

    public abstract String toString();

}
