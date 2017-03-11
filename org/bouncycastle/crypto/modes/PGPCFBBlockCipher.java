package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PGPCFBBlockCipher implements BlockCipher {
    private byte[] FR;
    private byte[] FRE;
    private byte[] IV;
    private int blockSize;
    private BlockCipher cipher;
    private int count;
    private boolean forEncryption;
    private boolean inlineIv;
    private byte[] tmp;

    public PGPCFBBlockCipher(BlockCipher blockCipher, boolean z) {
        this.cipher = blockCipher;
        this.inlineIv = z;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.FR = new byte[this.blockSize];
        this.FRE = new byte[this.blockSize];
        this.tmp = new byte[this.blockSize];
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 0; i4 < this.blockSize; i4++) {
                bArr2[i2 + i4] = encryptByte(bArr[i + i4], i4);
            }
            while (i3 < this.blockSize) {
                this.FR[i3] = bArr[i + i3];
                i3++;
            }
            return this.blockSize;
        }
    }

    private int decryptBlockWithIV(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else if (this.count == 0) {
            for (r0 = 0; r0 < this.blockSize; r0++) {
                this.FR[r0] = bArr[i + r0];
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            this.count += this.blockSize;
            return 0;
        } else if (this.count == this.blockSize) {
            System.arraycopy(bArr, i, this.tmp, 0, this.blockSize);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            this.FR[this.blockSize - 2] = this.tmp[0];
            this.FR[this.blockSize - 1] = this.tmp[1];
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (r0 = 0; r0 < this.blockSize - 2; r0++) {
                bArr2[i2 + r0] = encryptByte(this.tmp[r0 + 2], r0);
            }
            System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
            this.count += 2;
            return this.blockSize - 2;
        } else {
            if (this.count >= this.blockSize + 2) {
                System.arraycopy(bArr, i, this.tmp, 0, this.blockSize);
                bArr2[i2 + 0] = encryptByte(this.tmp[0], this.blockSize - 2);
                bArr2[i2 + 1] = encryptByte(this.tmp[1], this.blockSize - 1);
                System.arraycopy(this.tmp, 0, this.FR, this.blockSize - 2, 2);
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                for (r0 = 0; r0 < this.blockSize - 2; r0++) {
                    bArr2[(i2 + r0) + 2] = encryptByte(this.tmp[r0 + 2], r0);
                }
                System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
            }
            return this.blockSize;
        }
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 0; i4 < this.blockSize; i4++) {
                bArr2[i2 + i4] = encryptByte(bArr[i + i4], i4);
            }
            while (i3 < this.blockSize) {
                this.FR[i3] = bArr2[i2 + i3];
                i3++;
            }
            return this.blockSize;
        }
    }

    private int encryptBlockWithIV(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.count != 0) {
            if (this.count >= this.blockSize + 2) {
                if (this.blockSize + i2 > bArr2.length) {
                    throw new DataLengthException("output buffer too short");
                }
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                for (r0 = 0; r0 < this.blockSize; r0++) {
                    bArr2[i2 + r0] = encryptByte(bArr[i + r0], r0);
                }
                System.arraycopy(bArr2, i2, this.FR, 0, this.blockSize);
            }
            return this.blockSize;
        } else if (((this.blockSize * 2) + i2) + 2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (r0 = 0; r0 < this.blockSize; r0++) {
                bArr2[i2 + r0] = encryptByte(this.IV[r0], r0);
            }
            System.arraycopy(bArr2, i2, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            bArr2[this.blockSize + i2] = encryptByte(this.IV[this.blockSize - 2], 0);
            bArr2[(this.blockSize + i2) + 1] = encryptByte(this.IV[this.blockSize - 1], 1);
            System.arraycopy(bArr2, i2 + 2, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (r0 = 0; r0 < this.blockSize; r0++) {
                bArr2[((this.blockSize + i2) + 2) + r0] = encryptByte(bArr[i + r0], r0);
            }
            System.arraycopy(bArr2, (this.blockSize + i2) + 2, this.FR, 0, this.blockSize);
            this.count += (this.blockSize * 2) + 2;
            return (this.blockSize * 2) + 2;
        }
    }

    private byte encryptByte(byte b, int i) {
        return (byte) (this.FRE[i] ^ b);
    }

    public String getAlgorithmName() {
        return this.inlineIv ? this.cipher.getAlgorithmName() + "/PGPCFBwithIV" : this.cipher.getAlgorithmName() + "/PGPCFB";
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            Object iv = parametersWithIV.getIV();
            if (iv.length < this.IV.length) {
                System.arraycopy(iv, 0, this.IV, this.IV.length - iv.length, iv.length);
                for (int i = 0; i < this.IV.length - iv.length; i++) {
                    this.IV[i] = (byte) 0;
                }
            } else {
                System.arraycopy(iv, 0, this.IV, 0, this.IV.length);
            }
            reset();
            this.cipher.init(true, parametersWithIV.getParameters());
            return;
        }
        reset();
        this.cipher.init(true, cipherParameters);
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        return this.inlineIv ? this.forEncryption ? encryptBlockWithIV(bArr, i, bArr2, i2) : decryptBlockWithIV(bArr, i, bArr2, i2) : this.forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
    }

    public void reset() {
        this.count = 0;
        for (int i = 0; i != this.FR.length; i++) {
            if (this.inlineIv) {
                this.FR[i] = (byte) 0;
            } else {
                this.FR[i] = this.IV[i];
            }
        }
        this.cipher.reset();
    }
}
