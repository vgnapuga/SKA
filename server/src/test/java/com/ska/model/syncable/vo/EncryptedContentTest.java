package com.ska.model.syncable.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObjectBehaviorTest;
import com.ska.util.constant.EntityConstants;


public class EncryptedContentTest implements EncryptedValueObjectBehaviorTest {

    private static final String NULL_MESSAGE = "EncryptedContent value is <null>";
    private static final String EMPTY_MESSAGE = "EncryptedContent data is <empty>";

    @Test
    @Override
    public void shouldCreate_whenValidValue() {
        EncryptedContent noteTitle = assertDoesNotThrow(
                () -> new EncryptedContent(EncryptedValueObjectBehaviorTest.validValue));
        assertEquals(EncryptedValueObjectBehaviorTest.validValue, noteTitle.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedContent(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenEmptyValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedContent(new byte[] {}));
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        int invalidSize = EntityConstants.Content.MAX_ENCRYPTED_DATA_SIZE + 1;
        byte[] tooLongValue = new byte[invalidSize];

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedContent(tooLongValue));
        assertEquals(EntityConstants.Content.getDomainInvalidDataSizeMessage(invalidSize), exception.getMessage());
    }

}
