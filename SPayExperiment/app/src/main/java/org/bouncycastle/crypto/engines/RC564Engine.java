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
import org.bouncycastle.crypto.params.RC5Parameters;

public class RC564Engine
implements BlockCipher {
    private static final long P64 = -5196783011329398165L;
    private static final long Q64 = -7046029254386353131L;
    private static final int bytesPerWord = 8;
    private static final int wordSize = 64;
    private long[] _S = null;
    private int _noRounds = 12;
    private boolean forEncryption;

    private long bytesToWord(byte[] arrby, int n2) {
        long l2 = 0L;
        for (int i2 = 7; i2 >= 0; --i2) {
            l2 = (l2 << 8) + (long)(255 & arrby[i2 + n2]);
        }
        return l2;
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        long l2 = this.bytesToWord(arrby, n2);
        long l3 = this.bytesToWord(arrby, n2 + 8);
        for (int i2 = this._noRounds; i2 >= 1; --i2) {
            l3 = l2 ^ this.rotateRight(l3 - this._S[1 + i2 * 2], l2);
            l2 = l3 ^ this.rotateRight(l2 - this._S[i2 * 2], l3);
        }
        this.wordToBytes(l2 - this._S[0], arrby2, n3);
        this.wordToBytes(l3 - this._S[1], arrby2, n3 + 8);
        return 16;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4;
        long l2 = this.bytesToWord(arrby, n2) + this._S[0];
        long l3 = this.bytesToWord(arrby, n2 + 8) + this._S[n4];
        for (n4 = 1; n4 <= this._noRounds; ++n4) {
            l2 = this.rotateLeft(l2 ^ l3, l3) + this._S[n4 * 2];
            l3 = this.rotateLeft(l3 ^ l2, l2) + this._S[1 + n4 * 2];
        }
        this.wordToBytes(l2, arrby2, n3);
        this.wordToBytes(l3, arrby2, n3 + 8);
        return 16;
    }

    private long rotateLeft(long l2, long l3) {
        return l2 << (int)(l3 & 63L) | l2 >>> (int)(64L - (63L & l3));
    }

    private long rotateRight(long l2, long l3) {
        return l2 >>> (int)(l3 & 63L) | l2 << (int)(64L - (63L & l3));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(byte[] arrby) {
        int n2 = 0;
        long[] arrl = new long[(7 + arrby.length) / 8];
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            int n3 = i2 / 8;
            arrl[n3] = arrl[n3] + ((long)(255 & arrby[i2]) << 8 * (i2 % 8));
        }
        this._S = new long[2 * (1 + this._noRounds)];
        this._S[0] = -5196783011329398165L;
        for (int i3 = 1; i3 < this._S.length; ++i3) {
            this._S[i3] = -7046029254386353131L + this._S[i3 - 1];
        }
        int n4 = arrl.length > this._S.length ? 3 * arrl.length : 3 * this._S.length;
        long l2 = 0L;
        long l3 = 0L;
        int n5 = 0;
        int n6 = 0;
        while (n2 < n4) {
            long[] arrl2 = this._S;
            arrl2[n6] = l3 = this.rotateLeft(l2 + (l3 + this._S[n6]), 3L);
            arrl[n5] = l2 = this.rotateLeft(l2 + (l3 + arrl[n5]), l2 + l3);
            n6 = (n6 + 1) % this._S.length;
            n5 = (n5 + 1) % arrl.length;
            ++n2;
        }
        return;
    }

    private void wordToBytes(long l2, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            arrby[i2 + n2] = (byte)l2;
            l2 >>>= 8;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "RC5-64";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof RC5Parameters)) {
            throw new IllegalArgumentException("invalid parameter passed to RC564 init - " + cipherParameters.getClass().getName());
        }
        RC5Parameters rC5Parameters = (RC5Parameters)cipherParameters;
        this.forEncryption = bl;
        this._noRounds = rC5Parameters.getRounds();
        this.setKey(rC5Parameters.getKey());
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

