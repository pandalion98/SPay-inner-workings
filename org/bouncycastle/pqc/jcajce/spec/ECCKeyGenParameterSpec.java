package org.bouncycastle.pqc.jcajce.spec;

import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;

public class ECCKeyGenParameterSpec implements AlgorithmParameterSpec {
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    private int fieldPoly;
    private int f456m;
    private int f457n;
    private int f458t;

    public ECCKeyGenParameterSpec() {
        this(DEFAULT_M, DEFAULT_T);
    }

    public ECCKeyGenParameterSpec(int i) {
        if (i < 1) {
            throw new InvalidParameterException("key size must be positive");
        }
        this.f456m = 0;
        this.f457n = 1;
        while (this.f457n < i) {
            this.f457n <<= 1;
            this.f456m++;
        }
        this.f458t = this.f457n >>> 1;
        this.f458t /= this.f456m;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(this.f456m);
    }

    public ECCKeyGenParameterSpec(int i, int i2) {
        if (i < 1) {
            throw new InvalidParameterException("m must be positive");
        } else if (i > 32) {
            throw new InvalidParameterException("m is too large");
        } else {
            this.f456m = i;
            this.f457n = 1 << i;
            if (i2 < 0) {
                throw new InvalidParameterException("t must be positive");
            } else if (i2 > this.f457n) {
                throw new InvalidParameterException("t must be less than n = 2^m");
            } else {
                this.f458t = i2;
                this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(i);
            }
        }
    }

    public ECCKeyGenParameterSpec(int i, int i2, int i3) {
        this.f456m = i;
        if (i < 1) {
            throw new InvalidParameterException("m must be positive");
        } else if (i > 32) {
            throw new InvalidParameterException(" m is too large");
        } else {
            this.f457n = 1 << i;
            this.f458t = i2;
            if (i2 < 0) {
                throw new InvalidParameterException("t must be positive");
            } else if (i2 > this.f457n) {
                throw new InvalidParameterException("t must be less than n = 2^m");
            } else if (PolynomialRingGF2.degree(i3) == i && PolynomialRingGF2.isIrreducible(i3)) {
                this.fieldPoly = i3;
            } else {
                throw new InvalidParameterException("polynomial is not a field polynomial for GF(2^m)");
            }
        }
    }

    public int getFieldPoly() {
        return this.fieldPoly;
    }

    public int getM() {
        return this.f456m;
    }

    public int getN() {
        return this.f457n;
    }

    public int getT() {
        return this.f458t;
    }
}
