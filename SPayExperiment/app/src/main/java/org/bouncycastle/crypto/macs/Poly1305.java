/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;

public class Poly1305
implements Mac {
    private static final int BLOCK_SIZE = 16;
    private final BlockCipher cipher;
    private final byte[] currentBlock = new byte[16];
    private int currentBlockOffset = 0;
    private int h0;
    private int h1;
    private int h2;
    private int h3;
    private int h4;
    private int k0;
    private int k1;
    private int k2;
    private int k3;
    private int r0;
    private int r1;
    private int r2;
    private int r3;
    private int r4;
    private int s1;
    private int s2;
    private int s3;
    private int s4;
    private final byte[] singleByte = new byte[1];

    public Poly1305() {
        this.cipher = null;
    }

    public Poly1305(BlockCipher blockCipher) {
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit block cipher.");
        }
        this.cipher = blockCipher;
    }

    private static final long mul32x32_64(int n2, int n3) {
        return (long)n2 * (long)n3;
    }

    private void processBlock() {
        if (this.currentBlockOffset < 16) {
            this.currentBlock[this.currentBlockOffset] = 1;
            for (int i2 = 1 + this.currentBlockOffset; i2 < 16; ++i2) {
                this.currentBlock[i2] = 0;
            }
        }
        long l2 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt((byte[])this.currentBlock, (int)0);
        long l3 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt((byte[])this.currentBlock, (int)4);
        long l4 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt((byte[])this.currentBlock, (int)8);
        long l5 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt((byte[])this.currentBlock, (int)12);
        this.h0 = (int)((long)this.h0 + (0x3FFFFFFL & l2));
        this.h1 = (int)((long)this.h1 + (0x3FFFFFFL & (l2 | l3 << 32) >>> 26));
        this.h2 = (int)((long)this.h2 + (0x3FFFFFFL & (l3 | l4 << 32) >>> 20));
        this.h3 = (int)((long)this.h3 + (0x3FFFFFFL & (l4 | l5 << 32) >>> 14));
        this.h4 = (int)((long)this.h4 + (l5 >>> 8));
        if (this.currentBlockOffset == 16) {
            this.h4 = 16777216 + this.h4;
        }
        long l6 = Poly1305.mul32x32_64(this.h0, this.r0) + Poly1305.mul32x32_64(this.h1, this.s4) + Poly1305.mul32x32_64(this.h2, this.s3) + Poly1305.mul32x32_64(this.h3, this.s2) + Poly1305.mul32x32_64(this.h4, this.s1);
        long l7 = Poly1305.mul32x32_64(this.h0, this.r1) + Poly1305.mul32x32_64(this.h1, this.r0) + Poly1305.mul32x32_64(this.h2, this.s4) + Poly1305.mul32x32_64(this.h3, this.s3) + Poly1305.mul32x32_64(this.h4, this.s2);
        long l8 = Poly1305.mul32x32_64(this.h0, this.r2) + Poly1305.mul32x32_64(this.h1, this.r1) + Poly1305.mul32x32_64(this.h2, this.r0) + Poly1305.mul32x32_64(this.h3, this.s4) + Poly1305.mul32x32_64(this.h4, this.s3);
        long l9 = Poly1305.mul32x32_64(this.h0, this.r3) + Poly1305.mul32x32_64(this.h1, this.r2) + Poly1305.mul32x32_64(this.h2, this.r1) + Poly1305.mul32x32_64(this.h3, this.r0) + Poly1305.mul32x32_64(this.h4, this.s4);
        long l10 = Poly1305.mul32x32_64(this.h0, this.r4) + Poly1305.mul32x32_64(this.h1, this.r3) + Poly1305.mul32x32_64(this.h2, this.r2) + Poly1305.mul32x32_64(this.h3, this.r1) + Poly1305.mul32x32_64(this.h4, this.r0);
        this.h0 = 67108863 & (int)l6;
        long l11 = l7 + (l6 >>> 26);
        this.h1 = 67108863 & (int)l11;
        long l12 = l8 + (-1L & l11 >>> 26);
        this.h2 = 67108863 & (int)l12;
        long l13 = l9 + (-1L & l12 >>> 26);
        this.h3 = 67108863 & (int)l13;
        long l14 = l10 + (l13 >>> 26);
        this.h4 = 67108863 & (int)l14;
        long l15 = l14 >>> 26;
        this.h0 = (int)((long)this.h0 + l15 * 5L);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(byte[] arrby, byte[] arrby2) {
        if (this.cipher != null && (arrby2 == null || arrby2.length != 16)) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit IV.");
        }
        Poly1305KeyGenerator.checkKey(arrby);
        int n2 = Pack.littleEndianToInt((byte[])arrby, (int)16);
        int n3 = Pack.littleEndianToInt((byte[])arrby, (int)20);
        int n4 = Pack.littleEndianToInt((byte[])arrby, (int)24);
        int n5 = Pack.littleEndianToInt((byte[])arrby, (int)28);
        this.r0 = 67108863 & n2;
        this.r1 = 67108611 & (n2 >>> 26 | n3 << 6);
        this.r2 = 67092735 & (n3 >>> 20 | n4 << 12);
        this.r3 = 66076671 & (n4 >>> 14 | n5 << 18);
        this.r4 = 1048575 & n5 >>> 8;
        this.s1 = 5 * this.r1;
        this.s2 = 5 * this.r2;
        this.s3 = 5 * this.r3;
        this.s4 = 5 * this.r4;
        if (this.cipher != null) {
            byte[] arrby3 = new byte[16];
            this.cipher.init(true, new KeyParameter(arrby, 0, 16));
            this.cipher.processBlock(arrby2, 0, arrby3, 0);
            arrby = arrby3;
        }
        this.k0 = Pack.littleEndianToInt((byte[])arrby, (int)0);
        this.k1 = Pack.littleEndianToInt((byte[])arrby, (int)4);
        this.k2 = Pack.littleEndianToInt((byte[])arrby, (int)8);
        this.k3 = Pack.littleEndianToInt((byte[])arrby, (int)12);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("Output buffer is too short.");
        }
        if (this.currentBlockOffset > 0) {
            this.processBlock();
        }
        int n3 = this.h0 >>> 26;
        this.h0 = 67108863 & this.h0;
        this.h1 = n3 + this.h1;
        int n4 = this.h1 >>> 26;
        this.h1 = 67108863 & this.h1;
        this.h2 = n4 + this.h2;
        int n5 = this.h2 >>> 26;
        this.h2 = 67108863 & this.h2;
        this.h3 = n5 + this.h3;
        int n6 = this.h3 >>> 26;
        this.h3 = 67108863 & this.h3;
        this.h4 = n6 + this.h4;
        int n7 = this.h4 >>> 26;
        this.h4 = 67108863 & this.h4;
        this.h0 += n7 * 5;
        int n8 = 5 + this.h0;
        int n9 = n8 >>> 26;
        int n10 = n8 & 67108863;
        int n11 = n9 + this.h1;
        int n12 = n11 >>> 26;
        int n13 = n11 & 67108863;
        int n14 = n12 + this.h2;
        int n15 = n14 >>> 26;
        int n16 = n14 & 67108863;
        int n17 = n15 + this.h3;
        int n18 = n17 >>> 26;
        int n19 = n17 & 67108863;
        int n20 = n18 + this.h4 - 67108864;
        int n21 = -1 + (n20 >>> 31);
        int n22 = ~n21;
        this.h0 = n22 & this.h0 | n10 & n21;
        this.h1 = n22 & this.h1 | n13 & n21;
        this.h2 = n22 & this.h2 | n16 & n21;
        this.h3 = n22 & this.h3 | n19 & n21;
        this.h4 = n22 & this.h4 | n20 & n21;
        long l2 = (0xFFFFFFFFL & (long)(this.h0 | this.h1 << 26)) + (0xFFFFFFFFL & (long)this.k0);
        long l3 = (0xFFFFFFFFL & (long)(this.h1 >>> 6 | this.h2 << 20)) + (0xFFFFFFFFL & (long)this.k1);
        long l4 = (0xFFFFFFFFL & (long)(this.h2 >>> 12 | this.h3 << 14)) + (0xFFFFFFFFL & (long)this.k2);
        long l5 = (0xFFFFFFFFL & (long)(this.h3 >>> 18 | this.h4 << 8)) + (0xFFFFFFFFL & (long)this.k3);
        Pack.intToLittleEndian((int)((int)l2), (byte[])arrby, (int)n2);
        long l6 = l3 + (l2 >>> 32);
        Pack.intToLittleEndian((int)((int)l6), (byte[])arrby, (int)(n2 + 4));
        long l7 = l4 + (l6 >>> 32);
        Pack.intToLittleEndian((int)((int)l7), (byte[])arrby, (int)(n2 + 8));
        Pack.intToLittleEndian((int)((int)(l5 + (l7 >>> 32))), (byte[])arrby, (int)(n2 + 12));
        this.reset();
        return 16;
    }

    @Override
    public String getAlgorithmName() {
        if (this.cipher == null) {
            return "Poly1305";
        }
        return "Poly1305-" + this.cipher.getAlgorithmName();
    }

    @Override
    public int getMacSize() {
        return 16;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        byte[] arrby;
        if (this.cipher != null) {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("Poly1305 requires an IV when used with a block cipher.");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby2 = parametersWithIV.getIV();
            CipherParameters cipherParameters3 = parametersWithIV.getParameters();
            arrby = arrby2;
            cipherParameters2 = cipherParameters3;
        } else {
            cipherParameters2 = cipherParameters;
            arrby = null;
        }
        if (!(cipherParameters2 instanceof KeyParameter)) {
            throw new IllegalArgumentException("Poly1305 requires a key.");
        }
        this.setKey(((KeyParameter)cipherParameters2).getKey(), arrby);
        this.reset();
    }

    @Override
    public void reset() {
        this.currentBlockOffset = 0;
        this.h4 = 0;
        this.h3 = 0;
        this.h2 = 0;
        this.h1 = 0;
        this.h0 = 0;
    }

    @Override
    public void update(byte by) {
        this.singleByte[0] = by;
        this.update(this.singleByte, 0, 1);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        int n4;
        for (int i2 = 0; n3 > i2; i2 += n4) {
            if (this.currentBlockOffset == 16) {
                this.processBlock();
                this.currentBlockOffset = 0;
            }
            n4 = Math.min((int)(n3 - i2), (int)(16 - this.currentBlockOffset));
            System.arraycopy((Object)arrby, (int)(i2 + n2), (Object)this.currentBlock, (int)this.currentBlockOffset, (int)n4);
            this.currentBlockOffset = n4 + this.currentBlockOffset;
        }
    }
}

