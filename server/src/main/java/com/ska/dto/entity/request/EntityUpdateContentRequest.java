package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityContent;


public final record EntityUpdateContentRequest(@ValidEntityContent String encryptedNewContent) {
}
