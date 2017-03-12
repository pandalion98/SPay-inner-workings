package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class TEAEngine implements BlockCipher {
    private static final int block_size = 8;
    private static final int d_sum = -957401312;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private int _a;
    private int _b;
    private int _c;
    private int _d;
    private boolean _forEncryption;
    private boolean _initialised;

    public TEAEngine() {
        this._initialised = false;
    }

    private int bytesToInt(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((((bArr[i2] & GF2Field.MASK) << 16) | (bArr[i] << 24)) | ((bArr[i3] & GF2Field.MASK) << block_size)) | (bArr[i3 + 1] & GF2Field.MASK);
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int bytesToInt = bytesToInt(bArr, i);
        int bytesToInt2 = bytesToInt(bArr, i + 4);
        int i3 = d_sum;
        for (int i4 = 0; i4 != rounds; i4++) {
            bytesToInt2 -= (((bytesToInt << 4) + this._c) ^ (bytesToInt + i3)) ^ ((bytesToInt >>> 5) + this._d);
            bytesToInt -= (((bytesToInt2 << 4) + this._a) ^ (bytesToInt2 + i3)) ^ ((bytesToInt2 >>> 5) + this._b);
            i3 += 1640531527;
        }
        unpackInt(bytesToInt, bArr2, i2);
        unpackInt(bytesToInt2, bArr2, i2 + 4);
        return block_size;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        int bytesToInt = bytesToInt(bArr, i);
        int bytesToInt2 = bytesToInt(bArr, i + 4);
        int i4 = 0;
        while (i3 != rounds) {
            i4 -= 1640531527;
            bytesToInt += (((bytesToInt2 << 4) + this._a) ^ (bytesToInt2 + i4)) ^ ((bytesToInt2 >>> 5) + this._b);
            bytesToInt2 += (((bytesToInt << 4) + this._c) ^ (bytesToInt + i4)) ^ ((bytesToInt >>> 5) + this._d);
            i3++;
        }
        unpackInt(bytesToInt, bArr2, i2);
        unpackInt(bytesToInt2, bArr2, i2 + 4);
        return block_size;
    }

    private void setKey(byte[] bArr) {
        if (bArr.length != 16) {
            throw new IllegalArgumentException("Key size must be 128 bits.");
        }
        this._a = bytesToInt(bArr, 0);
        this._b = bytesToInt(bArr, 4);
        this._c = bytesToInt(bArr, block_size);
        this._d = bytesToInt(bArr, 12);
    }

    private void unpackInt(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        i3 = i4 + 1;
        bArr[i4] = (byte) (i >>> block_size);
        bArr[i3] = (byte) i;
    }

    public String getAlgorithmName() {
        return "TEA";
    }

    public int getBlockSize() {
        return block_size;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this._forEncryption = z;
            this._initialised = true;
            setKey(((KeyParameter) cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to TEA init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (!this._initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        } else if (i + block_size > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + block_size <= bArr2.length) {
            return this._forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
        } else {
            throw new OutputLengthException("output buffer too short");
        }
    }

    public void reset() {
    }
}
