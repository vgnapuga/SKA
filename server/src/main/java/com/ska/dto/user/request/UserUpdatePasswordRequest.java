package com.ska.dto.user.request;


import com.ska.dto.validation.user.ValidPassword;


public final record UserUpdatePasswordRequest(@ValidPassword String newPassword) {
}
