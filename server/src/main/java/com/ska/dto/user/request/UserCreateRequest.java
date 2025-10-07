package com.ska.dto.user.request;


import com.ska.dto.validation.user.ValidEmail;
import com.ska.dto.validation.user.ValidPassword;


public final record UserCreateRequest(@ValidEmail String email, @ValidPassword String password) {
}
