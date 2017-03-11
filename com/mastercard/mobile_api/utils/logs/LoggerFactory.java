package com.mastercard.mobile_api.utils.logs;

public abstract class LoggerFactory {
    private static LoggerFactory INSTANCE;
    private static String sVersion;

    public abstract Logger getLogger(Object obj);

    public static LoggerFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(LoggerFactory loggerFactory) {
        INSTANCE = loggerFactory;
    }

    public static String getVersion() {
        return sVersion;
    }

    public static void setVersion(String str) {
        sVersion = str;
    }
}
