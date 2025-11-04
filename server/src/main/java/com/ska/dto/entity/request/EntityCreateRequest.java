package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityContent;
import com.ska.dto.validation.encrypted.ValidEntityTitle;


public final record EntityCreateRequest(
        @ValidEntityTitle String encryptedTitle,
        @ValidEntityContent String encryptedContent) {
}
