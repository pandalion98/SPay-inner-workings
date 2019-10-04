/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class McElieceCCA2PrivateKeySpec
implements KeySpec {
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix h;
    private int k;
    private int n;
    private String oid;
    private Permutation p;
    private PolynomialGF2mSmallM[] qInv;

    public McElieceCCA2PrivateKeySpec(String string, int n, int n2, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, Permutation permutation, GF2Matrix gF2Matrix, PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        this.oid = string;
        this.n = n;
        this.k = n2;
        this.field = gF2mField;
        this.goppaPoly = polynomialGF2mSmallM;
        this.p = permutation;
        this.h = gF2Matrix;
        this.qInv = arrpolynomialGF2mSmallM;
    }

    public McElieceCCA2PrivateKeySpec(String string, int n, int n2, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[][] arrby5) {
        this.oid = string;
        this.n = n;
        this.k = n2;
        this.field = new GF2mField(arrby);
        this.goppaPoly = new PolynomialGF2mSmallM(this.field, arrby2);
        this.p = new Permutation(arrby3);
        this.h = new GF2Matrix(arrby4);
        this.qInv = new PolynomialGF2mSmallM[arrby5.length];
        for (int i = 0; i < arrby5.length; ++i) {
            this.qInv[i] = new PolynomialGF2mSmallM(this.field, arrby5[i]);
        }
    }

    public GF2mField getField() {
        return this.field;
    }

    public PolynomialGF2mSmallM getGoppaPoly() {
        return this.goppaPoly;
    }

    public GF2Matrix getH() {
        return this.h;
    }

    public int getK() {
        return this.k;
    }

    public int getN() {
        return this.n;
    }

    public String getOIDString() {
        return this.oid;
    }

    public Permutation getP() {
        return this.p;
    }

    public PolynomialGF2mSmallM[] getQInv() {
        return this.qInv;
    }
}

