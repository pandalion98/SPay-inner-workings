package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.crypto.modes.gcm.Tables1kGCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.Tables8kGCMMultiplier;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class GCMBlockCipher implements AEADBlockCipher {
    private static final int BLOCK_SIZE = 16;
    private byte[] f196H;
    private byte[] J0;
    private byte[] f197S;
    private byte[] S_at;
    private byte[] S_atPre;
    private byte[] atBlock;
    private int atBlockPos;
    private long atLength;
    private long atLengthPre;
    private byte[] bufBlock;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] counter;
    private GCMExponentiator exp;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private byte[] macBlock;
    private int macSize;
    private GCMMultiplier multiplier;
    private byte[] nonce;
    private long totalLength;

    public GCMBlockCipher(BlockCipher blockCipher) {
        this(blockCipher, null);
    }

    public GCMBlockCipher(BlockCipher blockCipher, GCMMultiplier gCMMultiplier) {
        if (blockCipher.getBlockSize() != BLOCK_SIZE) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
        if (gCMMultiplier == null) {
            gCMMultiplier = new Tables8kGCMMultiplier();
        }
        this.cipher = blockCipher;
        this.multiplier = gCMMultiplier;
    }

    private void gCTRBlock(byte[] bArr, byte[] bArr2, int i) {
        byte[] nextCounterBlock = getNextCounterBlock();
        GCMUtil.xor(nextCounterBlock, bArr);
        System.arraycopy(nextCounterBlock, 0, bArr2, i, BLOCK_SIZE);
        byte[] bArr3 = this.f197S;
        if (this.forEncryption) {
            bArr = nextCounterBlock;
        }
        gHASHBlock(bArr3, bArr);
        this.totalLength += 16;
    }

    private void gCTRPartial(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        Object nextCounterBlock = getNextCounterBlock();
        GCMUtil.xor(nextCounterBlock, bArr, i, i2);
        System.arraycopy(nextCounterBlock, 0, bArr2, i3, i2);
        byte[] bArr3 = this.f197S;
        if (this.forEncryption) {
            bArr = nextCounterBlock;
        }
        gHASHPartial(bArr3, bArr, 0, i2);
        this.totalLength += (long) i2;
    }

    private void gHASH(byte[] bArr, byte[] bArr2, int i) {
        for (int i2 = 0; i2 < i; i2 += BLOCK_SIZE) {
            gHASHPartial(bArr, bArr2, i2, Math.min(i - i2, BLOCK_SIZE));
        }
    }

    private void gHASHBlock(byte[] bArr, byte[] bArr2) {
        GCMUtil.xor(bArr, bArr2);
        this.multiplier.multiplyH(bArr);
    }

    private void gHASHPartial(byte[] bArr, byte[] bArr2, int i, int i2) {
        GCMUtil.xor(bArr, bArr2, i, i2);
        this.multiplier.multiplyH(bArr);
    }

    private byte[] getNextCounterBlock() {
        for (int i = 15; i >= 12; i--) {
            byte b = (byte) ((this.counter[i] + 1) & GF2Field.MASK);
            this.counter[i] = b;
            if (b != null) {
                break;
            }
        }
        byte[] bArr = new byte[BLOCK_SIZE];
        this.cipher.processBlock(this.counter, 0, bArr, 0);
        return bArr;
    }

    private void initCipher() {
        if (this.atLength > 0) {
            System.arraycopy(this.S_at, 0, this.S_atPre, 0, BLOCK_SIZE);
            this.atLengthPre = this.atLength;
        }
        if (this.atBlockPos > 0) {
            gHASHPartial(this.S_atPre, this.atBlock, 0, this.atBlockPos);
            this.atLengthPre += (long) this.atBlockPos;
        }
        if (this.atLengthPre > 0) {
            System.arraycopy(this.S_atPre, 0, this.f197S, 0, BLOCK_SIZE);
        }
    }

    private void outputBlock(byte[] bArr, int i) {
        if (bArr.length < i + BLOCK_SIZE) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.totalLength == 0) {
            initCipher();
        }
        gCTRBlock(this.bufBlock, bArr, i);
        if (this.forEncryption) {
            this.bufOff = 0;
            return;
        }
        System.arraycopy(this.bufBlock, BLOCK_SIZE, this.bufBlock, 0, this.macSize);
        this.bufOff = this.macSize;
    }

    private void reset(boolean z) {
        this.cipher.reset();
        this.f197S = new byte[BLOCK_SIZE];
        this.S_at = new byte[BLOCK_SIZE];
        this.S_atPre = new byte[BLOCK_SIZE];
        this.atBlock = new byte[BLOCK_SIZE];
        this.atBlockPos = 0;
        this.atLength = 0;
        this.atLengthPre = 0;
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0;
        if (this.bufBlock != null) {
            Arrays.fill(this.bufBlock, (byte) 0);
        }
        if (z) {
            this.macBlock = null;
        }
        if (this.initialAssociatedText != null) {
            processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    public int doFinal(byte[] bArr, int i) {
        if (this.totalLength == 0) {
            initCipher();
        }
        int i2 = this.bufOff;
        if (!this.forEncryption) {
            if (i2 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            i2 -= this.macSize;
        }
        if (i2 > 0) {
            if (bArr.length < i + i2) {
                throw new OutputLengthException("Output buffer too short");
            }
            gCTRPartial(this.bufBlock, 0, i2, bArr, i);
        }
        this.atLength += (long) this.atBlockPos;
        if (this.atLength > this.atLengthPre) {
            if (this.atBlockPos > 0) {
                gHASHPartial(this.S_at, this.atBlock, 0, this.atBlockPos);
            }
            if (this.atLengthPre > 0) {
                GCMUtil.xor(this.S_at, this.S_atPre);
            }
            long j = ((this.totalLength * 8) + 127) >>> 7;
            byte[] bArr2 = new byte[BLOCK_SIZE];
            if (this.exp == null) {
                this.exp = new Tables1kGCMExponentiator();
                this.exp.init(this.f196H);
            }
            this.exp.exponentiateX(j, bArr2);
            GCMUtil.multiply(this.S_at, bArr2);
            GCMUtil.xor(this.f197S, this.S_at);
        }
        byte[] bArr3 = new byte[BLOCK_SIZE];
        Pack.longToBigEndian(this.atLength * 8, bArr3, 0);
        Pack.longToBigEndian(this.totalLength * 8, bArr3, 8);
        gHASHBlock(this.f197S, bArr3);
        bArr3 = new byte[BLOCK_SIZE];
        this.cipher.processBlock(this.J0, 0, bArr3, 0);
        GCMUtil.xor(bArr3, this.f197S);
        this.macBlock = new byte[this.macSize];
        System.arraycopy(bArr3, 0, this.macBlock, 0, this.macSize);
        if (!this.forEncryption) {
            Object obj = new byte[this.macSize];
            System.arraycopy(this.bufBlock, i2, obj, 0, this.macSize);
            if (!Arrays.constantTimeAreEqual(this.macBlock, obj)) {
                throw new InvalidCipherTextException("mac check in GCM failed");
            }
        } else if (bArr.length < (i + i2) + this.macSize) {
            throw new OutputLengthException("Output buffer too short");
        } else {
            System.arraycopy(this.macBlock, 0, bArr, this.bufOff + i, this.macSize);
            i2 += this.macSize;
        }
        reset(false);
        return i2;
    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/GCM";
    }

    public byte[] getMac() {
        return Arrays.clone(this.macBlock);
    }

    public int getOutputSize(int i) {
        int i2 = this.bufOff + i;
        return this.forEncryption ? i2 + this.macSize : i2 < this.macSize ? 0 : i2 - this.macSize;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public int getUpdateOutputSize(int i) {
        int i2 = this.bufOff + i;
        if (!this.forEncryption) {
            if (i2 < this.macSize) {
                return 0;
            }
            i2 -= this.macSize;
        }
        return i2 - (i2 % BLOCK_SIZE);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            int macSize = aEADParameters.getMacSize();
            if (macSize < 32 || macSize > X509KeyUsage.digitalSignature || macSize % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + macSize);
            }
            this.macSize = macSize / 8;
            CipherParameters key = aEADParameters.getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = BLOCK_SIZE;
            Object obj = (KeyParameter) parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameters passed to GCM");
        }
        this.bufBlock = new byte[(z ? BLOCK_SIZE : this.macSize + BLOCK_SIZE)];
        if (this.nonce == null || this.nonce.length < 1) {
            throw new IllegalArgumentException("IV must be at least 1 byte");
        }
        if (key != null) {
            this.cipher.init(true, key);
            this.f196H = new byte[BLOCK_SIZE];
            this.cipher.processBlock(this.f196H, 0, this.f196H, 0);
            this.multiplier.init(this.f196H);
            this.exp = null;
        } else if (this.f196H == null) {
            throw new IllegalArgumentException("Key must be specified in initial init");
        }
        this.J0 = new byte[BLOCK_SIZE];
        if (this.nonce.length == 12) {
            System.arraycopy(this.nonce, 0, this.J0, 0, this.nonce.length);
            this.J0[15] = (byte) 1;
        } else {
            gHASH(this.J0, this.nonce, this.nonce.length);
            byte[] bArr = new byte[BLOCK_SIZE];
            Pack.longToBigEndian(((long) this.nonce.length) * 8, bArr, 8);
            gHASHBlock(this.J0, bArr);
        }
        this.f197S = new byte[BLOCK_SIZE];
        this.S_at = new byte[BLOCK_SIZE];
        this.S_atPre = new byte[BLOCK_SIZE];
        this.atBlock = new byte[BLOCK_SIZE];
        this.atBlockPos = 0;
        this.atLength = 0;
        this.atLengthPre = 0;
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0;
        if (this.initialAssociatedText != null) {
            processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    public void processAADByte(byte b) {
        this.atBlock[this.atBlockPos] = b;
        int i = this.atBlockPos + 1;
        this.atBlockPos = i;
        if (i == BLOCK_SIZE) {
            gHASHBlock(this.S_at, this.atBlock);
            this.atBlockPos = 0;
            this.atLength += 16;
        }
    }

    public void processAADBytes(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            this.atBlock[this.atBlockPos] = bArr[i + i3];
            int i4 = this.atBlockPos + 1;
            this.atBlockPos = i4;
            if (i4 == BLOCK_SIZE) {
                gHASHBlock(this.S_at, this.atBlock);
                this.atBlockPos = 0;
                this.atLength += 16;
            }
        }
    }

    public int processByte(byte b, byte[] bArr, int i) {
        this.bufBlock[this.bufOff] = b;
        int i2 = this.bufOff + 1;
        this.bufOff = i2;
        if (i2 != this.bufBlock.length) {
            return 0;
        }
        outputBlock(bArr, i);
        return BLOCK_SIZE;
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = 0;
        if (bArr.length < i + i2) {
            throw new DataLengthException("Input buffer too short");
        }
        for (int i5 = 0; i5 < i2; i5++) {
            this.bufBlock[this.bufOff] = bArr[i + i5];
            int i6 = this.bufOff + 1;
            this.bufOff = i6;
            if (i6 == this.bufBlock.length) {
                outputBlock(bArr2, i3 + i4);
                i4 += BLOCK_SIZE;
            }
        }
        return i4;
    }

    public void reset() {
        reset(true);
    }
}
