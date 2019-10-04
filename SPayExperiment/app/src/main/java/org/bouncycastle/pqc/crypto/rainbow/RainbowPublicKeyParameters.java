/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;

public class RainbowPublicKeyParameters
extends RainbowKeyParameters {
    private short[][] coeffquadratic;
    private short[] coeffscalar;
    private short[][] coeffsingular;

    public RainbowPublicKeyParameters(int n, short[][] arrs, short[][] arrs2, short[] arrs3) {
        super(false, n);
        this.coeffquadratic = arrs;
        this.coeffsingular = arrs2;
        this.coeffscalar = arrs3;
    }

    public short[][] getCoeffQuadratic() {
        return this.coeffquadratic;
    }

    public short[] getCoeffScalar() {
        return this.coeffscalar;
    }

    public short[][] getCoeffSingular() {
        return this.coeffsingular;
    }
}

