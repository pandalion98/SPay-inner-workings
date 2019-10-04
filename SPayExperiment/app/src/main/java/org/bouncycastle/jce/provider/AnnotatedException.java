/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

public class AnnotatedException
extends Exception
implements ExtException {
    private Throwable _underlyingException;

    public AnnotatedException(String string) {
        this(string, null);
    }

    public AnnotatedException(String string, Throwable throwable) {
        super(string);
        this._underlyingException = throwable;
    }

    @Override
    public Throwable getCause() {
        return this._underlyingException;
    }

    Throwable getUnderlyingException() {
        return this._underlyingException;
    }
}

