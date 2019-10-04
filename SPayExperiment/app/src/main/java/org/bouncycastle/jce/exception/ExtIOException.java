/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.jce.exception;

import java.io.IOException;
import org.bouncycastle.jce.exception.ExtException;

public class ExtIOException
extends IOException
implements ExtException {
    private Throwable cause;

    public ExtIOException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}

