package com.ska.dto.entity.request;


import com.ska.dto.validation.encrypted.ValidEntityTitle;


public final record EntityUpdateTitleRequest(@ValidEntityTitle String encryptedNewTitle) {
}
