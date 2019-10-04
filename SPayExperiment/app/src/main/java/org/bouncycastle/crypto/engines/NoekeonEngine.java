/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class NoekeonEngine
implements BlockCipher {
    private static final int genericSize = 16;
    private static final int[] nullVector = new int[]{0, 0, 0, 0};
    private static final int[] roundConstants = new int[]{128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212};
    private boolean _forEncryption;
    private boolean _initialised = false;
    private int[] decryptKeys = new int[4];
    private int[] state = new int[4];
    private int[] subKeys = new int[4];

    private int bytesToIntBig(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        int n4 = arrby[n2] << 24;
        int n5 = n3 + 1;
        int n6 = n4 | (255 & arrby[n3]) << 16;
        int n7 = n5 + 1;
        return n6 | (255 & arrby[n5]) << 8 | 255 & arrby[n7];
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4;
        this.state[0] = this.bytesToIntBig(arrby, n2);
        this.state[1] = this.bytesToIntBig(arrby, n2 + 4);
        this.state[2] = this.bytesToIntBig(arrby, n2 + 8);
        this.state[3] = this.bytesToIntBig(arrby, n2 + 12);
        System.arraycopy((Object)this.subKeys, (int)0, (Object)this.decryptKeys, (int)0, (int)this.subKeys.length);
        this.theta(this.decryptKeys, nullVector);
        for (n4 = 16; n4 > 0; --n4) {
            this.theta(this.state, this.decryptKeys);
            int[] arrn = this.state;
            arrn[0] = arrn[0] ^ roundConstants[n4];
            this.pi1(this.state);
            this.gamma(this.state);
            this.pi2(this.state);
        }
        this.theta(this.state, this.decryptKeys);
        int[] arrn = this.state;
        arrn[0] = arrn[0] ^ roundConstants[n4];
        this.intToBytesBig(this.state[0], arrby2, n3);
        this.intToBytesBig(this.state[1], arrby2, n3 + 4);
        this.intToBytesBig(this.state[2], arrby2, n3 + 8);
        this.intToBytesBig(this.state[3], arrby2, n3 + 12);
        return 16;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4;
        this.state[0] = this.bytesToIntBig(arrby, n2);
        this.state[1] = this.bytesToIntBig(arrby, n2 + 4);
        this.state[2] = this.bytesToIntBig(arrby, n2 + 8);
        this.state[3] = this.bytesToIntBig(arrby, n2 + 12);
        for (n4 = 0; n4 < 16; ++n4) {
            int[] arrn = this.state;
            arrn[0] = arrn[0] ^ roundConstants[n4];
            this.theta(this.state, this.subKeys);
            this.pi1(this.state);
            this.gamma(this.state);
            this.pi2(this.state);
        }
        int[] arrn = this.state;
        arrn[0] = arrn[0] ^ roundConstants[n4];
        this.theta(this.state, this.subKeys);
        this.intToBytesBig(this.state[0], arrby2, n3);
        this.intToBytesBig(this.state[1], arrby2, n3 + 4);
        this.intToBytesBig(this.state[2], arrby2, n3 + 8);
        this.intToBytesBig(this.state[3], arrby2, n3 + 12);
        return 16;
    }

    private void gamma(int[] arrn) {
        arrn[1] = arrn[1] ^ (-1 ^ arrn[3]) & (-1 ^ arrn[2]);
        arrn[0] = arrn[0] ^ arrn[2] & arrn[1];
        int n2 = arrn[3];
        arrn[3] = arrn[0];
        arrn[0] = n2;
        arrn[2] = arrn[2] ^ (arrn[0] ^ arrn[1] ^ arrn[3]);
        arrn[1] = arrn[1] ^ (-1 ^ arrn[3]) & (-1 ^ arrn[2]);
        arrn[0] = arrn[0] ^ arrn[2] & arrn[1];
    }

    private void intToBytesBig(int n2, byte[] arrby, int n3) {
        int n4 = n3 + 1;
        arrby[n3] = (byte)(n2 >>> 24);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(n2 >>> 16);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(n2 >>> 8);
        arrby[n6] = (byte)n2;
    }

    private void pi1(int[] arrn) {
        arrn[1] = this.rotl(arrn[1], 1);
        arrn[2] = this.rotl(arrn[2], 5);
        arrn[3] = this.rotl(arrn[3], 2);
    }

    private void pi2(int[] arrn) {
        arrn[1] = this.rotl(arrn[1], 31);
        arrn[2] = this.rotl(arrn[2], 27);
        arrn[3] = this.rotl(arrn[3], 30);
    }

    private int rotl(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private void setKey(byte[] arrby) {
        this.subKeys[0] = this.bytesToIntBig(arrby, 0);
        this.subKeys[1] = this.bytesToIntBig(arrby, 4);
        this.subKeys[2] = this.bytesToIntBig(arrby, 8);
        this.subKeys[3] = this.bytesToIntBig(arrby, 12);
    }

    private void theta(int[] arrn, int[] arrn2) {
        int n2 = arrn[0] ^ arrn[2];
        int n3 = n2 ^ (this.rotl(n2, 8) ^ this.rotl(n2, 24));
        arrn[1] = n3 ^ arrn[1];
        arrn[3] = n3 ^ arrn[3];
        for (int i2 = 0; i2 < 4; ++i2) {
            arrn[i2] = arrn[i2] ^ arrn2[i2];
        }
        int n4 = arrn[1] ^ arrn[3];
        int n5 = n4 ^ (this.rotl(n4, 8) ^ this.rotl(n4, 24));
        arrn[0] = n5 ^ arrn[0];
        arrn[2] = n5 ^ arrn[2];
    }

    @Override
    public String getAlgorithmName() {
        return "Noekeon";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to Noekeon init - " + cipherParameters.getClass().getName());
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
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > arrby2.length) {
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

