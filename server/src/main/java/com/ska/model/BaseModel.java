package com.ska.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


/**
 * Base abstract class for all database entities.
 *
 * Automatically manages creation timestamp.
 */
@MappedSuperclass
public abstract class BaseModel {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return this.id;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        BaseModel other = (BaseModel) obj;

        if (this.id == null || other.id == null)
            return false;

        return Objects.equals(this.id, other.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public abstract String toString();

}
