/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class RC4Engine
implements StreamCipher {
    private static final int STATE_LENGTH = 256;
    private byte[] engineState = null;
    private byte[] workingKey = null;
    private int x = 0;
    private int y = 0;

    private void setKey(byte[] arrby) {
        int n2 = 0;
        this.workingKey = arrby;
        this.x = 0;
        this.y = 0;
        if (this.engineState == null) {
            this.engineState = new byte[256];
        }
        for (int i2 = 0; i2 < 256; ++i2) {
            this.engineState[i2] = (byte)i2;
        }
        int n3 = 0;
        int n4 = 0;
        while (n2 < 256) {
            n3 = 255 & n3 + ((255 & arrby[n4]) + this.engineState[n2]);
            byte by = this.engineState[n2];
            this.engineState[n2] = this.engineState[n3];
            this.engineState[n3] = by;
            n4 = (n4 + 1) % arrby.length;
            ++n2;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "RC4";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = ((KeyParameter)cipherParameters).getKey();
            this.setKey(this.workingKey);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC4 init - " + cipherParameters.getClass().getName());
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            this.x = 255 & 1 + this.x;
            this.y = 255 & this.engineState[this.x] + this.y;
            byte by = this.engineState[this.x];
            this.engineState[this.x] = this.engineState[this.y];
            this.engineState[this.y] = by;
            arrby2[i2 + n4] = (byte)(arrby[i2 + n2] ^ this.engineState[255 & this.engineState[this.x] + this.engineState[this.y]]);
        }
        return n3;
    }

    @Override
    public void reset() {
        this.setKey(this.workingKey);
    }

    @Override
    public byte returnByte(byte by) {
        this.x = 255 & 1 + this.x;
        this.y = 255 & this.engineState[this.x] + this.y;
        byte by2 = this.engineState[this.x];
        this.engineState[this.x] = this.engineState[this.y];
        this.engineState[this.y] = by2;
        return (byte)(by ^ this.engineState[255 & this.engineState[this.x] + this.engineState[this.y]]);
    }
}

