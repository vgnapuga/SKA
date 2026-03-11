package com.ska.model.syncable;


import java.util.Objects;
import java.util.UUID;

import com.ska.model.BaseClientModel;
import com.ska.model.syncable.converter.EncryptedContentConverter;
import com.ska.model.syncable.converter.EncryptedMetadataConverter;
import com.ska.model.syncable.vo.EncryptedContent;
import com.ska.model.syncable.vo.EncryptedMetadata;
import com.ska.model.user.User;
import com.ska.util.constant.EntityConstants;
import com.ska.util.constant.UserConstants;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "entities")
@AssociationOverride(name = "owner", joinColumns = @JoinColumn(name = "owner_id", nullable = false, updatable = false), foreignKey = @ForeignKey(name = "fk_syncable_owner"))
public class Syncable extends BaseClientModel {

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
