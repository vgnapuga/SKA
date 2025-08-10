package com.ska.model.user.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.ska.vo.user.Email;


@Converter
public final class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Email(dbData);
    }
    
}
