package com.ska.dto.entity.response;


import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import com.ska.model.syncable.Syncable;
import com.ska.util.constant.EntityConstants;


public record EntityResponse(
        UUID uuid,
        String base64Title,
        String base64Content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static EntityResponse of(final Syncable entity) {
        Objects.requireNonNull(entity, EntityConstants.NULL_MESSAGE);

        String base64Title = Base64.getEncoder().encodeToString(entity.getTitleBytes().getValue());
        String base64Content = Base64.getEncoder().encodeToString(entity.getContentBytes().getValue());

        return new EntityResponse(
                entity.getUuid(),
                base64Title,
                base64Content,
                entity.getCreationTime(),
                entity.getUpdateTime());
    }

}
