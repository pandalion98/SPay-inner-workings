/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.util.encoders;

public class EncoderException
extends IllegalStateException {
    private Throwable cause;

    EncoderException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

