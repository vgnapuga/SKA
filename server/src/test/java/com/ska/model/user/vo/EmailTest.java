package com.ska.model.user.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ska.exception.DomainValidationException;
import com.ska.model.ValueObjectBehaviorTest;
import com.ska.util.constant.user.EmailConstants;


class EmailTest implements ValueObjectBehaviorTest<String> {

    public static final String NULL_MESSAGE = "Email value is <null>";
    public static final String BLANK_MESSAGE = "Email value is <blank>";

    @ParameterizedTest
    @ValueSource(strings = { "test@example.com", "user.name+tag@example-domain.co.ru", })
    @Override
    public void shouldCreate_whenValidValue(String validEmail) {
        Email email = assertDoesNotThrow(() -> new Email(validEmail));
        assertEquals(validEmail, email.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> new Email(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenBlankValue() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> new Email(""));
        assertEquals(BLANK_MESSAGE, exception.getMessage());
    }

    @Test
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        String tooLongEmail = "a".repeat(255) + "test@example.com";

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new Email(tooLongEmail));
        assertEquals(EmailConstants.Messages.INVALID_LENGTH_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "not-email", "missing@", "@missing.com", "spaces @example.com",
            "double..dot@example.com" })
    public void shouldThrowDomainValidationException_whenValueFormatInvalid(String invalidEmail) {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new Email(invalidEmail));
        assertEquals(EmailConstants.Messages.INVALID_FORMAT_MESSAGE, exception.getMessage());
    }

}
