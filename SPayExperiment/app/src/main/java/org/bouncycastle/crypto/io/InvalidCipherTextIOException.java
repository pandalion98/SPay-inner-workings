/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.io;

import org.bouncycastle.crypto.io.CipherIOException;

public class InvalidCipherTextIOException
extends CipherIOException {
    private static final long serialVersionUID = 1L;

    public InvalidCipherTextIOException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

