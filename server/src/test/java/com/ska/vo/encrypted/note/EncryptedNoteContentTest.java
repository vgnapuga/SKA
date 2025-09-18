package com.ska.vo.encrypted.note;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ska.constant.note.NoteContentConstants;
import com.ska.exception.DomainValidationException;
import com.ska.vo.encrypted.EncryptedValueObjectBehaviorTest;


public class EncryptedNoteContentTest implements EncryptedValueObjectBehaviorTest {

    private static final String NULL_MESSAGE = "EncryptedNoteContent value is <null>";
    private static final String EMPTY_MESSAGE = "EncryptedNoteContent data is <empty>";

    @Test
    @Override
    public void shouldCreate_whenValidValue() {
        EncryptedNoteContent noteTitle = assertDoesNotThrow(
                () -> new EncryptedNoteContent(EncryptedValueObjectBehaviorTest.validValue));
        assertEquals(EncryptedValueObjectBehaviorTest.validValue, noteTitle.getValue());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenNullValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteContent(null));
        assertEquals(NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenEmptyValue() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteContent(new byte[] {}));
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @Override
    public void shouldThrowDomainValidationException_whenValueLengthInvalid() {
        byte[] tooLongValue = new byte[NoteContentConstants.Format.MAX_ENCRYPTED_DATA_SIZE + 1];

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> new EncryptedNoteContent(tooLongValue));
        assertEquals(NoteContentConstants.Messages.INVALID_ENCRYPTED_DATA_SIZE, exception.getMessage());
    }

}
