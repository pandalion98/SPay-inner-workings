/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.EncodableDigest;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SHA1Digest
extends GeneralDigest
implements EncodableDigest {
    private static final int DIGEST_LENGTH = 20;
    private static final int Y1 = 1518500249;
    private static final int Y2 = 1859775393;
    private static final int Y3 = -1894007588;
    private static final int Y4 = -899497514;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int[] X = new int[80];
    private int xOff;

    public SHA1Digest() {
        this.reset();
    }

    public SHA1Digest(SHA1Digest sHA1Digest) {
        super(sHA1Digest);
        this.copyIn(sHA1Digest);
    }

    public SHA1Digest(byte[] arrby) {
        super(arrby);
        this.H1 = Pack.bigEndianToInt((byte[])arrby, (int)16);
        this.H2 = Pack.bigEndianToInt((byte[])arrby, (int)20);
        this.H3 = Pack.bigEndianToInt((byte[])arrby, (int)24);
        this.H4 = Pack.bigEndianToInt((byte[])arrby, (int)28);
        this.H5 = Pack.bigEndianToInt((byte[])arrby, (int)32);
        this.xOff = Pack.bigEndianToInt((byte[])arrby, (int)36);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            this.X[i2] = Pack.bigEndianToInt((byte[])arrby, (int)(40 + i2 * 4));
        }
    }

    private void copyIn(SHA1Digest sHA1Digest) {
        this.H1 = sHA1Digest.H1;
        this.H2 = sHA1Digest.H2;
        this.H3 = sHA1Digest.H3;
        this.H4 = sHA1Digest.H4;
        this.H5 = sHA1Digest.H5;
        System.arraycopy((Object)sHA1Digest.X, (int)0, (Object)this.X, (int)0, (int)sHA1Digest.X.length);
        this.xOff = sHA1Digest.xOff;
    }

    private int f(int n2, int n3, int n4) {
        return n2 & n3 | n4 & ~n2;
    }

    private int g(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    private int h(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    @Override
    public Memoable copy() {
        return new SHA1Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        Pack.intToBigEndian((int)this.H1, (byte[])arrby, (int)n2);
        Pack.intToBigEndian((int)this.H2, (byte[])arrby, (int)(n2 + 4));
        Pack.intToBigEndian((int)this.H3, (byte[])arrby, (int)(n2 + 8));
        Pack.intToBigEndian((int)this.H4, (byte[])arrby, (int)(n2 + 12));
        Pack.intToBigEndian((int)this.H5, (byte[])arrby, (int)(n2 + 16));
        this.reset();
        return 20;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-1";
    }

    @Override
    public int getDigestSize() {
        return 20;
    }

    @Override
    public byte[] getEncodedState() {
        byte[] arrby = new byte[40 + 4 * this.xOff];
        super.populateState(arrby);
        Pack.intToBigEndian((int)this.H1, (byte[])arrby, (int)16);
        Pack.intToBigEndian((int)this.H2, (byte[])arrby, (int)20);
        Pack.intToBigEndian((int)this.H3, (byte[])arrby, (int)24);
        Pack.intToBigEndian((int)this.H4, (byte[])arrby, (int)28);
        Pack.intToBigEndian((int)this.H5, (byte[])arrby, (int)32);
        Pack.intToBigEndian((int)this.xOff, (byte[])arrby, (int)36);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            Pack.intToBigEndian((int)this.X[i2], (byte[])arrby, (int)(40 + i2 * 4));
        }
        return arrby;
    }

    @Override
    protected void processBlock() {
        for (int i2 = 16; i2 < 80; ++i2) {
            int n2 = this.X[i2 - 3] ^ this.X[i2 - 8] ^ this.X[i2 - 14] ^ this.X[i2 - 16];
            this.X[i2] = n2 << 1 | n2 >>> 31;
        }
        int n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = this.H5;
        int n8 = 0;
        for (int i3 = 0; i3 < 4; ++i3) {
            int n9 = (n3 << 5 | n3 >>> 27) + this.f(n4, n5, n6);
            int[] arrn = this.X;
            int n10 = n8 + 1;
            int n11 = n7 + (1518500249 + (n9 + arrn[n8]));
            int n12 = n4 << 30 | n4 >>> 2;
            int n13 = (n11 << 5 | n11 >>> 27) + this.f(n3, n12, n5);
            int[] arrn2 = this.X;
            int n14 = n10 + 1;
            int n15 = n6 + (1518500249 + (n13 + arrn2[n10]));
            int n16 = n3 << 30 | n3 >>> 2;
            int n17 = (n15 << 5 | n15 >>> 27) + this.f(n11, n16, n12);
            int[] arrn3 = this.X;
            int n18 = n14 + 1;
            int n19 = n5 + (1518500249 + (n17 + arrn3[n14]));
            n7 = n11 << 30 | n11 >>> 2;
            int n20 = (n19 << 5 | n19 >>> 27) + this.f(n15, n7, n16);
            int[] arrn4 = this.X;
            int n21 = n18 + 1;
            n4 = n12 + (1518500249 + (n20 + arrn4[n18]));
            n6 = n15 << 30 | n15 >>> 2;
            int n22 = (n4 << 5 | n4 >>> 27) + this.f(n19, n6, n7);
            int[] arrn5 = this.X;
            n8 = n21 + 1;
            n3 = n16 + (1518500249 + (n22 + arrn5[n21]));
            n5 = n19 << 30 | n19 >>> 2;
        }
        for (int i4 = 0; i4 < 4; ++i4) {
            int n23 = (n3 << 5 | n3 >>> 27) + this.h(n4, n5, n6);
            int[] arrn = this.X;
            int n24 = n8 + 1;
            int n25 = n7 + (1859775393 + (n23 + arrn[n8]));
            int n26 = n4 << 30 | n4 >>> 2;
            int n27 = (n25 << 5 | n25 >>> 27) + this.h(n3, n26, n5);
            int[] arrn6 = this.X;
            int n28 = n24 + 1;
            int n29 = n6 + (1859775393 + (n27 + arrn6[n24]));
            int n30 = n3 << 30 | n3 >>> 2;
            int n31 = (n29 << 5 | n29 >>> 27) + this.h(n25, n30, n26);
            int[] arrn7 = this.X;
            int n32 = n28 + 1;
            int n33 = n5 + (1859775393 + (n31 + arrn7[n28]));
            n7 = n25 << 30 | n25 >>> 2;
            int n34 = (n33 << 5 | n33 >>> 27) + this.h(n29, n7, n30);
            int[] arrn8 = this.X;
            int n35 = n32 + 1;
            n4 = n26 + (1859775393 + (n34 + arrn8[n32]));
            n6 = n29 << 30 | n29 >>> 2;
            int n36 = (n4 << 5 | n4 >>> 27) + this.h(n33, n6, n7);
            int[] arrn9 = this.X;
            n8 = n35 + 1;
            n3 = n30 + (1859775393 + (n36 + arrn9[n35]));
            n5 = n33 << 30 | n33 >>> 2;
        }
        for (int i5 = 0; i5 < 4; ++i5) {
            int n37 = (n3 << 5 | n3 >>> 27) + this.g(n4, n5, n6);
            int[] arrn = this.X;
            int n38 = n8 + 1;
            int n39 = n7 + (-1894007588 + (n37 + arrn[n8]));
            int n40 = n4 << 30 | n4 >>> 2;
            int n41 = (n39 << 5 | n39 >>> 27) + this.g(n3, n40, n5);
            int[] arrn10 = this.X;
            int n42 = n38 + 1;
            int n43 = n6 + (-1894007588 + (n41 + arrn10[n38]));
            int n44 = n3 << 30 | n3 >>> 2;
            int n45 = (n43 << 5 | n43 >>> 27) + this.g(n39, n44, n40);
            int[] arrn11 = this.X;
            int n46 = n42 + 1;
            int n47 = n5 + (-1894007588 + (n45 + arrn11[n42]));
            n7 = n39 << 30 | n39 >>> 2;
            int n48 = (n47 << 5 | n47 >>> 27) + this.g(n43, n7, n44);
            int[] arrn12 = this.X;
            int n49 = n46 + 1;
            n4 = n40 + (-1894007588 + (n48 + arrn12[n46]));
            n6 = n43 << 30 | n43 >>> 2;
            int n50 = (n4 << 5 | n4 >>> 27) + this.g(n47, n6, n7);
            int[] arrn13 = this.X;
            n8 = n49 + 1;
            n3 = n44 + (-1894007588 + (n50 + arrn13[n49]));
            n5 = n47 << 30 | n47 >>> 2;
        }
        for (int i6 = 0; i6 <= 3; ++i6) {
            int n51 = (n3 << 5 | n3 >>> 27) + this.h(n4, n5, n6);
            int[] arrn = this.X;
            int n52 = n8 + 1;
            int n53 = n7 + (-899497514 + (n51 + arrn[n8]));
            int n54 = n4 << 30 | n4 >>> 2;
            int n55 = (n53 << 5 | n53 >>> 27) + this.h(n3, n54, n5);
            int[] arrn14 = this.X;
            int n56 = n52 + 1;
            int n57 = n6 + (-899497514 + (n55 + arrn14[n52]));
            int n58 = n3 << 30 | n3 >>> 2;
            int n59 = (n57 << 5 | n57 >>> 27) + this.h(n53, n58, n54);
            int[] arrn15 = this.X;
            int n60 = n56 + 1;
            int n61 = n5 + (-899497514 + (n59 + arrn15[n56]));
            n7 = n53 << 30 | n53 >>> 2;
            int n62 = (n61 << 5 | n61 >>> 27) + this.h(n57, n7, n58);
            int[] arrn16 = this.X;
            int n63 = n60 + 1;
            n4 = n54 + (-899497514 + (n62 + arrn16[n60]));
            n6 = n57 << 30 | n57 >>> 2;
            int n64 = (n4 << 5 | n4 >>> 27) + this.h(n61, n6, n7);
            int[] arrn17 = this.X;
            n8 = n63 + 1;
            n3 = n58 + (-899497514 + (n64 + arrn17[n63]));
            n5 = n61 << 30 | n61 >>> 2;
        }
        this.H1 = n3 + this.H1;
        this.H2 = n4 + this.H2;
        this.H3 = n5 + this.H3;
        this.H4 = n6 + this.H4;
        this.H5 = n7 + this.H5;
        this.xOff = 0;
        for (int i7 = 0; i7 < 16; ++i7) {
            this.X[i7] = 0;
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        this.X[14] = (int)(l2 >>> 32);
        this.X[15] = (int)(-1L & l2);
    }

    @Override
    protected void processWord(byte[] arrby, int n2) {
        int n3;
        int n4;
        int n5 = arrby[n2] << 24;
        int n6 = n2 + 1;
        int n7 = n5 | (255 & arrby[n6]) << 16;
        int n8 = n6 + 1;
        this.X[this.xOff] = n3 = n7 | (255 & arrby[n8]) << 8 | 255 & arrby[n8 + 1];
        this.xOff = n4 = 1 + this.xOff;
        if (n4 == 16) {
            this.processBlock();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.H5 = -1009589776;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        SHA1Digest sHA1Digest = (SHA1Digest)memoable;
        super.copyIn(sHA1Digest);
        this.copyIn(sHA1Digest);
    }
}

