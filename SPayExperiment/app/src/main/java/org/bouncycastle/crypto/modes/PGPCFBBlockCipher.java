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
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PGPCFBBlockCipher
implements BlockCipher {
    private byte[] FR;
    private byte[] FRE;
    private byte[] IV;
    private int blockSize;
    private BlockCipher cipher;
    private int count;
    private boolean forEncryption;
    private boolean inlineIv;
    private byte[] tmp;

    public PGPCFBBlockCipher(BlockCipher blockCipher, boolean bl) {
        this.cipher = blockCipher;
        this.inlineIv = bl;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.FR = new byte[this.blockSize];
        this.FRE = new byte[this.blockSize];
        this.tmp = new byte[this.blockSize];
    }

    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        int n4 = 0;
        do {
            int n5 = this.blockSize;
            if (n4 >= n5) break;
            arrby2[n3 + n4] = this.encryptByte(arrby[n2 + n4], n4);
            ++n4;
        } while (true);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            this.FR[i2] = arrby[n2 + i2];
        }
        return this.blockSize;
    }

    private int decryptBlockWithIV(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.count == 0) {
            for (int i2 = 0; i2 < this.blockSize; ++i2) {
                this.FR[i2] = arrby[n2 + i2];
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            this.count += this.blockSize;
            return 0;
        }
        if (this.count == this.blockSize) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.tmp, (int)0, (int)this.blockSize);
            System.arraycopy((Object)this.FR, (int)2, (Object)this.FR, (int)0, (int)(-2 + this.blockSize));
            this.FR[-2 + this.blockSize] = this.tmp[0];
            this.FR[-1 + this.blockSize] = this.tmp[1];
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i3 = 0; i3 < -2 + this.blockSize; ++i3) {
                arrby2[n3 + i3] = this.encryptByte(this.tmp[i3 + 2], i3);
            }
            System.arraycopy((Object)this.tmp, (int)2, (Object)this.FR, (int)0, (int)(-2 + this.blockSize));
            this.count = 2 + this.count;
            return -2 + this.blockSize;
        }
        if (this.count >= 2 + this.blockSize) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.tmp, (int)0, (int)this.blockSize);
            arrby2[n3 + 0] = this.encryptByte(this.tmp[0], -2 + this.blockSize);
            arrby2[n3 + 1] = this.encryptByte(this.tmp[1], -1 + this.blockSize);
            System.arraycopy((Object)this.tmp, (int)0, (Object)this.FR, (int)(-2 + this.blockSize), (int)2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 0; i4 < -2 + this.blockSize; ++i4) {
                arrby2[2 + (n3 + i4)] = this.encryptByte(this.tmp[i4 + 2], i4);
            }
            System.arraycopy((Object)this.tmp, (int)2, (Object)this.FR, (int)0, (int)(-2 + this.blockSize));
        }
        return this.blockSize;
    }

    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        int n4 = 0;
        do {
            int n5 = this.blockSize;
            if (n4 >= n5) break;
            arrby2[n3 + n4] = this.encryptByte(arrby[n2 + n4], n4);
            ++n4;
        } while (true);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            this.FR[i2] = arrby2[n3 + i2];
        }
        return this.blockSize;
    }

    private int encryptBlockWithIV(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (this.count == 0) {
            if (2 + (n3 + 2 * this.blockSize) > arrby2.length) {
                throw new DataLengthException("output buffer too short");
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i2 = 0; i2 < this.blockSize; ++i2) {
                arrby2[n3 + i2] = this.encryptByte(this.IV[i2], i2);
            }
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.FR, (int)0, (int)this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            arrby2[n3 + this.blockSize] = this.encryptByte(this.IV[-2 + this.blockSize], 0);
            arrby2[1 + (n3 + this.blockSize)] = this.encryptByte(this.IV[-1 + this.blockSize], 1);
            System.arraycopy((Object)arrby2, (int)(n3 + 2), (Object)this.FR, (int)0, (int)this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i3 = 0; i3 < this.blockSize; ++i3) {
                arrby2[i3 + (2 + (n3 + this.blockSize))] = this.encryptByte(arrby[n2 + i3], i3);
            }
            System.arraycopy((Object)arrby2, (int)(2 + (n3 + this.blockSize)), (Object)this.FR, (int)0, (int)this.blockSize);
            this.count += 2 + 2 * this.blockSize;
            return 2 + 2 * this.blockSize;
        }
        if (this.count >= 2 + this.blockSize) {
            if (n3 + this.blockSize > arrby2.length) {
                throw new DataLengthException("output buffer too short");
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 0; i4 < this.blockSize; ++i4) {
                arrby2[n3 + i4] = this.encryptByte(arrby[n2 + i4], i4);
            }
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.FR, (int)0, (int)this.blockSize);
        }
        return this.blockSize;
    }

    private byte encryptByte(byte by, int n2) {
        return (byte)(by ^ this.FRE[n2]);
    }

    @Override
    public String getAlgorithmName() {
        if (this.inlineIv) {
            return this.cipher.getAlgorithmName() + "/PGPCFBwithIV";
        }
        return this.cipher.getAlgorithmName() + "/PGPCFB";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
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
            this.cipher.init(true, parametersWithIV.getParameters());
            return;
        }
        this.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.inlineIv) {
            if (this.forEncryption) {
                return this.encryptBlockWithIV(arrby, n2, arrby2, n3);
            }
            return this.decryptBlockWithIV(arrby, n2, arrby2, n3);
        }
        if (this.forEncryption) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void reset() {
        this.count = 0;
        int n2 = 0;
        do {
            if (n2 == this.FR.length) {
                this.cipher.reset();
                return;
            }
            this.FR[n2] = this.inlineIv ? (byte)0 : this.IV[n2];
            ++n2;
        } while (true);
    }
}

