package com.ska.model.syncable.note.converter;

import com.ska.vo.encryptable.note.Title;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class TitleConverter implements AttributeConverter<Title, String> {

    @Override
    public String convertToDatabaseColumn(Title attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Title convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Title(dbData);
    }
    
}
