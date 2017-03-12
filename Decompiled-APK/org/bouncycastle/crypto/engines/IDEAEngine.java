package org.bouncycastle.crypto.engines;

import android.support.v4.view.MotionEventCompat;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class IDEAEngine implements BlockCipher {
    private static final int BASE = 65537;
    protected static final int BLOCK_SIZE = 8;
    private static final int MASK = 65535;
    private int[] workingKey;

    public IDEAEngine() {
        this.workingKey = null;
    }

    private int bytesToWord(byte[] bArr, int i) {
        return ((bArr[i] << BLOCK_SIZE) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) + (bArr[i + 1] & GF2Field.MASK);
    }

    private int[] expandKey(byte[] bArr) {
        int i;
        int[] iArr = new int[52];
        if (bArr.length < 16) {
            Object obj = new byte[16];
            System.arraycopy(bArr, 0, obj, obj.length - bArr.length, bArr.length);
            bArr = obj;
        }
        for (i = 0; i < BLOCK_SIZE; i++) {
            iArr[i] = bytesToWord(bArr, i * 2);
        }
        for (i = BLOCK_SIZE; i < 52; i++) {
            if ((i & 7) < 6) {
                iArr[i] = (((iArr[i - 7] & CertificateBody.profileType) << 9) | (iArr[i - 6] >> 7)) & MASK;
            } else if ((i & 7) == 6) {
                iArr[i] = (((iArr[i - 7] & CertificateBody.profileType) << 9) | (iArr[i - 14] >> 7)) & MASK;
            } else {
                iArr[i] = (((iArr[i - 15] & CertificateBody.profileType) << 9) | (iArr[i - 14] >> 7)) & MASK;
            }
        }
        return iArr;
    }

    private int[] generateWorkingKey(boolean z, byte[] bArr) {
        return z ? expandKey(bArr) : invertKey(expandKey(bArr));
    }

    private void ideaFunc(int[] iArr, byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        int bytesToWord = bytesToWord(bArr, i);
        int bytesToWord2 = bytesToWord(bArr, i + 2);
        int i4 = bytesToWord;
        bytesToWord = bytesToWord2;
        bytesToWord2 = bytesToWord(bArr, i + 4);
        int bytesToWord3 = bytesToWord(bArr, i + 6);
        int i5 = 0;
        while (i3 < BLOCK_SIZE) {
            int i6 = i5 + 1;
            i4 = mul(i4, iArr[i5]);
            i5 = i6 + 1;
            i6 = (bytesToWord + iArr[i6]) & MASK;
            bytesToWord = i5 + 1;
            bytesToWord2 = (iArr[i5] + bytesToWord2) & MASK;
            i5 = bytesToWord + 1;
            bytesToWord3 = mul(bytesToWord3, iArr[bytesToWord]);
            int i7 = i6 ^ bytesToWord3;
            int i8 = i5 + 1;
            bytesToWord = mul(bytesToWord2 ^ i4, iArr[i5]);
            i5 = i8 + 1;
            i7 = mul((i7 + bytesToWord) & MASK, iArr[i8]);
            i8 = (bytesToWord + i7) & MASK;
            i4 ^= i7;
            bytesToWord3 ^= i8;
            bytesToWord = i7 ^ bytesToWord2;
            bytesToWord2 = i8 ^ i6;
            i3++;
        }
        i3 = i5 + 1;
        wordToBytes(mul(i4, iArr[i5]), bArr2, i2);
        i5 = i3 + 1;
        wordToBytes(iArr[i3] + bytesToWord2, bArr2, i2 + 2);
        i3 = i5 + 1;
        wordToBytes(iArr[i5] + bytesToWord, bArr2, i2 + 4);
        wordToBytes(mul(bytesToWord3, iArr[i3]), bArr2, i2 + 6);
    }

    private int[] invertKey(int[] iArr) {
        int i;
        int i2 = 1;
        int[] iArr2 = new int[52];
        int mulInv = mulInv(iArr[0]);
        int addInv = addInv(iArr[1]);
        int addInv2 = addInv(iArr[2]);
        int i3 = 4;
        iArr2[51] = mulInv(iArr[3]);
        iArr2[50] = addInv2;
        iArr2[49] = addInv;
        addInv = 48;
        iArr2[48] = mulInv;
        while (i2 < BLOCK_SIZE) {
            mulInv = i3 + 1;
            i3 = iArr[i3];
            i = mulInv + 1;
            addInv--;
            iArr2[addInv] = iArr[mulInv];
            addInv--;
            iArr2[addInv] = i3;
            i3 = i + 1;
            mulInv = mulInv(iArr[i]);
            i = i3 + 1;
            addInv2 = addInv(iArr[i3]);
            int i4 = i + 1;
            i = addInv(iArr[i]);
            i3 = i4 + 1;
            addInv--;
            iArr2[addInv] = mulInv(iArr[i4]);
            addInv--;
            iArr2[addInv] = addInv2;
            addInv--;
            iArr2[addInv] = i;
            addInv--;
            iArr2[addInv] = mulInv;
            i2++;
        }
        i2 = i3 + 1;
        i3 = iArr[i3];
        mulInv = i2 + 1;
        addInv--;
        iArr2[addInv] = iArr[i2];
        i2 = addInv - 1;
        iArr2[i2] = i3;
        i3 = mulInv + 1;
        addInv = mulInv(iArr[mulInv]);
        mulInv = i3 + 1;
        i3 = addInv(iArr[i3]);
        i = mulInv + 1;
        mulInv = addInv(iArr[mulInv]);
        i2--;
        iArr2[i2] = mulInv(iArr[i]);
        i2--;
        iArr2[i2] = mulInv;
        i2--;
        iArr2[i2] = i3;
        iArr2[i2 - 1] = addInv;
        return iArr2;
    }

    private int mul(int i, int i2) {
        int i3;
        if (i == 0) {
            i3 = BASE - i2;
        } else if (i2 == 0) {
            i3 = BASE - i;
        } else {
            i3 = i * i2;
            int i4 = i3 & MASK;
            i3 >>>= 16;
            i3 = (i4 < i3 ? 1 : 0) + (i4 - i3);
        }
        return i3 & MASK;
    }

    private int mulInv(int i) {
        if (i < 2) {
            return i;
        }
        int i2 = BASE / i;
        int i3 = BASE % i;
        int i4 = i;
        i = 1;
        while (i3 != 1) {
            int i5 = i4 / i3;
            i4 %= i3;
            i = ((i5 * i2) + i) & MASK;
            if (i4 == 1) {
                return i;
            }
            i5 = i3 / i4;
            i3 %= i4;
            i2 = (i2 + (i5 * i)) & MASK;
        }
        return (1 - i2) & MASK;
    }

    private void wordToBytes(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >>> BLOCK_SIZE);
        bArr[i2 + 1] = (byte) i;
    }

    int addInv(int i) {
        return (0 - i) & MASK;
    }

    public String getAlgorithmName() {
        return "IDEA";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = generateWorkingKey(z, ((KeyParameter) cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to IDEA init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("IDEA engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            ideaFunc(this.workingKey, bArr, i, bArr2, i2);
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
