/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.sensorframework.sdi.exception;

public class SDIException
extends Exception {
    private int errorCode;
    private String message;

    public SDIException(int n2, String string) {
        super(string);
        this.errorCode = n2;
        this.message = string;
    }

    public String getMessage() {
        return this.message;
    }
}

