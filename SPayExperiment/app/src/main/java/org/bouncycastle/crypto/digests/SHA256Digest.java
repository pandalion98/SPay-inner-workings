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

public class SHA256Digest
extends GeneralDigest
implements EncodableDigest {
    private static final int DIGEST_LENGTH = 32;
    static final int[] K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int H8;
    private int[] X = new int[64];
    private int xOff;

    public SHA256Digest() {
        this.reset();
    }

    public SHA256Digest(SHA256Digest sHA256Digest) {
        super(sHA256Digest);
        this.copyIn(sHA256Digest);
    }

    public SHA256Digest(byte[] arrby) {
        super(arrby);
        this.H1 = Pack.bigEndianToInt((byte[])arrby, (int)16);
        this.H2 = Pack.bigEndianToInt((byte[])arrby, (int)20);
        this.H3 = Pack.bigEndianToInt((byte[])arrby, (int)24);
        this.H4 = Pack.bigEndianToInt((byte[])arrby, (int)28);
        this.H5 = Pack.bigEndianToInt((byte[])arrby, (int)32);
        this.H6 = Pack.bigEndianToInt((byte[])arrby, (int)36);
        this.H7 = Pack.bigEndianToInt((byte[])arrby, (int)40);
        this.H8 = Pack.bigEndianToInt((byte[])arrby, (int)44);
        this.xOff = Pack.bigEndianToInt((byte[])arrby, (int)48);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            this.X[i2] = Pack.bigEndianToInt((byte[])arrby, (int)(52 + i2 * 4));
        }
    }

    private int Ch(int n2, int n3, int n4) {
        return n2 & n3 ^ n4 & ~n2;
    }

    private int Maj(int n2, int n3, int n4) {
        return n2 & n3 ^ n2 & n4 ^ n3 & n4;
    }

    private int Sum0(int n2) {
        return (n2 >>> 2 | n2 << 30) ^ (n2 >>> 13 | n2 << 19) ^ (n2 >>> 22 | n2 << 10);
    }

    private int Sum1(int n2) {
        return (n2 >>> 6 | n2 << 26) ^ (n2 >>> 11 | n2 << 21) ^ (n2 >>> 25 | n2 << 7);
    }

    private int Theta0(int n2) {
        return (n2 >>> 7 | n2 << 25) ^ (n2 >>> 18 | n2 << 14) ^ n2 >>> 3;
    }

    private int Theta1(int n2) {
        return (n2 >>> 17 | n2 << 15) ^ (n2 >>> 19 | n2 << 13) ^ n2 >>> 10;
    }

    private void copyIn(SHA256Digest sHA256Digest) {
        super.copyIn(sHA256Digest);
        this.H1 = sHA256Digest.H1;
        this.H2 = sHA256Digest.H2;
        this.H3 = sHA256Digest.H3;
        this.H4 = sHA256Digest.H4;
        this.H5 = sHA256Digest.H5;
        this.H6 = sHA256Digest.H6;
        this.H7 = sHA256Digest.H7;
        this.H8 = sHA256Digest.H8;
        System.arraycopy((Object)sHA256Digest.X, (int)0, (Object)this.X, (int)0, (int)sHA256Digest.X.length);
        this.xOff = sHA256Digest.xOff;
    }

    @Override
    public Memoable copy() {
        return new SHA256Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        Pack.intToBigEndian((int)this.H1, (byte[])arrby, (int)n2);
        Pack.intToBigEndian((int)this.H2, (byte[])arrby, (int)(n2 + 4));
        Pack.intToBigEndian((int)this.H3, (byte[])arrby, (int)(n2 + 8));
        Pack.intToBigEndian((int)this.H4, (byte[])arrby, (int)(n2 + 12));
        Pack.intToBigEndian((int)this.H5, (byte[])arrby, (int)(n2 + 16));
        Pack.intToBigEndian((int)this.H6, (byte[])arrby, (int)(n2 + 20));
        Pack.intToBigEndian((int)this.H7, (byte[])arrby, (int)(n2 + 24));
        Pack.intToBigEndian((int)this.H8, (byte[])arrby, (int)(n2 + 28));
        this.reset();
        return 32;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-256";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public byte[] getEncodedState() {
        byte[] arrby = new byte[52 + 4 * this.xOff];
        super.populateState(arrby);
        Pack.intToBigEndian((int)this.H1, (byte[])arrby, (int)16);
        Pack.intToBigEndian((int)this.H2, (byte[])arrby, (int)20);
        Pack.intToBigEndian((int)this.H3, (byte[])arrby, (int)24);
        Pack.intToBigEndian((int)this.H4, (byte[])arrby, (int)28);
        Pack.intToBigEndian((int)this.H5, (byte[])arrby, (int)32);
        Pack.intToBigEndian((int)this.H6, (byte[])arrby, (int)36);
        Pack.intToBigEndian((int)this.H7, (byte[])arrby, (int)40);
        Pack.intToBigEndian((int)this.H8, (byte[])arrby, (int)44);
        Pack.intToBigEndian((int)this.xOff, (byte[])arrby, (int)48);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            Pack.intToBigEndian((int)this.X[i2], (byte[])arrby, (int)(52 + i2 * 4));
        }
        return arrby;
    }

    @Override
    protected void processBlock() {
        for (int i2 = 16; i2 <= 63; ++i2) {
            this.X[i2] = this.Theta1(this.X[i2 - 2]) + this.X[i2 - 7] + this.Theta0(this.X[i2 - 15]) + this.X[i2 - 16];
        }
        int n2 = this.H1;
        int n3 = this.H2;
        int n4 = this.H3;
        int n5 = this.H4;
        int n6 = this.H5;
        int n7 = this.H6;
        int n8 = this.H7;
        int n9 = this.H8;
        int n10 = n3;
        int n11 = n2;
        int n12 = n5;
        int n13 = n4;
        int n14 = n7;
        int n15 = n6;
        int n16 = n9;
        int n17 = n8;
        int n18 = 0;
        for (int i3 = 0; i3 < 8; ++i3) {
            int n19 = n16 + (this.Sum1(n15) + this.Ch(n15, n14, n17) + K[n18] + this.X[n18]);
            int n20 = n12 + n19;
            int n21 = n19 + (this.Sum0(n11) + this.Maj(n11, n10, n13));
            int n22 = n18 + 1;
            int n23 = n17 + (this.Sum1(n20) + this.Ch(n20, n15, n14) + K[n22] + this.X[n22]);
            int n24 = n13 + n23;
            int n25 = n23 + (this.Sum0(n21) + this.Maj(n21, n11, n10));
            int n26 = n22 + 1;
            int n27 = n14 + (this.Sum1(n24) + this.Ch(n24, n20, n15) + K[n26] + this.X[n26]);
            int n28 = n10 + n27;
            int n29 = n27 + (this.Sum0(n25) + this.Maj(n25, n21, n11));
            int n30 = n26 + 1;
            int n31 = n15 + (this.Sum1(n28) + this.Ch(n28, n24, n20) + K[n30] + this.X[n30]);
            int n32 = n11 + n31;
            int n33 = n31 + (this.Sum0(n29) + this.Maj(n29, n25, n21));
            int n34 = n30 + 1;
            int n35 = n20 + (this.Sum1(n32) + this.Ch(n32, n28, n24) + K[n34] + this.X[n34]);
            n16 = n21 + n35;
            n12 = n35 + (this.Sum0(n33) + this.Maj(n33, n29, n25));
            int n36 = n34 + 1;
            int n37 = n24 + (this.Sum1(n16) + this.Ch(n16, n32, n28) + K[n36] + this.X[n36]);
            n17 = n25 + n37;
            n13 = n37 + (this.Sum0(n12) + this.Maj(n12, n33, n29));
            int n38 = n36 + 1;
            int n39 = n28 + (this.Sum1(n17) + this.Ch(n17, n16, n32) + K[n38] + this.X[n38]);
            n14 = n29 + n39;
            n10 = n39 + (this.Sum0(n13) + this.Maj(n13, n12, n33));
            int n40 = n38 + 1;
            int n41 = n32 + (this.Sum1(n14) + this.Ch(n14, n17, n16) + K[n40] + this.X[n40]);
            n15 = n33 + n41;
            n11 = n41 + (this.Sum0(n10) + this.Maj(n10, n13, n12));
            n18 = n40 + 1;
        }
        this.H1 = n11 + this.H1;
        this.H2 = n10 + this.H2;
        this.H3 = n13 + this.H3;
        this.H4 = n12 + this.H4;
        this.H5 = n15 + this.H5;
        this.H6 = n14 + this.H6;
        this.H7 = n17 + this.H7;
        this.H8 = n16 + this.H8;
        this.xOff = 0;
        for (int i4 = 0; i4 < 16; ++i4) {
            this.X[i4] = 0;
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
        this.H1 = 1779033703;
        this.H2 = -1150833019;
        this.H3 = 1013904242;
        this.H4 = -1521486534;
        this.H5 = 1359893119;
        this.H6 = -1694144372;
        this.H7 = 528734635;
        this.H8 = 1541459225;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((SHA256Digest)memoable);
    }
}

