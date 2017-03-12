package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;

public class NISTCTSBlockCipher extends BufferedBlockCipher {
    public static final int CS1 = 1;
    public static final int CS2 = 2;
    public static final int CS3 = 3;
    private final int blockSize;
    private final int type;

    public NISTCTSBlockCipher(int i, BlockCipher blockCipher) {
        this.type = i;
        this.cipher = new CBCBlockCipher(blockCipher);
        this.blockSize = blockCipher.getBlockSize();
        this.buf = new byte[(this.blockSize * CS2)];
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
            if (this.bufOff < blockSize) {
                throw new DataLengthException("need at least one block of input for NISTCTS");
            } else if (this.bufOff > blockSize) {
                Object obj2 = new byte[blockSize];
                if (this.type == CS2 || this.type == CS3) {
                    this.cipher.processBlock(this.buf, 0, obj, 0);
                    System.arraycopy(this.buf, blockSize, obj2, 0, i3);
                    this.cipher.processBlock(obj2, 0, obj2, 0);
                    if (this.type == CS2 && i3 == blockSize) {
                        System.arraycopy(obj, 0, bArr, i, blockSize);
                        System.arraycopy(obj2, 0, bArr, blockSize + i, i3);
                    } else {
                        System.arraycopy(obj2, 0, bArr, i, blockSize);
                        System.arraycopy(obj, 0, bArr, i + blockSize, i3);
                    }
                } else {
                    System.arraycopy(this.buf, 0, obj, 0, blockSize);
                    this.cipher.processBlock(obj, 0, obj, 0);
                    System.arraycopy(obj, 0, bArr, i, i3);
                    System.arraycopy(this.buf, this.bufOff - i3, obj2, 0, i3);
                    this.cipher.processBlock(obj2, 0, obj2, 0);
                    System.arraycopy(obj2, 0, bArr, i3 + i, blockSize);
                }
            } else {
                this.cipher.processBlock(this.buf, 0, obj, 0);
                System.arraycopy(obj, 0, bArr, i, blockSize);
            }
        } else if (this.bufOff < blockSize) {
            throw new DataLengthException("need at least one block of input for CTS");
        } else {
            Object obj3 = new byte[blockSize];
            if (this.bufOff <= blockSize) {
                this.cipher.processBlock(this.buf, 0, obj, 0);
                System.arraycopy(obj, 0, bArr, i, blockSize);
            } else if (this.type == CS3 || (this.type == CS2 && (this.buf.length - this.bufOff) % blockSize != 0)) {
                if (this.cipher instanceof CBCBlockCipher) {
                    ((CBCBlockCipher) this.cipher).getUnderlyingCipher().processBlock(this.buf, 0, obj, 0);
                } else {
                    this.cipher.processBlock(this.buf, 0, obj, 0);
                }
                for (i2 = blockSize; i2 != this.bufOff; i2 += CS1) {
                    obj3[i2 - blockSize] = (byte) (obj[i2 - blockSize] ^ this.buf[i2]);
                }
                System.arraycopy(this.buf, blockSize, obj, 0, i3);
                this.cipher.processBlock(obj, 0, bArr, i);
                System.arraycopy(obj3, 0, bArr, i + blockSize, i3);
            } else {
                ((CBCBlockCipher) this.cipher).getUnderlyingCipher().processBlock(this.buf, this.bufOff - blockSize, obj3, 0);
                System.arraycopy(this.buf, 0, obj, 0, blockSize);
                if (i3 != blockSize) {
                    System.arraycopy(obj3, i3, obj, i3, blockSize - i3);
                }
                this.cipher.processBlock(obj, 0, obj, 0);
                System.arraycopy(obj, 0, bArr, i, blockSize);
                for (i2 = 0; i2 != i3; i2 += CS1) {
                    obj3[i2] = (byte) (obj3[i2] ^ this.buf[i2]);
                }
                System.arraycopy(obj3, 0, bArr, i + blockSize, i3);
            }
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
        this.bufOff = i2 + CS1;
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
