package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityContent;
import com.ska.dto.validation.encrypted.ValidEntityMetadata;


public final record EntityCreateRequest(
        @ValidEntityMetadata String encryptedMetadata,
        @ValidEntityContent String encryptedContent) {
}
