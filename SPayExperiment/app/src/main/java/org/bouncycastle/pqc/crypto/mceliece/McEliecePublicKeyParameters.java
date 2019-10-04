/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McEliecePublicKeyParameters
extends McElieceKeyParameters {
    private GF2Matrix g;
    private int n;
    private String oid;
    private int t;

    public McEliecePublicKeyParameters(String string, int n, int n2, GF2Matrix gF2Matrix, McElieceParameters mcElieceParameters) {
        super(false, mcElieceParameters);
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.g = new GF2Matrix(gF2Matrix);
    }

    public McEliecePublicKeyParameters(String string, int n, int n2, byte[] arrby, McElieceParameters mcElieceParameters) {
        super(false, mcElieceParameters);
        this.oid = string;
        this.n = n2;
        this.t = n;
        this.g = new GF2Matrix(arrby);
    }

    public GF2Matrix getG() {
        return this.g;
    }

    public int getK() {
        return this.g.getNumRows();
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

