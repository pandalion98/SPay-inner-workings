/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class CFBBlockCipher
extends StreamBlockCipher {
    private byte[] IV;
    private int blockSize;
    private int byteCount;
    private byte[] cfbOutV;
    private byte[] cfbV;
    private BlockCipher cipher = null;
    private boolean encrypting;
    private byte[] inBuf;

    public CFBBlockCipher(BlockCipher blockCipher, int n2) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = n2 / 8;
        this.IV = new byte[blockCipher.getBlockSize()];
        this.cfbV = new byte[blockCipher.getBlockSize()];
        this.cfbOutV = new byte[blockCipher.getBlockSize()];
        this.inBuf = new byte[this.blockSize];
    }

    private byte decryptByte(byte by) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
        }
        this.inBuf[this.byteCount] = by;
        byte[] arrby = this.cfbOutV;
        int n2 = this.byteCount;
        this.byteCount = n2 + 1;
        byte by2 = (byte)(by ^ arrby[n2]);
        if (this.byteCount == this.blockSize) {
            this.byteCount = 0;
            System.arraycopy((Object)this.cfbV, (int)this.blockSize, (Object)this.cfbV, (int)0, (int)(this.cfbV.length - this.blockSize));
            System.arraycopy((Object)this.inBuf, (int)0, (Object)this.cfbV, (int)(this.cfbV.length - this.blockSize), (int)this.blockSize);
        }
        return by2;
    }

    private byte encryptByte(byte by) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
        }
        byte by2 = (byte)(by ^ this.cfbOutV[this.byteCount]);
        byte[] arrby = this.inBuf;
        int n2 = this.byteCount;
        this.byteCount = n2 + 1;
        arrby[n2] = by2;
        if (this.byteCount == this.blockSize) {
            this.byteCount = 0;
            System.arraycopy((Object)this.cfbV, (int)this.blockSize, (Object)this.cfbV, (int)0, (int)(this.cfbV.length - this.blockSize));
            System.arraycopy((Object)this.inBuf, (int)0, (Object)this.cfbV, (int)(this.cfbV.length - this.blockSize), (int)this.blockSize);
        }
        return by2;
    }

    @Override
    protected byte calculateByte(byte by) {
        if (this.encrypting) {
            return this.encryptByte(by);
        }
        return this.decryptByte(by);
    }

    public int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.processBytes(arrby, n2, this.blockSize, arrby2, n3);
        return this.blockSize;
    }

    public int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.processBytes(arrby, n2, this.blockSize, arrby2, n3);
        return this.blockSize;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CFB" + 8 * this.blockSize;
    }

    @Override
    public int getBlockSize() {
        return this.blockSize;
    }

    public byte[] getCurrentIV() {
        return Arrays.clone((byte[])this.cfbV);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.encrypting = bl;
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby = parametersWithIV.getIV();
            if (arrby.length < this.IV.length) {
                System.arraycopy((Object)arrby, (int)0, (Object)this.IV, (int)(this.IV.length - arrby.length), (int)arrby.length);
                for (int i2 = 0; i2 < this.IV.length - arrby.length; ++i2) {
                    this.IV[i2] = 0;
                }
            } else {
                System.arraycopy((Object)arrby, (int)0, (Object)this.IV, (int)0, (int)this.IV.length);
            }
            this.reset();
            if (parametersWithIV.getParameters() == null) return;
            {
                this.cipher.init(true, parametersWithIV.getParameters());
                return;
            }
        } else {
            this.reset();
            if (cipherParameters == null) return;
            {
                this.cipher.init(true, cipherParameters);
                return;
            }
        }
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.processBytes(arrby, n2, this.blockSize, arrby2, n3);
        return this.blockSize;
    }

    @Override
    public void reset() {
        System.arraycopy((Object)this.IV, (int)0, (Object)this.cfbV, (int)0, (int)this.IV.length);
        Arrays.fill((byte[])this.inBuf, (byte)0);
        this.byteCount = 0;
        this.cipher.reset();
    }
}

