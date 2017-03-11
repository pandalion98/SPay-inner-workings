package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

public class OpenPGPCFBBlockCipher implements BlockCipher {
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

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 2;
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            byte b;
            byte b2;
            if (this.count > this.blockSize) {
                b = bArr[i];
                this.FR[this.blockSize - 2] = b;
                bArr2[i2] = encryptByte(b, this.blockSize - 2);
                b = bArr[i + 1];
                this.FR[this.blockSize - 1] = b;
                bArr2[i2 + 1] = encryptByte(b, this.blockSize - 1);
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i3 < this.blockSize) {
                    b2 = bArr[i + i3];
                    this.FR[i3 - 2] = b2;
                    bArr2[i2 + i3] = encryptByte(b2, i3 - 2);
                    i3++;
                }
            } else if (this.count == 0) {
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                for (i3 = 0; i3 < this.blockSize; i3++) {
                    this.FR[i3] = bArr[i + i3];
                    bArr2[i3] = encryptByte(bArr[i + i3], i3);
                }
                this.count += this.blockSize;
            } else if (this.count == this.blockSize) {
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                b = bArr[i];
                byte b3 = bArr[i + 1];
                bArr2[i2] = encryptByte(b, 0);
                bArr2[i2 + 1] = encryptByte(b3, 1);
                System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
                this.FR[this.blockSize - 2] = b;
                this.FR[this.blockSize - 1] = b3;
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i3 < this.blockSize) {
                    b2 = bArr[i + i3];
                    this.FR[i3 - 2] = b2;
                    bArr2[i2 + i3] = encryptByte(b2, i3 - 2);
                    i3++;
                }
                this.count += this.blockSize;
            }
            return this.blockSize;
        }
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 2;
        if (this.blockSize + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (this.blockSize + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            int i4;
            byte encryptByte;
            byte[] bArr3;
            int i5;
            if (this.count > this.blockSize) {
                byte[] bArr4 = this.FR;
                i4 = this.blockSize - 2;
                encryptByte = encryptByte(bArr[i], this.blockSize - 2);
                bArr2[i2] = encryptByte;
                bArr4[i4] = encryptByte;
                bArr4 = this.FR;
                i4 = this.blockSize - 1;
                int i6 = i2 + 1;
                byte encryptByte2 = encryptByte(bArr[i + 1], this.blockSize - 1);
                bArr2[i6] = encryptByte2;
                bArr4[i4] = encryptByte2;
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i3 < this.blockSize) {
                    bArr3 = this.FR;
                    i5 = i3 - 2;
                    i4 = i2 + i3;
                    encryptByte = encryptByte(bArr[i + i3], i3 - 2);
                    bArr2[i4] = encryptByte;
                    bArr3[i5] = encryptByte;
                    i3++;
                }
            } else if (this.count == 0) {
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                for (i3 = 0; i3 < this.blockSize; i3++) {
                    bArr3 = this.FR;
                    i5 = i2 + i3;
                    byte encryptByte3 = encryptByte(bArr[i + i3], i3);
                    bArr2[i5] = encryptByte3;
                    bArr3[i3] = encryptByte3;
                }
                this.count += this.blockSize;
            } else if (this.count == this.blockSize) {
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                bArr2[i2] = encryptByte(bArr[i], 0);
                bArr2[i2 + 1] = encryptByte(bArr[i + 1], 1);
                System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
                System.arraycopy(bArr2, i2, this.FR, this.blockSize - 2, 2);
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i3 < this.blockSize) {
                    bArr3 = this.FR;
                    i5 = i3 - 2;
                    i4 = i2 + i3;
                    encryptByte = encryptByte(bArr[i + i3], i3 - 2);
                    bArr2[i4] = encryptByte;
                    bArr3[i5] = encryptByte;
                    i3++;
                }
                this.count += this.blockSize;
            }
            return this.blockSize;
        }
    }

    private byte encryptByte(byte b, int i) {
        return (byte) (this.FRE[i] ^ b);
    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/OpenPGPCFB";
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        reset();
        this.cipher.init(true, cipherParameters);
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        return this.forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
    }

    public void reset() {
        this.count = 0;
        System.arraycopy(this.IV, 0, this.FR, 0, this.FR.length);
        this.cipher.reset();
    }
}
