/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class EAXBlockCipher
implements AEADBlockCipher {
    private static final byte cTAG = 2;
    private static final byte hTAG = 1;
    private static final byte nTAG;
    private byte[] associatedTextMac;
    private int blockSize;
    private byte[] bufBlock;
    private int bufOff;
    private SICBlockCipher cipher;
    private boolean cipherInitialized;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private Mac mac;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonceMac;

    public EAXBlockCipher(BlockCipher blockCipher) {
        this.blockSize = blockCipher.getBlockSize();
        this.mac = new CMac(blockCipher);
        this.macBlock = new byte[this.blockSize];
        this.associatedTextMac = new byte[this.mac.getMacSize()];
        this.nonceMac = new byte[this.mac.getMacSize()];
        this.cipher = new SICBlockCipher(blockCipher);
    }

    private void calculateMac() {
        byte[] arrby = new byte[this.blockSize];
        this.mac.doFinal(arrby, 0);
        for (int i2 = 0; i2 < this.macBlock.length; ++i2) {
            this.macBlock[i2] = (byte)(this.nonceMac[i2] ^ this.associatedTextMac[i2] ^ arrby[i2]);
        }
    }

    private void initCipher() {
        if (this.cipherInitialized) {
            return;
        }
        this.cipherInitialized = true;
        this.mac.doFinal(this.associatedTextMac, 0);
        byte[] arrby = new byte[this.blockSize];
        arrby[-1 + this.blockSize] = 2;
        this.mac.update(arrby, 0, this.blockSize);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int process(byte by, byte[] arrby, int n2) {
        int n3;
        byte[] arrby2 = this.bufBlock;
        int n4 = this.bufOff;
        this.bufOff = n4 + 1;
        arrby2[n4] = by;
        if (this.bufOff != this.bufBlock.length) {
            return 0;
        }
        if (arrby.length < n2 + this.blockSize) {
            throw new OutputLengthException("Output buffer is too short");
        }
        if (this.forEncryption) {
            n3 = this.cipher.processBlock(this.bufBlock, 0, arrby, n2);
            this.mac.update(arrby, n2, this.blockSize);
        } else {
            this.mac.update(this.bufBlock, 0, this.blockSize);
            n3 = this.cipher.processBlock(this.bufBlock, 0, arrby, n2);
        }
        this.bufOff = 0;
        if (!this.forEncryption) {
            System.arraycopy((Object)this.bufBlock, (int)this.blockSize, (Object)this.bufBlock, (int)0, (int)this.macSize);
            this.bufOff = this.macSize;
        }
        return n3;
    }

    private void reset(boolean bl) {
        this.cipher.reset();
        this.mac.reset();
        this.bufOff = 0;
        Arrays.fill((byte[])this.bufBlock, (byte)0);
        if (bl) {
            Arrays.fill((byte[])this.macBlock, (byte)0);
        }
        byte[] arrby = new byte[this.blockSize];
        arrby[-1 + this.blockSize] = 1;
        this.mac.update(arrby, 0, this.blockSize);
        this.cipherInitialized = false;
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    private boolean verifyMac(byte[] arrby, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < this.macSize; ++i2) {
            n3 |= this.macBlock[i2] ^ arrby[n2 + i2];
        }
        boolean bl = false;
        if (n3 == 0) {
            bl = true;
        }
        return bl;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.initCipher();
        int n3 = this.bufOff;
        byte[] arrby2 = new byte[this.bufBlock.length];
        this.bufOff = 0;
        if (this.forEncryption) {
            if (arrby.length < n2 + n3 + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            this.cipher.processBlock(this.bufBlock, 0, arrby2, 0);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)n3);
            this.mac.update(arrby2, 0, n3);
            this.calculateMac();
            System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby, (int)(n2 + n3), (int)this.macSize);
            this.reset(false);
            return n3 + this.macSize;
        }
        if (arrby.length < n2 + n3 - this.macSize) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (n3 < this.macSize) {
            throw new InvalidCipherTextException("data too short");
        }
        if (n3 > this.macSize) {
            this.mac.update(this.bufBlock, 0, n3 - this.macSize);
            this.cipher.processBlock(this.bufBlock, 0, arrby2, 0);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)(n3 - this.macSize));
        }
        this.calculateMac();
        if (!this.verifyMac(this.bufBlock, n3 - this.macSize)) {
            throw new InvalidCipherTextException("mac check in EAX failed");
        }
        this.reset(false);
        return n3 - this.macSize;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getUnderlyingCipher().getAlgorithmName() + "/EAX";
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public byte[] getMac() {
        byte[] arrby = new byte[this.macSize];
        System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby, (int)0, (int)this.macSize);
        return arrby;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
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
        return this.cipher.getUnderlyingCipher();
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        if (!this.forEncryption) {
            if (n3 < this.macSize) {
                return 0;
            }
            n3 -= this.macSize;
        }
        return n3 - n3 % this.blockSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        void var7_8;
        byte[] arrby;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            byte[] arrby2 = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            KeyParameter keyParameter = aEADParameters.getKey();
            arrby = arrby2;
            KeyParameter keyParameter2 = keyParameter;
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to EAX");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby3 = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.mac.getMacSize() / 2;
            CipherParameters cipherParameters2 = parametersWithIV.getParameters();
            arrby = arrby3;
            CipherParameters cipherParameters3 = cipherParameters2;
        }
        int n2 = bl ? this.blockSize : this.blockSize + this.macSize;
        this.bufBlock = new byte[n2];
        byte[] arrby4 = new byte[this.blockSize];
        this.mac.init((CipherParameters)var7_8);
        arrby4[-1 + this.blockSize] = 0;
        this.mac.update(arrby4, 0, this.blockSize);
        this.mac.update(arrby, 0, arrby.length);
        this.mac.doFinal(this.nonceMac, 0);
        this.cipher.init(true, new ParametersWithIV(null, this.nonceMac));
        this.reset();
    }

    @Override
    public void processAADByte(byte by) {
        if (this.cipherInitialized) {
            throw new IllegalStateException("AAD data cannot be added after encryption/decription processing has begun.");
        }
        this.mac.update(by);
    }

    @Override
    public void processAADBytes(byte[] arrby, int n2, int n3) {
        if (this.cipherInitialized) {
            throw new IllegalStateException("AAD data cannot be added after encryption/decryption processing has begun.");
        }
        this.mac.update(arrby, n2, n3);
    }

    @Override
    public int processByte(byte by, byte[] arrby, int n2) {
        this.initCipher();
        return this.process(by, arrby, n2);
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        this.initCipher();
        if (arrby.length < n2 + n3) {
            throw new DataLengthException("Input buffer too short");
        }
        int n5 = 0;
        for (int i2 = 0; i2 != n3; ++i2) {
            n5 += this.process(arrby[n2 + i2], arrby2, n4 + n5);
        }
        return n5;
    }

    @Override
    public void reset() {
        this.reset(true);
    }
}

