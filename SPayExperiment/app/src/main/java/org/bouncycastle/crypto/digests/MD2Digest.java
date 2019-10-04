/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Memoable;

public class MD2Digest
implements ExtendedDigest,
Memoable {
    private static final int DIGEST_LENGTH = 16;
    private static final byte[] S = new byte[]{41, 46, 67, -55, -94, -40, 124, 1, 61, 54, 84, -95, -20, -16, 6, 19, 98, -89, 5, -13, -64, -57, 115, -116, -104, -109, 43, -39, -68, 76, -126, -54, 30, -101, 87, 60, -3, -44, -32, 22, 103, 66, 111, 24, -118, 23, -27, 18, -66, 78, -60, -42, -38, -98, -34, 73, -96, -5, -11, -114, -69, 47, -18, 122, -87, 104, 121, -111, 21, -78, 7, 63, -108, -62, 16, -119, 11, 34, 95, 33, -128, 127, 93, -102, 90, -112, 50, 39, 53, 62, -52, -25, -65, -9, -105, 3, -1, 25, 48, -77, 72, -91, -75, -47, -41, 94, -110, 42, -84, 86, -86, -58, 79, -72, 56, -46, -106, -92, 125, -74, 118, -4, 107, -30, -100, 116, 4, -15, 69, -99, 112, 89, 100, 113, -121, 32, -122, 91, -49, 101, -26, 45, -88, 2, 27, 96, 37, -83, -82, -80, -71, -10, 28, 70, 97, 105, 52, 64, 126, 15, 85, 71, -93, 35, -35, 81, -81, 58, -61, 92, -7, -50, -70, -59, -22, 38, 44, 83, 13, 110, -123, 40, -124, 9, -45, -33, -51, -12, 65, -127, 77, 82, 106, -36, 55, -56, 108, -63, -85, -6, 36, -31, 123, 8, 12, -67, -79, 74, 120, -120, -107, -117, -29, 99, -24, 109, -23, -53, -43, -2, 59, 0, 29, 57, -14, -17, -73, 14, 102, 88, -48, -28, -90, 119, 114, -8, -21, 117, 75, 10, 49, 68, 80, -76, -113, -19, 31, 26, -37, -103, -115, 51, -97, 17, -125, 20};
    private byte[] C = new byte[16];
    private int COff;
    private byte[] M = new byte[16];
    private byte[] X = new byte[48];
    private int mOff;
    private int xOff;

    public MD2Digest() {
        this.reset();
    }

    public MD2Digest(MD2Digest mD2Digest) {
        this.copyIn(mD2Digest);
    }

    private void copyIn(MD2Digest mD2Digest) {
        System.arraycopy((Object)mD2Digest.X, (int)0, (Object)this.X, (int)0, (int)mD2Digest.X.length);
        this.xOff = mD2Digest.xOff;
        System.arraycopy((Object)mD2Digest.M, (int)0, (Object)this.M, (int)0, (int)mD2Digest.M.length);
        this.mOff = mD2Digest.mOff;
        System.arraycopy((Object)mD2Digest.C, (int)0, (Object)this.C, (int)0, (int)mD2Digest.C.length);
        this.COff = mD2Digest.COff;
    }

    @Override
    public Memoable copy() {
        return new MD2Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte by = (byte)(this.M.length - this.mOff);
        for (int i2 = this.mOff; i2 < this.M.length; ++i2) {
            this.M[i2] = by;
        }
        this.processCheckSum(this.M);
        this.processBlock(this.M);
        this.processBlock(this.C);
        System.arraycopy((Object)this.X, (int)this.xOff, (Object)arrby, (int)n2, (int)16);
        this.reset();
        return 16;
    }

    @Override
    public String getAlgorithmName() {
        return "MD2";
    }

    @Override
    public int getByteLength() {
        return 16;
    }

    @Override
    public int getDigestSize() {
        return 16;
    }

    protected void processBlock(byte[] arrby) {
        for (int i2 = 0; i2 < 16; ++i2) {
            this.X[i2 + 16] = arrby[i2];
            this.X[i2 + 32] = (byte)(arrby[i2] ^ this.X[i2]);
        }
        int n2 = 0;
        for (int i3 = 0; i3 < 18; ++i3) {
            int n3 = n2;
            for (int i4 = 0; i4 < 48; ++i4) {
                byte by;
                byte[] arrby2 = this.X;
                arrby2[i4] = by = (byte)(arrby2[i4] ^ S[n3]);
                n3 = by & 255;
            }
            int n4 = (n3 + i3) % 256;
            n2 = n4;
        }
    }

    protected void processCheckSum(byte[] arrby) {
        byte by = this.C[15];
        for (int i2 = 0; i2 < 16; ++i2) {
            byte[] arrby2 = this.C;
            arrby2[i2] = (byte)(arrby2[i2] ^ S[255 & (by ^ arrby[i2])]);
            by = this.C[i2];
        }
    }

    @Override
    public void reset() {
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
        this.mOff = 0;
        for (int i3 = 0; i3 != this.M.length; ++i3) {
            this.M[i3] = 0;
        }
        this.COff = 0;
        for (int i4 = 0; i4 != this.C.length; ++i4) {
            this.C[i4] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((MD2Digest)memoable);
    }

    @Override
    public void update(byte by) {
        byte[] arrby = this.M;
        int n2 = this.mOff;
        this.mOff = n2 + 1;
        arrby[n2] = by;
        if (this.mOff == 16) {
            this.processCheckSum(this.M);
            this.processBlock(this.M);
            this.mOff = 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte[] arrby, int n2, int n3) {
        while (this.mOff != 0 && n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
        int n4 = n3;
        int n5 = n2;
        while (n4 > 16) {
            System.arraycopy((Object)arrby, (int)n5, (Object)this.M, (int)0, (int)16);
            this.processCheckSum(this.M);
            this.processBlock(this.M);
            int n6 = n4 - 16;
            int n7 = n5 + 16;
            n4 = n6;
            n5 = n7;
        }
        while (n4 > 0) {
            this.update(arrby[n5]);
            ++n5;
            --n4;
        }
        return;
    }
}

