package org.bouncycastle.crypto.paddings;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class PaddedBufferedBlockCipher extends BufferedBlockCipher {
    BlockCipherPadding padding;

    public PaddedBufferedBlockCipher(BlockCipher blockCipher) {
        this(blockCipher, new PKCS7Padding());
    }

    public PaddedBufferedBlockCipher(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
        this.cipher = blockCipher;
        this.padding = blockCipherPadding;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    public int doFinal(byte[] bArr, int i) {
        int blockSize = this.cipher.getBlockSize();
        if (this.forEncryption) {
            if (this.bufOff != blockSize) {
                blockSize = 0;
            } else if ((blockSize * 2) + i > bArr.length) {
                reset();
                throw new OutputLengthException("output buffer too short");
            } else {
                blockSize = this.cipher.processBlock(this.buf, 0, bArr, i);
                this.bufOff = 0;
            }
            this.padding.addPadding(this.buf, this.bufOff);
            blockSize += this.cipher.processBlock(this.buf, 0, bArr, i + blockSize);
            reset();
            return blockSize;
        } else if (this.bufOff == blockSize) {
            blockSize = this.cipher.processBlock(this.buf, 0, this.buf, 0);
            this.bufOff = 0;
            try {
                blockSize -= this.padding.padCount(this.buf);
                System.arraycopy(this.buf, 0, bArr, i, blockSize);
                return blockSize;
            } finally {
                reset();
            }
        } else {
            reset();
            throw new DataLengthException("last block incomplete in decryption");
        }
    }

    public int getOutputSize(int i) {
        int i2 = this.bufOff + i;
        int length = i2 % this.buf.length;
        return length == 0 ? this.forEncryption ? i2 + this.buf.length : i2 : (i2 - length) + this.buf.length;
    }

    public int getUpdateOutputSize(int i) {
        int i2 = this.bufOff + i;
        int length = i2 % this.buf.length;
        return length == 0 ? Math.max(0, i2 - this.buf.length) : i2 - length;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        reset();
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.padding.init(parametersWithRandom.getRandom());
            this.cipher.init(z, parametersWithRandom.getParameters());
            return;
        }
        this.padding.init(null);
        this.cipher.init(z, cipherParameters);
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
        throw new OutputLengthException("output buffer too short");
    }
}
