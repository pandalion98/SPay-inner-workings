package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SM3Digest extends GeneralDigest {
    private static final int BLOCK_SIZE = 16;
    private static final int DIGEST_LENGTH = 32;
    private static final int[] f135T;
    private int[] f136V;
    private int[] f137W;
    private int[] W1;
    private int[] inwords;
    private int xOff;

    static {
        int i;
        int i2 = BLOCK_SIZE;
        f135T = new int[64];
        for (i = 0; i < BLOCK_SIZE; i++) {
            f135T[i] = (2043430169 >>> (32 - i)) | (2043430169 << i);
        }
        while (i2 < 64) {
            i = i2 % DIGEST_LENGTH;
            f135T[i2] = (2055708042 >>> (32 - i)) | (2055708042 << i);
            i2++;
        }
    }

    public SM3Digest() {
        this.f136V = new int[8];
        this.inwords = new int[BLOCK_SIZE];
        this.f137W = new int[68];
        this.W1 = new int[64];
        reset();
    }

    public SM3Digest(SM3Digest sM3Digest) {
        super((GeneralDigest) sM3Digest);
        this.f136V = new int[8];
        this.inwords = new int[BLOCK_SIZE];
        this.f137W = new int[68];
        this.W1 = new int[64];
        copyIn(sM3Digest);
    }

    private int FF0(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private int FF1(int i, int i2, int i3) {
        return ((i & i2) | (i & i3)) | (i2 & i3);
    }

    private int GG0(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private int GG1(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private int P0(int i) {
        return (((i << 9) | (i >>> 23)) ^ i) ^ ((i << 17) | (i >>> 15));
    }

    private int P1(int i) {
        return (((i << 15) | (i >>> 17)) ^ i) ^ ((i << 23) | (i >>> 9));
    }

    private void copyIn(SM3Digest sM3Digest) {
        System.arraycopy(sM3Digest.f136V, 0, this.f136V, 0, this.f136V.length);
        System.arraycopy(sM3Digest.inwords, 0, this.inwords, 0, this.inwords.length);
        this.xOff = sM3Digest.xOff;
    }

    public Memoable copy() {
        return new SM3Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        Pack.intToBigEndian(this.f136V[0], bArr, i + 0);
        Pack.intToBigEndian(this.f136V[1], bArr, i + 4);
        Pack.intToBigEndian(this.f136V[2], bArr, i + 8);
        Pack.intToBigEndian(this.f136V[3], bArr, i + 12);
        Pack.intToBigEndian(this.f136V[4], bArr, i + BLOCK_SIZE);
        Pack.intToBigEndian(this.f136V[5], bArr, i + 20);
        Pack.intToBigEndian(this.f136V[6], bArr, i + 24);
        Pack.intToBigEndian(this.f136V[7], bArr, i + 28);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "SM3";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock() {
        int i;
        int i2;
        int i3;
        for (i = 0; i < BLOCK_SIZE; i++) {
            this.f137W[i] = this.inwords[i];
        }
        for (i = BLOCK_SIZE; i < 68; i++) {
            i2 = this.f137W[i - 3];
            i2 = (i2 >>> 17) | (i2 << 15);
            i3 = this.f137W[i - 13];
            this.f137W[i] = (P1(i2 ^ (this.f137W[i - 16] ^ this.f137W[i - 9])) ^ ((i3 >>> 25) | (i3 << 7))) ^ this.f137W[i - 6];
        }
        for (i = 0; i < 64; i++) {
            this.W1[i] = this.f137W[i] ^ this.f137W[i + 4];
        }
        int i4 = this.f136V[0];
        int i5 = this.f136V[1];
        int i6 = this.f136V[2];
        int i7 = this.f136V[3];
        int i8 = this.f136V[4];
        int i9 = this.f136V[5];
        i3 = this.f136V[6];
        i2 = this.f136V[7];
        i = 0;
        while (i < BLOCK_SIZE) {
            int i10 = (i4 << 12) | (i4 >>> 20);
            int i11 = (i10 + i8) + f135T[i];
            i11 = (i11 >>> 25) | (i11 << 7);
            i10 = this.W1[i] + ((i7 + FF0(i4, i5, i6)) + (i10 ^ i11));
            i11 = ((i2 + GG0(i8, i9, i3)) + i11) + this.f137W[i];
            i7 = (i5 >>> 23) | (i5 << 9);
            i2 = (i9 << 19) | (i9 >>> 13);
            i++;
            i5 = i4;
            i4 = i10;
            int i12 = i8;
            i8 = P0(i11);
            i9 = i12;
            int i13 = i6;
            i6 = i7;
            i7 = i13;
            int i14 = i3;
            i3 = i2;
            i2 = i14;
        }
        i = BLOCK_SIZE;
        while (i < 64) {
            i10 = (i4 << 12) | (i4 >>> 20);
            i11 = (i10 + i8) + f135T[i];
            i11 = (i11 >>> 25) | (i11 << 7);
            i10 = this.W1[i] + ((i7 + FF1(i4, i5, i6)) + (i10 ^ i11));
            i11 = ((i2 + GG1(i8, i9, i3)) + i11) + this.f137W[i];
            i7 = (i5 >>> 23) | (i5 << 9);
            i2 = (i9 << 19) | (i9 >>> 13);
            i++;
            i5 = i4;
            i4 = i10;
            i12 = i8;
            i8 = P0(i11);
            i9 = i12;
            i13 = i6;
            i6 = i7;
            i7 = i13;
            i14 = i3;
            i3 = i2;
            i2 = i14;
        }
        int[] iArr = this.f136V;
        iArr[0] = i4 ^ iArr[0];
        iArr = this.f136V;
        iArr[1] = i5 ^ iArr[1];
        iArr = this.f136V;
        iArr[2] = i6 ^ iArr[2];
        iArr = this.f136V;
        iArr[3] = i7 ^ iArr[3];
        iArr = this.f136V;
        iArr[4] = i8 ^ iArr[4];
        iArr = this.f136V;
        iArr[5] = i9 ^ iArr[5];
        iArr = this.f136V;
        iArr[6] = i3 ^ iArr[6];
        iArr = this.f136V;
        iArr[7] = i2 ^ iArr[7];
        this.xOff = 0;
    }

    protected void processLength(long j) {
        if (this.xOff > 14) {
            this.inwords[this.xOff] = 0;
            this.xOff++;
            processBlock();
        }
        while (this.xOff < 14) {
            this.inwords[this.xOff] = 0;
            this.xOff++;
        }
        int[] iArr = this.inwords;
        int i = this.xOff;
        this.xOff = i + 1;
        iArr[i] = (int) (j >>> DIGEST_LENGTH);
        iArr = this.inwords;
        i = this.xOff;
        this.xOff = i + 1;
        iArr[i] = (int) j;
    }

    protected void processWord(byte[] bArr, int i) {
        int i2 = i + 1;
        i2++;
        this.inwords[this.xOff] = ((((bArr[i] & GF2Field.MASK) << 24) | ((bArr[i2] & GF2Field.MASK) << BLOCK_SIZE)) | ((bArr[i2] & GF2Field.MASK) << 8)) | (bArr[i2 + 1] & GF2Field.MASK);
        this.xOff++;
        if (this.xOff >= BLOCK_SIZE) {
            processBlock();
        }
    }

    public void reset() {
        super.reset();
        this.f136V[0] = 1937774191;
        this.f136V[1] = 1226093241;
        this.f136V[2] = 388252375;
        this.f136V[3] = -628488704;
        this.f136V[4] = -1452330820;
        this.f136V[5] = 372324522;
        this.f136V[6] = -477237683;
        this.f136V[7] = -1325724082;
        this.xOff = 0;
    }

    public void reset(Memoable memoable) {
        SM3Digest sM3Digest = (SM3Digest) memoable;
        super.copyIn(sM3Digest);
        copyIn(sM3Digest);
    }
}
