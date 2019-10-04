/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.gmss;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;

public class GMSSKeyParameters
extends AsymmetricKeyParameter {
    private GMSSParameters params;

    public GMSSKeyParameters(boolean bl, GMSSParameters gMSSParameters) {
        super(bl);
        this.params = gMSSParameters;
    }

    public GMSSParameters getParameters() {
        return this.params;
    }
}

