package com.ska.model.syncable.note.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObjectBehaviorTest;
import com.ska.util.constant.note.NoteTitleConstants;


public class EncryptedNoteTitleTest implements EncryptedValueObjectBehaviorTest {

    private static final String NULL_MESSAGE = "EncryptedNoteTitle value is <null>";
    private static final String EMPTY_MESSAGE = "EncryptedNoteTitle data is <empty>";

    @Test
    @Override
    public void shouldCreate_whenValidValue() {
        EncryptedNoteTitle noteTitle = assertDoesNotThrow(
                () -> new EncryptedNoteTitle(EncryptedValueObjectBehaviorTest.validValue));
        assertEquals(EncryptedValueObjectBehaviorTest.validValue, noteTitle.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteTitle(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenEmptyValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteTitle(new byte[] {}));
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        byte[] tooLongValue = new byte[NoteTitleConstants.Format.MAX_ENCRYPTED_DATA_SIZE + 1];

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteTitle(tooLongValue));
        assertEquals(NoteTitleConstants.Messages.INVALID_ENCRYPTED_DATA_SIZE, exception.getMessage());
    }

}
