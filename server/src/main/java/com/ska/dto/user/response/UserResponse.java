package com.ska.dto.user.response;


import java.time.LocalDateTime;

import com.ska.model.user.User;
import com.ska.util.constant.UserConstants;


public final record UserResponse(Long id, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static UserResponse of(final User user) {
        java.util.Objects.requireNonNull(user, UserConstants.NULL_MESSAGE);
        return new UserResponse(user.getId(), user.getEmail().getValue(), user.getCreationTime(), user.getUpdateTime());
    }

}
