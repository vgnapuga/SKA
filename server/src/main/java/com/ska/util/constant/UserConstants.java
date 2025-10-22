package com.ska.util.constant;


public final class UserConstants {

    public static final String NULL_MESSAGE = "User is <null>";
    public static final String EMAIL_NULL_MESSAGE = "Email to set is <null>";
    public static final String PASSWORD_NULL_MESSAGE = "Password to set is <null>";

    private UserConstants() {
        throw new UnsupportedOperationException("UserConstants.java - <util> class");
    }

    public static class Email {

        public static final int MAX_LENGTH = 254;
        public static final String REGEX = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

        public static final String DTO_REQUIRED_MESSAGE = "Email is required";
        public static final String DTO_INVALID_LENGTH_MESSAGE = "Email is longer than " + MAX_LENGTH + " characters";
        public static final String DTO_INVALID_FORMAT_MESSAGE = "Invalid email format";

        public static final String DOMAIN_INVALID_FORMAT_MESSAGE = "Email value format invalid";
        private static final String DOMAIN_TEMPLATE_INVALID_LENGTH_MESSAGE = "Email value cannot be longer than %d (actual length=%d)";

        public static final String getDomainInvalidLengthMessage(int actualLength) {
            return String.format(DOMAIN_TEMPLATE_INVALID_LENGTH_MESSAGE, MAX_LENGTH, actualLength);
        }

    }

    public static class Password {

        public static final int RAW_MIN_LENGTH = 6;
        public static final int BCRYPT_HASHED_SIZE = 60;
        public static final String[] BCRYPT_PREFIXES = { "$2a$", "$2b$", "$2y$" };

        public static final String DTO_REQUIRED_MESSAGE = "Password is required";
        public static final String DTO_INVALID_LENGTH_MESSAGE = "Password must be at least " + RAW_MIN_LENGTH +
                " characters long";

        public static final String DOMAIN_INVALID_FORMAT_MESSAGE = "Password value must be a valid BCrypt hash";

    }

}
