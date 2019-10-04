/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class CBCBlockCipher
implements BlockCipher {
    private byte[] IV;
    private int blockSize;
    private byte[] cbcNextV;
    private byte[] cbcV;
    private BlockCipher cipher = null;
    private boolean encrypting;

    public CBCBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.cbcV = new byte[this.blockSize];
        this.cbcNextV = new byte[this.blockSize];
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.cbcNextV, (int)0, (int)this.blockSize);
        int n4 = this.cipher.processBlock(arrby, n2, arrby2, n3);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            int n5 = n3 + i2;
            arrby2[n5] = (byte)(arrby2[n5] ^ this.cbcV[i2]);
        }
        byte[] arrby3 = this.cbcV;
        this.cbcV = this.cbcNextV;
        this.cbcNextV = arrby3;
        return n4;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            byte[] arrby3 = this.cbcV;
            arrby3[i2] = (byte)(arrby3[i2] ^ arrby[n2 + i2]);
        }
        int n4 = this.cipher.processBlock(this.cbcV, 0, arrby2, n3);
        System.arraycopy((Object)arrby2, (int)n3, (Object)this.cbcV, (int)0, (int)this.cbcV.length);
        return n4;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CBC";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        boolean bl2 = this.encrypting;
        this.encrypting = bl;
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby = parametersWithIV.getIV();
            if (arrby.length != this.blockSize) {
                throw new IllegalArgumentException("initialisation vector must be the same length as block size");
            }
            System.arraycopy((Object)arrby, (int)0, (Object)this.IV, (int)0, (int)arrby.length);
            this.reset();
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(bl, parametersWithIV.getParameters());
                return;
            } else {
                if (bl2 == bl) return;
                {
                    throw new IllegalArgumentException("cannot change encrypting state without providing key.");
                }
            }
        } else {
            this.reset();
            if (cipherParameters != null) {
                this.cipher.init(bl, cipherParameters);
                return;
            }
            if (bl2 == bl) return;
            {
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
        }
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.encrypting) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
        System.arraycopy((Object)this.IV, (int)0, (Object)this.cbcV, (int)0, (int)this.IV.length);
        Arrays.fill((byte[])this.cbcNextV, (byte)0);
        this.cipher.reset();
    }
}

