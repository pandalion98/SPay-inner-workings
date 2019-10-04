/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class MaxBytesExceededException
extends RuntimeCryptoException {
    public MaxBytesExceededException() {
    }

    public MaxBytesExceededException(String string) {
        super(string);
    }
}

