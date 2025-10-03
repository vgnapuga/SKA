package com.ska.model;


public interface ValueObjectBehaviorTest<T> {

    void shouldCreate_whenValidValue(T validValue);

    void shouldThrowDomainValidationException_whenNullValue();

    void shouldThrowDomainValidationException_whenBlankValue();

}
