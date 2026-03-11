package com.ska.model.syncable.converter;


import com.ska.model.syncable.vo.EncryptedMetadata;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public final class EncryptedMetadataConverter implements AttributeConverter<EncryptedMetadata, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EncryptedMetadata attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncryptedMetadata convertToEntityAttribute(byte[] dbData) {
        return dbData == null ? null : new EncryptedMetadata(dbData);
    }

}
