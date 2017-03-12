package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RC6Engine implements BlockCipher {
    private static final int LGW = 5;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private static final int _noRounds = 20;
    private static final int bytesPerWord = 4;
    private static final int wordSize = 32;
    private int[] _S;
    private boolean forEncryption;

    public RC6Engine() {
        this._S = null;
    }

    private int bytesToWord(byte[] bArr, int i) {
        int i2 = 0;
        for (int i3 = 3; i3 >= 0; i3--) {
            i2 = (i2 << 8) + (bArr[i3 + i] & GF2Field.MASK);
        }
        return i2;
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int bytesToWord = bytesToWord(bArr, i);
        int bytesToWord2 = bytesToWord(bArr, i + bytesPerWord);
        int bytesToWord3 = bytesToWord(bArr, i + 8);
        int bytesToWord4 = bytesToWord(bArr, i + 12);
        bytesToWord3 -= this._S[43];
        int i3 = bytesToWord - this._S[42];
        bytesToWord = _noRounds;
        while (bytesToWord >= 1) {
            int rotateLeft = rotateLeft(((i3 * 2) + 1) * i3, LGW);
            int rotateLeft2 = rotateLeft(((bytesToWord3 * 2) + 1) * bytesToWord3, LGW);
            bytesToWord2 = rotateRight(bytesToWord2 - this._S[(bytesToWord * 2) + 1], rotateLeft) ^ rotateLeft2;
            bytesToWord--;
            int i4 = bytesToWord3;
            bytesToWord3 = bytesToWord2;
            bytesToWord2 = i3;
            i3 = rotateRight(bytesToWord4 - this._S[bytesToWord * 2], rotateLeft2) ^ rotateLeft;
            bytesToWord4 = i4;
        }
        bytesToWord = bytesToWord4 - this._S[1];
        bytesToWord4 = bytesToWord2 - this._S[0];
        wordToBytes(i3, bArr2, i2);
        wordToBytes(bytesToWord4, bArr2, i2 + bytesPerWord);
        wordToBytes(bytesToWord3, bArr2, i2 + 8);
        wordToBytes(bytesToWord, bArr2, i2 + 12);
        return 16;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 1;
        int bytesToWord = bytesToWord(bArr, i);
        int bytesToWord2 = bytesToWord(bArr, i + bytesPerWord);
        int bytesToWord3 = bytesToWord(bArr, i + 8);
        int i4 = this._S[0] + bytesToWord2;
        bytesToWord2 = this._S[1] + bytesToWord(bArr, i + 12);
        int i5 = bytesToWord3;
        bytesToWord3 = bytesToWord;
        bytesToWord = i4;
        i4 = i5;
        while (i3 <= _noRounds) {
            int rotateLeft = rotateLeft(((bytesToWord * 2) + 1) * bytesToWord, LGW);
            int rotateLeft2 = rotateLeft(((bytesToWord2 * 2) + 1) * bytesToWord2, LGW);
            bytesToWord3 = rotateLeft(bytesToWord3 ^ rotateLeft, rotateLeft2) + this._S[i3 * 2];
            i3++;
            i5 = bytesToWord3;
            bytesToWord3 = bytesToWord;
            bytesToWord = rotateLeft(i4 ^ rotateLeft2, rotateLeft) + this._S[(i3 * 2) + 1];
            i4 = bytesToWord2;
            bytesToWord2 = i5;
        }
        i3 = this._S[42] + bytesToWord3;
        bytesToWord3 = this._S[43] + i4;
        wordToBytes(i3, bArr2, i2);
        wordToBytes(bytesToWord, bArr2, i2 + bytesPerWord);
        wordToBytes(bytesToWord3, bArr2, i2 + 8);
        wordToBytes(bytesToWord2, bArr2, i2 + 12);
        return 16;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    private int rotateRight(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private void setKey(byte[] bArr) {
        int[] iArr;
        int length;
        int i = 0;
        if ((bArr.length + 3) / bytesPerWord == 0) {
            iArr = new int[(((bArr.length + bytesPerWord) - 1) / bytesPerWord)];
        } else {
            iArr = new int[(((bArr.length + bytesPerWord) - 1) / bytesPerWord)];
        }
        for (length = bArr.length - 1; length >= 0; length--) {
            iArr[length / bytesPerWord] = (iArr[length / bytesPerWord] << 8) + (bArr[length] & GF2Field.MASK);
        }
        this._S = new int[44];
        this._S[0] = P32;
        for (length = 1; length < this._S.length; length++) {
            this._S[length] = this._S[length - 1] + Q32;
        }
        length = iArr.length > this._S.length ? iArr.length * 3 : this._S.length * 3;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i < length) {
            int[] iArr2 = this._S;
            i5 = rotateLeft((i5 + this._S[i3]) + i4, 3);
            iArr2[i3] = i5;
            i4 = rotateLeft((iArr[i2] + i5) + i4, i4 + i5);
            iArr[i2] = i4;
            i3 = (i3 + 1) % this._S.length;
            i2 = (i2 + 1) % iArr.length;
            i++;
        }
    }

    private void wordToBytes(int i, byte[] bArr, int i2) {
        for (int i3 = 0; i3 < bytesPerWord; i3++) {
            bArr[i3 + i2] = (byte) i;
            i >>>= 8;
        }
    }

    public String getAlgorithmName() {
        return "RC6";
    }

    public int getBlockSize() {
        return 16;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            KeyParameter keyParameter = (KeyParameter) cipherParameters;
            this.forEncryption = z;
            setKey(keyParameter.getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC6 init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int blockSize = getBlockSize();
        if (this._S == null) {
            throw new IllegalStateException("RC6 engine not initialised");
        } else if (i + blockSize > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (blockSize + i2 <= bArr2.length) {
            return this.forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
        } else {
            throw new OutputLengthException("output buffer too short");
        }
    }

    public void reset() {
    }
}
