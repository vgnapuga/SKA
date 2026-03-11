package com.ska.model;


import java.util.Objects;
import java.util.UUID;

import com.ska.model.user.User;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "devices")
@AssociationOverride(name = "owner", joinColumns = @JoinColumn(name = "owner_id", nullable = false, updatable = false), foreignKey = @ForeignKey(name = "fk_device_owner"))
public class Device extends BaseClientModel {

    protected Device() {
    }

    public Device(final User owner, UUID uuid) {
        this.owner = Objects.requireNonNull(owner, "User to set is <null>");
        this.uuid = Objects.requireNonNull(uuid, "UUID to set is <null>");
    }

    @Override
    public final String toString() {
        return String.format("Device{id=%d, owner_id=%d, uuid=%s}", this.id, this.owner.getId(), this.uuid.toString());
    }

}
