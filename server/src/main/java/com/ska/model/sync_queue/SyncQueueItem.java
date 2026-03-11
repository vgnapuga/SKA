package com.ska.model.sync_queue;


import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.BaseModel;
import com.ska.model.Device;
import com.ska.model.syncable.Syncable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "sync_queue", uniqueConstraints = @UniqueConstraint(name = "uq_sync_queue_device_syncable", columnNames = {
        "device_id", "syncable_id" }))
public class SyncQueueItem extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sync_queue_device"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "syncable_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sync_queue_syncable"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Syncable syncable;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private QueueAction action;

    protected SyncQueueItem() {
    }

    public SyncQueueItem(final Device device, Syncable syncable, QueueAction action) {
        this.device = Objects.requireNonNull(device, "Device to set is <null>");
        this.syncable = Objects.requireNonNull(syncable, "Syncable to set is <null>");
        this.action = Objects.requireNonNull(action, "QueueAction to set is <null>");
    }

    public final Device getDevice() {
        return this.device;
    }

    public final Syncable getSyncable() {
        return this.syncable;
    }

    public final QueueAction getAction() {
        return this.action;
    }

    @Override
    public final String toString() {
        return String.format(
                "SyncQueueItem{id=%d, device_uuid=%s, syncable_uuid=%s, action=%s}",
                this.id,
                this.device.getUuid(),
                this.syncable.getUuid(),
                this.action.toString());
    }

}
