package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;

public class McElieceParameters implements CipherParameters {
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    private int fieldPoly;
    private int f410m;
    private int f411n;
    private int f412t;

    public McElieceParameters() {
        this(DEFAULT_M, DEFAULT_T);
    }

    public McElieceParameters(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("key size must be positive");
        }
        this.f410m = 0;
        this.f411n = 1;
        while (this.f411n < i) {
            this.f411n <<= 1;
            this.f410m++;
        }
        this.f412t = this.f411n >>> 1;
        this.f412t /= this.f410m;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(this.f410m);
    }

    public McElieceParameters(int i, int i2) {
        if (i < 1) {
            throw new IllegalArgumentException("m must be positive");
        } else if (i > 32) {
            throw new IllegalArgumentException("m is too large");
        } else {
            this.f410m = i;
            this.f411n = 1 << i;
            if (i2 < 0) {
                throw new IllegalArgumentException("t must be positive");
            } else if (i2 > this.f411n) {
                throw new IllegalArgumentException("t must be less than n = 2^m");
            } else {
                this.f412t = i2;
                this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(i);
            }
        }
    }

    public McElieceParameters(int i, int i2, int i3) {
        this.f410m = i;
        if (i < 1) {
            throw new IllegalArgumentException("m must be positive");
        } else if (i > 32) {
            throw new IllegalArgumentException(" m is too large");
        } else {
            this.f411n = 1 << i;
            this.f412t = i2;
            if (i2 < 0) {
                throw new IllegalArgumentException("t must be positive");
            } else if (i2 > this.f411n) {
                throw new IllegalArgumentException("t must be less than n = 2^m");
            } else if (PolynomialRingGF2.degree(i3) == i && PolynomialRingGF2.isIrreducible(i3)) {
                this.fieldPoly = i3;
            } else {
                throw new IllegalArgumentException("polynomial is not a field polynomial for GF(2^m)");
            }
        }
    }

    public int getFieldPoly() {
        return this.fieldPoly;
    }

    public int getM() {
        return this.f410m;
    }

    public int getN() {
        return this.f411n;
    }

    public int getT() {
        return this.f412t;
    }
}
