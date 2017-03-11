package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class DESedeEngine extends DESEngine {
    protected static final int BLOCK_SIZE = 8;
    private boolean forEncryption;
    private int[] workingKey1;
    private int[] workingKey2;
    private int[] workingKey3;

    public DESedeEngine() {
        this.workingKey1 = null;
        this.workingKey2 = null;
        this.workingKey3 = null;
    }

    public String getAlgorithmName() {
        return "DESede";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            Object key = ((KeyParameter) cipherParameters).getKey();
            if (key.length == 24 || key.length == 16) {
                this.forEncryption = z;
                Object obj = new byte[BLOCK_SIZE];
                System.arraycopy(key, 0, obj, 0, obj.length);
                this.workingKey1 = generateWorkingKey(z, obj);
                Object obj2 = new byte[BLOCK_SIZE];
                System.arraycopy(key, BLOCK_SIZE, obj2, 0, obj2.length);
                this.workingKey2 = generateWorkingKey(!z, obj2);
                if (key.length == 24) {
                    obj = new byte[BLOCK_SIZE];
                    System.arraycopy(key, 16, obj, 0, obj.length);
                    this.workingKey3 = generateWorkingKey(z, obj);
                    return;
                }
                this.workingKey3 = this.workingKey1;
                return;
            }
            throw new IllegalArgumentException("key size must be 16 or 24 bytes.");
        }
        throw new IllegalArgumentException("invalid parameter passed to DESede init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey1 == null) {
            throw new IllegalStateException("DESede engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            byte[] bArr3 = new byte[BLOCK_SIZE];
            if (this.forEncryption) {
                desFunc(this.workingKey1, bArr, i, bArr3, 0);
                desFunc(this.workingKey2, bArr3, 0, bArr3, 0);
                desFunc(this.workingKey3, bArr3, 0, bArr2, i2);
            } else {
                desFunc(this.workingKey3, bArr, i, bArr3, 0);
                desFunc(this.workingKey2, bArr3, 0, bArr3, 0);
                desFunc(this.workingKey1, bArr3, 0, bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
