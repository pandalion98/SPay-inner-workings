/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSBox
implements CipherParameters {
    private CipherParameters parameters;
    private byte[] sBox;

    public ParametersWithSBox(CipherParameters cipherParameters, byte[] arrby) {
        this.parameters = cipherParameters;
        this.sBox = arrby;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }

    public byte[] getSBox() {
        return this.sBox;
    }
}

