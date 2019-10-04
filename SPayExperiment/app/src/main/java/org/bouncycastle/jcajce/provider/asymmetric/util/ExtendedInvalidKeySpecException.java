/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.spec.InvalidKeySpecException
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.security.spec.InvalidKeySpecException;

public class ExtendedInvalidKeySpecException
extends InvalidKeySpecException {
    private Throwable cause;

    public ExtendedInvalidKeySpecException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

