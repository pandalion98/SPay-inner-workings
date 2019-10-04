/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;

public class McElieceCCA2KeyParameters
extends AsymmetricKeyParameter {
    private McElieceCCA2Parameters params;

    public McElieceCCA2KeyParameters(boolean bl, McElieceCCA2Parameters mcElieceCCA2Parameters) {
        super(bl);
        this.params = mcElieceCCA2Parameters;
    }

    public McElieceCCA2Parameters getParameters() {
        return this.params;
    }
}

