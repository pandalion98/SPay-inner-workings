/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class OFBBlockCipher
extends StreamBlockCipher {
    private byte[] IV;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    private byte[] ofbOutV;
    private byte[] ofbV;

    public OFBBlockCipher(BlockCipher blockCipher, int n2) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = n2 / 8;
        this.IV = new byte[blockCipher.getBlockSize()];
        this.ofbV = new byte[blockCipher.getBlockSize()];
        this.ofbOutV = new byte[blockCipher.getBlockSize()];
    }

    @Override
    protected byte calculateByte(byte by) {
        if (this.byteCount == 0) {
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
        return this.cipher.getAlgorithmName() + "/OFB" + 8 * this.blockSize;
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
        System.arraycopy((Object)this.IV, (int)0, (Object)this.ofbV, (int)0, (int)this.IV.length);
        this.byteCount = 0;
        this.cipher.reset();
    }
}

