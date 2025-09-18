package com.ska.constant.user;


public final class PasswordConstants {

    private PasswordConstants() {
    }

    public record Format() {

        public static final int MIN_LENGTH = 6;
        public static final int BCRYPT_HASHED_SIZE = 60;
        public static final String[] BCRYPT_PREFIXES = { "$2a$", "$2b$", "$2y$" };

    }

    public record Messages() {

        public static final String REQUIRED_MESSAGE = "Password is required";
        public static final String INVALID_LENGTH_MESSAGE = "Password must be at least " + Format.MIN_LENGTH +
                " characters long";
        public static final String INVALID_BCRYPT_FORMAT_MESSAGE = "Password must be a valid BCrypt hash";

    }

}
