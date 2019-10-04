/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class NaccacheSternKeyParameters
extends AsymmetricKeyParameter {
    private BigInteger g;
    int lowerSigmaBound;
    private BigInteger n;

    public NaccacheSternKeyParameters(boolean bl, BigInteger bigInteger, BigInteger bigInteger2, int n2) {
        super(bl);
        this.g = bigInteger;
        this.n = bigInteger2;
        this.lowerSigmaBound = n2;
    }

    public BigInteger getG() {
        return this.g;
    }

    public int getLowerSigmaBound() {
        return this.lowerSigmaBound;
    }

    public BigInteger getModulus() {
        return this.n;
    }
}

