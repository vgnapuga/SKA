package com.ska.constant.note;


public final class NoteContentConstants {

    public class Format {
        
        public static final int MAX_ENCRYPTED_DATA_SIZE = 4096;

    }

    public class Messages {

        public static final String REQUIRED_MESSAGE = "Encrypted note content required";
        public static final String INVALID_ENCRYPTED_DATA_SIZE = "Encrypted note content too large";

    }


    private NoteContentConstants() {}
    
}
