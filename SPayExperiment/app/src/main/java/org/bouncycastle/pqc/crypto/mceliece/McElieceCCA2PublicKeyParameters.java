/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McElieceCCA2PublicKeyParameters
extends McElieceCCA2KeyParameters {
    private GF2Matrix matrixG;
    private int n;
    private String oid;
    private int t;

    public McElieceCCA2PublicKeyParameters(String string, int n, int n2, GF2Matrix gF2Matrix, McElieceCCA2Parameters mcElieceCCA2Parameters) {
        super(false, mcElieceCCA2Parameters);
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.matrixG = new GF2Matrix(gF2Matrix);
    }

    public McElieceCCA2PublicKeyParameters(String string, int n, int n2, byte[] arrby, McElieceCCA2Parameters mcElieceCCA2Parameters) {
        super(false, mcElieceCCA2Parameters);
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.matrixG = new GF2Matrix(arrby);
    }

    public int getK() {
        return this.matrixG.getNumRows();
    }

    public GF2Matrix getMatrixG() {
        return this.matrixG;
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

