/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.jcajce.spec;

import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.jcajce.spec.GMSSKeySpec;

public class GMSSPublicKeySpec
extends GMSSKeySpec {
    private byte[] gmssPublicKey;

    public GMSSPublicKeySpec(byte[] arrby, GMSSParameters gMSSParameters) {
        super(gMSSParameters);
        this.gmssPublicKey = arrby;
    }

    public byte[] getPublicKey() {
        return this.gmssPublicKey;
    }
}

