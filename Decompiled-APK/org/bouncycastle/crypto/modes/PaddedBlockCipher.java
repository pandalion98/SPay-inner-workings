package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class PaddedBlockCipher extends BufferedBlockCipher {
    public PaddedBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    public int doFinal(byte[] bArr, int i) {
        int i2;
        int blockSize = this.cipher.getBlockSize();
        if (this.forEncryption) {
            if (this.bufOff != blockSize) {
                i2 = 0;
            } else if ((blockSize * 2) + i > bArr.length) {
                throw new DataLengthException("output buffer too short");
            } else {
                i2 = this.cipher.processBlock(this.buf, 0, bArr, i);
                this.bufOff = 0;
            }
            byte b = (byte) (blockSize - this.bufOff);
            while (this.bufOff < blockSize) {
                this.buf[this.bufOff] = b;
                this.bufOff++;
            }
            i2 += this.cipher.processBlock(this.buf, 0, bArr, i + i2);
        } else if (this.bufOff == blockSize) {
            i2 = this.cipher.processBlock(this.buf, 0, this.buf, 0);
            this.bufOff = 0;
            int i3 = this.buf[blockSize - 1] & GF2Field.MASK;
            if (i3 < 0 || i3 > blockSize) {
                throw new InvalidCipherTextException("pad block corrupted");
            }
            i2 -= i3;
            System.arraycopy(this.buf, 0, bArr, i, i2);
        } else {
            throw new DataLengthException("last block incomplete in decryption");
        }
        reset();
        return i2;
    }

    public int getOutputSize(int i) {
        int i2 = this.bufOff + i;
        int length = i2 % this.buf.length;
        return length == 0 ? this.forEncryption ? i2 + this.buf.length : i2 : (i2 - length) + this.buf.length;
    }

    public int getUpdateOutputSize(int i) {
        int i2 = this.bufOff + i;
        int length = i2 % this.buf.length;
        return length == 0 ? i2 - this.buf.length : i2 - length;
    }

    public int processByte(byte b, byte[] bArr, int i) {
        int processBlock;
        if (this.bufOff == this.buf.length) {
            processBlock = this.cipher.processBlock(this.buf, 0, bArr, i);
            this.bufOff = 0;
        } else {
            processBlock = 0;
        }
        byte[] bArr2 = this.buf;
        int i2 = this.bufOff;
        this.bufOff = i2 + 1;
        bArr2[i2] = b;
        return processBlock;
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int blockSize = getBlockSize();
        int updateOutputSize = getUpdateOutputSize(i2);
        if (updateOutputSize <= 0 || updateOutputSize + i3 <= bArr2.length) {
            int i4;
            int length = this.buf.length - this.bufOff;
            if (i2 > length) {
                System.arraycopy(bArr, i, this.buf, this.bufOff, length);
                updateOutputSize = this.cipher.processBlock(this.buf, 0, bArr2, i3) + 0;
                this.bufOff = 0;
                i4 = i2 - length;
                length += i;
                while (i4 > this.buf.length) {
                    updateOutputSize += this.cipher.processBlock(bArr, length, bArr2, i3 + updateOutputSize);
                    i4 -= blockSize;
                    length += blockSize;
                }
            } else {
                updateOutputSize = 0;
                length = i;
                i4 = i2;
            }
            System.arraycopy(bArr, length, this.buf, this.bufOff, i4);
            this.bufOff = i4 + this.bufOff;
            return updateOutputSize;
        }
        throw new DataLengthException("output buffer too short");
    }
}
