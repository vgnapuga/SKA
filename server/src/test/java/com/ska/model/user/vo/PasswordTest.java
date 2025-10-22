package com.ska.model.user.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ska.exception.DomainValidationException;
import com.ska.model.ValueObjectBehaviorTest;
import com.ska.util.constant.UserConstants;


class PasswordTest implements ValueObjectBehaviorTest<String> {

    public static final String NULL_MESSAGE = "Password value is <null>";
    public static final String BLANK_MESSAGE = "Password value is <blank>";

    @ParameterizedTest
    @ValueSource(strings = { "$2a$10$validBcryptHashWith60Characters1234567890123456781234",
            "$2b$12$anotherValidBcryptHashExample123456789012345678911234",
            "$2y$14$anotherValidBcryptHashExample123452750123156789112134" })
    @Override
    public void shouldCreate_whenValidValue(String validHash) {
        Password password = assertDoesNotThrow(() -> new Password(validHash));
        assertEquals(validHash, password.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> new Password(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenBlankValue() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> new Password(""));
        assertEquals(BLANK_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "$2c$10$validBcryptHashWith60Characters1234567890123456781234",
            "$2a$10$validBcryptHashWith60Characters12345678901234567812341232134213",
            "$2a$10$validBcryptHashWith60Characters12345678901234567812",
            "$2b$10$validBcryptHashWith60Characters12345678901234567812341232134213",
            "$2b$10$validBcryptHashWith60Characters12345678901234567812",
            "$2y$10$validBcryptHashWith60Characters12345678901234567812341232134213",
            "$2y$10$validBcryptHashWith60Characters12345678901234567812" })
    public void shouldThrowDomainValidationException_whenValueInvalid(String invalidHash) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new Password(invalidHash));
        assertEquals(UserConstants.Password.DOMAIN_INVALID_FORMAT_MESSAGE, exception.getMessage());
    }

}
