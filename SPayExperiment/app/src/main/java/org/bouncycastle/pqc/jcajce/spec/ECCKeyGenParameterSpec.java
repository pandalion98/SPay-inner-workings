/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidParameterException
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;

public class ECCKeyGenParameterSpec
implements AlgorithmParameterSpec {
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    private int fieldPoly;
    private int m;
    private int n;
    private int t;

    public ECCKeyGenParameterSpec() {
        this(11, 50);
    }

    public ECCKeyGenParameterSpec(int n) {
        if (n < 1) {
            throw new InvalidParameterException("key size must be positive");
        }
        this.m = 0;
        this.n = 1;
        while (this.n < n) {
            this.n <<= 1;
            this.m = 1 + this.m;
        }
        this.t = this.n >>> 1;
        this.t /= this.m;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(this.m);
    }

    public ECCKeyGenParameterSpec(int n, int n2) {
        if (n < 1) {
            throw new InvalidParameterException("m must be positive");
        }
        if (n > 32) {
            throw new InvalidParameterException("m is too large");
        }
        this.m = n;
        this.n = 1 << n;
        if (n2 < 0) {
            throw new InvalidParameterException("t must be positive");
        }
        if (n2 > this.n) {
            throw new InvalidParameterException("t must be less than n = 2^m");
        }
        this.t = n2;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(n);
    }

    public ECCKeyGenParameterSpec(int n, int n2, int n3) {
        this.m = n;
        if (n < 1) {
            throw new InvalidParameterException("m must be positive");
        }
        if (n > 32) {
            throw new InvalidParameterException(" m is too large");
        }
        this.n = 1 << n;
        this.t = n2;
        if (n2 < 0) {
            throw new InvalidParameterException("t must be positive");
        }
        if (n2 > this.n) {
            throw new InvalidParameterException("t must be less than n = 2^m");
        }
        if (PolynomialRingGF2.degree(n3) == n && PolynomialRingGF2.isIrreducible(n3)) {
            this.fieldPoly = n3;
            return;
        }
        throw new InvalidParameterException("polynomial is not a field polynomial for GF(2^m)");
    }

    public int getFieldPoly() {
        return this.fieldPoly;
    }

    public int getM() {
        return this.m;
    }

    public int getN() {
        return this.n;
    }

    public int getT() {
        return this.t;
    }
}

