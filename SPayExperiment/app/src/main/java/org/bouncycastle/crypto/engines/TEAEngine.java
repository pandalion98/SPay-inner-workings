/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class TEAEngine
implements BlockCipher {
    private static final int block_size = 8;
    private static final int d_sum = -957401312;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private int _a;
    private int _b;
    private int _c;
    private int _d;
    private boolean _forEncryption;
    private boolean _initialised = false;

    private int bytesToInt(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        int n4 = arrby[n2] << 24;
        int n5 = n3 + 1;
        int n6 = n4 | (255 & arrby[n3]) << 16;
        int n7 = n5 + 1;
        return n6 | (255 & arrby[n5]) << 8 | 255 & arrby[n7];
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToInt(arrby, n2);
        int n5 = this.bytesToInt(arrby, n2 + 4);
        int n6 = -957401312;
        for (int i2 = 0; i2 != 32; ++i2) {
            n4 -= ((n5 -= (n4 << 4) + this._c ^ n4 + n6 ^ (n4 >>> 5) + this._d) << 4) + this._a ^ n5 + n6 ^ (n5 >>> 5) + this._b;
            n6 += 1640531527;
        }
        this.unpackInt(n4, arrby2, n3);
        this.unpackInt(n5, arrby2, n3 + 4);
        return 8;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToInt(arrby, n2);
        int n5 = this.bytesToInt(arrby, n2 + 4);
        int n6 = n4;
        int n7 = n5;
        int n8 = 0;
        for (int i2 = 0; i2 != 32; ++i2) {
            n7 += ((n6 += (n7 << 4) + this._a ^ n7 + (n8 -= 1640531527) ^ (n7 >>> 5) + this._b) << 4) + this._c ^ n6 + n8 ^ (n6 >>> 5) + this._d;
        }
        this.unpackInt(n6, arrby2, n3);
        this.unpackInt(n7, arrby2, n3 + 4);
        return 8;
    }

    private void setKey(byte[] arrby) {
        if (arrby.length != 16) {
            throw new IllegalArgumentException("Key size must be 128 bits.");
        }
        this._a = this.bytesToInt(arrby, 0);
        this._b = this.bytesToInt(arrby, 4);
        this._c = this.bytesToInt(arrby, 8);
        this._d = this.bytesToInt(arrby, 12);
    }

    private void unpackInt(int n2, byte[] arrby, int n3) {
        int n4 = n3 + 1;
        arrby[n3] = (byte)(n2 >>> 24);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(n2 >>> 16);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(n2 >>> 8);
        arrby[n6] = (byte)n2;
    }

    @Override
    public String getAlgorithmName() {
        return "TEA";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to TEA init - " + cipherParameters.getClass().getName());
        }
        this._forEncryption = bl;
        this._initialised = true;
        this.setKey(((KeyParameter)cipherParameters).getKey());
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (!this._initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + 8 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this._forEncryption) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
    }
}

