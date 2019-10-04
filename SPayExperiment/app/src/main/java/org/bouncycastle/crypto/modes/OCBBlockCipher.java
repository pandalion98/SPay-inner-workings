/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes;

import java.util.Vector;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class OCBBlockCipher
implements AEADBlockCipher {
    private static final int BLOCK_SIZE = 16;
    private byte[] Checksum;
    private byte[] KtopInput = null;
    private Vector L;
    private byte[] L_Asterisk;
    private byte[] L_Dollar;
    private byte[] OffsetHASH;
    private byte[] OffsetMAIN = new byte[16];
    private byte[] OffsetMAIN_0 = new byte[16];
    private byte[] Stretch = new byte[24];
    private byte[] Sum;
    private boolean forEncryption;
    private byte[] hashBlock;
    private long hashBlockCount;
    private int hashBlockPos;
    private BlockCipher hashCipher;
    private byte[] initialAssociatedText;
    private byte[] macBlock;
    private int macSize;
    private byte[] mainBlock;
    private long mainBlockCount;
    private int mainBlockPos;
    private BlockCipher mainCipher;

    public OCBBlockCipher(BlockCipher blockCipher, BlockCipher blockCipher2) {
        if (blockCipher == null) {
            throw new IllegalArgumentException("'hashCipher' cannot be null");
        }
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("'hashCipher' must have a block size of 16");
        }
        if (blockCipher2 == null) {
            throw new IllegalArgumentException("'mainCipher' cannot be null");
        }
        if (blockCipher2.getBlockSize() != 16) {
            throw new IllegalArgumentException("'mainCipher' must have a block size of 16");
        }
        if (!blockCipher.getAlgorithmName().equals((Object)blockCipher2.getAlgorithmName())) {
            throw new IllegalArgumentException("'hashCipher' and 'mainCipher' must be the same algorithm");
        }
        this.hashCipher = blockCipher;
        this.mainCipher = blockCipher2;
    }

    protected static byte[] OCB_double(byte[] arrby) {
        byte[] arrby2 = new byte[16];
        int n2 = OCBBlockCipher.shiftLeft(arrby, arrby2);
        arrby2[15] = (byte)(arrby2[15] ^ 135 >>> (1 - n2 << 3));
        return arrby2;
    }

    protected static void OCB_extend(byte[] arrby, int n2) {
        arrby[n2] = -128;
        while (++n2 < 16) {
            arrby[n2] = 0;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static int OCB_ntz(long l2) {
        if (l2 == 0L) {
            return 64;
        }
        int n2 = 0;
        while ((1L & l2) == 0L) {
            ++n2;
            l2 >>>= 1;
        }
        return n2;
    }

    protected static int shiftLeft(byte[] arrby, byte[] arrby2) {
        int n2 = 16;
        int n3 = 0;
        while (--n2 >= 0) {
            int n4 = 255 & arrby[n2];
            arrby2[n2] = (byte)(n3 | n4 << 1);
            n3 = 1 & n4 >>> 7;
        }
        return n3;
    }

    protected static void xor(byte[] arrby, byte[] arrby2) {
        for (int i2 = 15; i2 >= 0; --i2) {
            arrby[i2] = (byte)(arrby[i2] ^ arrby2[i2]);
        }
    }

    protected void clear(byte[] arrby) {
        if (arrby != null) {
            Arrays.fill((byte[])arrby, (byte)0);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        int n3;
        boolean bl = this.forEncryption;
        byte[] arrby2 = null;
        if (!bl) {
            if (this.mainBlockPos < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            this.mainBlockPos -= this.macSize;
            arrby2 = new byte[this.macSize];
            System.arraycopy((Object)this.mainBlock, (int)this.mainBlockPos, (Object)arrby2, (int)0, (int)this.macSize);
        }
        if (this.hashBlockPos > 0) {
            OCBBlockCipher.OCB_extend(this.hashBlock, this.hashBlockPos);
            this.updateHASH(this.L_Asterisk);
        }
        if (this.mainBlockPos > 0) {
            if (this.forEncryption) {
                OCBBlockCipher.OCB_extend(this.mainBlock, this.mainBlockPos);
                OCBBlockCipher.xor(this.Checksum, this.mainBlock);
            }
            OCBBlockCipher.xor(this.OffsetMAIN, this.L_Asterisk);
            byte[] arrby3 = new byte[16];
            this.hashCipher.processBlock(this.OffsetMAIN, 0, arrby3, 0);
            OCBBlockCipher.xor(this.mainBlock, arrby3);
            if (arrby.length < n2 + this.mainBlockPos) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy((Object)this.mainBlock, (int)0, (Object)arrby, (int)n2, (int)this.mainBlockPos);
            if (!this.forEncryption) {
                OCBBlockCipher.OCB_extend(this.mainBlock, this.mainBlockPos);
                OCBBlockCipher.xor(this.Checksum, this.mainBlock);
            }
        }
        OCBBlockCipher.xor(this.Checksum, this.OffsetMAIN);
        OCBBlockCipher.xor(this.Checksum, this.L_Dollar);
        this.hashCipher.processBlock(this.Checksum, 0, this.Checksum, 0);
        OCBBlockCipher.xor(this.Checksum, this.Sum);
        this.macBlock = new byte[this.macSize];
        System.arraycopy((Object)this.Checksum, (int)0, (Object)this.macBlock, (int)0, (int)this.macSize);
        int n4 = this.mainBlockPos;
        if (this.forEncryption) {
            if (arrby.length < n2 + n4 + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy((Object)this.macBlock, (int)0, (Object)arrby, (int)(n2 + n4), (int)this.macSize);
            n3 = n4 + this.macSize;
        } else {
            if (!Arrays.constantTimeAreEqual((byte[])this.macBlock, (byte[])arrby2)) {
                throw new InvalidCipherTextException("mac check in OCB failed");
            }
            n3 = n4;
        }
        this.reset(false);
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.mainCipher.getAlgorithmName() + "/OCB";
    }

    protected byte[] getLSub(int n2) {
        while (n2 >= this.L.size()) {
            this.L.addElement((Object)OCBBlockCipher.OCB_double((byte[])this.L.lastElement()));
        }
        return (byte[])this.L.elementAt(n2);
    }

    @Override
    public byte[] getMac() {
        return Arrays.clone((byte[])this.macBlock);
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.mainBlockPos;
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
        return this.mainCipher;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.mainBlockPos;
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
        byte[] arrby;
        boolean bl2 = this.forEncryption;
        this.forEncryption = bl;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            arrby = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            int n2 = aEADParameters.getMacSize();
            if (n2 < 64 || n2 > 128 || n2 % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + n2);
            }
            this.macSize = n2 / 8;
            keyParameter = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to OCB");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby2 = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = 16;
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
            arrby = arrby2;
        }
        this.hashBlock = new byte[16];
        int n3 = bl ? 16 : 16 + this.macSize;
        this.mainBlock = new byte[n3];
        if (arrby == null) {
            arrby = new byte[]{};
        }
        if (arrby.length > 15) {
            throw new IllegalArgumentException("IV must be no more than 15 bytes");
        }
        if (keyParameter != null) {
            this.hashCipher.init(true, keyParameter);
            this.mainCipher.init(bl, keyParameter);
            this.KtopInput = null;
        } else if (bl2 != bl) {
            throw new IllegalArgumentException("cannot change encrypting state without providing key.");
        }
        this.L_Asterisk = new byte[16];
        this.hashCipher.processBlock(this.L_Asterisk, 0, this.L_Asterisk, 0);
        this.L_Dollar = OCBBlockCipher.OCB_double(this.L_Asterisk);
        this.L = new Vector();
        this.L.addElement((Object)OCBBlockCipher.OCB_double(this.L_Dollar));
        int n4 = this.processNonce(arrby);
        int n5 = n4 % 8;
        int n6 = n4 / 8;
        if (n5 == 0) {
            System.arraycopy((Object)this.Stretch, (int)n6, (Object)this.OffsetMAIN_0, (int)0, (int)16);
        } else {
            int n7 = n6;
            for (int i2 = 0; i2 < 16; ++i2) {
                int n8 = 255 & this.Stretch[n7];
                byte[] arrby3 = this.Stretch;
                int n9 = 255 & arrby3[++n7];
                this.OffsetMAIN_0[i2] = (byte)(n8 << n5 | n9 >>> 8 - n5);
            }
        }
        this.hashBlockPos = 0;
        this.mainBlockPos = 0;
        this.hashBlockCount = 0L;
        this.mainBlockCount = 0L;
        this.OffsetHASH = new byte[16];
        this.Sum = new byte[16];
        System.arraycopy((Object)this.OffsetMAIN_0, (int)0, (Object)this.OffsetMAIN, (int)0, (int)16);
        this.Checksum = new byte[16];
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    @Override
    public void processAADByte(byte by) {
        int n2;
        this.hashBlock[this.hashBlockPos] = by;
        this.hashBlockPos = n2 = 1 + this.hashBlockPos;
        if (n2 == this.hashBlock.length) {
            this.processHashBlock();
        }
    }

    @Override
    public void processAADBytes(byte[] arrby, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4;
            this.hashBlock[this.hashBlockPos] = arrby[n2 + i2];
            this.hashBlockPos = n4 = 1 + this.hashBlockPos;
            if (n4 != this.hashBlock.length) continue;
            this.processHashBlock();
        }
    }

    @Override
    public int processByte(byte by, byte[] arrby, int n2) {
        int n3;
        this.mainBlock[this.mainBlockPos] = by;
        this.mainBlockPos = n3 = 1 + this.mainBlockPos;
        if (n3 == this.mainBlock.length) {
            this.processMainBlock(arrby, n2);
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
            this.mainBlock[this.mainBlockPos] = arrby[n2 + i2];
            this.mainBlockPos = n6 = 1 + this.mainBlockPos;
            if (n6 != this.mainBlock.length) continue;
            this.processMainBlock(arrby2, n4 + n5);
            n5 += 16;
        }
        return n5;
    }

    protected void processHashBlock() {
        long l2;
        this.hashBlockCount = l2 = 1L + this.hashBlockCount;
        this.updateHASH(this.getLSub(OCBBlockCipher.OCB_ntz(l2)));
        this.hashBlockPos = 0;
    }

    protected void processMainBlock(byte[] arrby, int n2) {
        long l2;
        if (arrby.length < n2 + 16) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.forEncryption) {
            OCBBlockCipher.xor(this.Checksum, this.mainBlock);
            this.mainBlockPos = 0;
        }
        byte[] arrby2 = this.OffsetMAIN;
        this.mainBlockCount = l2 = 1L + this.mainBlockCount;
        OCBBlockCipher.xor(arrby2, this.getLSub(OCBBlockCipher.OCB_ntz(l2)));
        OCBBlockCipher.xor(this.mainBlock, this.OffsetMAIN);
        this.mainCipher.processBlock(this.mainBlock, 0, this.mainBlock, 0);
        OCBBlockCipher.xor(this.mainBlock, this.OffsetMAIN);
        System.arraycopy((Object)this.mainBlock, (int)0, (Object)arrby, (int)n2, (int)16);
        if (!this.forEncryption) {
            OCBBlockCipher.xor(this.Checksum, this.mainBlock);
            System.arraycopy((Object)this.mainBlock, (int)16, (Object)this.mainBlock, (int)0, (int)this.macSize);
            this.mainBlockPos = this.macSize;
        }
    }

    protected int processNonce(byte[] arrby) {
        byte[] arrby2 = new byte[16];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)(arrby2.length - arrby.length), (int)arrby.length);
        arrby2[0] = (byte)(this.macSize << 4);
        int n2 = 15 - arrby.length;
        arrby2[n2] = (byte)(1 | arrby2[n2]);
        int n3 = 63 & arrby2[15];
        arrby2[15] = (byte)(192 & arrby2[15]);
        if (this.KtopInput == null || !Arrays.areEqual((byte[])arrby2, (byte[])this.KtopInput)) {
            byte[] arrby3 = new byte[16];
            this.KtopInput = arrby2;
            this.hashCipher.processBlock(this.KtopInput, 0, arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)this.Stretch, (int)0, (int)16);
            for (int i2 = 0; i2 < 8; ++i2) {
                this.Stretch[i2 + 16] = (byte)(arrby3[i2] ^ arrby3[i2 + 1]);
            }
        }
        return n3;
    }

    @Override
    public void reset() {
        this.reset(true);
    }

    protected void reset(boolean bl) {
        this.hashCipher.reset();
        this.mainCipher.reset();
        this.clear(this.hashBlock);
        this.clear(this.mainBlock);
        this.hashBlockPos = 0;
        this.mainBlockPos = 0;
        this.hashBlockCount = 0L;
        this.mainBlockCount = 0L;
        this.clear(this.OffsetHASH);
        this.clear(this.Sum);
        System.arraycopy((Object)this.OffsetMAIN_0, (int)0, (Object)this.OffsetMAIN, (int)0, (int)16);
        this.clear(this.Checksum);
        if (bl) {
            this.macBlock = null;
        }
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    protected void updateHASH(byte[] arrby) {
        OCBBlockCipher.xor(this.OffsetHASH, arrby);
        OCBBlockCipher.xor(this.hashBlock, this.OffsetHASH);
        this.hashCipher.processBlock(this.hashBlock, 0, this.hashBlock, 0);
        OCBBlockCipher.xor(this.Sum, this.hashBlock);
    }
}

