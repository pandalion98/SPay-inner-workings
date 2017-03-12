package org.bouncycastle.crypto.digests;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;

public class MD5Digest extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] f124X;
    private int xOff;

    public MD5Digest() {
        this.f124X = new int[S33];
        reset();
    }

    public MD5Digest(MD5Digest mD5Digest) {
        super((GeneralDigest) mD5Digest);
        this.f124X = new int[S33];
        copyIn(mD5Digest);
    }

    private int m1725F(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private int m1726G(int i, int i2, int i3) {
        return (i & i3) | ((i3 ^ -1) & i2);
    }

    private int m1727H(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private int m1728K(int i, int i2, int i3) {
        return ((i3 ^ -1) | i) ^ i2;
    }

    private void copyIn(MD5Digest mD5Digest) {
        super.copyIn(mD5Digest);
        this.H1 = mD5Digest.H1;
        this.H2 = mD5Digest.H2;
        this.H3 = mD5Digest.H3;
        this.H4 = mD5Digest.H4;
        System.arraycopy(mD5Digest.f124X, 0, this.f124X, 0, mD5Digest.f124X.length);
        this.xOff = mD5Digest.xOff;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (32 - i2));
    }

    private void unpackWord(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + 1] = (byte) (i >>> 8);
        bArr[i2 + 2] = (byte) (i >>> S33);
        bArr[i2 + 3] = (byte) (i >>> 24);
    }

    public Memoable copy() {
        return new MD5Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        unpackWord(this.H1, bArr, i);
        unpackWord(this.H2, bArr, i + S31);
        unpackWord(this.H3, bArr, i + 8);
        unpackWord(this.H4, bArr, i + S12);
        reset();
        return S33;
    }

    public String getAlgorithmName() {
        return "MD5";
    }

    public int getDigestSize() {
        return S33;
    }

    protected void processBlock() {
        int i = this.H1;
        int i2 = this.H2;
        int i3 = this.H3;
        int i4 = this.H4;
        i = rotateLeft(((i + m1725F(i2, i3, i4)) + this.f124X[0]) - 680876936, S11) + i2;
        i4 = rotateLeft(((i4 + m1725F(i, i2, i3)) + this.f124X[1]) - 389564586, S12) + i;
        i3 = rotateLeft(((i3 + m1725F(i4, i, i2)) + this.f124X[2]) + 606105819, S13) + i4;
        i2 = rotateLeft(((i2 + m1725F(i3, i4, i)) + this.f124X[3]) - 1044525330, S14) + i3;
        i = rotateLeft(((i + m1725F(i2, i3, i4)) + this.f124X[S31]) - 176418897, S11) + i2;
        i4 = rotateLeft(((i4 + m1725F(i, i2, i3)) + this.f124X[S21]) + 1200080426, S12) + i;
        i3 = rotateLeft(((i3 + m1725F(i4, i, i2)) + this.f124X[S41]) - 1473231341, S13) + i4;
        i2 = rotateLeft(((i2 + m1725F(i3, i4, i)) + this.f124X[S11]) - 45705983, S14) + i3;
        i = rotateLeft(((i + m1725F(i2, i3, i4)) + this.f124X[8]) + 1770035416, S11) + i2;
        i4 = rotateLeft(((i4 + m1725F(i, i2, i3)) + this.f124X[S22]) - 1958414417, S12) + i;
        i3 = rotateLeft(((i3 + m1725F(i4, i, i2)) + this.f124X[S42]) - 42063, S13) + i4;
        i2 = rotateLeft(((i2 + m1725F(i3, i4, i)) + this.f124X[S32]) - 1990404162, S14) + i3;
        i = rotateLeft(((i + m1725F(i2, i3, i4)) + this.f124X[S12]) + 1804603682, S11) + i2;
        i4 = rotateLeft(((i4 + m1725F(i, i2, i3)) + this.f124X[13]) - 40341101, S12) + i;
        i3 = rotateLeft(((i3 + m1725F(i4, i, i2)) + this.f124X[S23]) - 1502002290, S13) + i4;
        i2 = rotateLeft(((i2 + m1725F(i3, i4, i)) + this.f124X[S43]) + 1236535329, S14) + i3;
        i = rotateLeft(((i + m1726G(i2, i3, i4)) + this.f124X[1]) - 165796510, S21) + i2;
        i4 = rotateLeft(((i4 + m1726G(i, i2, i3)) + this.f124X[S41]) - 1069501632, S22) + i;
        i3 = rotateLeft(((i3 + m1726G(i4, i, i2)) + this.f124X[S32]) + 643717713, S23) + i4;
        i2 = rotateLeft(((i2 + m1726G(i3, i4, i)) + this.f124X[0]) - 373897302, S24) + i3;
        i = rotateLeft(((i + m1726G(i2, i3, i4)) + this.f124X[S21]) - 701558691, S21) + i2;
        i4 = rotateLeft(((i4 + m1726G(i, i2, i3)) + this.f124X[S42]) + 38016083, S22) + i;
        i3 = rotateLeft(((i3 + m1726G(i4, i, i2)) + this.f124X[S43]) - 660478335, S23) + i4;
        i2 = rotateLeft(((i2 + m1726G(i3, i4, i)) + this.f124X[S31]) - 405537848, S24) + i3;
        i = rotateLeft(((i + m1726G(i2, i3, i4)) + this.f124X[S22]) + 568446438, S21) + i2;
        i4 = rotateLeft(((i4 + m1726G(i, i2, i3)) + this.f124X[S23]) - 1019803690, S22) + i;
        i3 = rotateLeft(((i3 + m1726G(i4, i, i2)) + this.f124X[3]) - 187363961, S23) + i4;
        i2 = rotateLeft(((i2 + m1726G(i3, i4, i)) + this.f124X[8]) + 1163531501, S24) + i3;
        i = rotateLeft(((i + m1726G(i2, i3, i4)) + this.f124X[13]) - 1444681467, S21) + i2;
        i4 = rotateLeft(((i4 + m1726G(i, i2, i3)) + this.f124X[2]) - 51403784, S22) + i;
        i3 = rotateLeft(((i3 + m1726G(i4, i, i2)) + this.f124X[S11]) + 1735328473, S23) + i4;
        i2 = rotateLeft(((i2 + m1726G(i3, i4, i)) + this.f124X[S12]) - 1926607734, S24) + i3;
        i = rotateLeft(((i + m1727H(i2, i3, i4)) + this.f124X[S21]) - 378558, S31) + i2;
        i4 = rotateLeft(((i4 + m1727H(i, i2, i3)) + this.f124X[8]) - 2022574463, S32) + i;
        i3 = rotateLeft(((i3 + m1727H(i4, i, i2)) + this.f124X[S32]) + 1839030562, S33) + i4;
        i2 = rotateLeft(((i2 + m1727H(i3, i4, i)) + this.f124X[S23]) - 35309556, S34) + i3;
        i = rotateLeft(((i + m1727H(i2, i3, i4)) + this.f124X[1]) - 1530992060, S31) + i2;
        i4 = rotateLeft(((i4 + m1727H(i, i2, i3)) + this.f124X[S31]) + 1272893353, S32) + i;
        i3 = rotateLeft(((i3 + m1727H(i4, i, i2)) + this.f124X[S11]) - 155497632, S33) + i4;
        i2 = rotateLeft(((i2 + m1727H(i3, i4, i)) + this.f124X[S42]) - 1094730640, S34) + i3;
        i = rotateLeft(((i + m1727H(i2, i3, i4)) + this.f124X[13]) + 681279174, S31) + i2;
        i4 = rotateLeft(((i4 + m1727H(i, i2, i3)) + this.f124X[0]) - 358537222, S32) + i;
        i3 = rotateLeft(((i3 + m1727H(i4, i, i2)) + this.f124X[3]) - 722521979, S33) + i4;
        i2 = rotateLeft(((i2 + m1727H(i3, i4, i)) + this.f124X[S41]) + 76029189, S34) + i3;
        i = rotateLeft(((i + m1727H(i2, i3, i4)) + this.f124X[S22]) - 640364487, S31) + i2;
        i4 = rotateLeft(((i4 + m1727H(i, i2, i3)) + this.f124X[S12]) - 421815835, S32) + i;
        i3 = rotateLeft(((i3 + m1727H(i4, i, i2)) + this.f124X[S43]) + 530742520, S33) + i4;
        i2 = rotateLeft(((i2 + m1727H(i3, i4, i)) + this.f124X[2]) - 995338651, S34) + i3;
        i = rotateLeft(((i + m1728K(i2, i3, i4)) + this.f124X[0]) - 198630844, S41) + i2;
        i4 = rotateLeft(((i4 + m1728K(i, i2, i3)) + this.f124X[S11]) + 1126891415, S42) + i;
        i3 = rotateLeft(((i3 + m1728K(i4, i, i2)) + this.f124X[S23]) - 1416354905, S43) + i4;
        i2 = rotateLeft(((i2 + m1728K(i3, i4, i)) + this.f124X[S21]) - 57434055, S44) + i3;
        i = rotateLeft(((i + m1728K(i2, i3, i4)) + this.f124X[S12]) + 1700485571, S41) + i2;
        i4 = rotateLeft(((i4 + m1728K(i, i2, i3)) + this.f124X[3]) - 1894986606, S42) + i;
        i3 = rotateLeft(((i3 + m1728K(i4, i, i2)) + this.f124X[S42]) - 1051523, S43) + i4;
        i2 = rotateLeft(((i2 + m1728K(i3, i4, i)) + this.f124X[1]) - 2054922799, S44) + i3;
        i = rotateLeft(((i + m1728K(i2, i3, i4)) + this.f124X[8]) + 1873313359, S41) + i2;
        i4 = rotateLeft(((i4 + m1728K(i, i2, i3)) + this.f124X[S43]) - 30611744, S42) + i;
        i3 = rotateLeft(((i3 + m1728K(i4, i, i2)) + this.f124X[S41]) - 1560198380, S43) + i4;
        i2 = rotateLeft(((i2 + m1728K(i3, i4, i)) + this.f124X[13]) + 1309151649, S44) + i3;
        i = rotateLeft(((i + m1728K(i2, i3, i4)) + this.f124X[S31]) - 145523070, S41) + i2;
        i4 = rotateLeft(((i4 + m1728K(i, i2, i3)) + this.f124X[S32]) - 1120210379, S42) + i;
        i3 = rotateLeft(((i3 + m1728K(i4, i, i2)) + this.f124X[2]) + 718787259, S43) + i4;
        i2 = rotateLeft(((i2 + m1728K(i3, i4, i)) + this.f124X[S22]) - 343485551, S44) + i3;
        this.H1 = i + this.H1;
        this.H2 += i2;
        this.H3 += i3;
        this.H4 += i4;
        this.xOff = 0;
        for (i = 0; i != this.f124X.length; i++) {
            this.f124X[i] = 0;
        }
    }

    protected void processLength(long j) {
        if (this.xOff > S23) {
            processBlock();
        }
        this.f124X[S23] = (int) (-1 & j);
        this.f124X[S43] = (int) (j >>> 32);
    }

    protected void processWord(byte[] bArr, int i) {
        int[] iArr = this.f124X;
        int i2 = this.xOff;
        this.xOff = i2 + 1;
        iArr[i2] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << S33)) | ((bArr[i + 3] & GF2Field.MASK) << 24);
        if (this.xOff == S33) {
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
        for (int i = 0; i != this.f124X.length; i++) {
            this.f124X[i] = 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((MD5Digest) memoable);
    }
}
