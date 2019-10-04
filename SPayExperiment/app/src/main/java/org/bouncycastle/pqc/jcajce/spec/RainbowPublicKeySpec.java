/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.KeySpec;

public class RainbowPublicKeySpec
implements KeySpec {
    private short[][] coeffquadratic;
    private short[] coeffscalar;
    private short[][] coeffsingular;
    private int docLength;

    public RainbowPublicKeySpec(int n, short[][] arrs, short[][] arrs2, short[] arrs3) {
        this.docLength = n;
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

    public int getDocLength() {
        return this.docLength;
    }
}

