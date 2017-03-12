package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;

public class RIPEMD128Digest extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int[] f125X;
    private int xOff;

    public RIPEMD128Digest() {
        this.f125X = new int[DIGEST_LENGTH];
        reset();
    }

    public RIPEMD128Digest(RIPEMD128Digest rIPEMD128Digest) {
        super((GeneralDigest) rIPEMD128Digest);
        this.f125X = new int[DIGEST_LENGTH];
        copyIn(rIPEMD128Digest);
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

    private void copyIn(RIPEMD128Digest rIPEMD128Digest) {
        super.copyIn(rIPEMD128Digest);
        this.H0 = rIPEMD128Digest.H0;
        this.H1 = rIPEMD128Digest.H1;
        this.H2 = rIPEMD128Digest.H2;
        this.H3 = rIPEMD128Digest.H3;
        System.arraycopy(rIPEMD128Digest.f125X, 0, this.f125X, 0, rIPEMD128Digest.f125X.length);
        this.xOff = rIPEMD128Digest.xOff;
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
        bArr[i2 + 2] = (byte) (i >>> DIGEST_LENGTH);
        bArr[i2 + 3] = (byte) (i >>> 24);
    }

    public Memoable copy() {
        return new RIPEMD128Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        unpackWord(this.H0, bArr, i);
        unpackWord(this.H1, bArr, i + 4);
        unpackWord(this.H2, bArr, i + 8);
        unpackWord(this.H3, bArr, i + 12);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "RIPEMD128";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock() {
        int i = this.H0;
        int i2 = this.H1;
        int i3 = this.H2;
        int i4 = this.H3;
        int F1 = F1(i, i2, i3, i4, this.f125X[0], 11);
        int F12 = F1(i4, F1, i2, i3, this.f125X[1], 14);
        int F13 = F1(i3, F12, F1, i2, this.f125X[2], 15);
        int F14 = F1(i2, F13, F12, F1, this.f125X[3], 12);
        int F15 = F1(F1, F14, F13, F12, this.f125X[4], 5);
        int F16 = F1(F12, F15, F14, F13, this.f125X[5], 8);
        int F17 = F1(F13, F16, F15, F14, this.f125X[6], 7);
        int F18 = F1(F14, F17, F16, F15, this.f125X[7], 9);
        int F19 = F1(F15, F18, F17, F16, this.f125X[8], 11);
        F12 = F1(F16, F19, F18, F17, this.f125X[9], 13);
        F13 = F1(F17, F12, F19, F18, this.f125X[10], 14);
        int F110 = F1(F18, F13, F12, F19, this.f125X[11], 15);
        F15 = F1(F19, F110, F13, F12, this.f125X[12], 6);
        F16 = F1(F12, F15, F110, F13, this.f125X[13], 7);
        int F111 = F1(F13, F16, F15, F110, this.f125X[14], 9);
        F18 = F1(F110, F111, F16, F15, this.f125X[15], 8);
        F19 = F2(F15, F18, F111, F16, this.f125X[7], 7);
        int F2 = F2(F16, F19, F18, F111, this.f125X[4], 6);
        F13 = F2(F111, F2, F19, F18, this.f125X[13], 8);
        F110 = F2(F18, F13, F2, F19, this.f125X[1], 13);
        int F22 = F2(F19, F110, F13, F2, this.f125X[10], 11);
        F16 = F2(F2, F22, F110, F13, this.f125X[6], 9);
        F111 = F2(F13, F16, F22, F110, this.f125X[15], 7);
        int F23 = F2(F110, F111, F16, F22, this.f125X[3], 15);
        F19 = F2(F22, F23, F111, F16, this.f125X[12], 7);
        F2 = F2(F16, F19, F23, F111, this.f125X[0], 12);
        int F24 = F2(F111, F2, F19, F23, this.f125X[9], 15);
        F110 = F2(F23, F24, F2, F19, this.f125X[5], 9);
        F22 = F2(F19, F110, F24, F2, this.f125X[2], 11);
        int F25 = F2(F2, F22, F110, F24, this.f125X[14], 7);
        F111 = F2(F24, F25, F22, F110, this.f125X[11], 13);
        F23 = F2(F110, F111, F25, F22, this.f125X[8], 12);
        int F3 = F3(F22, F23, F111, F25, this.f125X[3], 11);
        F2 = F3(F25, F3, F23, F111, this.f125X[10], 13);
        F24 = F3(F111, F2, F3, F23, this.f125X[14], 6);
        int F32 = F3(F23, F24, F2, F3, this.f125X[4], 7);
        F22 = F3(F3, F32, F24, F2, this.f125X[9], 14);
        F25 = F3(F2, F22, F32, F24, this.f125X[15], 9);
        int F33 = F3(F24, F25, F22, F32, this.f125X[8], 13);
        F23 = F3(F32, F33, F25, F22, this.f125X[1], 15);
        F3 = F3(F22, F23, F33, F25, this.f125X[2], 14);
        int F34 = F3(F25, F3, F23, F33, this.f125X[7], 8);
        F24 = F3(F33, F34, F3, F23, this.f125X[0], 13);
        F32 = F3(F23, F24, F34, F3, this.f125X[6], 6);
        int F35 = F3(F3, F32, F24, F34, this.f125X[13], 5);
        F25 = F3(F34, F35, F32, F24, this.f125X[11], 12);
        F33 = F3(F24, F25, F35, F32, this.f125X[5], 7);
        int F36 = F3(F32, F33, F25, F35, this.f125X[12], 5);
        F3 = F4(F35, F36, F33, F25, this.f125X[1], 11);
        F34 = F4(F25, F3, F36, F33, this.f125X[9], 12);
        int F4 = F4(F33, F34, F3, F36, this.f125X[11], 14);
        F32 = F4(F36, F4, F34, F3, this.f125X[10], 15);
        F35 = F4(F3, F32, F4, F34, this.f125X[0], 14);
        int F42 = F4(F34, F35, F32, F4, this.f125X[8], 15);
        F33 = F4(F4, F42, F35, F32, this.f125X[12], 9);
        F36 = F4(F32, F33, F42, F35, this.f125X[4], 8);
        int F43 = F4(F35, F36, F33, F42, this.f125X[13], 9);
        F34 = F4(F42, F43, F36, F33, this.f125X[3], 14);
        F4 = F4(F33, F34, F43, F36, this.f125X[7], 5);
        int F44 = F4(F36, F4, F34, F43, this.f125X[15], 6);
        F35 = F4(F43, F44, F4, F34, this.f125X[14], 8);
        F42 = F4(F34, F35, F44, F4, this.f125X[5], 6);
        int F45 = F4(F4, F42, F35, F44, this.f125X[6], 5);
        F36 = F4(F44, F45, F42, F35, this.f125X[2], 12);
        F1 = FF4(i, i2, i3, i4, this.f125X[5], 8);
        int FF4 = FF4(i4, F1, i2, i3, this.f125X[14], 9);
        int FF42 = FF4(i3, FF4, F1, i2, this.f125X[7], 9);
        F12 = FF4(i2, FF42, FF4, F1, this.f125X[0], 11);
        F17 = FF4(F1, F12, FF42, FF4, this.f125X[9], 13);
        F14 = FF4(FF4, F17, F12, FF42, this.f125X[2], 15);
        i2 = FF4(FF42, F14, F17, F12, this.f125X[11], 15);
        F18 = FF4(F12, i2, F14, F17, this.f125X[4], 5);
        F15 = FF4(F17, F18, i2, F14, this.f125X[13], 7);
        F12 = FF4(F14, F15, F18, i2, this.f125X[6], 7);
        i2 = FF4(i2, F12, F15, F18, this.f125X[15], 8);
        FF42 = FF4(F18, i2, F12, F15, this.f125X[8], 11);
        F15 = FF4(F15, FF42, i2, F12, this.f125X[1], 14);
        i3 = FF4(F12, F15, FF42, i2, this.f125X[10], 14);
        FF4 = FF4(i2, i3, F15, FF42, this.f125X[3], 12);
        F18 = FF4(FF42, FF4, i3, F15, this.f125X[12], 6);
        i4 = FF3(F15, F18, FF4, i3, this.f125X[6], 9);
        F1 = FF3(i3, i4, F18, FF4, this.f125X[11], 13);
        F13 = FF3(FF4, F1, i4, F18, this.f125X[3], 15);
        FF42 = FF3(F18, F13, F1, i4, this.f125X[7], 7);
        F14 = FF3(i4, FF42, F13, F1, this.f125X[0], 12);
        F16 = FF3(F1, F14, FF42, F13, this.f125X[13], 8);
        FF4 = FF3(F13, F16, F14, FF42, this.f125X[5], 9);
        F17 = FF3(FF42, FF4, F16, F14, this.f125X[10], 11);
        F19 = FF3(F14, F17, FF4, F16, this.f125X[14], 7);
        F1 = FF3(F16, F19, F17, FF4, this.f125X[15], 7);
        F12 = FF3(FF4, F1, F19, F17, this.f125X[8], 12);
        F110 = FF3(F17, F12, F1, F19, this.f125X[12], 7);
        F14 = FF3(F19, F110, F12, F1, this.f125X[4], 6);
        F15 = FF3(F1, F14, F110, F12, this.f125X[9], 15);
        F111 = FF3(F12, F15, F14, F110, this.f125X[1], 13);
        F17 = FF3(F110, F111, F15, F14, this.f125X[2], 11);
        F18 = FF2(F14, F17, F111, F15, this.f125X[15], 9);
        F2 = FF2(F15, F18, F17, F111, this.f125X[5], 7);
        F12 = FF2(F111, F2, F18, F17, this.f125X[1], 15);
        F13 = FF2(F17, F12, F2, F18, this.f125X[3], 11);
        F22 = FF2(F18, F13, F12, F2, this.f125X[7], 8);
        F15 = FF2(F2, F22, F13, F12, this.f125X[14], 6);
        F16 = FF2(F12, F15, F22, F13, this.f125X[6], 6);
        F23 = FF2(F13, F16, F15, F22, this.f125X[9], 14);
        F18 = FF2(F22, F23, F16, F15, this.f125X[11], 12);
        F19 = FF2(F15, F18, F23, F16, this.f125X[8], 13);
        F24 = FF2(F16, F19, F18, F23, this.f125X[12], 5);
        F13 = FF2(F23, F24, F19, F18, this.f125X[2], 14);
        F110 = FF2(F18, F13, F24, F19, this.f125X[10], 13);
        F25 = FF2(F19, F110, F13, F24, this.f125X[0], 13);
        F16 = FF2(F24, F25, F110, F13, this.f125X[4], 7);
        F111 = FF2(F13, F16, F25, F110, this.f125X[13], 5);
        F18 = FF1(F110, F111, F16, F25, this.f125X[8], 15);
        F19 = FF1(F25, F18, F111, F16, this.f125X[6], 5);
        F2 = FF1(F16, F19, F18, F111, this.f125X[4], 8);
        F13 = FF1(F111, F2, F19, F18, this.f125X[1], 11);
        F110 = FF1(F18, F13, F2, F19, this.f125X[3], 14);
        F22 = FF1(F19, F110, F13, F2, this.f125X[11], 14);
        F16 = FF1(F2, F22, F110, F13, this.f125X[15], 6);
        F111 = FF1(F13, F16, F22, F110, this.f125X[0], 14);
        F23 = FF1(F110, F111, F16, F22, this.f125X[5], 6);
        F19 = FF1(F22, F23, F111, F16, this.f125X[12], 9);
        F2 = FF1(F16, F19, F23, F111, this.f125X[2], 12);
        F24 = FF1(F111, F2, F19, F23, this.f125X[13], 9);
        F110 = FF1(F23, F24, F2, F19, this.f125X[9], 12);
        F22 = FF1(F19, F110, F24, F2, this.f125X[7], 5);
        F25 = FF1(F2, F22, F110, F24, this.f125X[10], 15);
        int FF1 = FF1(F24, F25, F22, F110, this.f125X[14], 8);
        i = (this.H1 + F45) + F22;
        this.H1 = (this.H2 + F42) + F110;
        this.H2 = FF1 + (this.H3 + F35);
        this.H3 = (this.H0 + F36) + F25;
        this.H0 = i;
        this.xOff = 0;
        for (FF1 = 0; FF1 != this.f125X.length; FF1++) {
            this.f125X[FF1] = 0;
        }
    }

    protected void processLength(long j) {
        if (this.xOff > 14) {
            processBlock();
        }
        this.f125X[14] = (int) (-1 & j);
        this.f125X[15] = (int) (j >>> 32);
    }

    protected void processWord(byte[] bArr, int i) {
        int[] iArr = this.f125X;
        int i2 = this.xOff;
        this.xOff = i2 + 1;
        iArr[i2] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << DIGEST_LENGTH)) | ((bArr[i + 3] & GF2Field.MASK) << 24);
        if (this.xOff == DIGEST_LENGTH) {
            processBlock();
        }
    }

    public void reset() {
        super.reset();
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.xOff = 0;
        for (int i = 0; i != this.f125X.length; i++) {
            this.f125X[i] = 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((RIPEMD128Digest) memoable);
    }
}
