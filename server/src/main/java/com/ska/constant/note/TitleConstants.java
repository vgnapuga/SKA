package com.ska.constant.note;


public final class TitleConstants {
    
    public class Numeric {

        public static final int MAX_LENGTH = 64;

    }

    public class Messages {

        public static final String REQUIRED_MESSAGE = "Note title is required";
        public static final String INVALID_LENGTH_MESSAGE = "Note title is longer than " + Numeric.MAX_LENGTH + " characters";

    }

}
