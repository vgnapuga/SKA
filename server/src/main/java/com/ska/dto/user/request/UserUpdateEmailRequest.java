package com.ska.dto.user.request;


import com.ska.dto.validation.user.ValidEmail;


public final record UserUpdateEmailRequest(@ValidEmail String newEmail) {
}
