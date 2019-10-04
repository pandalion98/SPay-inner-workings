/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.gmss;

import org.bouncycastle.pqc.crypto.gmss.GMSSKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;

public class GMSSPublicKeyParameters
extends GMSSKeyParameters {
    private byte[] gmssPublicKey;

    public GMSSPublicKeyParameters(byte[] arrby, GMSSParameters gMSSParameters) {
        super(false, gMSSParameters);
        this.gmssPublicKey = arrby;
    }

    public byte[] getPublicKey() {
        return this.gmssPublicKey;
    }
}

