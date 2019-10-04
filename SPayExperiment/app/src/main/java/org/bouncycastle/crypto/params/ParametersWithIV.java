/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithIV
implements CipherParameters {
    private byte[] iv;
    private CipherParameters parameters;

    public ParametersWithIV(CipherParameters cipherParameters, byte[] arrby) {
        this(cipherParameters, arrby, 0, arrby.length);
    }

    public ParametersWithIV(CipherParameters cipherParameters, byte[] arrby, int n2, int n3) {
        this.iv = new byte[n3];
        this.parameters = cipherParameters;
        System.arraycopy((Object)arrby, (int)n2, (Object)this.iv, (int)0, (int)n3);
    }

    public byte[] getIV() {
        return this.iv;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}

