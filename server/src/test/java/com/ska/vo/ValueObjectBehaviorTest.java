package com.ska.vo;


public interface ValueObjectBehaviorTest<T> {
    
    void shouldCreate_whenValidValue(T validValue);
    void shouldThrowDomainValidationException_whenNullValue();
    void shouldThrowDomainValidationException_whenBlankValue();

}
