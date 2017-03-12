package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;

public class OldCTSBlockCipher extends BufferedBlockCipher {
    private int blockSize;

    public OldCTSBlockCipher(BlockCipher blockCipher) {
        if ((blockCipher instanceof OFBBlockCipher) || (blockCipher instanceof CFBBlockCipher)) {
            throw new IllegalArgumentException("CTSBlockCipher can only accept ECB, or CBC ciphers");
        }
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.buf = new byte[(this.blockSize * 2)];
        this.bufOff = 0;
    }

    public int doFinal(byte[] bArr, int i) {
        if (this.bufOff + i > bArr.length) {
            throw new DataLengthException("output buffer to small in doFinal");
        }
        int i2;
        int blockSize = this.cipher.getBlockSize();
        int i3 = this.bufOff - blockSize;
        Object obj = new byte[blockSize];
        if (this.forEncryption) {
            this.cipher.processBlock(this.buf, 0, obj, 0);
            if (this.bufOff < blockSize) {
                throw new DataLengthException("need at least one block of input for CTS");
            }
            for (i2 = this.bufOff; i2 != this.buf.length; i2++) {
                this.buf[i2] = obj[i2 - blockSize];
            }
            for (i2 = blockSize; i2 != this.bufOff; i2++) {
                byte[] bArr2 = this.buf;
                bArr2[i2] = (byte) (bArr2[i2] ^ obj[i2 - blockSize]);
            }
            if (this.cipher instanceof CBCBlockCipher) {
                ((CBCBlockCipher) this.cipher).getUnderlyingCipher().processBlock(this.buf, blockSize, bArr, i);
            } else {
                this.cipher.processBlock(this.buf, blockSize, bArr, i);
            }
            System.arraycopy(obj, 0, bArr, i + blockSize, i3);
        } else {
            Object obj2 = new byte[blockSize];
            if (this.cipher instanceof CBCBlockCipher) {
                ((CBCBlockCipher) this.cipher).getUnderlyingCipher().processBlock(this.buf, 0, obj, 0);
            } else {
                this.cipher.processBlock(this.buf, 0, obj, 0);
            }
            for (i2 = blockSize; i2 != this.bufOff; i2++) {
                obj2[i2 - blockSize] = (byte) (obj[i2 - blockSize] ^ this.buf[i2]);
            }
            System.arraycopy(this.buf, blockSize, obj, 0, i3);
            this.cipher.processBlock(obj, 0, bArr, i);
            System.arraycopy(obj2, 0, bArr, i + blockSize, i3);
        }
        i2 = this.bufOff;
        reset();
        return i2;
    }

    public int getOutputSize(int i) {
        return this.bufOff + i;
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
            System.arraycopy(this.buf, this.blockSize, this.buf, 0, this.blockSize);
            this.bufOff = this.blockSize;
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
            int length = this.buf.length - this.bufOff;
            if (i2 > length) {
                System.arraycopy(bArr, i, this.buf, this.bufOff, length);
                updateOutputSize = this.cipher.processBlock(this.buf, 0, bArr2, i3) + 0;
                System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
                this.bufOff = blockSize;
                i2 -= length;
                i += length;
                while (i2 > blockSize) {
                    System.arraycopy(bArr, i, this.buf, this.bufOff, blockSize);
                    updateOutputSize += this.cipher.processBlock(this.buf, 0, bArr2, i3 + updateOutputSize);
                    System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
                    i2 -= blockSize;
                    i += blockSize;
                }
            } else {
                updateOutputSize = 0;
            }
            System.arraycopy(bArr, i, this.buf, this.bufOff, i2);
            this.bufOff += i2;
            return updateOutputSize;
        }
        throw new DataLengthException("output buffer too short");
    }
}
