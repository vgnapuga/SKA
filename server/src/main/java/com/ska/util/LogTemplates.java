package com.ska.util;


/**
 * Util class for logging messages.
 * 
 * Provides methods to avoid duplication (DRY principle). Centralized log
 * message management with private constructor.
 */
public final class LogTemplates {

    private static final String START_PART = " - start";

    private LogTemplates() {
    }

    public static String startLog(String operationName) {
        return operationName + START_PART;
    }

    public static String validationStartLog(String operationName) {
        return operationName + " validation" + START_PART;
    }

    public static String checkStartLog(String operationName) {
        return operationName + " check" + START_PART;
    }

    public static String checkBase64StartLog(String encryptedName) {
        return checkStartLog(encryptedName + " Base64");
    }

    public static String generateUuidStartLog() {
        return startLog("UUID generation");
    }

    public static String dataBaseQueryStartLog() {
        return startLog("Database query");
    }

    public static String checkUserExistenceStartLog() {
        return checkStartLog("User existence");
    }

    public static String userIdValidationStartLog() {
        return validationStartLog("User ID");
    }

    public static String checkPermissionStartLog(String operationName) {
        return checkStartLog(operationName + " permission");
    }

}
