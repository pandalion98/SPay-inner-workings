/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;

public class KDFParameters
implements DerivationParameters {
    byte[] iv;
    byte[] shared;

    public KDFParameters(byte[] arrby, byte[] arrby2) {
        this.shared = arrby;
        this.iv = arrby2;
    }

    public byte[] getIV() {
        return this.iv;
    }

    public byte[] getSharedSecret() {
        return this.shared;
    }
}

