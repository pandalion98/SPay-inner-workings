/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class CCMBlockCipher
implements AEADBlockCipher {
    private ExposedByteArrayOutputStream associatedText = new ExposedByteArrayOutputStream();
    private int blockSize;
    private BlockCipher cipher;
    private ExposedByteArrayOutputStream data = new ExposedByteArrayOutputStream();
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonce;

    public CCMBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.macBlock = new byte[this.blockSize];
        if (this.blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int calculateMac(byte[] arrby, int n2, int n3, byte[] arrby2) {
        int n4 = 1;
        CBCBlockCipherMac cBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, 8 * this.macSize);
        cBCBlockCipherMac.init(this.keyParam);
        byte[] arrby3 = new byte[16];
        if (this.hasAssociatedText()) {
            arrby3[0] = (byte)(64 | arrby3[0]);
        }
        arrby3[0] = (byte)(arrby3[0] | (7 & (-2 + cBCBlockCipherMac.getMacSize()) / 2) << 3);
        arrby3[0] = (byte)(arrby3[0] | 7 & -1 + (15 - this.nonce.length));
        System.arraycopy((Object)this.nonce, (int)0, (Object)arrby3, (int)n4, (int)this.nonce.length);
        for (int i2 = n3; i2 > 0; i2 >>>= 8, ++n4) {
            arrby3[arrby3.length - n4] = (byte)(i2 & 255);
        }
        cBCBlockCipherMac.update(arrby3, 0, arrby3.length);
        if (this.hasAssociatedText()) {
            int n5;
            int n6;
            int n7 = this.getAssociatedTextLength();
            if (n7 < 65280) {
                cBCBlockCipherMac.update((byte)(n7 >> 8));
                cBCBlockCipherMac.update((byte)n7);
                n6 = 2;
            } else {
                cBCBlockCipherMac.update((byte)-1);
                cBCBlockCipherMac.update((byte)-2);
                cBCBlockCipherMac.update((byte)(n7 >> 24));
                cBCBlockCipherMac.update((byte)(n7 >> 16));
                cBCBlockCipherMac.update((byte)(n7 >> 8));
                cBCBlockCipherMac.update((byte)n7);
                n6 = 6;
            }
            if (this.initialAssociatedText != null) {
                cBCBlockCipherMac.update(this.initialAssociatedText, 0, this.initialAssociatedText.length);
            }
            if (this.associatedText.size() > 0) {
                cBCBlockCipherMac.update(this.associatedText.getBuffer(), 0, this.associatedText.size());
            }
            if ((n5 = (n6 + n7) % 16) != 0) {
                while (n5 != 16) {
                    cBCBlockCipherMac.update((byte)0);
                    ++n5;
                }
            }
        }
        cBCBlockCipherMac.update(arrby, n2, n3);
        return cBCBlockCipherMac.doFinal(arrby2, 0);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getAssociatedTextLength() {
        int n2;
        int n3 = this.associatedText.size();
        if (this.initialAssociatedText == null) {
            n2 = 0;
            do {
                return n2 + n3;
                break;
            } while (true);
        }
        n2 = this.initialAssociatedText.length;
        return n2 + n3;
    }

    private boolean hasAssociatedText() {
        return this.getAssociatedTextLength() > 0;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        int n3 = this.processPacket(this.data.getBuffer(), 0, this.data.size(), arrby, n2);
        this.reset();
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }

    @Override
    public byte[] getMac() {
        byte[] arrby = new byte[this.macSize];
        System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        return arrby;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.data.size();
        if (this.forEncryption) {
            return n3 + this.macSize;
        }
        if (n3 < this.macSize) {
            return 0;
        }
        return n3 - this.macSize;
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        void var4_5;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            KeyParameter keyParameter = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to CCM");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.macBlock.length / 2;
            CipherParameters cipherParameters2 = parametersWithIV.getParameters();
        }
        if (var4_5 != null) {
            this.keyParam = var4_5;
        }
        if (this.nonce != null && this.nonce.length >= 7 && this.nonce.length <= 13) {
            this.reset();
            return;
        }
        throw new IllegalArgumentException("nonce must have length from 7 to 13 octets");
    }

    @Override
    public void processAADByte(byte by) {
        this.associatedText.write((int)by);
    }

    @Override
    public void processAADBytes(byte[] arrby, int n2, int n3) {
        this.associatedText.write(arrby, n2, n3);
    }

    @Override
    public int processByte(byte by, byte[] arrby, int n2) {
        this.data.write((int)by);
        return 0;
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (arrby.length < n2 + n3) {
            throw new DataLengthException("Input buffer too short");
        }
        this.data.write(arrby, n2, n3);
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int processPacket(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        int n5;
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        int n6 = 15 - this.nonce.length;
        if (n6 < 4 && n3 >= 1 << n6 * 8) {
            throw new IllegalStateException("CCM packet too large for choice of q.");
        }
        byte[] arrby3 = new byte[this.blockSize];
        arrby3[0] = (byte)(7 & n6 - 1);
        System.arraycopy((Object)this.nonce, (int)0, (Object)arrby3, (int)1, (int)this.nonce.length);
        SICBlockCipher sICBlockCipher = new SICBlockCipher(this.cipher);
        sICBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, arrby3));
        if (this.forEncryption) {
            int n7 = n3 + this.macSize;
            if (arrby2.length < n7 + n4) {
                throw new OutputLengthException("Output buffer too short.");
            }
            this.calculateMac(arrby, n2, n3, this.macBlock);
            sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            int n8 = n4;
            int n9 = n2;
            do {
                if (n9 >= n2 + n3 - this.blockSize) {
                    byte[] arrby4 = new byte[this.blockSize];
                    System.arraycopy((Object)arrby, (int)n9, (Object)arrby4, (int)0, (int)(n3 + n2 - n9));
                    sICBlockCipher.processBlock(arrby4, 0, arrby4, 0);
                    System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)n8, (int)(n3 + n2 - n9));
                    System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby2, (int)(n4 + n3), (int)this.macSize);
                    return n7;
                }
                sICBlockCipher.processBlock(arrby, n9, arrby2, n8);
                n8 += this.blockSize;
                n9 += this.blockSize;
            } while (true);
        }
        if (n3 < this.macSize) {
            throw new InvalidCipherTextException("data too short");
        }
        int n10 = n3 - this.macSize;
        if (arrby2.length < n10 + n4) {
            throw new OutputLengthException("Output buffer too short.");
        }
        System.arraycopy((Object)arrby, (int)(n2 + n10), (Object)this.macBlock, (int)0, (int)this.macSize);
        sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
        for (int i2 = this.macSize; i2 != this.macBlock.length; ++i2) {
            this.macBlock[i2] = 0;
        }
        int n11 = n4;
        for (n5 = n2; n5 < n2 + n10 - this.blockSize; n11 += this.blockSize, n5 += this.blockSize) {
            sICBlockCipher.processBlock(arrby, n5, arrby2, n11);
        }
        byte[] arrby5 = new byte[this.blockSize];
        System.arraycopy((Object)arrby, (int)n5, (Object)arrby5, (int)0, (int)(n10 - (n5 - n2)));
        sICBlockCipher.processBlock(arrby5, 0, arrby5, 0);
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby2, (int)n11, (int)(n10 - (n5 - n2)));
        byte[] arrby6 = new byte[this.blockSize];
        this.calculateMac(arrby2, n4, n10, arrby6);
        if (!Arrays.constantTimeAreEqual((byte[])this.macBlock, (byte[])arrby6)) {
            throw new InvalidCipherTextException("mac check in CCM failed");
        }
        return n10;
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] processPacket(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        if (this.forEncryption) {
            arrby2 = new byte[n3 + this.macSize];
        } else {
            if (n3 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            arrby2 = new byte[n3 - this.macSize];
        }
        this.processPacket(arrby, n2, n3, arrby2, 0);
        return arrby2;
    }

    @Override
    public void reset() {
        this.cipher.reset();
        this.associatedText.reset();
        this.data.reset();
    }

    private class ExposedByteArrayOutputStream
    extends ByteArrayOutputStream {
        public byte[] getBuffer() {
            return this.buf;
        }
    }

}

