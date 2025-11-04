package com.ska.service.contract;


import java.util.List;
import java.util.UUID;

import com.ska.dto.entity.request.EntityCreateRequest;
import com.ska.dto.entity.request.EntityUpdateAllRequest;
import com.ska.dto.entity.request.EntityUpdateContentRequest;
import com.ska.dto.entity.request.EntityUpdateTitleRequest;
import com.ska.model.syncable.Syncable;


public interface EntityService {

    public Syncable create(Long userId, UUID uuid, EntityCreateRequest request);

    public List<Syncable> getAll(Long userId);

    public Syncable getByUuid(Long userId, UUID entityUuid);

    public Syncable updateTitleAndContent(Long userId, UUID entityUuid, EntityUpdateAllRequest request);

    public Syncable updateTitle(Long userId, UUID entityUuid, EntityUpdateTitleRequest request);

    public Syncable updateContent(Long userId, UUID entityUuid, EntityUpdateContentRequest request);

    public void delete(Long userId, UUID entityUuid);

}
