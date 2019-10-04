/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;

public class MGFParameters
implements DerivationParameters {
    byte[] seed;

    public MGFParameters(byte[] arrby) {
        this(arrby, 0, arrby.length);
    }

    public MGFParameters(byte[] arrby, int n2, int n3) {
        this.seed = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)this.seed, (int)0, (int)n3);
    }

    public byte[] getSeed() {
        return this.seed;
    }
}

