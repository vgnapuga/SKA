package com.ska.model.syncable.converter;


import com.ska.model.syncable.vo.EncryptedContent;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class EncryptedContentConverter implements AttributeConverter<EncryptedContent, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EncryptedContent attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncryptedContent convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new EncryptedContent(dbData);
    }

}
