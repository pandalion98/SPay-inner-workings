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

public class OpenPGPCFBBlockCipher
implements BlockCipher {
    private byte[] FR;
    private byte[] FRE;
    private byte[] IV;
    private int blockSize;
    private BlockCipher cipher;
    private int count;
    private boolean forEncryption;

    public OpenPGPCFBBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.FR = new byte[this.blockSize];
        this.FRE = new byte[this.blockSize];
    }

    /*
     * Enabled aggressive block sorting
     */
    private int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = 2;
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.count > this.blockSize) {
            byte by;
            byte by2;
            this.FR[-2 + this.blockSize] = by2 = arrby[n2];
            arrby2[n3] = this.encryptByte(by2, -2 + this.blockSize);
            this.FR[-1 + this.blockSize] = by = arrby[n2 + 1];
            arrby2[n3 + 1] = this.encryptByte(by, -1 + this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            while (n4 < this.blockSize) {
                byte by3;
                this.FR[n4 - 2] = by3 = arrby[n2 + n4];
                arrby2[n3 + n4] = this.encryptByte(by3, n4 - 2);
                ++n4;
            }
            return this.blockSize;
        }
        if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            int n5 = 0;
            do {
                if (n5 >= this.blockSize) {
                    this.count += this.blockSize;
                    return this.blockSize;
                }
                this.FR[n5] = arrby[n2 + n5];
                arrby2[n5] = this.encryptByte(arrby[n2 + n5], n5);
                ++n5;
            } while (true);
        }
        if (this.count != this.blockSize) return this.blockSize;
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        byte by = arrby[n2];
        byte by4 = arrby[n2 + 1];
        arrby2[n3] = this.encryptByte(by, 0);
        arrby2[n3 + 1] = this.encryptByte(by4, 1);
        System.arraycopy((Object)this.FR, (int)n4, (Object)this.FR, (int)0, (int)(-2 + this.blockSize));
        this.FR[-2 + this.blockSize] = by;
        this.FR[-1 + this.blockSize] = by4;
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        do {
            byte by5;
            if (n4 >= this.blockSize) {
                this.count += this.blockSize;
                return this.blockSize;
            }
            this.FR[n4 - 2] = by5 = arrby[n2 + n4];
            arrby2[n3 + n4] = this.encryptByte(by5, n4 - 2);
            ++n4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = 2;
        if (n2 + this.blockSize > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.count > this.blockSize) {
            byte by;
            byte by2;
            byte[] arrby3 = this.FR;
            int n5 = -2 + this.blockSize;
            arrby2[n3] = by2 = this.encryptByte(arrby[n2], -2 + this.blockSize);
            arrby3[n5] = by2;
            byte[] arrby4 = this.FR;
            int n6 = -1 + this.blockSize;
            int n7 = n3 + 1;
            arrby2[n7] = by = this.encryptByte(arrby[n2 + 1], -1 + this.blockSize);
            arrby4[n6] = by;
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            while (n4 < this.blockSize) {
                byte by3;
                byte[] arrby5 = this.FR;
                int n8 = n4 - 2;
                int n9 = n3 + n4;
                arrby2[n9] = by3 = this.encryptByte(arrby[n2 + n4], n4 - 2);
                arrby5[n8] = by3;
                ++n4;
            }
            return this.blockSize;
        }
        if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            int n10 = 0;
            do {
                byte by;
                if (n10 >= this.blockSize) {
                    this.count += this.blockSize;
                    return this.blockSize;
                }
                byte[] arrby6 = this.FR;
                int n11 = n3 + n10;
                arrby2[n11] = by = this.encryptByte(arrby[n2 + n10], n10);
                arrby6[n10] = by;
                ++n10;
            } while (true);
        }
        if (this.count != this.blockSize) return this.blockSize;
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        arrby2[n3] = this.encryptByte(arrby[n2], 0);
        arrby2[n3 + 1] = this.encryptByte(arrby[n2 + 1], 1);
        System.arraycopy((Object)this.FR, (int)n4, (Object)this.FR, (int)0, (int)(-2 + this.blockSize));
        System.arraycopy((Object)arrby2, (int)n3, (Object)this.FR, (int)(-2 + this.blockSize), (int)n4);
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        do {
            byte by;
            if (n4 >= this.blockSize) {
                this.count += this.blockSize;
                return this.blockSize;
            }
            byte[] arrby7 = this.FR;
            int n12 = n4 - 2;
            int n13 = n3 + n4;
            arrby2[n13] = by = this.encryptByte(arrby[n2 + n4], n4 - 2);
            arrby7[n12] = by;
            ++n4;
        } while (true);
    }

    private byte encryptByte(byte by, int n2) {
        return (byte)(by ^ this.FRE[n2]);
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/OpenPGPCFB";
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
        this.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.forEncryption) {
            return this.encryptBlock(arrby, n2, arrby2, n3);
        }
        return this.decryptBlock(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
        this.count = 0;
        System.arraycopy((Object)this.IV, (int)0, (Object)this.FR, (int)0, (int)this.FR.length);
        this.cipher.reset();
    }
}

