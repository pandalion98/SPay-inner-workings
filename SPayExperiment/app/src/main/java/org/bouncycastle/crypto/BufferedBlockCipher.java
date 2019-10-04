/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;

public class BufferedBlockCipher {
    protected byte[] buf;
    protected int bufOff;
    protected BlockCipher cipher;
    protected boolean forEncryption;
    protected boolean partialBlockOkay;
    protected boolean pgpCFB;

    protected BufferedBlockCipher() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public BufferedBlockCipher(BlockCipher blockCipher) {
        boolean bl = true;
        this.cipher = blockCipher;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
        String string = blockCipher.getAlgorithmName();
        int n2 = 1 + string.indexOf(47);
        boolean bl2 = n2 > 0 && string.startsWith("PGP", n2) ? bl : false;
        this.pgpCFB = bl2;
        if (this.pgpCFB || blockCipher instanceof StreamCipher) {
            this.partialBlockOkay = bl;
            return;
        }
        if (n2 <= 0 || !string.startsWith("OpenPGP", n2)) {
            bl = false;
        }
        this.partialBlockOkay = bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int doFinal(byte[] arrby, int n2) {
        try {
            if (n2 + this.bufOff > arrby.length) {
                throw new OutputLengthException("output buffer too short for doFinal()");
            }
            int n3 = this.bufOff;
            int n4 = 0;
            if (n3 == 0) return n4;
            if (!this.partialBlockOkay) {
                throw new DataLengthException("data not block size aligned");
            }
            this.cipher.processBlock(this.buf, 0, this.buf, 0);
            n4 = this.bufOff;
            this.bufOff = 0;
            System.arraycopy((Object)this.buf, (int)0, (Object)arrby, (int)n2, (int)n4);
            return n4;
        }
        finally {
            this.reset();
        }
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public int getOutputSize(int n2) {
        return n2 + this.bufOff;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getUpdateOutputSize(int n2) {
        int n3;
        int n4 = n2 + this.bufOff;
        if (this.pgpCFB) {
            n3 = n4 % this.buf.length - (2 + this.cipher.getBlockSize());
            do {
                return n4 - n3;
                break;
            } while (true);
        }
        n3 = n4 % this.buf.length;
        return n4 - n3;
    }

    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
        this.reset();
        this.cipher.init(bl, cipherParameters);
    }

    public int processByte(byte by, byte[] arrby, int n2) {
        byte[] arrby2 = this.buf;
        int n3 = this.bufOff;
        this.bufOff = n3 + 1;
        arrby2[n3] = by;
        if (this.bufOff == this.buf.length) {
            int n4 = this.cipher.processBlock(this.buf, 0, arrby, n2);
            this.bufOff = 0;
            return n4;
        }
        return 0;
    }

    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        int n5;
        int n6;
        int n7;
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n8 = this.getBlockSize();
        int n9 = this.getUpdateOutputSize(n3);
        if (n9 > 0 && n9 + n4 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        int n10 = this.buf.length - this.bufOff;
        if (n3 > n10) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n10);
            n6 = 0 + this.cipher.processBlock(this.buf, 0, arrby2, n4);
            this.bufOff = 0;
            n7 = n3 - n10;
            n5 = n10 + n2;
            while (n7 > this.buf.length) {
                n6 += this.cipher.processBlock(arrby, n5, arrby2, n4 + n6);
                n7 -= n8;
                n5 += n8;
            }
        } else {
            n6 = 0;
            n7 = n3;
            n5 = n2;
        }
        System.arraycopy((Object)arrby, (int)n5, (Object)this.buf, (int)this.bufOff, (int)n7);
        this.bufOff = n7 + this.bufOff;
        if (this.bufOff == this.buf.length) {
            n6 += this.cipher.processBlock(this.buf, 0, arrby2, n4 + n6);
            this.bufOff = 0;
        }
        return n6;
    }

    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}

