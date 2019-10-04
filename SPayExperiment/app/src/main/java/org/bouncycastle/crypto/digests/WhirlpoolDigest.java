/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;

public final class WhirlpoolDigest
implements ExtendedDigest,
Memoable {
    private static final int BITCOUNT_ARRAY_SIZE = 32;
    private static final int BYTE_LENGTH = 64;
    private static final long[] C0;
    private static final long[] C1;
    private static final long[] C2;
    private static final long[] C3;
    private static final long[] C4;
    private static final long[] C5;
    private static final long[] C6;
    private static final long[] C7;
    private static final int DIGEST_LENGTH_BYTES = 64;
    private static final short[] EIGHT;
    private static final int REDUCTION_POLYNOMIAL = 285;
    private static final int ROUNDS = 10;
    private static final int[] SBOX;
    private long[] _K = new long[8];
    private long[] _L = new long[8];
    private short[] _bitCount = new short[32];
    private long[] _block = new long[8];
    private byte[] _buffer = new byte[64];
    private int _bufferPos = 0;
    private long[] _hash = new long[8];
    private final long[] _rc = new long[11];
    private long[] _state = new long[8];

    static {
        SBOX = new int[]{24, 35, 198, 232, 135, 184, 1, 79, 54, 166, 210, 245, 121, 111, 145, 82, 96, 188, 155, 142, 163, 12, 123, 53, 29, 224, 215, 194, 46, 75, 254, 87, 21, 119, 55, 229, 159, 240, 74, 218, 88, 201, 41, 10, 177, 160, 107, 133, 189, 93, 16, 244, 203, 62, 5, 103, 228, 39, 65, 139, 167, 125, 149, 216, 251, 238, 124, 102, 221, 23, 71, 158, 202, 45, 191, 7, 173, 90, 131, 51, 99, 2, 170, 113, 200, 25, 73, 217, 242, 227, 91, 136, 154, 38, 50, 176, 233, 15, 213, 128, 190, 205, 52, 72, 255, 122, 144, 95, 32, 104, 26, 174, 180, 84, 147, 34, 100, 241, 115, 18, 64, 8, 195, 236, 219, 161, 141, 61, 151, 0, 207, 43, 118, 130, 214, 27, 181, 175, 106, 80, 69, 243, 48, 239, 63, 85, 162, 234, 101, 186, 47, 192, 222, 28, 253, 77, 146, 117, 6, 138, 178, 230, 14, 31, 98, 212, 168, 150, 249, 197, 37, 89, 132, 114, 57, 76, 94, 120, 56, 140, 209, 165, 226, 97, 179, 33, 156, 30, 67, 199, 252, 4, 81, 153, 109, 13, 250, 223, 126, 36, 59, 171, 206, 17, 143, 78, 183, 235, 60, 129, 148, 247, 185, 19, 44, 211, 231, 110, 196, 3, 86, 68, 127, 169, 42, 187, 193, 83, 220, 11, 157, 108, 49, 116, 246, 70, 172, 137, 20, 225, 22, 58, 105, 9, 112, 182, 208, 237, 204, 66, 152, 164, 40, 92, 248, 134};
        C0 = new long[256];
        C1 = new long[256];
        C2 = new long[256];
        C3 = new long[256];
        C4 = new long[256];
        C5 = new long[256];
        C6 = new long[256];
        C7 = new long[256];
        EIGHT = new short[32];
        WhirlpoolDigest.EIGHT[31] = 8;
    }

    public WhirlpoolDigest() {
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = SBOX[i2];
            int n3 = this.maskWithReductionPolynomial(n2 << 1);
            int n4 = this.maskWithReductionPolynomial(n3 << 1);
            int n5 = n4 ^ n2;
            int n6 = this.maskWithReductionPolynomial(n4 << 1);
            int n7 = n6 ^ n2;
            WhirlpoolDigest.C0[i2] = this.packIntoLong(n2, n2, n4, n2, n6, n5, n3, n7);
            WhirlpoolDigest.C1[i2] = this.packIntoLong(n7, n2, n2, n4, n2, n6, n5, n3);
            WhirlpoolDigest.C2[i2] = this.packIntoLong(n3, n7, n2, n2, n4, n2, n6, n5);
            WhirlpoolDigest.C3[i2] = this.packIntoLong(n5, n3, n7, n2, n2, n4, n2, n6);
            WhirlpoolDigest.C4[i2] = this.packIntoLong(n6, n5, n3, n7, n2, n2, n4, n2);
            WhirlpoolDigest.C5[i2] = this.packIntoLong(n2, n6, n5, n3, n7, n2, n2, n4);
            WhirlpoolDigest.C6[i2] = this.packIntoLong(n4, n2, n6, n5, n3, n7, n2, n2);
            WhirlpoolDigest.C7[i2] = this.packIntoLong(n2, n4, n2, n6, n5, n3, n7, n2);
        }
        this._rc[0] = 0L;
        for (int i3 = 1; i3 <= 10; ++i3) {
            int n8 = 8 * (i3 - 1);
            this._rc[i3] = -72057594037927936L & C0[n8] ^ 0xFF000000000000L & C1[n8 + 1] ^ 0xFF0000000000L & C2[n8 + 2] ^ 0xFF00000000L & C3[n8 + 3] ^ 0xFF000000L & C4[n8 + 4] ^ 0xFF0000L & C5[n8 + 5] ^ 65280L & C6[n8 + 6] ^ 255L & C7[n8 + 7];
        }
    }

    public WhirlpoolDigest(WhirlpoolDigest whirlpoolDigest) {
        this.reset(whirlpoolDigest);
    }

    private long bytesToLongFromBuffer(byte[] arrby, int n2) {
        return (255L & (long)arrby[n2 + 0]) << 56 | (255L & (long)arrby[n2 + 1]) << 48 | (255L & (long)arrby[n2 + 2]) << 40 | (255L & (long)arrby[n2 + 3]) << 32 | (255L & (long)arrby[n2 + 4]) << 24 | (255L & (long)arrby[n2 + 5]) << 16 | (255L & (long)arrby[n2 + 6]) << 8 | 255L & (long)arrby[n2 + 7];
    }

    private void convertLongToByteArray(long l2, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            arrby[n2 + i2] = (byte)(255L & l2 >> 56 - i2 * 8);
        }
    }

    private byte[] copyBitLength() {
        byte[] arrby = new byte[32];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(255 & this._bitCount[i2]);
        }
        return arrby;
    }

    private void finish() {
        byte[] arrby = this.copyBitLength();
        byte[] arrby2 = this._buffer;
        int n2 = this._bufferPos;
        this._bufferPos = n2 + 1;
        arrby2[n2] = (byte)(128 | arrby2[n2]);
        if (this._bufferPos == this._buffer.length) {
            this.processFilledBuffer(this._buffer, 0);
        }
        if (this._bufferPos > 32) {
            while (this._bufferPos != 0) {
                this.update((byte)0);
            }
        }
        while (this._bufferPos <= 32) {
            this.update((byte)0);
        }
        System.arraycopy((Object)arrby, (int)0, (Object)this._buffer, (int)32, (int)arrby.length);
        this.processFilledBuffer(this._buffer, 0);
    }

    private void increment() {
        int n2 = 0;
        for (int i2 = -1 + this._bitCount.length; i2 >= 0; --i2) {
            int n3 = n2 + ((255 & this._bitCount[i2]) + EIGHT[i2]);
            n2 = n3 >>> 8;
            this._bitCount[i2] = (short)(n3 & 255);
        }
    }

    private int maskWithReductionPolynomial(int n2) {
        if ((long)n2 >= 256L) {
            n2 ^= 285;
        }
        return n2;
    }

    private long packIntoLong(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        return (long)n2 << 56 ^ (long)n3 << 48 ^ (long)n4 << 40 ^ (long)n5 << 32 ^ (long)n6 << 24 ^ (long)n7 << 16 ^ (long)n8 << 8 ^ (long)n9;
    }

    private void processFilledBuffer(byte[] arrby, int n2) {
        for (int i2 = 0; i2 < this._state.length; ++i2) {
            this._block[i2] = this.bytesToLongFromBuffer(this._buffer, i2 * 8);
        }
        this.processBlock();
        this._bufferPos = 0;
        Arrays.fill((byte[])this._buffer, (byte)0);
    }

    @Override
    public Memoable copy() {
        return new WhirlpoolDigest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        for (int i2 = 0; i2 < 8; ++i2) {
            this.convertLongToByteArray(this._hash[i2], arrby, n2 + i2 * 8);
        }
        this.reset();
        return this.getDigestSize();
    }

    @Override
    public String getAlgorithmName() {
        return "Whirlpool";
    }

    @Override
    public int getByteLength() {
        return 64;
    }

    @Override
    public int getDigestSize() {
        return 64;
    }

    protected void processBlock() {
        for (int i2 = 0; i2 < 8; ++i2) {
            long l2;
            long[] arrl = this._state;
            long l3 = this._block[i2];
            long[] arrl2 = this._K;
            arrl2[i2] = l2 = this._hash[i2];
            arrl[i2] = l3 ^ l2;
        }
        int n2 = 1;
        do {
            if (n2 > 10) break;
            for (int i3 = 0; i3 < 8; ++i3) {
                this._L[i3] = 0L;
                long[] arrl = this._L;
                arrl[i3] = arrl[i3] ^ C0[255 & (int)(this._K[7 & i3 + 0] >>> 56)];
                long[] arrl3 = this._L;
                arrl3[i3] = arrl3[i3] ^ C1[255 & (int)(this._K[7 & i3 - 1] >>> 48)];
                long[] arrl4 = this._L;
                arrl4[i3] = arrl4[i3] ^ C2[255 & (int)(this._K[7 & i3 - 2] >>> 40)];
                long[] arrl5 = this._L;
                arrl5[i3] = arrl5[i3] ^ C3[255 & (int)(this._K[7 & i3 - 3] >>> 32)];
                long[] arrl6 = this._L;
                arrl6[i3] = arrl6[i3] ^ C4[255 & (int)(this._K[7 & i3 - 4] >>> 24)];
                long[] arrl7 = this._L;
                arrl7[i3] = arrl7[i3] ^ C5[255 & (int)(this._K[7 & i3 - 5] >>> 16)];
                long[] arrl8 = this._L;
                arrl8[i3] = arrl8[i3] ^ C6[255 & (int)(this._K[7 & i3 - 6] >>> 8)];
                long[] arrl9 = this._L;
                arrl9[i3] = arrl9[i3] ^ C7[255 & (int)this._K[7 & i3 - 7]];
            }
            System.arraycopy((Object)this._L, (int)0, (Object)this._K, (int)0, (int)this._K.length);
            long[] arrl = this._K;
            arrl[0] = arrl[0] ^ this._rc[n2];
            for (int i4 = 0; i4 < 8; ++i4) {
                this._L[i4] = this._K[i4];
                long[] arrl10 = this._L;
                arrl10[i4] = arrl10[i4] ^ C0[255 & (int)(this._state[7 & i4 + 0] >>> 56)];
                long[] arrl11 = this._L;
                arrl11[i4] = arrl11[i4] ^ C1[255 & (int)(this._state[7 & i4 - 1] >>> 48)];
                long[] arrl12 = this._L;
                arrl12[i4] = arrl12[i4] ^ C2[255 & (int)(this._state[7 & i4 - 2] >>> 40)];
                long[] arrl13 = this._L;
                arrl13[i4] = arrl13[i4] ^ C3[255 & (int)(this._state[7 & i4 - 3] >>> 32)];
                long[] arrl14 = this._L;
                arrl14[i4] = arrl14[i4] ^ C4[255 & (int)(this._state[7 & i4 - 4] >>> 24)];
                long[] arrl15 = this._L;
                arrl15[i4] = arrl15[i4] ^ C5[255 & (int)(this._state[7 & i4 - 5] >>> 16)];
                long[] arrl16 = this._L;
                arrl16[i4] = arrl16[i4] ^ C6[255 & (int)(this._state[7 & i4 - 6] >>> 8)];
                long[] arrl17 = this._L;
                arrl17[i4] = arrl17[i4] ^ C7[255 & (int)this._state[7 & i4 - 7]];
            }
            System.arraycopy((Object)this._L, (int)0, (Object)this._state, (int)0, (int)this._state.length);
            ++n2;
        } while (true);
        for (int i5 = 0; i5 < 8; ++i5) {
            long[] arrl = this._hash;
            arrl[i5] = arrl[i5] ^ (this._state[i5] ^ this._block[i5]);
        }
    }

    @Override
    public void reset() {
        this._bufferPos = 0;
        Arrays.fill((short[])this._bitCount, (short)0);
        Arrays.fill((byte[])this._buffer, (byte)0);
        Arrays.fill((long[])this._hash, (long)0L);
        Arrays.fill((long[])this._K, (long)0L);
        Arrays.fill((long[])this._L, (long)0L);
        Arrays.fill((long[])this._block, (long)0L);
        Arrays.fill((long[])this._state, (long)0L);
    }

    @Override
    public void reset(Memoable memoable) {
        WhirlpoolDigest whirlpoolDigest = (WhirlpoolDigest)memoable;
        System.arraycopy((Object)whirlpoolDigest._rc, (int)0, (Object)this._rc, (int)0, (int)this._rc.length);
        System.arraycopy((Object)whirlpoolDigest._buffer, (int)0, (Object)this._buffer, (int)0, (int)this._buffer.length);
        this._bufferPos = whirlpoolDigest._bufferPos;
        System.arraycopy((Object)whirlpoolDigest._bitCount, (int)0, (Object)this._bitCount, (int)0, (int)this._bitCount.length);
        System.arraycopy((Object)whirlpoolDigest._hash, (int)0, (Object)this._hash, (int)0, (int)this._hash.length);
        System.arraycopy((Object)whirlpoolDigest._K, (int)0, (Object)this._K, (int)0, (int)this._K.length);
        System.arraycopy((Object)whirlpoolDigest._L, (int)0, (Object)this._L, (int)0, (int)this._L.length);
        System.arraycopy((Object)whirlpoolDigest._block, (int)0, (Object)this._block, (int)0, (int)this._block.length);
        System.arraycopy((Object)whirlpoolDigest._state, (int)0, (Object)this._state, (int)0, (int)this._state.length);
    }

    @Override
    public void update(byte by) {
        this._buffer[this._bufferPos] = by;
        this._bufferPos = 1 + this._bufferPos;
        if (this._bufferPos == this._buffer.length) {
            this.processFilledBuffer(this._buffer, 0);
        }
        this.increment();
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        while (n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
    }
}

