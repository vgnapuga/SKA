package com.ska.model.note.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.ska.vo.note.Title;


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
