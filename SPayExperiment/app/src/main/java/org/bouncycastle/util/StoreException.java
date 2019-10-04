/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.util;

public class StoreException
extends RuntimeException {
    private Throwable _e;

    public StoreException(String string, Throwable throwable) {
        super(string);
        this._e = throwable;
    }

    public Throwable getCause() {
        return this._e;
    }
}

