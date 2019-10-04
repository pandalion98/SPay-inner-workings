/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;

public class ECPrivateKeyParameters
extends ECKeyParameters {
    BigInteger d;

    public ECPrivateKeyParameters(BigInteger bigInteger, ECDomainParameters eCDomainParameters) {
        super(true, eCDomainParameters);
        this.d = bigInteger;
    }

    public BigInteger getD() {
        return this.d;
    }
}

