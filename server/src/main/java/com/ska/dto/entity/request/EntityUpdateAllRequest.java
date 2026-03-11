package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityContent;
import com.ska.dto.validation.encrypted.ValidEntityMetadata;


public final record EntityUpdateAllRequest(
        @ValidEntityMetadata String encryptedNewMetadata,
        @ValidEntityContent String encryptedNewContent) {
}
