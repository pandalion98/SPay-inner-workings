/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.util.io.pem;

import java.io.IOException;

public class PemGenerationException
extends IOException {
    private Throwable cause;

    public PemGenerationException(String string) {
        super(string);
    }

    public PemGenerationException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

