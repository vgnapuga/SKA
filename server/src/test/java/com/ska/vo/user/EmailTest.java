package com.ska.vo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import com.ska.constant.user.EmailConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.ValueObjectBehaviorTest;


class EmailTest implements ValueObjectBehaviorTest<String> {

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name+tag@example-domain.co.ru", 
    })
    @Override
    public void shouldCreate_whenValidValue(String validEmail) {
        Email email = assertDoesNotThrow(() -> new Email(validEmail));
        assertEquals(validEmail, email.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(null)
        );
        assertEquals("Email value is <null>", exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenBlankValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email("")  
        );
        assertEquals("Email value is <blank>", exception.getMessage());
    }

    @Test
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
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
    public void shouldThrowDomainValidationException_whenValueFormatInvalid(String invalidEmail) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class, () -> new Email(invalidEmail)
        );
        assertEquals(EmailConstants.INVALID_FORMAT_MESSAGE, exception.getMessage());
    }
    


}
