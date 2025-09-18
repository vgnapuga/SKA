package com.ska.constant.note;


public final class NoteTitleConstants {

    private NoteTitleConstants() {
    }

    public record Format() {

        public static final int MAX_ENCRYPTED_DATA_SIZE = 64;

    }

    public record Messages() {

        public static final String REQUIRED_MESSAGE = "Encrypted note title is required";
        public static final String INVALID_ENCRYPTED_DATA_SIZE = "Encrypted note title too large";

    }

}
