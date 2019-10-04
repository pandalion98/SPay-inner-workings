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

public class McEliecePrivateKeySpec
implements KeySpec {
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix h;
    private int k;
    private int n;
    private String oid;
    private Permutation p1;
    private Permutation p2;
    private PolynomialGF2mSmallM[] qInv;
    private GF2Matrix sInv;

    public McEliecePrivateKeySpec(String string, int n, int n2, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, GF2Matrix gF2Matrix, Permutation permutation, Permutation permutation2, GF2Matrix gF2Matrix2, PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        this.oid = string;
        this.k = n2;
        this.n = n;
        this.field = gF2mField;
        this.goppaPoly = polynomialGF2mSmallM;
        this.sInv = gF2Matrix;
        this.p1 = permutation;
        this.p2 = permutation2;
        this.h = gF2Matrix2;
        this.qInv = arrpolynomialGF2mSmallM;
    }

    public McEliecePrivateKeySpec(String string, int n, int n2, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[][] arrby7) {
        this.oid = string;
        this.n = n;
        this.k = n2;
        this.field = new GF2mField(arrby);
        this.goppaPoly = new PolynomialGF2mSmallM(this.field, arrby2);
        this.sInv = new GF2Matrix(arrby3);
        this.p1 = new Permutation(arrby4);
        this.p2 = new Permutation(arrby5);
        this.h = new GF2Matrix(arrby6);
        this.qInv = new PolynomialGF2mSmallM[arrby7.length];
        for (int i = 0; i < arrby7.length; ++i) {
            this.qInv[i] = new PolynomialGF2mSmallM(this.field, arrby7[i]);
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

    public Permutation getP1() {
        return this.p1;
    }

    public Permutation getP2() {
        return this.p2;
    }

    public PolynomialGF2mSmallM[] getQInv() {
        return this.qInv;
    }

    public GF2Matrix getSInv() {
        return this.sInv;
    }
}

