package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;

public class MD4Digest extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private static final int S11 = 3;
    private static final int S12 = 7;
    private static final int S13 = 11;
    private static final int S14 = 19;
    private static final int S21 = 3;
    private static final int S22 = 5;
    private static final int S23 = 9;
    private static final int S24 = 13;
    private static final int S31 = 3;
    private static final int S32 = 9;
    private static final int S33 = 11;
    private static final int S34 = 15;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] f123X;
    private int xOff;

    public MD4Digest() {
        this.f123X = new int[DIGEST_LENGTH];
        reset();
    }

    public MD4Digest(MD4Digest mD4Digest) {
        super((GeneralDigest) mD4Digest);
        this.f123X = new int[DIGEST_LENGTH];
        copyIn(mD4Digest);
    }

    private int m1722F(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private int m1723G(int i, int i2, int i3) {
        return ((i & i2) | (i & i3)) | (i2 & i3);
    }

    private int m1724H(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private void copyIn(MD4Digest mD4Digest) {
        super.copyIn(mD4Digest);
        this.H1 = mD4Digest.H1;
        this.H2 = mD4Digest.H2;
        this.H3 = mD4Digest.H3;
        this.H4 = mD4Digest.H4;
        System.arraycopy(mD4Digest.f123X, 0, this.f123X, 0, mD4Digest.f123X.length);
        this.xOff = mD4Digest.xOff;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (32 - i2));
    }

    private void unpackWord(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + 1] = (byte) (i >>> 8);
        bArr[i2 + 2] = (byte) (i >>> DIGEST_LENGTH);
        bArr[i2 + S31] = (byte) (i >>> 24);
    }

    public Memoable copy() {
        return new MD4Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        unpackWord(this.H1, bArr, i);
        unpackWord(this.H2, bArr, i + 4);
        unpackWord(this.H3, bArr, i + 8);
        unpackWord(this.H4, bArr, i + 12);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "MD4";
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock() {
        int i = this.H1;
        int i2 = this.H2;
        int i3 = this.H3;
        int i4 = this.H4;
        i = rotateLeft((i + m1722F(i2, i3, i4)) + this.f123X[0], S31);
        i4 = rotateLeft((i4 + m1722F(i, i2, i3)) + this.f123X[1], S12);
        i3 = rotateLeft((i3 + m1722F(i4, i, i2)) + this.f123X[2], S33);
        i2 = rotateLeft((i2 + m1722F(i3, i4, i)) + this.f123X[S31], S14);
        i = rotateLeft((i + m1722F(i2, i3, i4)) + this.f123X[4], S31);
        i4 = rotateLeft((i4 + m1722F(i, i2, i3)) + this.f123X[S22], S12);
        i3 = rotateLeft((i3 + m1722F(i4, i, i2)) + this.f123X[6], S33);
        i2 = rotateLeft((i2 + m1722F(i3, i4, i)) + this.f123X[S12], S14);
        i = rotateLeft((i + m1722F(i2, i3, i4)) + this.f123X[8], S31);
        i4 = rotateLeft((i4 + m1722F(i, i2, i3)) + this.f123X[S32], S12);
        i3 = rotateLeft((i3 + m1722F(i4, i, i2)) + this.f123X[10], S33);
        i2 = rotateLeft((i2 + m1722F(i3, i4, i)) + this.f123X[S33], S14);
        i = rotateLeft((i + m1722F(i2, i3, i4)) + this.f123X[12], S31);
        i4 = rotateLeft((i4 + m1722F(i, i2, i3)) + this.f123X[S24], S12);
        i3 = rotateLeft((i3 + m1722F(i4, i, i2)) + this.f123X[14], S33);
        i2 = rotateLeft((i2 + m1722F(i3, i4, i)) + this.f123X[S34], S14);
        i = rotateLeft(((i + m1723G(i2, i3, i4)) + this.f123X[0]) + 1518500249, S31);
        i4 = rotateLeft(((i4 + m1723G(i, i2, i3)) + this.f123X[4]) + 1518500249, S22);
        i3 = rotateLeft(((i3 + m1723G(i4, i, i2)) + this.f123X[8]) + 1518500249, S32);
        i2 = rotateLeft(((i2 + m1723G(i3, i4, i)) + this.f123X[12]) + 1518500249, S24);
        i = rotateLeft(((i + m1723G(i2, i3, i4)) + this.f123X[1]) + 1518500249, S31);
        i4 = rotateLeft(((i4 + m1723G(i, i2, i3)) + this.f123X[S22]) + 1518500249, S22);
        i3 = rotateLeft(((i3 + m1723G(i4, i, i2)) + this.f123X[S32]) + 1518500249, S32);
        i2 = rotateLeft(((i2 + m1723G(i3, i4, i)) + this.f123X[S24]) + 1518500249, S24);
        i = rotateLeft(((i + m1723G(i2, i3, i4)) + this.f123X[2]) + 1518500249, S31);
        i4 = rotateLeft(((i4 + m1723G(i, i2, i3)) + this.f123X[6]) + 1518500249, S22);
        i3 = rotateLeft(((i3 + m1723G(i4, i, i2)) + this.f123X[10]) + 1518500249, S32);
        i2 = rotateLeft(((i2 + m1723G(i3, i4, i)) + this.f123X[14]) + 1518500249, S24);
        i = rotateLeft(((i + m1723G(i2, i3, i4)) + this.f123X[S31]) + 1518500249, S31);
        i4 = rotateLeft(((i4 + m1723G(i, i2, i3)) + this.f123X[S12]) + 1518500249, S22);
        i3 = rotateLeft(((i3 + m1723G(i4, i, i2)) + this.f123X[S33]) + 1518500249, S32);
        i2 = rotateLeft(((i2 + m1723G(i3, i4, i)) + this.f123X[S34]) + 1518500249, S24);
        i = rotateLeft(((i + m1724H(i2, i3, i4)) + this.f123X[0]) + 1859775393, S31);
        i4 = rotateLeft(((i4 + m1724H(i, i2, i3)) + this.f123X[8]) + 1859775393, S32);
        i3 = rotateLeft(((i3 + m1724H(i4, i, i2)) + this.f123X[4]) + 1859775393, S33);
        i2 = rotateLeft(((i2 + m1724H(i3, i4, i)) + this.f123X[12]) + 1859775393, S34);
        i = rotateLeft(((i + m1724H(i2, i3, i4)) + this.f123X[2]) + 1859775393, S31);
        i4 = rotateLeft(((i4 + m1724H(i, i2, i3)) + this.f123X[10]) + 1859775393, S32);
        i3 = rotateLeft(((i3 + m1724H(i4, i, i2)) + this.f123X[6]) + 1859775393, S33);
        i2 = rotateLeft(((i2 + m1724H(i3, i4, i)) + this.f123X[14]) + 1859775393, S34);
        i = rotateLeft(((i + m1724H(i2, i3, i4)) + this.f123X[1]) + 1859775393, S31);
        i4 = rotateLeft(((i4 + m1724H(i, i2, i3)) + this.f123X[S32]) + 1859775393, S32);
        i3 = rotateLeft(((i3 + m1724H(i4, i, i2)) + this.f123X[S22]) + 1859775393, S33);
        i2 = rotateLeft(((i2 + m1724H(i3, i4, i)) + this.f123X[S24]) + 1859775393, S34);
        i = rotateLeft(((i + m1724H(i2, i3, i4)) + this.f123X[S31]) + 1859775393, S31);
        i4 = rotateLeft(((i4 + m1724H(i, i2, i3)) + this.f123X[S33]) + 1859775393, S32);
        i3 = rotateLeft(((i3 + m1724H(i4, i, i2)) + this.f123X[S12]) + 1859775393, S33);
        i2 = rotateLeft(((i2 + m1724H(i3, i4, i)) + this.f123X[S34]) + 1859775393, S34);
        this.H1 = i + this.H1;
        this.H2 += i2;
        this.H3 += i3;
        this.H4 += i4;
        this.xOff = 0;
        for (i = 0; i != this.f123X.length; i++) {
            this.f123X[i] = 0;
        }
    }

    protected void processLength(long j) {
        if (this.xOff > 14) {
            processBlock();
        }
        this.f123X[14] = (int) (-1 & j);
        this.f123X[S34] = (int) (j >>> 32);
    }

    protected void processWord(byte[] bArr, int i) {
        int[] iArr = this.f123X;
        int i2 = this.xOff;
        this.xOff = i2 + 1;
        iArr[i2] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << DIGEST_LENGTH)) | ((bArr[i + S31] & GF2Field.MASK) << 24);
        if (this.xOff == DIGEST_LENGTH) {
            processBlock();
        }
    }

    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.xOff = 0;
        for (int i = 0; i != this.f123X.length; i++) {
            this.f123X[i] = 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((MD4Digest) memoable);
    }
}
