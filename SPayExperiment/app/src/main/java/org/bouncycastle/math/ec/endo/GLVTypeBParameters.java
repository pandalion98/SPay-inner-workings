/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;

public class GLVTypeBParameters {
    protected final BigInteger beta;
    protected final int bits;
    protected final BigInteger g1;
    protected final BigInteger g2;
    protected final BigInteger lambda;
    protected final BigInteger[] v1;
    protected final BigInteger[] v2;

    public GLVTypeBParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, int n) {
        this.beta = bigInteger;
        this.lambda = bigInteger2;
        this.v1 = arrbigInteger;
        this.v2 = arrbigInteger2;
        this.g1 = bigInteger3;
        this.g2 = bigInteger4;
        this.bits = n;
    }

    public BigInteger getBeta() {
        return this.beta;
    }

    public int getBits() {
        return this.bits;
    }

    public BigInteger getG1() {
        return this.g1;
    }

    public BigInteger getG2() {
        return this.g2;
    }

    public BigInteger getLambda() {
        return this.lambda;
    }

    public BigInteger[] getV1() {
        return this.v1;
    }

    public BigInteger[] getV2() {
        return this.v2;
    }
}

