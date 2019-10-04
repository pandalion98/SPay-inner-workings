/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;

public class BlockCipherMac
implements Mac {
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] mac;
    private int macSize;

    public BlockCipherMac(BlockCipher blockCipher) {
        this(blockCipher, 8 * blockCipher.getBlockSize() / 2);
    }

    public BlockCipherMac(BlockCipher blockCipher, int n2) {
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        this.cipher = new CBCBlockCipher(blockCipher);
        this.macSize = n2 / 8;
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        int n3 = this.cipher.getBlockSize();
        while (this.bufOff < n3) {
            this.buf[this.bufOff] = 0;
            this.bufOff = 1 + this.bufOff;
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        System.arraycopy((Object)this.mac, (int)0, (Object)arrby, (int)n2, (int)this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }

    @Override
    public void update(byte by) {
        if (this.bufOff == this.buf.length) {
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
        }
        byte[] arrby = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        arrby[n2] = by;
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n4 = this.cipher.getBlockSize();
        int n5 = n4 - this.bufOff;
        if (n3 > n5) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n5);
            int n6 = 0 + this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
            n3 -= n5;
            n2 += n5;
            while (n3 > n4) {
                n6 += this.cipher.processBlock(arrby, n2, this.mac, 0);
                n3 -= n4;
                n2 += n4;
            }
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n3);
        this.bufOff = n3 + this.bufOff;
    }
}

