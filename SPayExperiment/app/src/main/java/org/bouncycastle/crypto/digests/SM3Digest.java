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

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SM3Digest
extends GeneralDigest {
    private static final int BLOCK_SIZE = 16;
    private static final int DIGEST_LENGTH = 32;
    private static final int[] T;
    private int[] V = new int[8];
    private int[] W = new int[68];
    private int[] W1 = new int[64];
    private int[] inwords = new int[16];
    private int xOff;

    static {
        int n2 = 16;
        T = new int[64];
        for (int i2 = 0; i2 < n2; ++i2) {
            SM3Digest.T[i2] = 2043430169 << i2 | 2043430169 >>> 32 - i2;
        }
        while (n2 < 64) {
            int n3 = n2 % 32;
            SM3Digest.T[n2] = 2055708042 << n3 | 2055708042 >>> 32 - n3;
            ++n2;
        }
    }

    public SM3Digest() {
        this.reset();
    }

    public SM3Digest(SM3Digest sM3Digest) {
        super(sM3Digest);
        this.copyIn(sM3Digest);
    }

    private int FF0(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    private int FF1(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    private int GG0(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    private int GG1(int n2, int n3, int n4) {
        return n2 & n3 | n4 & ~n2;
    }

    private int P0(int n2) {
        int n3 = n2 << 9 | n2 >>> 23;
        return (n2 << 17 | n2 >>> 15) ^ (n3 ^ n2);
    }

    private int P1(int n2) {
        int n3 = n2 << 15 | n2 >>> 17;
        return (n2 << 23 | n2 >>> 9) ^ (n3 ^ n2);
    }

    private void copyIn(SM3Digest sM3Digest) {
        System.arraycopy((Object)sM3Digest.V, (int)0, (Object)this.V, (int)0, (int)this.V.length);
        System.arraycopy((Object)sM3Digest.inwords, (int)0, (Object)this.inwords, (int)0, (int)this.inwords.length);
        this.xOff = sM3Digest.xOff;
    }

    @Override
    public Memoable copy() {
        return new SM3Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        Pack.intToBigEndian((int)this.V[0], (byte[])arrby, (int)(n2 + 0));
        Pack.intToBigEndian((int)this.V[1], (byte[])arrby, (int)(n2 + 4));
        Pack.intToBigEndian((int)this.V[2], (byte[])arrby, (int)(n2 + 8));
        Pack.intToBigEndian((int)this.V[3], (byte[])arrby, (int)(n2 + 12));
        Pack.intToBigEndian((int)this.V[4], (byte[])arrby, (int)(n2 + 16));
        Pack.intToBigEndian((int)this.V[5], (byte[])arrby, (int)(n2 + 20));
        Pack.intToBigEndian((int)this.V[6], (byte[])arrby, (int)(n2 + 24));
        Pack.intToBigEndian((int)this.V[7], (byte[])arrby, (int)(n2 + 28));
        this.reset();
        return 32;
    }

    @Override
    public String getAlgorithmName() {
        return "SM3";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    protected void processBlock() {
        for (int i2 = 0; i2 < 16; ++i2) {
            this.W[i2] = this.inwords[i2];
        }
        for (int i3 = 16; i3 < 68; ++i3) {
            int n2 = this.W[i3 - 3];
            int n3 = n2 << 15 | n2 >>> 17;
            int n4 = this.W[i3 - 13];
            int n5 = n4 << 7 | n4 >>> 25;
            this.W[i3] = n5 ^ this.P1(n3 ^ (this.W[i3 - 16] ^ this.W[i3 - 9])) ^ this.W[i3 - 6];
        }
        for (int i4 = 0; i4 < 64; ++i4) {
            this.W1[i4] = this.W[i4] ^ this.W[i4 + 4];
        }
        int n6 = this.V[0];
        int n7 = this.V[1];
        int n8 = this.V[2];
        int n9 = this.V[3];
        int n10 = this.V[4];
        int n11 = this.V[5];
        int n12 = this.V[6];
        int n13 = this.V[7];
        for (int i5 = 0; i5 < 16; ++i5) {
            int n14 = n6 << 12 | n6 >>> 20;
            int n15 = n14 + n10 + T[i5];
            int n16 = n15 << 7 | n15 >>> 25;
            int n17 = (n14 ^ n16) + (n9 + this.FF0(n6, n7, n8)) + this.W1[i5];
            int n18 = n16 + (n13 + this.GG0(n10, n11, n12)) + this.W[i5];
            int n19 = n7 << 9 | n7 >>> 23;
            int n20 = n11 << 19 | n11 >>> 13;
            int n21 = this.P0(n18);
            n7 = n6;
            n6 = n17;
            int n22 = n10;
            n10 = n21;
            n11 = n22;
            int n23 = n8;
            n8 = n19;
            n9 = n23;
            int n24 = n12;
            n12 = n20;
            n13 = n24;
        }
        for (int i6 = 16; i6 < 64; ++i6) {
            int n25 = n6 << 12 | n6 >>> 20;
            int n26 = n25 + n10 + T[i6];
            int n27 = n26 << 7 | n26 >>> 25;
            int n28 = (n25 ^ n27) + (n9 + this.FF1(n6, n7, n8)) + this.W1[i6];
            int n29 = n27 + (n13 + this.GG1(n10, n11, n12)) + this.W[i6];
            int n30 = n7 << 9 | n7 >>> 23;
            int n31 = n11 << 19 | n11 >>> 13;
            int n32 = this.P0(n29);
            n7 = n6;
            n6 = n28;
            int n33 = n10;
            n10 = n32;
            n11 = n33;
            int n34 = n8;
            n8 = n30;
            n9 = n34;
            int n35 = n12;
            n12 = n31;
            n13 = n35;
        }
        int[] arrn = this.V;
        arrn[0] = n6 ^ arrn[0];
        int[] arrn2 = this.V;
        arrn2[1] = n7 ^ arrn2[1];
        int[] arrn3 = this.V;
        arrn3[2] = n8 ^ arrn3[2];
        int[] arrn4 = this.V;
        arrn4[3] = n9 ^ arrn4[3];
        int[] arrn5 = this.V;
        arrn5[4] = n10 ^ arrn5[4];
        int[] arrn6 = this.V;
        arrn6[5] = n11 ^ arrn6[5];
        int[] arrn7 = this.V;
        arrn7[6] = n12 ^ arrn7[6];
        int[] arrn8 = this.V;
        arrn8[7] = n13 ^ arrn8[7];
        this.xOff = 0;
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.inwords[this.xOff] = 0;
            this.xOff = 1 + this.xOff;
            this.processBlock();
        }
        while (this.xOff < 14) {
            this.inwords[this.xOff] = 0;
            this.xOff = 1 + this.xOff;
        }
        int[] arrn = this.inwords;
        int n2 = this.xOff;
        this.xOff = n2 + 1;
        arrn[n2] = (int)(l2 >>> 32);
        int[] arrn2 = this.inwords;
        int n3 = this.xOff;
        this.xOff = n3 + 1;
        arrn2[n3] = (int)l2;
    }

    @Override
    protected void processWord(byte[] arrby, int n2) {
        int n3;
        int n4 = (255 & arrby[n2]) << 24;
        int n5 = n2 + 1;
        int n6 = n4 | (255 & arrby[n5]) << 16;
        int n7 = n5 + 1;
        this.inwords[this.xOff] = n3 = n6 | (255 & arrby[n7]) << 8 | 255 & arrby[n7 + 1];
        this.xOff = 1 + this.xOff;
        if (this.xOff >= 16) {
            this.processBlock();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.V[0] = 1937774191;
        this.V[1] = 1226093241;
        this.V[2] = 388252375;
        this.V[3] = -628488704;
        this.V[4] = -1452330820;
        this.V[5] = 372324522;
        this.V[6] = -477237683;
        this.V[7] = -1325724082;
        this.xOff = 0;
    }

    @Override
    public void reset(Memoable memoable) {
        SM3Digest sM3Digest = (SM3Digest)memoable;
        super.copyIn(sM3Digest);
        this.copyIn(sM3Digest);
    }
}

