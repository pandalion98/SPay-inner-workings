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

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RC5Parameters;

public class RC532Engine
implements BlockCipher {
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private int[] _S = null;
    private int _noRounds = 12;
    private boolean forEncryption;

    private int bytesToWord(byte[] arrby, int n2) {
        return 255 & arrby[n2] | (255 & arrby[n2 + 1]) << 8 | (255 & arrby[n2 + 2]) << 16 | (255 & arrby[n2 + 3]) << 24;
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToWord(arrby, n2);
        int n5 = this.bytesToWord(arrby, n2 + 4);
        for (int i2 = this._noRounds; i2 >= 1; --i2) {
            n5 = n4 ^ this.rotateRight(n5 - this._S[1 + i2 * 2], n4);
            n4 = n5 ^ this.rotateRight(n4 - this._S[i2 * 2], n5);
        }
        this.wordToBytes(n4 - this._S[0], arrby2, n3);
        this.wordToBytes(n5 - this._S[1], arrby2, n3 + 4);
        return 8;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4;
        int n5 = this.bytesToWord(arrby, n2) + this._S[0];
        int n6 = this.bytesToWord(arrby, n2 + 4) + this._S[n4];
        for (n4 = 1; n4 <= this._noRounds; ++n4) {
            n5 = this.rotateLeft(n5 ^ n6, n6) + this._S[n4 * 2];
            n6 = this.rotateLeft(n6 ^ n5, n5) + this._S[1 + n4 * 2];
        }
        this.wordToBytes(n5, arrby2, n3);
        this.wordToBytes(n6, arrby2, n3 + 4);
        return 8;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << (n3 & 31) | n2 >>> 32 - (n3 & 31);
    }

    private int rotateRight(int n2, int n3) {
        return n2 >>> (n3 & 31) | n2 << 32 - (n3 & 31);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(byte[] arrby) {
        int n2 = 0;
        int[] arrn = new int[(3 + arrby.length) / 4];
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            int n3 = i2 / 4;
            arrn[n3] = arrn[n3] + ((255 & arrby[i2]) << 8 * (i2 % 4));
        }
        this._S = new int[2 * (1 + this._noRounds)];
        this._S[0] = -1209970333;
        for (int i3 = 1; i3 < this._S.length; ++i3) {
            this._S[i3] = -1640531527 + this._S[i3 - 1];
        }
        int n4 = arrn.length > this._S.length ? 3 * arrn.length : 3 * this._S.length;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        while (n2 < n4) {
            int[] arrn2 = this._S;
            arrn2[n6] = n8 = this.rotateLeft(n7 + (n8 + this._S[n6]), 3);
            arrn[n5] = n7 = this.rotateLeft(n7 + (n8 + arrn[n5]), n7 + n8);
            n6 = (n6 + 1) % this._S.length;
            n5 = (n5 + 1) % arrn.length;
            ++n2;
        }
        return;
    }

    private void wordToBytes(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        arrby[n3 + 1] = (byte)(n2 >> 8);
        arrby[n3 + 2] = (byte)(n2 >> 16);
        arrby[n3 + 3] = (byte)(n2 >> 24);
    }

    @Override
    public String getAlgorithmName() {
        return "RC5-32";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof RC5Parameters) {
            RC5Parameters rC5Parameters = (RC5Parameters)cipherParameters;
            this._noRounds = rC5Parameters.getRounds();
            this.setKey(rC5Parameters.getKey());
        } else {
            if (!(cipherParameters instanceof KeyParameter)) {
                throw new IllegalArgumentException("invalid parameter passed to RC532 init - " + cipherParameters.getClass().getName());
            }
            this.setKey(((KeyParameter)cipherParameters).getKey());
        }
        this.forEncryption = bl;
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.forEncryption) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
    }
}

