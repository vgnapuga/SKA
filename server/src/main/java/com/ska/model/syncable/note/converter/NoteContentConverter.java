package com.ska.model.syncable.note.converter;


import com.ska.model.syncable.note.vo.NoteContent;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class NoteContentConverter implements AttributeConverter<NoteContent, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(NoteContent attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public NoteContent convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new NoteContent(dbData);
    }

}
