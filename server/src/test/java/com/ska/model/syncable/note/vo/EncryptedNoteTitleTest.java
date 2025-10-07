package com.ska.model.syncable.note.vo;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ska.exception.DomainValidationException;
import com.ska.model.syncable.EncryptedValueObjectBehaviorTest;
import com.ska.util.constant.NoteConstants;


public class EncryptedNoteTitleTest implements EncryptedValueObjectBehaviorTest {

    private static final String NULL_MESSAGE = "EncryptedNoteTitle value is <null>";
    private static final String EMPTY_MESSAGE = "EncryptedNoteTitle data is <empty>";

    @Test
    @Override
    public void shouldCreate_whenValidValue() {
        NoteTitle noteTitle = assertDoesNotThrow(() -> new NoteTitle(EncryptedValueObjectBehaviorTest.validValue));
        assertEquals(EncryptedValueObjectBehaviorTest.validValue, noteTitle.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> new NoteTitle(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenEmptyValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new NoteTitle(new byte[] {}));
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        int invalidSize = NoteConstants.Title.MAX_ENCRYPTED_DATA_SIZE + 1;
        byte[] tooLongValue = new byte[invalidSize];

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new NoteTitle(tooLongValue));
        assertEquals(NoteConstants.Title.getDomainInvalidDataSizeMessage(invalidSize), exception.getMessage());
    }

}
