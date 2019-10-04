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

public class McEliecePublicKeySpec
implements KeySpec {
    private GF2Matrix g;
    private int n;
    private String oid;
    private int t;

    public McEliecePublicKeySpec(String string, int n, int n2, GF2Matrix gF2Matrix) {
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.g = new GF2Matrix(gF2Matrix);
    }

    public McEliecePublicKeySpec(String string, int n, int n2, byte[] arrby) {
        this.oid = string;
        this.n = n2;
        this.t = n;
        this.g = new GF2Matrix(arrby);
    }

    public GF2Matrix getG() {
        return this.g;
    }

    public int getN() {
        return this.n;
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.t;
    }
}

