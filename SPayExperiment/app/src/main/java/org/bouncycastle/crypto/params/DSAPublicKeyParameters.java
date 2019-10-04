/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;

public class DSAPublicKeyParameters
extends DSAKeyParameters {
    private BigInteger y;

    public DSAPublicKeyParameters(BigInteger bigInteger, DSAParameters dSAParameters) {
        super(false, dSAParameters);
        this.y = bigInteger;
    }

    public BigInteger getY() {
        return this.y;
    }
}

