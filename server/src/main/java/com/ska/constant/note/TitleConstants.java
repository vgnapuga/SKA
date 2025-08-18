package com.ska.constant.note;


public final class TitleConstants {
    
    public class Format {

        public static final int MAX_LENGTH = 64;

    }

    public class Messages {

        public static final String REQUIRED_MESSAGE = "Note title is required";
        public static final String INVALID_LENGTH_MESSAGE = "Note title is longer than " + Format.MAX_LENGTH + " characters";

    }


    private TitleConstants() {}

}
