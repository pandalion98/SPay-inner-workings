/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.IESParameters;

public class IESWithCipherParameters
extends IESParameters {
    private int cipherKeySize;

    public IESWithCipherParameters(byte[] arrby, byte[] arrby2, int n2, int n3) {
        super(arrby, arrby2, n2);
        this.cipherKeySize = n3;
    }

    public int getCipherKeySize() {
        return this.cipherKeySize;
    }
}

