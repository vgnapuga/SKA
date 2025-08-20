package com.ska.model.syncable.note.converter;

import com.ska.vo.encryptable.note.Content;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class ContentConverter implements AttributeConverter<Content, String> {

    @Override
    public String convertToDatabaseColumn(Content attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Content convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Content(dbData);
    }
    
}
