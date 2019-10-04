/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spaytzsvc.api;

public class TAException
extends Exception {
    public static final int ERROR_DEVICE_INTEGRITY_COULDNT_VERIFY = 1;
    public static final int ERROR_DEVICE_INTEGRITY_FAILED = 2;
    public static final int ERROR_TA_CANNOT_FIND = 3;
    public static final int ERROR_TA_DECAPSULATE_WRAP_FAILED = 4;
    protected int errorCode;

    public TAException(String string, int n2) {
        super(string);
        this.errorCode = n2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}

