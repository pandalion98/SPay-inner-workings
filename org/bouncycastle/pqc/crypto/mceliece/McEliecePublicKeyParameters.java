package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McEliecePublicKeyParameters extends McElieceKeyParameters {
    private GF2Matrix f437g;
    private int f438n;
    private String oid;
    private int f439t;

    public McEliecePublicKeyParameters(String str, int i, int i2, GF2Matrix gF2Matrix, McElieceParameters mcElieceParameters) {
        super(false, mcElieceParameters);
        this.oid = str;
        this.f438n = i;
        this.f439t = i2;
        this.f437g = new GF2Matrix(gF2Matrix);
    }

    public McEliecePublicKeyParameters(String str, int i, int i2, byte[] bArr, McElieceParameters mcElieceParameters) {
        super(false, mcElieceParameters);
        this.oid = str;
        this.f438n = i2;
        this.f439t = i;
        this.f437g = new GF2Matrix(bArr);
    }

    public GF2Matrix getG() {
        return this.f437g;
    }

    public int getK() {
        return this.f437g.getNumRows();
    }

    public int getN() {
        return this.f438n;
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.f439t;
    }
}
