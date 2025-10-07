package com.ska.model.syncable.note.converter;


import com.ska.model.syncable.note.vo.NoteTitle;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class NoteTitleConverter implements AttributeConverter<NoteTitle, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(NoteTitle attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public NoteTitle convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new NoteTitle(dbData);
    }

}
