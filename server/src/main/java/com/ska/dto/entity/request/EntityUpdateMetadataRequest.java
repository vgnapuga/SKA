package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityMetadata;


public final record EntityUpdateMetadataRequest(@ValidEntityMetadata String encryptedNewMetadata) {
}
