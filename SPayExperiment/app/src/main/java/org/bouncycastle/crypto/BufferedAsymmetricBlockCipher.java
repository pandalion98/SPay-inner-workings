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

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

public class BufferedAsymmetricBlockCipher {
    protected byte[] buf;
    protected int bufOff;
    private final AsymmetricBlockCipher cipher;

    public BufferedAsymmetricBlockCipher(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.cipher = asymmetricBlockCipher;
    }

    public byte[] doFinal() {
        byte[] arrby = this.cipher.processBlock(this.buf, 0, this.bufOff);
        this.reset();
        return arrby;
    }

    public int getBufferPosition() {
        return this.bufOff;
    }

    public int getInputBlockSize() {
        return this.cipher.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return this.cipher.getOutputBlockSize();
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.reset();
        this.cipher.init(bl, cipherParameters);
        int n2 = this.cipher.getInputBlockSize();
        int n3 = bl ? 1 : 0;
        this.buf = new byte[n3 + n2];
        this.bufOff = 0;
    }

    public void processByte(byte by) {
        if (this.bufOff >= this.buf.length) {
            throw new DataLengthException("attempt to process message too long for cipher");
        }
        byte[] arrby = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        arrby[n2] = by;
    }

    public void processBytes(byte[] arrby, int n2, int n3) {
        if (n3 == 0) {
            return;
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        if (n3 + this.bufOff > this.buf.length) {
            throw new DataLengthException("attempt to process message too long for cipher");
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n3);
        this.bufOff = n3 + this.bufOff;
    }

    public void reset() {
        if (this.buf != null) {
            for (int i2 = 0; i2 < this.buf.length; ++i2) {
                this.buf[i2] = 0;
            }
        }
        this.bufOff = 0;
    }
}

