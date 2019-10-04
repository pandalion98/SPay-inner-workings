/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class RainbowKeyParameters
extends AsymmetricKeyParameter {
    private int docLength;

    public RainbowKeyParameters(boolean bl, int n) {
        super(bl);
        this.docLength = n;
    }

    public int getDocLength() {
        return this.docLength;
    }
}

