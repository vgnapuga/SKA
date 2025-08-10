package com.ska.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.ska.vo.user.*;


@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = com.ska.model.user.converter.EmailConverter.class)
    private Email email;

    @Convert(converter = com.ska.model.user.converter.PasswordConverter.class)
    private Password password;


    public User() {}

    public User(final Email email, final Password password) {
        this.email = email;
        this.password = password;
    }


    public final void changeEmail(final Email newEmail, final boolean isUnique) {
        if (!isUnique)
            throw new EntityExistsException("Email=" + newEmail.toString() + " already exists");

        this.email = newEmail;
    }
    public final void changePassword(final Password newPassword) {
        this.password = newPassword;
    }

    public final Long getId() {
        return this.id;
    }
    public final Email getEmail() {
        return this.email;
    }
    public final Password getPassword() {
        return this.password;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof User))
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
            ", email=" + this.email.toString() +
            ", password=" + this.password.toString() +
            "}";
    }

}
