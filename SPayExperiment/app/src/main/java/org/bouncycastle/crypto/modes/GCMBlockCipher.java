/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.crypto.modes.gcm.Tables1kGCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.Tables8kGCMMultiplier;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class GCMBlockCipher
implements AEADBlockCipher {
    private static final int BLOCK_SIZE = 16;
    private byte[] H;
    private byte[] J0;
    private byte[] S;
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
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
        if (gCMMultiplier == null) {
            gCMMultiplier = new Tables8kGCMMultiplier();
        }
        this.cipher = blockCipher;
        this.multiplier = gCMMultiplier;
    }

    private void gCTRBlock(byte[] arrby, byte[] arrby2, int n2) {
        byte[] arrby3 = this.getNextCounterBlock();
        GCMUtil.xor(arrby3, arrby);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n2, (int)16);
        byte[] arrby4 = this.S;
        if (this.forEncryption) {
            arrby = arrby3;
        }
        this.gHASHBlock(arrby4, arrby);
        this.totalLength = 16L + this.totalLength;
    }

    private void gCTRPartial(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        byte[] arrby3 = this.getNextCounterBlock();
        GCMUtil.xor(arrby3, arrby, n2, n3);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n4, (int)n3);
        byte[] arrby4 = this.S;
        if (this.forEncryption) {
            arrby = arrby3;
        }
        this.gHASHPartial(arrby4, arrby, 0, n3);
        this.totalLength += (long)n3;
    }

    private void gHASH(byte[] arrby, byte[] arrby2, int n2) {
        for (int i2 = 0; i2 < n2; i2 += 16) {
            this.gHASHPartial(arrby, arrby2, i2, Math.min((int)(n2 - i2), (int)16));
        }
    }

    private void gHASHBlock(byte[] arrby, byte[] arrby2) {
        GCMUtil.xor(arrby, arrby2);
        this.multiplier.multiplyH(arrby);
    }

    private void gHASHPartial(byte[] arrby, byte[] arrby2, int n2, int n3) {
        GCMUtil.xor(arrby, arrby2, n2, n3);
        this.multiplier.multiplyH(arrby);
    }

    private byte[] getNextCounterBlock() {
        int n2 = 15;
        do {
            block4 : {
                block3 : {
                    byte by;
                    if (n2 < 12) break block3;
                    this.counter[n2] = by = (byte)(255 & 1 + this.counter[n2]);
                    if (by == 0) break block4;
                }
                byte[] arrby = new byte[16];
                this.cipher.processBlock(this.counter, 0, arrby, 0);
                return arrby;
            }
            --n2;
        } while (true);
    }

    private void initCipher() {
        if (this.atLength > 0L) {
            System.arraycopy((Object)this.S_at, (int)0, (Object)this.S_atPre, (int)0, (int)16);
            this.atLengthPre = this.atLength;
        }
        if (this.atBlockPos > 0) {
            this.gHASHPartial(this.S_atPre, this.atBlock, 0, this.atBlockPos);
            this.atLengthPre += (long)this.atBlockPos;
        }
        if (this.atLengthPre > 0L) {
            System.arraycopy((Object)this.S_atPre, (int)0, (Object)this.S, (int)0, (int)16);
        }
    }

    private void outputBlock(byte[] arrby, int n2) {
        if (arrby.length < n2 + 16) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.totalLength == 0L) {
            this.initCipher();
        }
        this.gCTRBlock(this.bufBlock, arrby, n2);
        if (this.forEncryption) {
            this.bufOff = 0;
            return;
        }
        System.arraycopy((Object)this.bufBlock, (int)16, (Object)this.bufBlock, (int)0, (int)this.macSize);
        this.bufOff = this.macSize;
    }

    private void reset(boolean bl) {
        this.cipher.reset();
        this.S = new byte[16];
        this.S_at = new byte[16];
        this.S_atPre = new byte[16];
        this.atBlock = new byte[16];
        this.atBlockPos = 0;
        this.atLength = 0L;
        this.atLengthPre = 0L;
        this.counter = Arrays.clone((byte[])this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
        if (this.bufBlock != null) {
            Arrays.fill((byte[])this.bufBlock, (byte)0);
        }
        if (bl) {
            this.macBlock = null;
        }
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        if (this.totalLength == 0L) {
            this.initCipher();
        }
        int n3 = this.bufOff;
        if (!this.forEncryption) {
            if (n3 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            n3 -= this.macSize;
        }
        if (n3 > 0) {
            if (arrby.length < n2 + n3) {
                throw new OutputLengthException("Output buffer too short");
            }
            this.gCTRPartial(this.bufBlock, 0, n3, arrby, n2);
        }
        this.atLength += (long)this.atBlockPos;
        if (this.atLength > this.atLengthPre) {
            if (this.atBlockPos > 0) {
                this.gHASHPartial(this.S_at, this.atBlock, 0, this.atBlockPos);
            }
            if (this.atLengthPre > 0L) {
                GCMUtil.xor(this.S_at, this.S_atPre);
            }
            long l2 = 127L + 8L * this.totalLength >>> 7;
            byte[] arrby2 = new byte[16];
            if (this.exp == null) {
                this.exp = new Tables1kGCMExponentiator();
                this.exp.init(this.H);
            }
            this.exp.exponentiateX(l2, arrby2);
            GCMUtil.multiply(this.S_at, arrby2);
            GCMUtil.xor(this.S, this.S_at);
        }
        byte[] arrby3 = new byte[16];
        Pack.longToBigEndian((long)(8L * this.atLength), (byte[])arrby3, (int)0);
        Pack.longToBigEndian((long)(8L * this.totalLength), (byte[])arrby3, (int)8);
        this.gHASHBlock(this.S, arrby3);
        byte[] arrby4 = new byte[16];
        this.cipher.processBlock(this.J0, 0, arrby4, 0);
        GCMUtil.xor(arrby4, this.S);
        this.macBlock = new byte[this.macSize];
        System.arraycopy((Object)arrby4, (int)0, (Object)this.macBlock, (int)0, (int)this.macSize);
        if (this.forEncryption) {
            if (arrby.length < n2 + n3 + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby, (int)(n2 + this.bufOff), (int)this.macSize);
            n3 += this.macSize;
        } else {
            byte[] arrby5 = new byte[this.macSize];
            System.arraycopy((Object)this.bufBlock, (int)n3, (Object)arrby5, (int)0, (int)this.macSize);
            if (!Arrays.constantTimeAreEqual((byte[])this.macBlock, (byte[])arrby5)) {
                throw new InvalidCipherTextException("mac check in GCM failed");
            }
        }
        this.reset(false);
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/GCM";
    }

    @Override
    public byte[] getMac() {
        return Arrays.clone((byte[])this.macBlock);
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
        return this.cipher;
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
        return n3 - n3 % 16;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        KeyParameter keyParameter;
        this.forEncryption = bl;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            int n2 = aEADParameters.getMacSize();
            if (n2 < 32 || n2 > 128 || n2 % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + n2);
            }
            this.macSize = n2 / 8;
            keyParameter = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to GCM");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = 16;
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
        }
        int n3 = bl ? 16 : 16 + this.macSize;
        this.bufBlock = new byte[n3];
        if (this.nonce == null || this.nonce.length < 1) {
            throw new IllegalArgumentException("IV must be at least 1 byte");
        }
        if (keyParameter != null) {
            this.cipher.init(true, keyParameter);
            this.H = new byte[16];
            this.cipher.processBlock(this.H, 0, this.H, 0);
            this.multiplier.init(this.H);
            this.exp = null;
        } else if (this.H == null) {
            throw new IllegalArgumentException("Key must be specified in initial init");
        }
        this.J0 = new byte[16];
        if (this.nonce.length == 12) {
            System.arraycopy((Object)this.nonce, (int)0, (Object)this.J0, (int)0, (int)this.nonce.length);
            this.J0[15] = 1;
        } else {
            this.gHASH(this.J0, this.nonce, this.nonce.length);
            byte[] arrby = new byte[16];
            Pack.longToBigEndian((long)(8L * (long)this.nonce.length), (byte[])arrby, (int)8);
            this.gHASHBlock(this.J0, arrby);
        }
        this.S = new byte[16];
        this.S_at = new byte[16];
        this.S_atPre = new byte[16];
        this.atBlock = new byte[16];
        this.atBlockPos = 0;
        this.atLength = 0L;
        this.atLengthPre = 0L;
        this.counter = Arrays.clone((byte[])this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    @Override
    public void processAADByte(byte by) {
        int n2;
        this.atBlock[this.atBlockPos] = by;
        this.atBlockPos = n2 = 1 + this.atBlockPos;
        if (n2 == 16) {
            this.gHASHBlock(this.S_at, this.atBlock);
            this.atBlockPos = 0;
            this.atLength = 16L + this.atLength;
        }
    }

    @Override
    public void processAADBytes(byte[] arrby, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4;
            this.atBlock[this.atBlockPos] = arrby[n2 + i2];
            this.atBlockPos = n4 = 1 + this.atBlockPos;
            if (n4 != 16) continue;
            this.gHASHBlock(this.S_at, this.atBlock);
            this.atBlockPos = 0;
            this.atLength = 16L + this.atLength;
        }
    }

    @Override
    public int processByte(byte by, byte[] arrby, int n2) {
        int n3;
        this.bufBlock[this.bufOff] = by;
        this.bufOff = n3 = 1 + this.bufOff;
        if (n3 == this.bufBlock.length) {
            this.outputBlock(arrby, n2);
            return 16;
        }
        return 0;
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        int n5 = 0;
        if (arrby.length < n2 + n3) {
            throw new DataLengthException("Input buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            int n6;
            this.bufBlock[this.bufOff] = arrby[n2 + i2];
            this.bufOff = n6 = 1 + this.bufOff;
            if (n6 != this.bufBlock.length) continue;
            this.outputBlock(arrby2, n4 + n5);
            n5 += 16;
        }
        return n5;
    }

    @Override
    public void reset() {
        this.reset(true);
    }
}

