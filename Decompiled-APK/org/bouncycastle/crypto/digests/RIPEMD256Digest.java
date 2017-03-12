package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;

public class RIPEMD256Digest extends GeneralDigest {
    private static final int DIGEST_LENGTH = 32;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int[] f127X;
    private int xOff;

    public RIPEMD256Digest() {
        this.f127X = new int[16];
        reset();
    }

    public RIPEMD256Digest(RIPEMD256Digest rIPEMD256Digest) {
        super((GeneralDigest) rIPEMD256Digest);
        this.f127X = new int[16];
        copyIn(rIPEMD256Digest);
    }

    private int F1(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL((f1(i2, i3, i4) + i) + i5, i6);
    }

    private int F2(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f2(i2, i3, i4) + i) + i5) + 1518500249, i6);
    }

    private int F3(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f3(i2, i3, i4) + i) + i5) + 1859775393, i6);
    }

    private int F4(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f4(i2, i3, i4) + i) + i5) - 1894007588, i6);
    }

    private int FF1(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL((f1(i2, i3, i4) + i) + i5, i6);
    }

    private int FF2(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f2(i2, i3, i4) + i) + i5) + 1836072691, i6);
    }

    private int FF3(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f3(i2, i3, i4) + i) + i5) + 1548603684, i6);
    }

    private int FF4(int i, int i2, int i3, int i4, int i5, int i6) {
        return RL(((f4(i2, i3, i4) + i) + i5) + 1352829926, i6);
    }

    private int RL(int i, int i2) {
        return (i << i2) | (i >>> (32 - i2));
    }

    private void copyIn(RIPEMD256Digest rIPEMD256Digest) {
        super.copyIn(rIPEMD256Digest);
        this.H0 = rIPEMD256Digest.H0;
        this.H1 = rIPEMD256Digest.H1;
        this.H2 = rIPEMD256Digest.H2;
        this.H3 = rIPEMD256Digest.H3;
        this.H4 = rIPEMD256Digest.H4;
        this.H5 = rIPEMD256Digest.H5;
        this.H6 = rIPEMD256Digest.H6;
        this.H7 = rIPEMD256Digest.H7;
        System.arraycopy(rIPEMD256Digest.f127X, 0, this.f127X, 0, rIPEMD256Digest.f127X.length);
        this.xOff = rIPEMD256Digest.xOff;
    }

    private int f1(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private int f2(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private int f3(int i, int i2, int i3) {
        return ((i2 ^ -1) | i) ^ i3;
    }

    private int f4(int i, int i2, int i3) {
        return (i & i3) | ((i3 ^ -1) & i2);
    }

    private void unpackWord(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + 1] = (byte) (i >>> 8);
        bArr[i2 + 2] = (byte) (i >>> 16);
        bArr[i2 + 3] = (byte) (i >>> 24);
    }

    public Memoable copy() {
        return new RIPEMD256Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        unpackWord(this.H0, bArr, i);
        unpackWord(this.H1, bArr, i + 4);
        unpackWord(this.H2, bArr, i + 8);
        unpackWord(this.H3, bArr, i + 12);
        unpackWord(this.H4, bArr, i + 16);
        unpackWord(this.H5, bArr, i + 20);
        unpackWord(this.H6, bArr, i + 24);
        unpackWord(this.H7, bArr, i + 28);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "RIPEMD256";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock() {
        int i = this.H0;
        int i2 = this.H1;
        int i3 = this.H2;
        int i4 = this.H3;
        int i5 = this.H4;
        int i6 = this.H5;
        int i7 = this.H6;
        int i8 = this.H7;
        int F1 = F1(i, i2, i3, i4, this.f127X[0], 11);
        int F12 = F1(i4, F1, i2, i3, this.f127X[1], 14);
        int F13 = F1(i3, F12, F1, i2, this.f127X[2], 15);
        int F14 = F1(i2, F13, F12, F1, this.f127X[3], 12);
        int F15 = F1(F1, F14, F13, F12, this.f127X[4], 5);
        int F16 = F1(F12, F15, F14, F13, this.f127X[5], 8);
        i2 = F1(F13, F16, F15, F14, this.f127X[6], 7);
        int F17 = F1(F14, i2, F16, F15, this.f127X[7], 9);
        int F18 = F1(F15, F17, i2, F16, this.f127X[8], 11);
        F14 = F1(F16, F18, F17, i2, this.f127X[9], 13);
        i2 = F1(i2, F14, F18, F17, this.f127X[10], 14);
        F13 = F1(F17, i2, F14, F18, this.f127X[11], 15);
        F18 = F1(F18, F13, i2, F14, this.f127X[12], 6);
        i3 = F1(F14, F18, F13, i2, this.f127X[13], 7);
        F12 = F1(i2, i3, F18, F13, this.f127X[14], 9);
        F13 = F1(F13, F12, i3, F18, this.f127X[15], 8);
        int FF4 = FF4(i5, i6, i7, i8, this.f127X[5], 8);
        int FF42 = FF4(i8, FF4, i6, i7, this.f127X[14], 9);
        i5 = FF4(i7, FF42, FF4, i6, this.f127X[7], 9);
        int FF43 = FF4(i6, i5, FF42, FF4, this.f127X[0], 11);
        int FF44 = FF4(FF4, FF43, i5, FF42, this.f127X[9], 13);
        i8 = FF4(FF42, FF44, FF43, i5, this.f127X[2], 15);
        int FF45 = FF4(i5, i8, FF44, FF43, this.f127X[11], 15);
        i6 = FF4(FF43, FF45, i8, FF44, this.f127X[4], 5);
        i7 = FF4(FF44, i6, FF45, i8, this.f127X[13], 7);
        FF42 = FF4(i8, i7, i6, FF45, this.f127X[6], 7);
        i5 = FF4(FF45, FF42, i7, i6, this.f127X[15], 8);
        int FF46 = FF4(i6, i5, FF42, i7, this.f127X[8], 11);
        FF44 = FF4(i7, FF46, i5, FF42, this.f127X[1], 14);
        i8 = FF4(FF42, FF44, FF46, i5, this.f127X[10], 14);
        int FF47 = FF4(i5, i8, FF44, FF46, this.f127X[3], 12);
        FF46 = FF4(FF46, FF47, i8, FF44, this.f127X[12], 6);
        i4 = F2(FF44, F13, F12, i3, this.f127X[7], 7);
        FF4 = F2(i3, i4, F13, F12, this.f127X[4], 6);
        int F2 = F2(F12, FF4, i4, F13, this.f127X[13], 8);
        F13 = F2(F13, F2, FF4, i4, this.f127X[1], 13);
        FF43 = F2(i4, F13, F2, FF4, this.f127X[10], 11);
        int F22 = F2(FF4, FF43, F13, F2, this.f127X[6], 9);
        F12 = F2(F2, F22, FF43, F13, this.f127X[15], 7);
        FF45 = F2(F13, F12, F22, FF43, this.f127X[3], 15);
        int F23 = F2(FF43, FF45, F12, F22, this.f127X[12], 7);
        F22 = F2(F22, F23, FF45, F12, this.f127X[0], 12);
        F2 = F2(F12, F22, F23, FF45, this.f127X[9], 15);
        i2 = F2(FF45, F2, F22, F23, this.f127X[5], 9);
        F13 = F2(F23, i2, F2, F22, this.f127X[2], 11);
        F22 = F2(F22, F13, i2, F2, this.f127X[14], 7);
        i3 = F2(F2, F22, F13, i2, this.f127X[11], 13);
        int F24 = F2(i2, i3, F22, F13, this.f127X[8], 12);
        i7 = FF3(F18, FF46, FF47, i8, this.f127X[6], 9);
        F16 = FF3(i8, i7, FF46, FF47, this.f127X[11], 13);
        F18 = FF3(FF47, F16, i7, FF46, this.f127X[3], 15);
        FF46 = FF3(FF46, F18, F16, i7, this.f127X[7], 7);
        F15 = FF3(i7, FF46, F18, F16, this.f127X[0], 12);
        F17 = FF3(F16, F15, FF46, F18, this.f127X[13], 8);
        FF43 = FF3(F18, F17, F15, FF46, this.f127X[5], 9);
        F14 = FF3(FF46, FF43, F17, F15, this.f127X[10], 11);
        int FF3 = FF3(F15, F14, FF43, F17, this.f127X[14], 7);
        FF45 = FF3(F17, FF3, F14, FF43, this.f127X[15], 7);
        F18 = FF3(FF43, FF45, FF3, F14, this.f127X[8], 12);
        FF4 = FF3(F14, F18, FF45, FF3, this.f127X[12], 7);
        FF42 = FF3(FF3, FF4, F18, FF45, this.f127X[4], 6);
        F17 = FF3(FF45, FF42, FF4, F18, this.f127X[9], 15);
        FF43 = FF3(F18, F17, FF42, FF4, this.f127X[1], 13);
        F12 = FF3(FF4, FF43, F17, FF42, this.f127X[2], 11);
        F23 = F3(F13, F12, i3, F22, this.f127X[3], 11);
        i4 = F3(F22, F23, F12, i3, this.f127X[10], 13);
        F1 = F3(i3, i4, F23, F12, this.f127X[14], 6);
        int F3 = F3(F12, F1, i4, F23, this.f127X[4], 7);
        F13 = F3(F23, F3, F1, i4, this.f127X[9], 14);
        F22 = F3(i4, F13, F3, F1, this.f127X[15], 9);
        int F32 = F3(F1, F22, F13, F3, this.f127X[8], 13);
        F12 = F3(F3, F32, F22, F13, this.f127X[1], 15);
        F23 = F3(F13, F12, F32, F22, this.f127X[2], 14);
        int F33 = F3(F22, F23, F12, F32, this.f127X[7], 8);
        F1 = F3(F32, F33, F23, F12, this.f127X[0], 13);
        F3 = F3(F12, F1, F33, F23, this.f127X[6], 6);
        int F34 = F3(F23, F3, F1, F33, this.f127X[13], 5);
        F22 = F3(F33, F34, F3, F1, this.f127X[11], 12);
        F32 = F3(F1, F22, F34, F3, this.f127X[5], 7);
        int F35 = F3(F3, F32, F22, F34, this.f127X[12], 5);
        FF3 = FF2(FF42, F24, FF43, F17, this.f127X[15], 9);
        FF45 = FF2(F17, FF3, F24, FF43, this.f127X[5], 7);
        F18 = FF2(FF43, FF45, FF3, F24, this.f127X[1], 15);
        FF4 = FF2(F24, F18, FF45, FF3, this.f127X[3], 11);
        FF42 = FF2(FF3, FF4, F18, FF45, this.f127X[7], 8);
        F17 = FF2(FF45, FF42, FF4, F18, this.f127X[14], 6);
        FF43 = FF2(F18, F17, FF42, FF4, this.f127X[6], 6);
        FF44 = FF2(FF4, FF43, F17, FF42, this.f127X[9], 14);
        FF3 = FF2(FF42, FF44, FF43, F17, this.f127X[11], 12);
        FF45 = FF2(F17, FF3, FF44, FF43, this.f127X[8], 13);
        i6 = FF2(FF43, FF45, FF3, FF44, this.f127X[12], 5);
        FF4 = FF2(FF44, i6, FF45, FF3, this.f127X[2], 14);
        FF42 = FF2(FF3, FF4, i6, FF45, this.f127X[10], 13);
        i5 = FF2(FF45, FF42, FF4, i6, this.f127X[0], 13);
        FF43 = FF2(i6, i5, FF42, FF4, this.f127X[4], 7);
        FF44 = FF2(FF4, FF43, i5, FF42, this.f127X[13], 5);
        i2 = F4(F34, F35, FF43, F22, this.f127X[1], 11);
        F13 = F4(F22, i2, F35, FF43, this.f127X[9], 12);
        int F4 = F4(FF43, F13, i2, F35, this.f127X[11], 14);
        i3 = F4(F35, F4, F13, i2, this.f127X[10], 15);
        F12 = F4(i2, i3, F4, F13, this.f127X[0], 14);
        int F42 = F4(F13, F12, i3, F4, this.f127X[8], 15);
        i4 = F4(F4, F42, F12, i3, this.f127X[12], 9);
        F1 = F4(i3, i4, F42, F12, this.f127X[4], 8);
        int F43 = F4(F12, F1, i4, F42, this.f127X[13], 9);
        F13 = F4(F42, F43, F1, i4, this.f127X[3], 14);
        F16 = F4(i4, F13, F43, F1, this.f127X[7], 5);
        int F44 = F4(F1, F16, F13, F43, this.f127X[15], 6);
        F12 = F4(F43, F44, F16, F13, this.f127X[14], 8);
        F15 = F4(F13, F12, F44, F16, this.f127X[5], 6);
        int F45 = F4(F16, F15, F12, F44, this.f127X[6], 5);
        F24 = F4(F44, F45, F15, F12, this.f127X[2], 12);
        i8 = FF1(FF42, FF44, F32, i5, this.f127X[8], 15);
        F33 = FF1(i5, i8, FF44, F32, this.f127X[6], 5);
        i6 = FF1(F32, F33, i8, FF44, this.f127X[4], 8);
        i7 = FF1(FF44, i6, F33, i8, this.f127X[1], 11);
        F34 = FF1(i8, i7, i6, F33, this.f127X[3], 14);
        i5 = FF1(F33, F34, i7, i6, this.f127X[11], 14);
        FF46 = FF1(i6, i5, F34, i7, this.f127X[15], 6);
        F35 = FF1(i7, FF46, i5, F34, this.f127X[0], 14);
        i8 = FF1(F34, F35, FF46, i5, this.f127X[5], 6);
        FF47 = FF1(i5, i8, F35, FF46, this.f127X[12], 9);
        i6 = FF1(FF46, FF47, i8, F35, this.f127X[2], 12);
        i7 = FF1(F35, i6, FF47, i8, this.f127X[13], 9);
        int FF1 = FF1(i8, i7, i6, FF47, this.f127X[9], 12);
        i5 = FF1(FF47, FF1, i7, i6, this.f127X[7], 5);
        FF46 = FF1(i6, i5, FF1, i7, this.f127X[10], 15);
        i = FF1(i7, FF46, i5, FF1, this.f127X[14], 8);
        this.H0 += F12;
        this.H1 = F24 + this.H1;
        this.H2 += F45;
        this.H3 += i5;
        this.H4 += FF1;
        this.H5 += i;
        this.H6 += FF46;
        this.H7 += F15;
        this.xOff = 0;
        for (F24 = 0; F24 != this.f127X.length; F24++) {
            this.f127X[F24] = 0;
        }
    }

    protected void processLength(long j) {
        if (this.xOff > 14) {
            processBlock();
        }
        this.f127X[14] = (int) (-1 & j);
        this.f127X[15] = (int) (j >>> DIGEST_LENGTH);
    }

    protected void processWord(byte[] bArr, int i) {
        int[] iArr = this.f127X;
        int i2 = this.xOff;
        this.xOff = i2 + 1;
        iArr[i2] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << 16)) | ((bArr[i + 3] & GF2Field.MASK) << 24);
        if (this.xOff == 16) {
            processBlock();
        }
    }

    public void reset() {
        super.reset();
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.H4 = 1985229328;
        this.H5 = -19088744;
        this.H6 = -1985229329;
        this.H7 = 19088743;
        this.xOff = 0;
        for (int i = 0; i != this.f127X.length; i++) {
            this.f127X[i] = 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((RIPEMD256Digest) memoable);
    }
}
