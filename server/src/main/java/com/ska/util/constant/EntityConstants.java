package com.ska.util.constant;


public final class EntityConstants {

    public static final String NULL_MESSAGE = "Entity is <null>";
    public static final String UUID_NULL_MESSAGE = "Entity UUID us <null>";

    private EntityConstants() {
        throw new UnsupportedOperationException("EntityConstants.java - <util> class");
    }

    public static class Metadata {

        public static final int ENCRYPTED_DATA_SIZE_MAX = 512;
        public static final int BASE64_SIZE_MAX = 700; // depends of MAX_ENCRYPTED_DATA_SIZE (formula:
                                                       // ceil(MAX_ENCRYPTED_DATA_SIZE / 3) * 4)

        public static final String NULL_MESSAGE = "EncryptedMetadata to set is <null>";

        public static final String DTO_REQUIRED_MESSAGE = "Base64 string of encrypted metadata is required";
        public static final String DTO_INVALID_BASE64_LENGTH_MESSAGE = "Base64 string of encrypted metadata is longer than " +
                BASE64_SIZE_MAX;

        private static final String DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE = "EncryptedMetadata value cannot be larger than %d (actual size=%d)";

        public static final String getDomainInvalidDataSizeMessage(int actualSize) {
            return String.format(DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE, ENCRYPTED_DATA_SIZE_MAX, actualSize);
        }

    }

    public static class Content {

        public static final int ENCRYPTED_DATA_SIZE_MAX = 65536;
        public static final int BASE64_SIZE_MAX = 88000; // depends of MAX_ENCRYPTED_DATA_SIZE (formula:
                                                         // ceil(MAX_ENCRYPTED_DATA_SIZE / 3) * 4)

        public static final String NULL_MESSAGE = "EncryptedContent to set is <null>";

        public static final String DTO_REQUIRED_MESSAGE = "Base64 encrypted content required";
        public static final String DTO_INVALID_BASE64_LENGTH_MESSAGE = "Base64 string of encrypted content is longer than " +
                BASE64_SIZE_MAX;

        private static final String DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE = "EncryptedContent value cannot be larger than %d (actual size=%d)";

        public static final String getDomainInvalidDataSizeMessage(int actualSize) {
            return String.format(DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE, ENCRYPTED_DATA_SIZE_MAX, actualSize);
        }

    }

}
