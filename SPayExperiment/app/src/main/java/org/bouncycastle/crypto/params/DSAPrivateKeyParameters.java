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

public class DSAPrivateKeyParameters
extends DSAKeyParameters {
    private BigInteger x;

    public DSAPrivateKeyParameters(BigInteger bigInteger, DSAParameters dSAParameters) {
        super(true, dSAParameters);
        this.x = bigInteger;
    }

    public BigInteger getX() {
        return this.x;
    }
}

