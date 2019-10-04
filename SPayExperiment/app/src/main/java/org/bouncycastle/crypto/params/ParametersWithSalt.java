/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSalt
implements CipherParameters {
    private CipherParameters parameters;
    private byte[] salt;

    public ParametersWithSalt(CipherParameters cipherParameters, byte[] arrby) {
        this(cipherParameters, arrby, 0, arrby.length);
    }

    public ParametersWithSalt(CipherParameters cipherParameters, byte[] arrby, int n2, int n3) {
        this.salt = new byte[n3];
        this.parameters = cipherParameters;
        System.arraycopy((Object)arrby, (int)n2, (Object)this.salt, (int)0, (int)n3);
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }

    public byte[] getSalt() {
        return this.salt;
    }
}

