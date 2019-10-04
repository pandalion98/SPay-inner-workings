/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.exception;

public class CryptoException
extends RuntimeException {
    private int errorCode;

    public CryptoException(int n2, String string) {
        super(string);
        this.errorCode = n2;
    }

    public CryptoException(String string) {
        super(string);
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }
}

