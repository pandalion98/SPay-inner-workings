package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McElieceCCA2PublicKeyParameters extends McElieceCCA2KeyParameters {
    private GF2Matrix matrixG;
    private int f417n;
    private String oid;
    private int f418t;

    public McElieceCCA2PublicKeyParameters(String str, int i, int i2, GF2Matrix gF2Matrix, McElieceCCA2Parameters mcElieceCCA2Parameters) {
        super(false, mcElieceCCA2Parameters);
        this.oid = str;
        this.f417n = i;
        this.f418t = i2;
        this.matrixG = new GF2Matrix(gF2Matrix);
    }

    public McElieceCCA2PublicKeyParameters(String str, int i, int i2, byte[] bArr, McElieceCCA2Parameters mcElieceCCA2Parameters) {
        super(false, mcElieceCCA2Parameters);
        this.oid = str;
        this.f417n = i;
        this.f418t = i2;
        this.matrixG = new GF2Matrix(bArr);
    }

    public int getK() {
        return this.matrixG.getNumRows();
    }

    public GF2Matrix getMatrixG() {
        return this.matrixG;
    }

    public int getN() {
        return this.f417n;
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.f418t;
    }
}
