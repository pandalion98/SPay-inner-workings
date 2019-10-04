/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class IESParameters
implements CipherParameters {
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;

    public IESParameters(byte[] arrby, byte[] arrby2, int n2) {
        this.derivation = arrby;
        this.encoding = arrby2;
        this.macKeySize = n2;
    }

    public byte[] getDerivationV() {
        return this.derivation;
    }

    public byte[] getEncodingV() {
        return this.encoding;
    }

    public int getMacKeySize() {
        return this.macKeySize;
    }
}

