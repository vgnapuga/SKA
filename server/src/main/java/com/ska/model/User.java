package com.ska.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;


    public User() {}

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }


    public final void setEmail(final String email) {
        this.email = email;
    }
    public final void setPassword(final String password) {
        this.password = password;
    }

    public final Long getId() {
        return this.id;
    }
    public final String getEmail() {
        return this.email;
    }
    public final String getPassword() {
        return this.password;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || obj.getClass() != this.getClass())
            return false;

        User user = (User) obj;
        return java.util.Objects.equals(user.id, this.id);
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hash(this.id);
    }

    @Override
    public final String toString() {
        return "User{" +
            "id=" + this.id +
            ", email=" + this.email +
            "}";
    }

}
