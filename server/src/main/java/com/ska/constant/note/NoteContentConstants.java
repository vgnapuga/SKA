package com.ska.constant.note;


public final class NoteContentConstants {

    private NoteContentConstants() {
    }

    public record Format() {

        public static final int MAX_ENCRYPTED_DATA_SIZE = 2048;

    }

    public record Messages() {

        public static final String REQUIRED_MESSAGE = "Encrypted note content required";
        public static final String INVALID_ENCRYPTED_DATA_SIZE = "Encrypted note content too large";

    }

}
