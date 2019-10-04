/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class GOFBBlockCipher
extends StreamBlockCipher {
    static final int C1 = 16843012;
    static final int C2 = 16843009;
    private byte[] IV;
    int N3;
    int N4;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    boolean firstStep = true;
    private byte[] ofbOutV;
    private byte[] ofbV;

    public GOFBBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        if (this.blockSize != 8) {
            throw new IllegalArgumentException("GCTR only for 64 bit block ciphers");
        }
        this.IV = new byte[blockCipher.getBlockSize()];
        this.ofbV = new byte[blockCipher.getBlockSize()];
        this.ofbOutV = new byte[blockCipher.getBlockSize()];
    }

    private int bytesToint(byte[] arrby, int n2) {
        return (-16777216 & arrby[n2 + 3] << 24) + (16711680 & arrby[n2 + 2] << 16) + (65280 & arrby[n2 + 1] << 8) + (255 & arrby[n2]);
    }

    private void intTobytes(int n2, byte[] arrby, int n3) {
        arrby[n3 + 3] = (byte)(n2 >>> 24);
        arrby[n3 + 2] = (byte)(n2 >>> 16);
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3] = (byte)n2;
    }

    @Override
    protected byte calculateByte(byte by) {
        if (this.byteCount == 0) {
            if (this.firstStep) {
                this.firstStep = false;
                this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
                this.N3 = this.bytesToint(this.ofbOutV, 0);
                this.N4 = this.bytesToint(this.ofbOutV, 4);
            }
            this.N3 = 16843009 + this.N3;
            this.N4 = 16843012 + this.N4;
            this.intTobytes(this.N3, this.ofbV, 0);
            this.intTobytes(this.N4, this.ofbV, 4);
            this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
        }
        byte[] arrby = this.ofbOutV;
        int n2 = this.byteCount;
        this.byteCount = n2 + 1;
        byte by2 = (byte)(by ^ arrby[n2]);
        if (this.byteCount == this.blockSize) {
            this.byteCount = 0;
            System.arraycopy((Object)this.ofbV, (int)this.blockSize, (Object)this.ofbV, (int)0, (int)(this.ofbV.length - this.blockSize));
            System.arraycopy((Object)this.ofbOutV, (int)0, (Object)this.ofbV, (int)(this.ofbV.length - this.blockSize), (int)this.blockSize);
        }
        return by2;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/GCTR";
    }

    @Override
    public int getBlockSize() {
        return this.blockSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.firstStep = true;
        this.N3 = 0;
        this.N4 = 0;
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
        this.firstStep = true;
        this.N3 = 0;
        this.N4 = 0;
        System.arraycopy((Object)this.IV, (int)0, (Object)this.ofbV, (int)0, (int)this.IV.length);
        this.byteCount = 0;
        this.cipher.reset();
    }
}

