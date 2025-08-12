package com.ska.vo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import com.ska.constants.user.EmailConstants;
import com.ska.exceptions.DomainValidationException;



class EmailTest {

    @Test
    void testCreateNullEmail() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(null)
        );
        assertEquals("Email value is <null>", exception.getMessage());
    }

    @Test
    void testCreateBlankEmail() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email("")  
        );
        assertEquals("Email value is <blank>", exception.getMessage());
    }

    @Test
    void testCreateTooLongEmail() {
        String tooLongEmail = "a".repeat(255) + "test@example.com";

        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(tooLongEmail)    
        );
        assertEquals(EmailConstants.INVALID_LENGTH_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "not-email",
        "missing@", 
        "@missing.com",
        "spaces @example.com",
        "double..dot@example.com"
    })
    void testCreateInvalidEmail(String invalidEmail) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(invalidEmail)
        );
        assertEquals(EmailConstants.INVALID_FORMAT_MESSAGE, exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name+tag@example-domain.co.ru", 
    })
    void testCreateValidEmail(String validEmail) {
        Email email = assertDoesNotThrow(() -> new Email(validEmail));
        assertEquals(validEmail, email.getValue());
    }

}
