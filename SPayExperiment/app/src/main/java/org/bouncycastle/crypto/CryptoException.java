/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto;

public class CryptoException
extends Exception {
    private Throwable cause;

    public CryptoException() {
    }

    public CryptoException(String string) {
        super(string);
    }

    public CryptoException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

