package org.bouncycastle.crypto.modes;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class CCMBlockCipher implements AEADBlockCipher {
    private ExposedByteArrayOutputStream associatedText;
    private int blockSize;
    private BlockCipher cipher;
    private ExposedByteArrayOutputStream data;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonce;

    private class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        public byte[] getBuffer() {
            return this.buf;
        }
    }

    public CCMBlockCipher(BlockCipher blockCipher) {
        this.associatedText = new ExposedByteArrayOutputStream();
        this.data = new ExposedByteArrayOutputStream();
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.macBlock = new byte[this.blockSize];
        if (this.blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }

    private int calculateMac(byte[] bArr, int i, int i2, byte[] bArr2) {
        int i3 = 1;
        Mac cBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cBCBlockCipherMac.init(this.keyParam);
        Object obj = new byte[16];
        if (hasAssociatedText()) {
            obj[0] = (byte) (obj[0] | 64);
        }
        obj[0] = (byte) (obj[0] | ((((cBCBlockCipherMac.getMacSize() - 2) / 2) & 7) << 3));
        obj[0] = (byte) (obj[0] | (((15 - this.nonce.length) - 1) & 7));
        System.arraycopy(this.nonce, 0, obj, 1, this.nonce.length);
        int i4 = i2;
        while (i4 > 0) {
            obj[obj.length - i3] = (byte) (i4 & GF2Field.MASK);
            i4 >>>= 8;
            i3++;
        }
        cBCBlockCipherMac.update(obj, 0, obj.length);
        if (hasAssociatedText()) {
            i4 = getAssociatedTextLength();
            if (i4 < MotionEventCompat.ACTION_POINTER_INDEX_MASK) {
                cBCBlockCipherMac.update((byte) (i4 >> 8));
                cBCBlockCipherMac.update((byte) i4);
                i3 = 2;
            } else {
                cBCBlockCipherMac.update((byte) -1);
                cBCBlockCipherMac.update((byte) -2);
                cBCBlockCipherMac.update((byte) (i4 >> 24));
                cBCBlockCipherMac.update((byte) (i4 >> 16));
                cBCBlockCipherMac.update((byte) (i4 >> 8));
                cBCBlockCipherMac.update((byte) i4);
                i3 = 6;
            }
            if (this.initialAssociatedText != null) {
                cBCBlockCipherMac.update(this.initialAssociatedText, 0, this.initialAssociatedText.length);
            }
            if (this.associatedText.size() > 0) {
                cBCBlockCipherMac.update(this.associatedText.getBuffer(), 0, this.associatedText.size());
            }
            i3 = (i3 + i4) % 16;
            if (i3 != 0) {
                while (i3 != 16) {
                    cBCBlockCipherMac.update((byte) 0);
                    i3++;
                }
            }
        }
        cBCBlockCipherMac.update(bArr, i, i2);
        return cBCBlockCipherMac.doFinal(bArr2, 0);
    }

    private int getAssociatedTextLength() {
        return (this.initialAssociatedText == null ? 0 : this.initialAssociatedText.length) + this.associatedText.size();
    }

    private boolean hasAssociatedText() {
        return getAssociatedTextLength() > 0;
    }

    public int doFinal(byte[] bArr, int i) {
        int processPacket = processPacket(this.data.getBuffer(), 0, this.data.size(), bArr, i);
        reset();
        return processPacket;
    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }

    public byte[] getMac() {
        Object obj = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, obj, 0, obj.length);
        return obj;
    }

    public int getOutputSize(int i) {
        int size = this.data.size() + i;
        return this.forEncryption ? size + this.macSize : size < this.macSize ? 0 : size - this.macSize;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public int getUpdateOutputSize(int i) {
        return 0;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        CipherParameters key;
        this.forEncryption = z;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            key = aEADParameters.getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.macBlock.length / 2;
            key = parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameters passed to CCM");
        }
        if (key != null) {
            this.keyParam = key;
        }
        if (this.nonce == null || this.nonce.length < 7 || this.nonce.length > 13) {
            throw new IllegalArgumentException("nonce must have length from 7 to 13 octets");
        }
        reset();
    }

    public void processAADByte(byte b) {
        this.associatedText.write(b);
    }

    public void processAADBytes(byte[] bArr, int i, int i2) {
        this.associatedText.write(bArr, i, i2);
    }

    public int processByte(byte b, byte[] bArr, int i) {
        this.data.write(b);
        return 0;
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (bArr.length < i + i2) {
            throw new DataLengthException("Input buffer too short");
        }
        this.data.write(bArr, i, i2);
        return 0;
    }

    public int processPacket(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        int length = 15 - this.nonce.length;
        if (length >= 4 || i2 < (1 << (length * 8))) {
            Object obj = new byte[this.blockSize];
            obj[0] = (byte) ((length - 1) & 7);
            System.arraycopy(this.nonce, 0, obj, 1, this.nonce.length);
            BlockCipher sICBlockCipher = new SICBlockCipher(this.cipher);
            sICBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, obj));
            int i4;
            int i5;
            Object obj2;
            if (this.forEncryption) {
                i4 = i2 + this.macSize;
                if (bArr2.length < i4 + i3) {
                    throw new OutputLengthException("Output buffer too short.");
                }
                calculateMac(bArr, i, i2, this.macBlock);
                sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
                length = i3;
                i5 = i;
                while (i5 < (i + i2) - this.blockSize) {
                    sICBlockCipher.processBlock(bArr, i5, bArr2, length);
                    length += this.blockSize;
                    i5 += this.blockSize;
                }
                obj2 = new byte[this.blockSize];
                System.arraycopy(bArr, i5, obj2, 0, (i2 + i) - i5);
                sICBlockCipher.processBlock(obj2, 0, obj2, 0);
                System.arraycopy(obj2, 0, bArr2, length, (i2 + i) - i5);
                System.arraycopy(this.macBlock, 0, bArr2, i3 + i2, this.macSize);
                return i4;
            } else if (i2 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            } else {
                i4 = i2 - this.macSize;
                if (bArr2.length < i4 + i3) {
                    throw new OutputLengthException("Output buffer too short.");
                }
                System.arraycopy(bArr, i + i4, this.macBlock, 0, this.macSize);
                sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
                for (length = this.macSize; length != this.macBlock.length; length++) {
                    this.macBlock[length] = (byte) 0;
                }
                length = i3;
                i5 = i;
                while (i5 < (i + i4) - this.blockSize) {
                    sICBlockCipher.processBlock(bArr, i5, bArr2, length);
                    length += this.blockSize;
                    i5 += this.blockSize;
                }
                obj2 = new byte[this.blockSize];
                System.arraycopy(bArr, i5, obj2, 0, i4 - (i5 - i));
                sICBlockCipher.processBlock(obj2, 0, obj2, 0);
                System.arraycopy(obj2, 0, bArr2, length, i4 - (i5 - i));
                byte[] bArr3 = new byte[this.blockSize];
                calculateMac(bArr2, i3, i4, bArr3);
                if (Arrays.constantTimeAreEqual(this.macBlock, bArr3)) {
                    return i4;
                }
                throw new InvalidCipherTextException("mac check in CCM failed");
            }
        }
        throw new IllegalStateException("CCM packet too large for choice of q.");
    }

    public byte[] processPacket(byte[] bArr, int i, int i2) {
        byte[] bArr2;
        if (this.forEncryption) {
            bArr2 = new byte[(this.macSize + i2)];
        } else if (i2 < this.macSize) {
            throw new InvalidCipherTextException("data too short");
        } else {
            bArr2 = new byte[(i2 - this.macSize)];
        }
        processPacket(bArr, i, i2, bArr2, 0);
        return bArr2;
    }

    public void reset() {
        this.cipher.reset();
        this.associatedText.reset();
        this.data.reset();
    }
}
