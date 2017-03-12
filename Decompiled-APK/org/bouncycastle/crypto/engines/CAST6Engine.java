package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.tls.CipherSuite;

public final class CAST6Engine extends CAST5Engine {
    protected static final int BLOCK_SIZE = 16;
    protected static final int ROUNDS = 12;
    protected int[] _Km;
    protected int[] _Kr;
    protected int[] _Tm;
    protected int[] _Tr;
    private int[] _workingKey;

    public CAST6Engine() {
        this._Kr = new int[48];
        this._Km = new int[48];
        this._Tr = new int[CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256];
        this._Tm = new int[CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256];
        this._workingKey = new int[8];
    }

    protected final void CAST_Decipher(int i, int i2, int i3, int i4, int[] iArr) {
        int i5;
        int i6 = i4;
        int i7 = i3;
        int i8 = i2;
        int i9 = i;
        for (i5 = 0; i5 < 6; i5++) {
            int i10 = (11 - i5) * 4;
            i7 ^= F1(i6, this._Km[i10], this._Kr[i10]);
            i8 ^= F2(i7, this._Km[i10 + 1], this._Kr[i10 + 1]);
            i9 ^= F3(i8, this._Km[i10 + 2], this._Kr[i10 + 2]);
            i6 ^= F1(i9, this._Km[i10 + 3], this._Kr[i10 + 3]);
        }
        for (i5 = 6; i5 < ROUNDS; i5++) {
            i10 = (11 - i5) * 4;
            i6 ^= F1(i9, this._Km[i10 + 3], this._Kr[i10 + 3]);
            i9 ^= F3(i8, this._Km[i10 + 2], this._Kr[i10 + 2]);
            i8 ^= F2(i7, this._Km[i10 + 1], this._Kr[i10 + 1]);
            i7 ^= F1(i6, this._Km[i10], this._Kr[i10]);
        }
        iArr[0] = i9;
        iArr[1] = i8;
        iArr[2] = i7;
        iArr[3] = i6;
    }

    protected final void CAST_Encipher(int i, int i2, int i3, int i4, int[] iArr) {
        int i5;
        int i6 = i4;
        int i7 = i3;
        int i8 = i2;
        int i9 = i;
        for (i5 = 0; i5 < 6; i5++) {
            int i10 = i5 * 4;
            i7 ^= F1(i6, this._Km[i10], this._Kr[i10]);
            i8 ^= F2(i7, this._Km[i10 + 1], this._Kr[i10 + 1]);
            i9 ^= F3(i8, this._Km[i10 + 2], this._Kr[i10 + 2]);
            i6 ^= F1(i9, this._Km[i10 + 3], this._Kr[i10 + 3]);
        }
        for (i5 = 6; i5 < ROUNDS; i5++) {
            i10 = i5 * 4;
            i6 ^= F1(i9, this._Km[i10 + 3], this._Kr[i10 + 3]);
            i9 ^= F3(i8, this._Km[i10 + 2], this._Kr[i10 + 2]);
            i8 ^= F2(i7, this._Km[i10 + 1], this._Kr[i10 + 1]);
            i7 ^= F1(i6, this._Km[i10], this._Kr[i10]);
        }
        iArr[0] = i9;
        iArr[1] = i8;
        iArr[2] = i7;
        iArr[3] = i6;
    }

    protected int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr = new int[4];
        CAST_Decipher(BytesTo32bits(bArr, i), BytesTo32bits(bArr, i + 4), BytesTo32bits(bArr, i + 8), BytesTo32bits(bArr, i + ROUNDS), iArr);
        Bits32ToBytes(iArr[0], bArr2, i2);
        Bits32ToBytes(iArr[1], bArr2, i2 + 4);
        Bits32ToBytes(iArr[2], bArr2, i2 + 8);
        Bits32ToBytes(iArr[3], bArr2, i2 + ROUNDS);
        return BLOCK_SIZE;
    }

    protected int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr = new int[4];
        CAST_Encipher(BytesTo32bits(bArr, i), BytesTo32bits(bArr, i + 4), BytesTo32bits(bArr, i + 8), BytesTo32bits(bArr, i + ROUNDS), iArr);
        Bits32ToBytes(iArr[0], bArr2, i2);
        Bits32ToBytes(iArr[1], bArr2, i2 + 4);
        Bits32ToBytes(iArr[2], bArr2, i2 + 8);
        Bits32ToBytes(iArr[3], bArr2, i2 + ROUNDS);
        return BLOCK_SIZE;
    }

    public String getAlgorithmName() {
        return "CAST6";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void reset() {
    }

    protected void setKey(byte[] bArr) {
        int i = 1518500249;
        int i2 = 19;
        int i3 = 0;
        while (i3 < 24) {
            int i4 = i;
            i = i2;
            for (i2 = 0; i2 < 8; i2++) {
                this._Tm[(i3 * 8) + i2] = i4;
                i4 += 1859775393;
                this._Tr[(i3 * 8) + i2] = i;
                i = (i + 17) & 31;
            }
            i3++;
            i2 = i;
            i = i4;
        }
        Object obj = new byte[64];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        for (i2 = 0; i2 < 8; i2++) {
            this._workingKey[i2] = BytesTo32bits(obj, i2 * 4);
        }
        for (i2 = 0; i2 < ROUNDS; i2++) {
            i = (i2 * 2) * 8;
            int[] iArr = this._workingKey;
            iArr[6] = iArr[6] ^ F1(this._workingKey[7], this._Tm[i], this._Tr[i]);
            iArr = this._workingKey;
            iArr[5] = iArr[5] ^ F2(this._workingKey[6], this._Tm[i + 1], this._Tr[i + 1]);
            iArr = this._workingKey;
            iArr[4] = iArr[4] ^ F3(this._workingKey[5], this._Tm[i + 2], this._Tr[i + 2]);
            iArr = this._workingKey;
            iArr[3] = iArr[3] ^ F1(this._workingKey[4], this._Tm[i + 3], this._Tr[i + 3]);
            iArr = this._workingKey;
            iArr[2] = iArr[2] ^ F2(this._workingKey[3], this._Tm[i + 4], this._Tr[i + 4]);
            iArr = this._workingKey;
            iArr[1] = iArr[1] ^ F3(this._workingKey[2], this._Tm[i + 5], this._Tr[i + 5]);
            iArr = this._workingKey;
            iArr[0] = iArr[0] ^ F1(this._workingKey[1], this._Tm[i + 6], this._Tr[i + 6]);
            iArr = this._workingKey;
            iArr[7] = F2(this._workingKey[0], this._Tm[i + 7], this._Tr[i + 7]) ^ iArr[7];
            i = ((i2 * 2) + 1) * 8;
            iArr = this._workingKey;
            iArr[6] = iArr[6] ^ F1(this._workingKey[7], this._Tm[i], this._Tr[i]);
            iArr = this._workingKey;
            iArr[5] = iArr[5] ^ F2(this._workingKey[6], this._Tm[i + 1], this._Tr[i + 1]);
            iArr = this._workingKey;
            iArr[4] = iArr[4] ^ F3(this._workingKey[5], this._Tm[i + 2], this._Tr[i + 2]);
            iArr = this._workingKey;
            iArr[3] = iArr[3] ^ F1(this._workingKey[4], this._Tm[i + 3], this._Tr[i + 3]);
            iArr = this._workingKey;
            iArr[2] = iArr[2] ^ F2(this._workingKey[3], this._Tm[i + 4], this._Tr[i + 4]);
            iArr = this._workingKey;
            iArr[1] = iArr[1] ^ F3(this._workingKey[2], this._Tm[i + 5], this._Tr[i + 5]);
            iArr = this._workingKey;
            iArr[0] = iArr[0] ^ F1(this._workingKey[1], this._Tm[i + 6], this._Tr[i + 6]);
            iArr = this._workingKey;
            iArr[7] = F2(this._workingKey[0], this._Tm[i + 7], this._Tr[i + 7]) ^ iArr[7];
            this._Kr[i2 * 4] = this._workingKey[0] & 31;
            this._Kr[(i2 * 4) + 1] = this._workingKey[2] & 31;
            this._Kr[(i2 * 4) + 2] = this._workingKey[4] & 31;
            this._Kr[(i2 * 4) + 3] = this._workingKey[6] & 31;
            this._Km[i2 * 4] = this._workingKey[7];
            this._Km[(i2 * 4) + 1] = this._workingKey[5];
            this._Km[(i2 * 4) + 2] = this._workingKey[3];
            this._Km[(i2 * 4) + 3] = this._workingKey[1];
        }
    }
}
