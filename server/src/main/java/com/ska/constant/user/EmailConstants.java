package com.ska.constant.user;


public final class EmailConstants {

    public class Format {

        public static final int MAX_LENGTH = 254;
        public static final String REGEX = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    
    }

    public class Messages {

        public static final String REQUIRED_MESSAGE = "Email is required";
        public static final String INVALID_FORMAT_MESSAGE = "Invalid email format";
        public static final String INVALID_LENGTH_MESSAGE = "Email is longer than " + Format.MAX_LENGTH + " characters";

    }


    private EmailConstants() {}

}
