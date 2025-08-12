package com.ska.vo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import com.ska.constants.user.EmailConstants;
import com.ska.exceptions.DomainValidationException;
import com.ska.vo.BaseTest;


class EmailTest implements BaseTest<String> {

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name+tag@example-domain.co.ru", 
    })
    @Override
    public void testCreateValid(String validEmail) {
        Email email = assertDoesNotThrow(() -> new Email(validEmail));
        assertEquals(validEmail, email.getValue());
    }

    @Test
    @Override
    public void testCreateNull() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(null)
        );
        assertEquals("Email value is <null>", exception.getMessage());
    }

    @Test
    @Override
    public void testCreateBlank() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email("")  
        );
        assertEquals("Email value is <blank>", exception.getMessage());
    }

    @Test
    void testCreateTooLong() {
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
    void testCreateInvalid(String invalidEmail) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(invalidEmail)
        );
        assertEquals(EmailConstants.INVALID_FORMAT_MESSAGE, exception.getMessage());
    }
    


}
