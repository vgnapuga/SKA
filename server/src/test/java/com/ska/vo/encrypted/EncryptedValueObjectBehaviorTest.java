package com.ska.vo.encrypted;


public interface EncryptedValueObjectBehaviorTest {

    static final byte[] validValue = { 1, 2, 3, 4, 5, 6, 7, 8 };

    void shouldCreate_whenValidValue();

    void shouldThrowDomainValidationException_whenNullValue();

    void shouldThrowDomainValidationException_whenEmptyValue();

    void shouldThrowDomainValidationException_whenValueLengthInvalid();

}
