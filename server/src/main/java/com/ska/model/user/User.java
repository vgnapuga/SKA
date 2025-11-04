package com.ska.model.user;


import java.util.Objects;

import com.ska.exception.DomainValidationException;
import com.ska.model.BaseModel;
import com.ska.model.user.converter.EmailConverter;
import com.ska.model.user.converter.PasswordConverter;
import com.ska.model.user.vo.Email;
import com.ska.model.user.vo.Password;
import com.ska.util.constant.UserConstants;

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

    @Column(name = "email", nullable = false, unique = true, length = UserConstants.Email.MAX_LENGTH)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "password", nullable = false, length = UserConstants.Password.BCRYPT_HASHED_SIZE)
    @Convert(converter = PasswordConverter.class)
    private Password password;

    protected User() {
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
    public User(Email email, Password password) {
        this.email = Objects.requireNonNull(email, UserConstants.EMAIL_NULL_MESSAGE);
        this.password = Objects.requireNonNull(password, UserConstants.PASSWORD_NULL_MESSAGE);
    }

    /**
     * 
     * Updates user email.
     * 
     * @param newEmail the new email to set
     * @see Email - email value object
     */
    public final void changeEmail(Email newEmail) {
        this.email = Objects.requireNonNull(newEmail, UserConstants.EMAIL_NULL_MESSAGE);
    }

    /**
     * Updates user password.
     * 
     * @param newPassword the new password to set
     * @see Password - password value object
     */
    public final void changePassword(Password newPassword) {
        this.password = Objects.requireNonNull(newPassword, UserConstants.PASSWORD_NULL_MESSAGE);
    }

    public Email getEmail() {
        return this.email;
    }

    public Password getPassword() {
        return this.password;
    }

    @Override
    public final String toString() {
        return String.format(
                "User{id=%s, email=%s, password=***, created_at=%s}",
                this.id,
                this.email.toString(),
                this.createdAt == null ? "null" : this.createdAt.toString());
    }

}
