package com.ska.vo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import com.ska.constant.user.PasswordConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.BaseTest;


class PasswordTest implements BaseTest<String> {
    
    @ParameterizedTest
    @ValueSource(strings = {
        "$2a$10$validBcryptHashWith60Characters1234567890123456781234", 
        "$2b$12$anotherValidBcryptHashExample123456789012345678911234",
        "$2y$14$anotherValidBcryptHashExample123452750123156789112134"
    })
    @Override
    public void testCreateValid(String validHash) {
        Password password = assertDoesNotThrow(() -> new Password(validHash));
        assertEquals(validHash, password.getValue());
    }

    @Test
    @Override
    public void testCreateNull() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Password(null)    
        );
        assertEquals("Password value is <null>", exception.getMessage());
    }

    @Test
    @Override
    public void testCreateBlank() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Password("")    
        );
        assertEquals("Password value is <blank>", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "$2c$10$validBcryptHashWith60Characters1234567890123456781234",
        "$2a$10$validBcryptHashWith60Characters12345678901234567812341232134213",
        "$2a$10$validBcryptHashWith60Characters12345678901234567812",
        "$2b$10$validBcryptHashWith60Characters12345678901234567812341232134213",
        "$2b$10$validBcryptHashWith60Characters12345678901234567812",
        "$2y$10$validBcryptHashWith60Characters12345678901234567812341232134213",
        "$2y$10$validBcryptHashWith60Characters12345678901234567812"
    })
    void testCreateInvalid(String invalidHash) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Password(invalidHash)    
        );
        assertEquals(PasswordConstants.INVALID_BCRYPT_FORMAT_MESSAGE, exception.getMessage());
    }

}
