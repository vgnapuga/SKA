package com.ska.model.syncable;


import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.BaseModel;
import com.ska.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;


@MappedSuperclass
public abstract class SyncableModel extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_entity_owner"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected User user;

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
