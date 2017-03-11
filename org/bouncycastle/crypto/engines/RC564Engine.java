package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.RC5Parameters;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RC564Engine implements BlockCipher {
    private static final long P64 = -5196783011329398165L;
    private static final long Q64 = -7046029254386353131L;
    private static final int bytesPerWord = 8;
    private static final int wordSize = 64;
    private long[] _S;
    private int _noRounds;
    private boolean forEncryption;

    public RC564Engine() {
        this._noRounds = 12;
        this._S = null;
    }

    private long bytesToWord(byte[] bArr, int i) {
        long j = 0;
        for (int i2 = 7; i2 >= 0; i2--) {
            j = (j << bytesPerWord) + ((long) (bArr[i2 + i] & GF2Field.MASK));
        }
        return j;
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        long bytesToWord = bytesToWord(bArr, i);
        long bytesToWord2 = bytesToWord(bArr, i + bytesPerWord);
        for (int i3 = this._noRounds; i3 >= 1; i3--) {
            bytesToWord2 = rotateRight(bytesToWord2 - this._S[(i3 * 2) + 1], bytesToWord) ^ bytesToWord;
            bytesToWord = rotateRight(bytesToWord - this._S[i3 * 2], bytesToWord2) ^ bytesToWord2;
        }
        wordToBytes(bytesToWord - this._S[0], bArr2, i2);
        wordToBytes(bytesToWord2 - this._S[1], bArr2, i2 + bytesPerWord);
        return 16;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 1;
        long bytesToWord = this._S[0] + bytesToWord(bArr, i);
        long bytesToWord2 = bytesToWord(bArr, i + bytesPerWord) + this._S[1];
        while (i3 <= this._noRounds) {
            bytesToWord = rotateLeft(bytesToWord ^ bytesToWord2, bytesToWord2) + this._S[i3 * 2];
            bytesToWord2 = rotateLeft(bytesToWord2 ^ bytesToWord, bytesToWord) + this._S[(i3 * 2) + 1];
            i3++;
        }
        wordToBytes(bytesToWord, bArr2, i2);
        wordToBytes(bytesToWord2, bArr2, i2 + bytesPerWord);
        return 16;
    }

    private long rotateLeft(long j, long j2) {
        return (j << ((int) (j2 & 63))) | (j >>> ((int) (64 - (63 & j2))));
    }

    private long rotateRight(long j, long j2) {
        return (j >>> ((int) (j2 & 63))) | (j << ((int) (64 - (63 & j2))));
    }

    private void setKey(byte[] bArr) {
        int i;
        int i2 = 0;
        long[] jArr = new long[((bArr.length + 7) / bytesPerWord)];
        for (i = 0; i != bArr.length; i++) {
            int i3 = i / bytesPerWord;
            jArr[i3] = jArr[i3] + (((long) (bArr[i] & GF2Field.MASK)) << ((i % bytesPerWord) * bytesPerWord));
        }
        this._S = new long[((this._noRounds + 1) * 2)];
        this._S[0] = P64;
        for (i = 1; i < this._S.length; i++) {
            this._S[i] = this._S[i - 1] + Q64;
        }
        i = jArr.length > this._S.length ? jArr.length * 3 : this._S.length * 3;
        long j = 0;
        long j2 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i2 < i) {
            long[] jArr2 = this._S;
            j2 = rotateLeft((j2 + this._S[i5]) + j, 3);
            jArr2[i5] = j2;
            j = rotateLeft((jArr[i4] + j2) + j, j + j2);
            jArr[i4] = j;
            i5 = (i5 + 1) % this._S.length;
            i4 = (i4 + 1) % jArr.length;
            i2++;
        }
    }

    private void wordToBytes(long j, byte[] bArr, int i) {
        for (int i2 = 0; i2 < bytesPerWord; i2++) {
            bArr[i2 + i] = (byte) ((int) j);
            j >>>= 8;
        }
    }

    public String getAlgorithmName() {
        return "RC5-64";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof RC5Parameters) {
            RC5Parameters rC5Parameters = (RC5Parameters) cipherParameters;
            this.forEncryption = z;
            this._noRounds = rC5Parameters.getRounds();
            setKey(rC5Parameters.getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC564 init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        return this.forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
    }

    public void reset() {
    }
}
