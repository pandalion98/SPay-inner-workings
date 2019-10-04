/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;

public class ISO18033KDFParameters
implements DerivationParameters {
    byte[] seed;

    public ISO18033KDFParameters(byte[] arrby) {
        this.seed = arrby;
    }

    public byte[] getSeed() {
        return this.seed;
    }
}

