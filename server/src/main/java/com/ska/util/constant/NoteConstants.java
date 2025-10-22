package com.ska.util.constant;


public final class NoteConstants {

    public static final String NULL_MESSAGE = "Note is <null>";

    private NoteConstants() {
        throw new UnsupportedOperationException("NoteConstants.java - <util> class");
    }

    public static class Title {

        public static final int MAX_ENCRYPTED_DATA_SIZE = 64;
        public static final int MAX_BASE64_SIZE = 88; // depends of MAX_ENCRYPTED_DATA_SIZE (formula:
                                                      // ceil(MAX_ENCRYPTED_DATA_SIZE / 3) * 4)

        public static final String NULL_MESSAGE = "NoteTitle to set is <null>";

        public static final String DTO_REQUIRED_MESSAGE = "Base64 string of note title is required";
        public static final String DTO_INVALID_BASE64_LENGTH_MESSAGE = "Base64 string of note title is longer than " +
                MAX_BASE64_SIZE;

        private static final String DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE = "NoteTitle value cannot be larger than %d (actual size=%d)";

        public static final String getDomainInvalidDataSizeMessage(int actualSize) {
            return String.format(DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE, MAX_ENCRYPTED_DATA_SIZE, actualSize);
        }

    }

    public static class Content {

        public static final int MAX_ENCRYPTED_DATA_SIZE = 2048;
        public static final int MAX_BASE64_SIZE = 2732; // depends of MAX_ENCRYPTED_DATA_SIZE (formula:
                                                        // ceil(MAX_ENCRYPTED_DATA_SIZE / 3) * 4)

        public static final String NULL_MESSAGE = "NoteContent to set is <null>";

        public static final String DTO_REQUIRED_MESSAGE = "Base64 note content required";
        public static final String DTO_INVALID_BASE64_LENGTH_MESSAGE = "Base64 string of note content is longer than " +
                MAX_BASE64_SIZE;

        private static final String DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE = "NoteContent value cannot be larger than %d (actual size=%d)";

        public static final String getDomainInvalidDataSizeMessage(int actualSize) {
            return String.format(DOMAIN_TEMPLATE_INVALID_DATA_SIZE_MESSAGE, MAX_ENCRYPTED_DATA_SIZE, actualSize);
        }

    }

}
