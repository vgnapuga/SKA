package com.ska.constant.note;


public final class ContentConstants {

    public class Format {
        
        public static final int MAX_LENGTH = 8192;

    }

    public class Messages {

        public static final String INVALID_LENGTH_MESSAGE = "Note content is longer than " + Format.MAX_LENGTH + " characters";

    }


    private ContentConstants() {}
    
}
