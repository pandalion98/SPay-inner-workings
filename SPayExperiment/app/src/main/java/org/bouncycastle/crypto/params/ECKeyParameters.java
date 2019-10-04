/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;

public class ECKeyParameters
extends AsymmetricKeyParameter {
    ECDomainParameters params;

    protected ECKeyParameters(boolean bl, ECDomainParameters eCDomainParameters) {
        super(bl);
        this.params = eCDomainParameters;
    }

    public ECDomainParameters getParameters() {
        return this.params;
    }
}

