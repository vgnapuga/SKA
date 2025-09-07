package com.ska.constant.note;


public final class NoteTitleConstants {

    public class Format {

        public static final int MAX_ENCRYPTED_DATA_SIZE = 64;

    }

    public class Messages {

        public static final String REQUIRED_MESSAGE = "Encrypted note title is required";
        public static final String INVALID_ENCRYPTED_DATA_SIZE = "Encrypted note title too large";

    }

    private NoteTitleConstants() {
    }

}
