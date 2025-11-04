package com.ska.model.syncable.converter;


import com.ska.model.syncable.vo.EncryptedTitle;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class EncryptedTitleConverter implements AttributeConverter<EncryptedTitle, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EncryptedTitle attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncryptedTitle convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new EncryptedTitle(dbData);
    }

}
