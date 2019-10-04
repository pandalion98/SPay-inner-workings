/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CRLException
 */
package org.bouncycastle.jce.provider;

import java.security.cert.CRLException;

class ExtCRLException
extends CRLException {
    Throwable cause;

    ExtCRLException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

