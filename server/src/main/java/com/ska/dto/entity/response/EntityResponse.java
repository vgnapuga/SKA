package com.ska.dto.entity.response;


import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import com.ska.model.syncable.Syncable;
import com.ska.util.constant.EntityConstants;


public record EntityResponse(UUID uuid, String base64Metadata, String base64Content) {

    public static EntityResponse of(final Syncable entity) {
        Objects.requireNonNull(entity, EntityConstants.NULL_MESSAGE);

        String base64Metadata = Base64.getEncoder().encodeToString(entity.getMetadataBytes().getValue());
        String base64Content = Base64.getEncoder().encodeToString(entity.getContentBytes().getValue());

        return new EntityResponse(entity.getUuid(), base64Metadata, base64Content);
    }

}
