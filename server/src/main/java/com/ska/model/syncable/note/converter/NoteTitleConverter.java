package com.ska.model.syncable.note.converter;

import com.ska.vo.encrypted.note.EncryptedNoteTitle;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class NoteTitleConverter implements AttributeConverter<EncryptedNoteTitle, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EncryptedNoteTitle attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncryptedNoteTitle convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new EncryptedNoteTitle(dbData);
    }
    
}
