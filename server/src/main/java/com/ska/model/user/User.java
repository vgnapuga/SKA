package com.ska.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.Table;

import com.ska.model.BaseModel;
import com.ska.vo.user.*;


@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name = "email", unique = true, nullable = false, length = 255)
    @Convert(converter = com.ska.model.user.converter.EmailConverter.class)
    private Email email;

    @Column(name = "password", nullable = false, length = 60)
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
