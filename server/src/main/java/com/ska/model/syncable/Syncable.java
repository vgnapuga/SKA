package com.ska.model.syncable;


import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.BaseModel;
import com.ska.model.syncable.converter.EncryptedContentConverter;
import com.ska.model.syncable.converter.EncryptedMetadataConverter;
import com.ska.model.syncable.vo.EncryptedContent;
import com.ska.model.syncable.vo.EncryptedMetadata;
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

    @Column(name = "encrypted_metadata", nullable = false, length = EntityConstants.Metadata.ENCRYPTED_DATA_SIZE_MAX)
    @Convert(converter = EncryptedMetadataConverter.class)
    private EncryptedMetadata encryptedMetadata;

    @Column(name = "encrypted_content", nullable = false, length = EntityConstants.Content.ENCRYPTED_DATA_SIZE_MAX)
    @Convert(converter = EncryptedContentConverter.class)
    private EncryptedContent encryptedContent;

    protected Syncable() {
    }

    public Syncable(
            final User owner,
            UUID uuid,
            EncryptedMetadata encryptedMetadata,
            EncryptedContent encryptedContent) {
        this.owner = Objects.requireNonNull(owner, UserConstants.NULL_MESSAGE);
        this.uuid = Objects.requireNonNull(uuid, EntityConstants.UUID_NULL_MESSAGE);
        this.encryptedMetadata = Objects.requireNonNull(encryptedMetadata, EntityConstants.Metadata.NULL_MESSAGE);
        this.encryptedContent = Objects.requireNonNull(encryptedContent, EntityConstants.Content.NULL_MESSAGE);
    }

    public final void changeMetadata(EncryptedMetadata newEncryptedMetadata) {
        this.encryptedMetadata = Objects.requireNonNull(newEncryptedMetadata, EntityConstants.Metadata.NULL_MESSAGE);
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

    public EncryptedMetadata getMetadataBytes() {
        return this.encryptedMetadata;
    }

    public EncryptedContent getContentBytes() {
        return this.encryptedContent;
    }

    @Override
    public final String toString() {
        return String.format(
                "Syncable{id=%d, owner_id=%d, uuid=%s, metadata=***, content=***}",
                this.id,
                this.owner.getId(),
                this.uuid.toString());
    }

}
