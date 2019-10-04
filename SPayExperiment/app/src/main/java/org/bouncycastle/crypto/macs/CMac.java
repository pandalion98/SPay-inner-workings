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
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;

public class CMac
implements Mac {
    private static final byte CONSTANT_128 = -121;
    private static final byte CONSTANT_64 = 27;
    private byte[] L;
    private byte[] Lu;
    private byte[] Lu2;
    private byte[] ZEROES;
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] mac;
    private int macSize;

    public CMac(BlockCipher blockCipher) {
        this(blockCipher, 8 * blockCipher.getBlockSize());
    }

    public CMac(BlockCipher blockCipher, int n2) {
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (n2 > 8 * blockCipher.getBlockSize()) {
            throw new IllegalArgumentException("MAC size must be less or equal to " + 8 * blockCipher.getBlockSize());
        }
        if (blockCipher.getBlockSize() != 8 && blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Block size must be either 64 or 128 bits");
        }
        this.cipher = new CBCBlockCipher(blockCipher);
        this.macSize = n2 / 8;
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.ZEROES = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static byte[] doubleLu(byte[] arrby) {
        byte[] arrby2 = new byte[arrby.length];
        int n2 = CMac.shiftLeft(arrby, arrby2);
        int n3 = arrby.length == 16 ? -121 : 27;
        int n4 = n3 & 255;
        int n5 = -1 + arrby.length;
        arrby2[n5] = (byte)(arrby2[n5] ^ n4 >>> (1 - n2 << 3));
        return arrby2;
    }

    private static int shiftLeft(byte[] arrby, byte[] arrby2) {
        int n2 = arrby.length;
        int n3 = 0;
        while (--n2 >= 0) {
            int n4 = 255 & arrby[n2];
            arrby2[n2] = (byte)(n3 | n4 << 1);
            n3 = 1 & n4 >>> 7;
        }
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte[] arrby2;
        int n3 = this.cipher.getBlockSize();
        if (this.bufOff == n3) {
            arrby2 = this.Lu;
        } else {
            new ISO7816d4Padding().addPadding(this.buf, this.bufOff);
            arrby2 = this.Lu2;
        }
        int n4 = 0;
        do {
            if (n4 >= this.mac.length) {
                this.cipher.processBlock(this.buf, 0, this.mac, 0);
                System.arraycopy((Object)this.mac, (int)0, (Object)arrby, (int)n2, (int)this.macSize);
                this.reset();
                return this.macSize;
            }
            byte[] arrby3 = this.buf;
            arrby3[n4] = (byte)(arrby3[n4] ^ arrby2[n4]);
            ++n4;
        } while (true);
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
        this.validate(cipherParameters);
        this.cipher.init(true, cipherParameters);
        this.L = new byte[this.ZEROES.length];
        this.cipher.processBlock(this.ZEROES, 0, this.L, 0);
        this.Lu = CMac.doubleLu(this.L);
        this.Lu2 = CMac.doubleLu(this.Lu);
        this.reset();
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
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
            n3 -= n5;
            n2 += n5;
            while (n3 > n4) {
                this.cipher.processBlock(arrby, n2, this.mac, 0);
                n3 -= n4;
                n2 += n4;
            }
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n3);
        this.bufOff = n3 + this.bufOff;
    }

    void validate(CipherParameters cipherParameters) {
        if (cipherParameters != null && !(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("CMac mode only permits key to be set.");
        }
    }
}

