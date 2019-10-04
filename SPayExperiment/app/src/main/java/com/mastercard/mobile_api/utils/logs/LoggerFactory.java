/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.utils.logs;

import com.mastercard.mobile_api.utils.logs.Logger;

public abstract class LoggerFactory {
    private static LoggerFactory INSTANCE;
    private static String sVersion;

    public static LoggerFactory getInstance() {
        return INSTANCE;
    }

    public static String getVersion() {
        return sVersion;
    }

    public static void setInstance(LoggerFactory loggerFactory) {
        INSTANCE = loggerFactory;
    }

    public static void setVersion(String string) {
        sVersion = string;
    }

    public abstract Logger getLogger(Object var1);
}

