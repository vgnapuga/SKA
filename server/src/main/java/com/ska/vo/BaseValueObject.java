package com.ska.vo;


import com.ska.exception.DomainValidationException;


/**
 * Base abstract class for all value objects.
 * 
 * Provides automatic null validation in constructor.
 * 
 * @param T the type of value wrapped by this value object
 * @see DomainValidationException - thrown on validation failure
 */
public abstract class BaseValueObject<T> {

    protected final T value;

    /**
     * Creates a new value object with validation.
     * 
     * @param value the value to wrap
     * @throws DomainValidationException if value is null or invalid
     */
    protected BaseValueObject(final T value) {
        validateNotNull(value);
        checkValidation(value);
        this.value = value;
    }

    /**
     * Abstract validation method.
     * 
     * @param value the value to validate
     */
    protected abstract void checkValidation(final T value);

    private final void validateNotNull(final T value) {
        if (value == null)
            throw new DomainValidationException(this.getClass().getSimpleName() + " value is <null>");
    }

    /**
     * String not blank validation method.
     * 
     * @param value the string value to validate
     * @throws DomainValidationException if value is blank
     */
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

        return java.util.Objects.equals(this.value, ((BaseValueObject<?>) obj).value);
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return String.format("%s{value=%s}", this.getClass().getSimpleName(), this.value);
    }

}
