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

public class RC6Engine
implements BlockCipher {
    private static final int LGW = 5;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private static final int _noRounds = 20;
    private static final int bytesPerWord = 4;
    private static final int wordSize = 32;
    private int[] _S = null;
    private boolean forEncryption;

    private int bytesToWord(byte[] arrby, int n2) {
        int n3 = 0;
        for (int i2 = 3; i2 >= 0; --i2) {
            n3 = (n3 << 8) + (255 & arrby[i2 + n2]);
        }
        return n3;
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToWord(arrby, n2);
        int n5 = this.bytesToWord(arrby, n2 + 4);
        int n6 = this.bytesToWord(arrby, n2 + 8);
        int n7 = this.bytesToWord(arrby, n2 + 12);
        int n8 = n6 - this._S[43];
        int n9 = n4 - this._S[42];
        for (int i2 = 20; i2 >= 1; --i2) {
            int n10 = this.rotateLeft(n9 * (1 + n9 * 2), 5);
            int n11 = this.rotateLeft(n8 * (1 + n8 * 2), 5);
            int n12 = n11 ^ this.rotateRight(n5 - this._S[1 + i2 * 2], n10);
            int n13 = n10 ^ this.rotateRight(n7 - this._S[i2 * 2], n11);
            int n14 = n8;
            n8 = n12;
            n5 = n9;
            n9 = n13;
            n7 = n14;
        }
        int n15 = n7 - this._S[1];
        int n16 = n5 - this._S[0];
        this.wordToBytes(n9, arrby2, n3);
        this.wordToBytes(n16, arrby2, n3 + 4);
        this.wordToBytes(n8, arrby2, n3 + 8);
        this.wordToBytes(n15, arrby2, n3 + 12);
        return 16;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4;
        int n5 = this.bytesToWord(arrby, n2);
        int n6 = this.bytesToWord(arrby, n2 + 4);
        int n7 = this.bytesToWord(arrby, n2 + 8);
        int n8 = this.bytesToWord(arrby, n2 + 12);
        int n9 = n6 + this._S[0];
        int n10 = n8 + this._S[n4];
        int n11 = n5;
        int n12 = n9;
        int n13 = n7;
        for (n4 = 1; n4 <= 20; ++n4) {
            int n14 = this.rotateLeft(n12 * (1 + n12 * 2), 5);
            int n15 = this.rotateLeft(n10 * (1 + n10 * 2), 5);
            int n16 = this.rotateLeft(n11 ^ n14, n15) + this._S[n4 * 2];
            int n17 = this.rotateLeft(n13 ^ n15, n14) + this._S[1 + n4 * 2];
            n11 = n12;
            n12 = n17;
            n13 = n10;
            n10 = n16;
        }
        int n18 = n11 + this._S[42];
        int n19 = n13 + this._S[43];
        this.wordToBytes(n18, arrby2, n3);
        this.wordToBytes(n12, arrby2, n3 + 4);
        this.wordToBytes(n19, arrby2, n3 + 8);
        this.wordToBytes(n10, arrby2, n3 + 12);
        return 16;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    private int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(byte[] arrby) {
        int n2 = 0;
        if ((3 + arrby.length) / 4 == 0) {
            // empty if block
        }
        int[] arrn = new int[(-1 + (4 + arrby.length)) / 4];
        for (int i2 = -1 + arrby.length; i2 >= 0; --i2) {
            arrn[i2 / 4] = (arrn[i2 / 4] << 8) + (255 & arrby[i2]);
        }
        this._S = new int[44];
        this._S[0] = -1209970333;
        for (int i3 = 1; i3 < this._S.length; ++i3) {
            this._S[i3] = -1640531527 + this._S[i3 - 1];
        }
        int n3 = arrn.length > this._S.length ? 3 * arrn.length : 3 * this._S.length;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        while (n2 < n3) {
            int[] arrn2 = this._S;
            arrn2[n5] = n7 = this.rotateLeft(n6 + (n7 + this._S[n5]), 3);
            arrn[n4] = n6 = this.rotateLeft(n6 + (n7 + arrn[n4]), n6 + n7);
            n5 = (n5 + 1) % this._S.length;
            n4 = (n4 + 1) % arrn.length;
            ++n2;
        }
        return;
    }

    private void wordToBytes(int n2, byte[] arrby, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            arrby[i2 + n3] = (byte)n2;
            n2 >>>= 8;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "RC6";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to RC6 init - " + cipherParameters.getClass().getName());
        }
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        this.forEncryption = bl;
        this.setKey(keyParameter.getKey());
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.getBlockSize();
        if (this._S == null) {
            throw new IllegalStateException("RC6 engine not initialised");
        }
        if (n2 + n4 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
    }
}

