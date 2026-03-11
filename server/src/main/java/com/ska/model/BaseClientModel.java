package com.ska.model;


import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ska.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class BaseClientModel {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "uuid", columnDefinition = "UUID", nullable = false, unique = true, updatable = false)
    protected UUID uuid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected User owner;

    public final Long getId() {
        return this.id;
    }

    public final UUID getUuid() {
        return this.uuid;
    }

    public final User getOwner() {
        return this.owner;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        BaseClientModel other = (BaseClientModel) obj;

        if (this.uuid == null || other.uuid == null)
            return false;

        return Objects.equals(this.uuid, other.uuid);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.uuid);
    }

    @Override
    public abstract String toString();

}
