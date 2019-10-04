/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;

public class NullEngine
implements BlockCipher {
    protected static final int DEFAULT_BLOCK_SIZE = 1;
    private final int blockSize;
    private boolean initialised;

    public NullEngine() {
        this(1);
    }

    public NullEngine(int n2) {
        this.blockSize = n2;
    }

    @Override
    public String getAlgorithmName() {
        return "Null";
    }

    @Override
    public int getBlockSize() {
        return this.blockSize;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.initialised = true;
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        }
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            arrby2[n3 + i2] = arrby[n2 + i2];
        }
        return this.blockSize;
    }

    @Override
    public void reset() {
    }
}

