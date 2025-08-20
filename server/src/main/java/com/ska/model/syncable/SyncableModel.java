package com.ska.model.syncable;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import com.ska.model.BaseModel;


@MappedSuperclass
public abstract class SyncableModel extends BaseModel {

    @Column(name = "uuid", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    protected UUID uuid;


    @PrePersist
    private void generateUuid() {
        if (this.uuid == null)
            this.uuid = UUID.randomUUID();
    }

    public final UUID getUuid() {
        return this.uuid;
    }
    
}
