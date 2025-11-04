package com.ska.util.constant;


public final class UserConstants {

    public static final String NULL_MESSAGE = "User is <null>";
    public static final String EMAIL_NULL_MESSAGE = "Email to set is <null>";
    public static final String PASSWORD_NULL_MESSAGE = "Password to set is <null>";

    private UserConstants() {
        throw new UnsupportedOperationException("UserConstants.java - <util> class");
    }

    public static class Email {

        private Email() {
            throw new UnsupportedOperationException("UserConstants.Email.java - <util> class");
        }

        public static final int LENGTH_MAX = 254;
        public static final String REGEX = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

        public static final String DTO_REQUIRED_MESSAGE = "Email is required";
        public static final String DTO_INVALID_LENGTH_MESSAGE = "Email is longer than " + LENGTH_MAX + " characters";
        public static final String DTO_INVALID_FORMAT_MESSAGE = "Invalid email format";

        public static final String DOMAIN_INVALID_FORMAT_MESSAGE = "Email value format invalid";
        private static final String DOMAIN_TEMPLATE_INVALID_LENGTH_MESSAGE = "Email value cannot be longer than %d (actual length=%d)";

        public static final String getDomainInvalidLengthMessage(int actualLength) {
            return String.format(DOMAIN_TEMPLATE_INVALID_LENGTH_MESSAGE, LENGTH_MAX, actualLength);
        }

    }

    public static class Password {

        private Password() {
            throw new UnsupportedOperationException("UserConstants.Password.java - <util> class");
        }

        public static final int ARGON2_PHC_SIZE_MIN = 80;
        public static final int ARGON2_PHC_SIZE_MAX = 200;
        public static final String ARGON2_PHC_REGEX = "^\\$argon2id\\$v=\\d+\\$m=\\d+,t=\\d+,p=\\d+\\$[A-Za-z0-9+/]+\\$[A-Za-z0-9+/]+$";

        public static final int BCRYPT_HASH_SIZE = 60;
        public static final String[] BCRYPT_PREFIXES = { "$2a$", "$2b$", "$2y$" };

        public static final String DTO_REQUIRED_MESSAGE = "Password Argon2 PHC is required";
        public static final String DTO_INVALID_LENGTH_MESSAGE = "Argon2 PHC must be between " + ARGON2_PHC_SIZE_MIN +
                " and " + ARGON2_PHC_SIZE_MAX + " characters long";
        public static final String DTO_INVALID_FORMAT_MESSAGE = "Invalid password Argon2 PHC format";

        public static final String DOMAIN_INVALID_FORMAT_MESSAGE = "Password value must be a valid BCrypt hash";

    }

}
