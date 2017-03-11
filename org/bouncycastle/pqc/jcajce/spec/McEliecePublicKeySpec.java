package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McEliecePublicKeySpec implements KeySpec {
    private GF2Matrix f468g;
    private int f469n;
    private String oid;
    private int f470t;

    public McEliecePublicKeySpec(String str, int i, int i2, GF2Matrix gF2Matrix) {
        this.oid = str;
        this.f469n = i;
        this.f470t = i2;
        this.f468g = new GF2Matrix(gF2Matrix);
    }

    public McEliecePublicKeySpec(String str, int i, int i2, byte[] bArr) {
        this.oid = str;
        this.f469n = i2;
        this.f470t = i;
        this.f468g = new GF2Matrix(bArr);
    }

    public GF2Matrix getG() {
        return this.f468g;
    }

    public int getN() {
        return this.f469n;
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.f470t;
    }
}
