package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SHA1Digest extends GeneralDigest implements EncodableDigest {
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
    private int[] f129X;
    private int xOff;

    public SHA1Digest() {
        this.f129X = new int[80];
        reset();
    }

    public SHA1Digest(SHA1Digest sHA1Digest) {
        super((GeneralDigest) sHA1Digest);
        this.f129X = new int[80];
        copyIn(sHA1Digest);
    }

    public SHA1Digest(byte[] bArr) {
        super(bArr);
        this.f129X = new int[80];
        this.H1 = Pack.bigEndianToInt(bArr, 16);
        this.H2 = Pack.bigEndianToInt(bArr, DIGEST_LENGTH);
        this.H3 = Pack.bigEndianToInt(bArr, 24);
        this.H4 = Pack.bigEndianToInt(bArr, 28);
        this.H5 = Pack.bigEndianToInt(bArr, 32);
        this.xOff = Pack.bigEndianToInt(bArr, 36);
        for (int i = 0; i != this.xOff; i++) {
            this.f129X[i] = Pack.bigEndianToInt(bArr, (i * 4) + 40);
        }
    }

    private void copyIn(SHA1Digest sHA1Digest) {
        this.H1 = sHA1Digest.H1;
        this.H2 = sHA1Digest.H2;
        this.H3 = sHA1Digest.H3;
        this.H4 = sHA1Digest.H4;
        this.H5 = sHA1Digest.H5;
        System.arraycopy(sHA1Digest.f129X, 0, this.f129X, 0, sHA1Digest.f129X.length);
        this.xOff = sHA1Digest.xOff;
    }

    private int m1729f(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private int m1730g(int i, int i2, int i3) {
        return ((i & i2) | (i & i3)) | (i2 & i3);
    }

    private int m1731h(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    public Memoable copy() {
        return new SHA1Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        Pack.intToBigEndian(this.H1, bArr, i);
        Pack.intToBigEndian(this.H2, bArr, i + 4);
        Pack.intToBigEndian(this.H3, bArr, i + 8);
        Pack.intToBigEndian(this.H4, bArr, i + 12);
        Pack.intToBigEndian(this.H5, bArr, i + 16);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "SHA-1";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    public byte[] getEncodedState() {
        byte[] bArr = new byte[((this.xOff * 4) + 40)];
        super.populateState(bArr);
        Pack.intToBigEndian(this.H1, bArr, 16);
        Pack.intToBigEndian(this.H2, bArr, (int) DIGEST_LENGTH);
        Pack.intToBigEndian(this.H3, bArr, 24);
        Pack.intToBigEndian(this.H4, bArr, 28);
        Pack.intToBigEndian(this.H5, bArr, 32);
        Pack.intToBigEndian(this.xOff, bArr, 36);
        for (int i = 0; i != this.xOff; i++) {
            Pack.intToBigEndian(this.f129X[i], bArr, (i * 4) + 40);
        }
        return bArr;
    }

    protected void processBlock() {
        int i;
        int i2;
        for (i = 16; i < 80; i++) {
            i2 = ((this.f129X[i - 3] ^ this.f129X[i - 8]) ^ this.f129X[i - 14]) ^ this.f129X[i - 16];
            this.f129X[i] = (i2 >>> 31) | (i2 << 1);
        }
        int i3 = this.H1;
        int i4 = this.H2;
        int i5 = this.H3;
        int i6 = this.H4;
        int i7 = this.H5;
        i2 = 0;
        for (i = 0; i < 4; i++) {
            int i8 = i2 + 1;
            i2 = ((this.f129X[i2] + (((i3 << 5) | (i3 >>> 27)) + m1729f(i4, i5, i6))) + Y1) + i7;
            i4 = (i4 >>> 2) | (i4 << 30);
            int i9 = i8 + 1;
            i6 += ((((i2 << 5) | (i2 >>> 27)) + m1729f(i3, i4, i5)) + this.f129X[i8]) + Y1;
            i3 = (i3 >>> 2) | (i3 << 30);
            i8 = i9 + 1;
            i5 += ((((i6 << 5) | (i6 >>> 27)) + m1729f(i2, i3, i4)) + this.f129X[i9]) + Y1;
            i7 = (i2 << 30) | (i2 >>> 2);
            i9 = i8 + 1;
            i4 += ((((i5 << 5) | (i5 >>> 27)) + m1729f(i6, i7, i3)) + this.f129X[i8]) + Y1;
            i6 = (i6 >>> 2) | (i6 << 30);
            int f = m1729f(i5, i6, i7) + ((i4 << 5) | (i4 >>> 27));
            i2 = i9 + 1;
            i3 += (f + this.f129X[i9]) + Y1;
            i5 = (i5 >>> 2) | (i5 << 30);
        }
        for (i = 0; i < 4; i++) {
            i8 = i2 + 1;
            i2 = ((this.f129X[i2] + (((i3 << 5) | (i3 >>> 27)) + m1731h(i4, i5, i6))) + Y2) + i7;
            i4 = (i4 >>> 2) | (i4 << 30);
            i9 = i8 + 1;
            i6 += ((((i2 << 5) | (i2 >>> 27)) + m1731h(i3, i4, i5)) + this.f129X[i8]) + Y2;
            i3 = (i3 >>> 2) | (i3 << 30);
            i8 = i9 + 1;
            i5 += ((((i6 << 5) | (i6 >>> 27)) + m1731h(i2, i3, i4)) + this.f129X[i9]) + Y2;
            i7 = (i2 << 30) | (i2 >>> 2);
            i9 = i8 + 1;
            i4 += ((((i5 << 5) | (i5 >>> 27)) + m1731h(i6, i7, i3)) + this.f129X[i8]) + Y2;
            i6 = (i6 >>> 2) | (i6 << 30);
            f = m1731h(i5, i6, i7) + ((i4 << 5) | (i4 >>> 27));
            i2 = i9 + 1;
            i3 += (f + this.f129X[i9]) + Y2;
            i5 = (i5 >>> 2) | (i5 << 30);
        }
        for (i = 0; i < 4; i++) {
            i8 = i2 + 1;
            i2 = ((this.f129X[i2] + (((i3 << 5) | (i3 >>> 27)) + m1730g(i4, i5, i6))) + Y3) + i7;
            i4 = (i4 >>> 2) | (i4 << 30);
            i9 = i8 + 1;
            i6 += ((((i2 << 5) | (i2 >>> 27)) + m1730g(i3, i4, i5)) + this.f129X[i8]) + Y3;
            i3 = (i3 >>> 2) | (i3 << 30);
            i8 = i9 + 1;
            i5 += ((((i6 << 5) | (i6 >>> 27)) + m1730g(i2, i3, i4)) + this.f129X[i9]) + Y3;
            i7 = (i2 << 30) | (i2 >>> 2);
            i9 = i8 + 1;
            i4 += ((((i5 << 5) | (i5 >>> 27)) + m1730g(i6, i7, i3)) + this.f129X[i8]) + Y3;
            i6 = (i6 >>> 2) | (i6 << 30);
            f = m1730g(i5, i6, i7) + ((i4 << 5) | (i4 >>> 27));
            i2 = i9 + 1;
            i3 += (f + this.f129X[i9]) + Y3;
            i5 = (i5 >>> 2) | (i5 << 30);
        }
        for (i = 0; i <= 3; i++) {
            i8 = i2 + 1;
            i2 = ((this.f129X[i2] + (((i3 << 5) | (i3 >>> 27)) + m1731h(i4, i5, i6))) + Y4) + i7;
            i4 = (i4 >>> 2) | (i4 << 30);
            i9 = i8 + 1;
            i6 += ((((i2 << 5) | (i2 >>> 27)) + m1731h(i3, i4, i5)) + this.f129X[i8]) + Y4;
            i3 = (i3 >>> 2) | (i3 << 30);
            i8 = i9 + 1;
            i5 += ((((i6 << 5) | (i6 >>> 27)) + m1731h(i2, i3, i4)) + this.f129X[i9]) + Y4;
            i7 = (i2 << 30) | (i2 >>> 2);
            i9 = i8 + 1;
            i4 += ((((i5 << 5) | (i5 >>> 27)) + m1731h(i6, i7, i3)) + this.f129X[i8]) + Y4;
            i6 = (i6 >>> 2) | (i6 << 30);
            f = m1731h(i5, i6, i7) + ((i4 << 5) | (i4 >>> 27));
            i2 = i9 + 1;
            i3 += (f + this.f129X[i9]) + Y4;
            i5 = (i5 >>> 2) | (i5 << 30);
        }
        this.H1 += i3;
        this.H2 += i4;
        this.H3 += i5;
        this.H4 += i6;
        this.H5 += i7;
        this.xOff = 0;
        for (i = 0; i < 16; i++) {
            this.f129X[i] = 0;
        }
    }

    protected void processLength(long j) {
        if (this.xOff > 14) {
            processBlock();
        }
        this.f129X[14] = (int) (j >>> 32);
        this.f129X[15] = (int) (-1 & j);
    }

    protected void processWord(byte[] bArr, int i) {
        int i2 = i + 1;
        i2++;
        this.f129X[this.xOff] = (((bArr[i] << 24) | ((bArr[i2] & GF2Field.MASK) << 16)) | ((bArr[i2] & GF2Field.MASK) << 8)) | (bArr[i2 + 1] & GF2Field.MASK);
        int i3 = this.xOff + 1;
        this.xOff = i3;
        if (i3 == 16) {
            processBlock();
        }
    }

    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.H5 = -1009589776;
        this.xOff = 0;
        for (int i = 0; i != this.f129X.length; i++) {
            this.f129X[i] = 0;
        }
    }

    public void reset(Memoable memoable) {
        SHA1Digest sHA1Digest = (SHA1Digest) memoable;
        super.copyIn(sHA1Digest);
        copyIn(sHA1Digest);
    }
}
