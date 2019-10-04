/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.sensorframework.sda;

public class SDAException
extends Exception {
    private static final long serialVersionUID = -6952859423645368705L;
    private int errorCode;
    private String message;

    public SDAException(int n2, String string) {
        super(string);
        this.errorCode = n2;
        this.message = string;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}

