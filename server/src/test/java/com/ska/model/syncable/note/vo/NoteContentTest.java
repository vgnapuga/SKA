package com.ska.model.syncable.note.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObjectBehaviorTest;
import com.ska.util.constant.NoteConstants;


public class NoteContentTest implements EncryptedValueObjectBehaviorTest {

    private static final String NULL_MESSAGE = "NoteContent value is <null>";
    private static final String EMPTY_MESSAGE = "NoteContent data is <empty>";

    @Test
    @Override
    public void shouldCreate_whenValidValue() {
        NoteContent noteTitle = assertDoesNotThrow(() -> new NoteContent(EncryptedValueObjectBehaviorTest.validValue));
        assertEquals(EncryptedValueObjectBehaviorTest.validValue, noteTitle.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new NoteContent(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenEmptyValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new NoteContent(new byte[] {}));
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        int invalidSize = NoteConstants.Content.MAX_ENCRYPTED_DATA_SIZE + 1;
        byte[] tooLongValue = new byte[invalidSize];

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new NoteContent(tooLongValue));
        assertEquals(NoteConstants.Content.getDomainInvalidDataSizeMessage(invalidSize), exception.getMessage());
    }

}
