package com.ska.model.syncable.note.converter;

import com.ska.vo.encryptable.note.EncryptedNoteContent;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class NoteContentConverter implements AttributeConverter<EncryptedNoteContent, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EncryptedNoteContent attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncryptedNoteContent convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new EncryptedNoteContent(dbData);
    }
    
}
