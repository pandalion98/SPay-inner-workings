package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RC4Engine implements StreamCipher {
    private static final int STATE_LENGTH = 256;
    private byte[] engineState;
    private byte[] workingKey;
    private int f162x;
    private int f163y;

    public RC4Engine() {
        this.engineState = null;
        this.f162x = 0;
        this.f163y = 0;
        this.workingKey = null;
    }

    private void setKey(byte[] bArr) {
        int i;
        int i2 = 0;
        this.workingKey = bArr;
        this.f162x = 0;
        this.f163y = 0;
        if (this.engineState == null) {
            this.engineState = new byte[STATE_LENGTH];
        }
        for (i = 0; i < STATE_LENGTH; i++) {
            this.engineState[i] = (byte) i;
        }
        i = 0;
        int i3 = 0;
        while (i2 < STATE_LENGTH) {
            i = (i + ((bArr[i3] & GF2Field.MASK) + this.engineState[i2])) & GF2Field.MASK;
            byte b = this.engineState[i2];
            this.engineState[i2] = this.engineState[i];
            this.engineState[i] = b;
            i3 = (i3 + 1) % bArr.length;
            i2++;
        }
    }

    public String getAlgorithmName() {
        return "RC4";
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = ((KeyParameter) cipherParameters).getKey();
            setKey(this.workingKey);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC4 init - " + cipherParameters.getClass().getName());
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i3 + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            for (int i4 = 0; i4 < i2; i4++) {
                this.f162x = (this.f162x + 1) & GF2Field.MASK;
                this.f163y = (this.engineState[this.f162x] + this.f163y) & GF2Field.MASK;
                byte b = this.engineState[this.f162x];
                this.engineState[this.f162x] = this.engineState[this.f163y];
                this.engineState[this.f163y] = b;
                bArr2[i4 + i3] = (byte) (bArr[i4 + i] ^ this.engineState[(this.engineState[this.f162x] + this.engineState[this.f163y]) & GF2Field.MASK]);
            }
            return i2;
        }
    }

    public void reset() {
        setKey(this.workingKey);
    }

    public byte returnByte(byte b) {
        this.f162x = (this.f162x + 1) & GF2Field.MASK;
        this.f163y = (this.engineState[this.f162x] + this.f163y) & GF2Field.MASK;
        byte b2 = this.engineState[this.f162x];
        this.engineState[this.f162x] = this.engineState[this.f163y];
        this.engineState[this.f163y] = b2;
        return (byte) (this.engineState[(this.engineState[this.f162x] + this.engineState[this.f163y]) & GF2Field.MASK] ^ b);
    }
}
