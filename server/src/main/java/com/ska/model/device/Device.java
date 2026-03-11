package com.ska.model.device;


import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.BaseModel;
import com.ska.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "devices")
public class Device extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_device_owner"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "uuid", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID uuid;

    protected Device() {
    }

    public Device(final User owner, UUID uuid) {
        this.owner = Objects.requireNonNull(owner, "User to set is <null>");
        this.uuid = Objects.requireNonNull(uuid, "UUID to set is <null>");
    }

    public final User getOwner() {
        return this.owner;
    }

    public final UUID getUuid() {
        return this.uuid;
    }

    @Override
    public final String toString() {
        return String.format("Device{id=%d, owner_id=%d, uuid=%s,}", this.id, this.owner.getId(), this.uuid.toString());
    }

}
