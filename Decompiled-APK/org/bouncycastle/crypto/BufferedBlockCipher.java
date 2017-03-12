package org.bouncycastle.crypto;

public class BufferedBlockCipher {
    protected byte[] buf;
    protected int bufOff;
    protected BlockCipher cipher;
    protected boolean forEncryption;
    protected boolean partialBlockOkay;
    protected boolean pgpCFB;

    protected BufferedBlockCipher() {
    }

    public BufferedBlockCipher(BlockCipher blockCipher) {
        boolean z = true;
        this.cipher = blockCipher;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
        String algorithmName = blockCipher.getAlgorithmName();
        int indexOf = algorithmName.indexOf(47) + 1;
        boolean z2 = indexOf > 0 && algorithmName.startsWith("PGP", indexOf);
        this.pgpCFB = z2;
        if (this.pgpCFB || (blockCipher instanceof StreamCipher)) {
            this.partialBlockOkay = true;
            return;
        }
        if (indexOf <= 0 || !algorithmName.startsWith("OpenPGP", indexOf)) {
            z = false;
        }
        this.partialBlockOkay = z;
    }

    public int doFinal(byte[] bArr, int i) {
        int i2 = 0;
        try {
            if (this.bufOff + i > bArr.length) {
                throw new OutputLengthException("output buffer too short for doFinal()");
            }
            if (this.bufOff != 0) {
                if (this.partialBlockOkay) {
                    this.cipher.processBlock(this.buf, 0, this.buf, 0);
                    i2 = this.bufOff;
                    this.bufOff = 0;
                    System.arraycopy(this.buf, 0, bArr, i, i2);
                } else {
                    throw new DataLengthException("data not block size aligned");
                }
            }
            reset();
            return i2;
        } catch (Throwable th) {
            reset();
        }
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public int getOutputSize(int i) {
        return this.bufOff + i;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public int getUpdateOutputSize(int i) {
        int i2 = i + this.bufOff;
        return i2 - (this.pgpCFB ? (i2 % this.buf.length) - (this.cipher.getBlockSize() + 2) : i2 % this.buf.length);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        reset();
        this.cipher.init(z, cipherParameters);
    }

    public int processByte(byte b, byte[] bArr, int i) {
        byte[] bArr2 = this.buf;
        int i2 = this.bufOff;
        this.bufOff = i2 + 1;
        bArr2[i2] = b;
        if (this.bufOff != this.buf.length) {
            return 0;
        }
        int processBlock = this.cipher.processBlock(this.buf, 0, bArr, i);
        this.bufOff = 0;
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
                i4 = i2;
                length = i;
            }
            System.arraycopy(bArr, length, this.buf, this.bufOff, i4);
            this.bufOff = i4 + this.bufOff;
            if (this.bufOff != this.buf.length) {
                return updateOutputSize;
            }
            updateOutputSize += this.cipher.processBlock(this.buf, 0, bArr2, i3 + updateOutputSize);
            this.bufOff = 0;
            return updateOutputSize;
        }
        throw new OutputLengthException("output buffer too short");
    }

    public void reset() {
        for (int i = 0; i < this.buf.length; i++) {
            this.buf[i] = (byte) 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}
