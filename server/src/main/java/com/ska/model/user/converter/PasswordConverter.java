package com.ska.model.user.converter;


import com.ska.vo.user.Password;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class PasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Password(dbData);
    }

}
