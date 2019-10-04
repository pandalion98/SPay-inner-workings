/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

class MacCFBBlockCipher {
    private byte[] IV;
    private int blockSize;
    private byte[] cfbOutV;
    private byte[] cfbV;
    private BlockCipher cipher = null;

    public MacCFBBlockCipher(BlockCipher blockCipher, int n2) {
        this.cipher = blockCipher;
        this.blockSize = n2 / 8;
        this.IV = new byte[blockCipher.getBlockSize()];
        this.cfbV = new byte[blockCipher.getBlockSize()];
        this.cfbOutV = new byte[blockCipher.getBlockSize()];
    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CFB" + 8 * this.blockSize;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    void getMacBlock(byte[] arrby) {
        this.cipher.processBlock(this.cfbV, 0, arrby, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            this.reset();
            this.cipher.init(true, cipherParameters);
            return;
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] arrby = parametersWithIV.getIV();
        if (arrby.length < this.IV.length) {
            System.arraycopy((Object)arrby, (int)0, (Object)this.IV, (int)(this.IV.length - arrby.length), (int)arrby.length);
        } else {
            System.arraycopy((Object)arrby, (int)0, (Object)this.IV, (int)0, (int)this.IV.length);
        }
        this.reset();
        this.cipher.init(true, parametersWithIV.getParameters());
    }

    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            arrby2[n3 + i2] = (byte)(this.cfbOutV[i2] ^ arrby[n2 + i2]);
        }
        System.arraycopy((Object)this.cfbV, (int)this.blockSize, (Object)this.cfbV, (int)0, (int)(this.cfbV.length - this.blockSize));
        System.arraycopy((Object)arrby2, (int)n3, (Object)this.cfbV, (int)(this.cfbV.length - this.blockSize), (int)this.blockSize);
        return this.blockSize;
    }

    public void reset() {
        System.arraycopy((Object)this.IV, (int)0, (Object)this.cfbV, (int)0, (int)this.IV.length);
        this.cipher.reset();
    }
}

