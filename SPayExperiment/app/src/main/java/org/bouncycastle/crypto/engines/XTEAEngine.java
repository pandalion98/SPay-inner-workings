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

public class XTEAEngine
implements BlockCipher {
    private static final int block_size = 8;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private int[] _S = new int[4];
    private boolean _forEncryption;
    private boolean _initialised = false;
    private int[] _sum0 = new int[32];
    private int[] _sum1 = new int[32];

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
        for (int i2 = 31; i2 >= 0; --i2) {
            n4 -= (n5 -= n4 + (n4 << 4 ^ n4 >>> 5) ^ this._sum1[i2]) + (n5 << 4 ^ n5 >>> 5) ^ this._sum0[i2];
        }
        this.unpackInt(n4, arrby2, n3);
        this.unpackInt(n5, arrby2, n3 + 4);
        return 8;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToInt(arrby, n2);
        int n5 = this.bytesToInt(arrby, n2 + 4);
        for (int i2 = 0; i2 < 32; ++i2) {
            n5 += (n4 += n5 + (n5 << 4 ^ n5 >>> 5) ^ this._sum0[i2]) + (n4 << 4 ^ n4 >>> 5) ^ this._sum1[i2];
        }
        this.unpackInt(n4, arrby2, n3);
        this.unpackInt(n5, arrby2, n3 + 4);
        return 8;
    }

    private void setKey(byte[] arrby) {
        int n2 = 0;
        if (arrby.length != 16) {
            throw new IllegalArgumentException("Key size must be 128 bits.");
        }
        int n3 = 0;
        int n4 = 0;
        while (n4 < 4) {
            this._S[n4] = this.bytesToInt(arrby, n3);
            ++n4;
            n3 += 4;
        }
        for (int i2 = 0; i2 < 32; ++i2) {
            this._sum0[i2] = n2 + this._S[n2 & 3];
            this._sum1[i2] = (n2 -= 1640531527) + this._S[3 & n2 >>> 11];
        }
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
        return "XTEA";
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

