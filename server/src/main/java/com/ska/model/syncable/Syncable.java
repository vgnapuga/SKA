package com.ska.model.syncable;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.BaseModel;
import com.ska.model.syncable.converter.EncryptedContentConverter;
import com.ska.model.syncable.converter.EncryptedTitleConverter;
import com.ska.model.syncable.vo.EncryptedContent;
import com.ska.model.syncable.vo.EncryptedTitle;
import com.ska.model.user.User;
import com.ska.util.constant.EntityConstants;
import com.ska.util.constant.UserConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


@Entity
@Table(name = "entities")
public class Syncable extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_entity_owner"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "uuid", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "encrypted_title", nullable = false, length = EntityConstants.Title.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = EncryptedTitleConverter.class)
    private EncryptedTitle encryptedTitle;

    @Column(name = "encrypted_content", nullable = false, length = EntityConstants.Content.MAX_ENCRYPTED_DATA_SIZE)
    @Convert(converter = EncryptedContentConverter.class)
    private EncryptedContent encryptedContent;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITHOUT TIMEZONE", nullable = true, updatable = true)
    private LocalDateTime updatedAt = null;

    protected Syncable() {
    }

    public Syncable(final User owner, UUID uuid, EncryptedTitle encryptedTitle, EncryptedContent encryptedContent) {
        this.owner = Objects.requireNonNull(owner, UserConstants.NULL_MESSAGE);
        this.uuid = Objects.requireNonNull(uuid, EntityConstants.UUID_NULL_MESSAGE);
        this.encryptedTitle = Objects.requireNonNull(encryptedTitle, EntityConstants.Title.NULL_MESSAGE);
        this.encryptedContent = Objects.requireNonNull(encryptedContent, EntityConstants.Content.NULL_MESSAGE);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public final void changeTitle(EncryptedTitle newEncryptedTitle) {
        this.encryptedTitle = Objects.requireNonNull(newEncryptedTitle, EntityConstants.Title.NULL_MESSAGE);
    }

    public final void changeContent(EncryptedContent newEncryptedContent) {
        this.encryptedContent = Objects.requireNonNull(newEncryptedContent, EntityConstants.Content.NULL_MESSAGE);
    }

    public User getOwner() {
        return this.owner;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EncryptedTitle getTitleBytes() {
        return this.encryptedTitle;
    }

    public EncryptedContent getContentBytes() {
        return this.encryptedContent;
    }

    public LocalDateTime getUpdateTime() {
        return this.updatedAt;
    }

    @Override
    public final String toString() {
        return String.format(
                "Syncable{id=%d, owner_id=%d, uuid=%s, title=***, content=***, created_at=%s, updated_at=%s}",
                this.id,
                this.owner.getId(),
                this.uuid.toString(),
                this.createdAt == null ? "<null>" : this.createdAt.toString(),
                this.updatedAt == null ? "<null>" : this.updatedAt.toString());
    }

}
