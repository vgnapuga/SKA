package com.ska.model.user;


import com.ska.exception.DomainValidationException;
import com.ska.model.BaseModel;
import com.ska.model.user.converter.EmailConverter;
import com.ska.model.user.converter.PasswordConverter;
import com.ska.model.user.vo.Email;
import com.ska.model.user.vo.Password;
import com.ska.util.constant.user.EmailConstants;
import com.ska.util.constant.user.PasswordConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * User domain entity representing system user.
 * 
 * Extends {@link BaseModel}. Provides email and password management.
 * 
 * @see Email - email value object
 * @see Password - password value object
 * @see DomainValidationException - thrown if email or password validation
 * failure
 */
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name = "email", nullable = false, unique = true, length = EmailConstants.Format.MAX_LENGTH)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "password", nullable = false, length = PasswordConstants.Format.BCRYPT_HASHED_SIZE)
    @Convert(converter = PasswordConverter.class)
    private Password password;

    public User() {
    }

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
     * Updates user email.
     * 
     * @param newEmail the new email to set
     * @see Email - email value object
     */
    public final void changeEmail(final Email newEmail) {
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
    public final String toString() {
        return String.format("User{id=%d, email=%s, password=***}", this.id, this.email.toString());
    }

}
