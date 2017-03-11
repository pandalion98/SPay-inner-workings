package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;

public class NullEngine implements BlockCipher {
    protected static final int DEFAULT_BLOCK_SIZE = 1;
    private final int blockSize;
    private boolean initialised;

    public NullEngine() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public NullEngine(int i) {
        this.blockSize = i;
    }

    public String getAlgorithmName() {
        return "Null";
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.initialised = true;
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        } else if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            for (int i3 = 0; i3 < this.blockSize; i3 += DEFAULT_BLOCK_SIZE) {
                bArr2[i2 + i3] = bArr[i + i3];
            }
            return this.blockSize;
        }
    }

    public void reset() {
    }
}
