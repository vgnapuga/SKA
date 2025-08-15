package com.ska.util;


public final class LogTemplates {

    private static final String START_PART = " - start";
    
    private LogTemplates() {}


    public static String startLog(final String operationName) {
        return operationName + START_PART;
    }

    public static String validationStartLog(final String operationName) {
        return operationName + " validation" + START_PART;
    }

    public static String checkStartLog(final String operationName) {
        return operationName + " check" + START_PART;
    }

}
