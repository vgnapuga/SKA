package com.ska.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ska.constant.user.*;
import com.ska.exception.ResourceAlreadyExistsException;
import com.ska.exception.DomainValidationException;
import com.ska.model.BaseModel;
import com.ska.vo.user.*;


/**
 * User domain entity representing system user.
 * 
 * Extends {@link BaseModel}.
 * Provides email and password management with uniqueness check.
 * 
 * @see Email - email value object
 * @see Password - password value object
 * @see ResourceAlreadyExistsException - thrown if resource already exists
 * @see DomainValidationException - thrown if email or password validation failure
 */
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name = "email", unique = true, nullable = false, length = EmailConstants.MAX_LENGTH)
    @Convert(converter = com.ska.model.user.converter.EmailConverter.class)
    private Email email;

    @Column(name = "password", nullable = false, length = PasswordConstants.BCRYPT_HASHED_SIZE)
    @Convert(converter = com.ska.model.user.converter.PasswordConverter.class)
    private Password password;


    public User() {}


    /**
     * Creates a new user with validated email and password.
     * 
     * @param email the email to set
     * @param password the password to set
     * @throws DomainValidationException if email or password validation failure
     * @see Email - email value object
     * @see Password - password value object
     */
    public User(final Email email, final Password password) {
        this.email = email;
        this.password = password;
    }


    /**
     * Updates user email with database uniqueness valiadtion .
     * 
     * @param newEmail the new email to set
     * @param isUnique true if email is unique in database
     * @throws ResourceAlreadyExistsException if email already exists
     * @see Email - email value object
     */
    public final void changeEmail(final Email newEmail, final boolean isUnique) {
        if (!isUnique)
            throw new ResourceAlreadyExistsException(
                String.format("Email=%s already exists", newEmail.toString())
            );

        this.email = newEmail;
    }

    /**
     * Updates user password.
     * 
     * @param newPassword the new password to set
     * @see Password - password value object
     */
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
        return String.format(
            "User{id=%d, email=%s, password=***}",
            this.id, this.email.toString()
        );
    }

}
