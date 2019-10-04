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
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class ISO9797Alg3Mac
implements Mac {
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private KeyParameter lastKey2;
    private KeyParameter lastKey3;
    private byte[] mac;
    private int macSize;
    private BlockCipherPadding padding;

    public ISO9797Alg3Mac(BlockCipher blockCipher) {
        this(blockCipher, 8 * blockCipher.getBlockSize(), null);
    }

    public ISO9797Alg3Mac(BlockCipher blockCipher, int n2) {
        this(blockCipher, n2, null);
    }

    public ISO9797Alg3Mac(BlockCipher blockCipher, int n2, BlockCipherPadding blockCipherPadding) {
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (!(blockCipher instanceof DESEngine)) {
            throw new IllegalArgumentException("cipher must be instance of DESEngine");
        }
        this.cipher = new CBCBlockCipher(blockCipher);
        this.padding = blockCipherPadding;
        this.macSize = n2 / 8;
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    public ISO9797Alg3Mac(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
        this(blockCipher, 8 * blockCipher.getBlockSize(), blockCipherPadding);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        int n3 = this.cipher.getBlockSize();
        if (this.padding == null) {
            while (this.bufOff < n3) {
                this.buf[this.bufOff] = 0;
                this.bufOff = 1 + this.bufOff;
            }
        } else {
            if (this.bufOff == n3) {
                this.cipher.processBlock(this.buf, 0, this.mac, 0);
                this.bufOff = 0;
            }
            this.padding.addPadding(this.buf, this.bufOff);
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        DESEngine dESEngine = new DESEngine();
        dESEngine.init(false, this.lastKey2);
        dESEngine.processBlock(this.mac, 0, this.mac, 0);
        dESEngine.init(true, this.lastKey3);
        dESEngine.processBlock(this.mac, 0, this.mac, 0);
        System.arraycopy((Object)this.mac, (int)0, (Object)arrby, (int)n2, (int)this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public String getAlgorithmName() {
        return "ISO9797Alg3";
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(CipherParameters cipherParameters) {
        KeyParameter keyParameter;
        this.reset();
        if (!(cipherParameters instanceof KeyParameter) && !(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("params must be an instance of KeyParameter or ParametersWithIV");
        }
        KeyParameter keyParameter2 = cipherParameters instanceof KeyParameter ? (KeyParameter)cipherParameters : (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
        byte[] arrby = keyParameter2.getKey();
        if (arrby.length == 16) {
            keyParameter = new KeyParameter(arrby, 0, 8);
            this.lastKey2 = new KeyParameter(arrby, 8, 8);
            this.lastKey3 = keyParameter;
        } else {
            if (arrby.length != 24) {
                throw new IllegalArgumentException("Key must be either 112 or 168 bit long");
            }
            keyParameter = new KeyParameter(arrby, 0, 8);
            this.lastKey2 = new KeyParameter(arrby, 8, 8);
            this.lastKey3 = new KeyParameter(arrby, 16, 8);
        }
        if (cipherParameters instanceof ParametersWithIV) {
            this.cipher.init(true, new ParametersWithIV(keyParameter, ((ParametersWithIV)cipherParameters).getIV()));
            return;
        }
        this.cipher.init(true, keyParameter);
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

