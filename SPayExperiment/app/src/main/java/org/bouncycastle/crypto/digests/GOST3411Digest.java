/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class GOST3411Digest
implements ExtendedDigest,
Memoable {
    private static final byte[] C2 = new byte[]{0, -1, 0, -1, 0, -1, 0, -1, -1, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, 0, -1, -1, 0, 0, 0, -1, -1, 0, -1};
    private static final int DIGEST_LENGTH = 32;
    private byte[][] C;
    private byte[] H = new byte[32];
    private byte[] K;
    private byte[] L = new byte[32];
    private byte[] M = new byte[32];
    byte[] S;
    private byte[] Sum = new byte[32];
    byte[] U;
    byte[] V;
    byte[] W;
    byte[] a;
    private long byteCount;
    private BlockCipher cipher;
    private byte[] sBox;
    short[] wS;
    short[] w_S;
    private byte[] xBuf;
    private int xBufOff;

    public GOST3411Digest() {
        int[] arrn = new int[]{4, 32};
        this.C = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.sBox = GOST28147Engine.getSBox("D-A");
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }

    public GOST3411Digest(GOST3411Digest gOST3411Digest) {
        int[] arrn = new int[]{4, 32};
        this.C = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.reset(gOST3411Digest);
    }

    public GOST3411Digest(byte[] arrby) {
        int[] arrn = new int[]{4, 32};
        this.C = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.sBox = Arrays.clone((byte[])arrby);
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }

    private byte[] A(byte[] arrby) {
        for (int i2 = 0; i2 < 8; ++i2) {
            this.a[i2] = (byte)(arrby[i2] ^ arrby[i2 + 8]);
        }
        System.arraycopy((Object)arrby, (int)8, (Object)arrby, (int)0, (int)24);
        System.arraycopy((Object)this.a, (int)0, (Object)arrby, (int)24, (int)8);
        return arrby;
    }

    private void E(byte[] arrby, byte[] arrby2, int n2, byte[] arrby3, int n3) {
        this.cipher.init(true, new KeyParameter(arrby));
        this.cipher.processBlock(arrby3, n3, arrby2, n2);
    }

    private byte[] P(byte[] arrby) {
        for (int i2 = 0; i2 < 8; ++i2) {
            this.K[i2 * 4] = arrby[i2];
            this.K[1 + i2 * 4] = arrby[i2 + 8];
            this.K[2 + i2 * 4] = arrby[i2 + 16];
            this.K[3 + i2 * 4] = arrby[i2 + 24];
        }
        return this.K;
    }

    private void cpyBytesToShort(byte[] arrby, short[] arrs) {
        for (int i2 = 0; i2 < arrby.length / 2; ++i2) {
            arrs[i2] = (short)(65280 & arrby[1 + i2 * 2] << 8 | 255 & arrby[i2 * 2]);
        }
    }

    private void cpyShortToBytes(short[] arrs, byte[] arrby) {
        for (int i2 = 0; i2 < arrby.length / 2; ++i2) {
            arrby[1 + i2 * 2] = (byte)(arrs[i2] >> 8);
            arrby[i2 * 2] = (byte)arrs[i2];
        }
    }

    private void finish() {
        Pack.longToLittleEndian((long)(8L * this.byteCount), (byte[])this.L, (int)0);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processBlock(this.L, 0);
        this.processBlock(this.Sum, 0);
    }

    private void fw(byte[] arrby) {
        this.cpyBytesToShort(arrby, this.wS);
        this.w_S[15] = (short)(this.wS[0] ^ this.wS[1] ^ this.wS[2] ^ this.wS[3] ^ this.wS[12] ^ this.wS[15]);
        System.arraycopy((Object)this.wS, (int)1, (Object)this.w_S, (int)0, (int)15);
        this.cpyShortToBytes(this.w_S, arrby);
    }

    private void sumByteArray(byte[] arrby) {
        int n2 = 0;
        for (int i2 = 0; i2 != this.Sum.length; ++i2) {
            int n3 = n2 + ((255 & this.Sum[i2]) + (255 & arrby[i2]));
            this.Sum[i2] = (byte)n3;
            n2 = n3 >>> 8;
        }
    }

    @Override
    public Memoable copy() {
        return new GOST3411Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        System.arraycopy((Object)this.H, (int)0, (Object)arrby, (int)n2, (int)this.H.length);
        this.reset();
        return 32;
    }

    @Override
    public String getAlgorithmName() {
        return "GOST3411";
    }

    @Override
    public int getByteLength() {
        return 32;
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    protected void processBlock(byte[] arrby, int n2) {
        System.arraycopy((Object)arrby, (int)n2, (Object)this.M, (int)0, (int)32);
        System.arraycopy((Object)this.H, (int)0, (Object)this.U, (int)0, (int)32);
        System.arraycopy((Object)this.M, (int)0, (Object)this.V, (int)0, (int)32);
        for (int i2 = 0; i2 < 32; ++i2) {
            this.W[i2] = (byte)(this.U[i2] ^ this.V[i2]);
        }
        this.E(this.P(this.W), this.S, 0, this.H, 0);
        for (int i3 = 1; i3 < 4; ++i3) {
            byte[] arrby2 = this.A(this.U);
            for (int i4 = 0; i4 < 32; ++i4) {
                this.U[i4] = (byte)(arrby2[i4] ^ this.C[i3][i4]);
            }
            this.V = this.A(this.A(this.V));
            for (int i5 = 0; i5 < 32; ++i5) {
                this.W[i5] = (byte)(this.U[i5] ^ this.V[i5]);
            }
            this.E(this.P(this.W), this.S, i3 * 8, this.H, i3 * 8);
        }
        for (int i6 = 0; i6 < 12; ++i6) {
            this.fw(this.S);
        }
        for (int i7 = 0; i7 < 32; ++i7) {
            this.S[i7] = (byte)(this.S[i7] ^ this.M[i7]);
        }
        this.fw(this.S);
        for (int i8 = 0; i8 < 32; ++i8) {
            this.S[i8] = (byte)(this.H[i8] ^ this.S[i8]);
        }
        for (int i9 = 0; i9 < 61; ++i9) {
            this.fw(this.S);
        }
        System.arraycopy((Object)this.S, (int)0, (Object)this.H, (int)0, (int)this.H.length);
    }

    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        for (int i2 = 0; i2 < this.H.length; ++i2) {
            this.H[i2] = 0;
        }
        for (int i3 = 0; i3 < this.L.length; ++i3) {
            this.L[i3] = 0;
        }
        for (int i4 = 0; i4 < this.M.length; ++i4) {
            this.M[i4] = 0;
        }
        for (int i5 = 0; i5 < this.C[1].length; ++i5) {
            this.C[1][i5] = 0;
        }
        for (int i6 = 0; i6 < this.C[3].length; ++i6) {
            this.C[3][i6] = 0;
        }
        for (int i7 = 0; i7 < this.Sum.length; ++i7) {
            this.Sum[i7] = 0;
        }
        for (int i8 = 0; i8 < this.xBuf.length; ++i8) {
            this.xBuf[i8] = 0;
        }
        System.arraycopy((Object)C2, (int)0, (Object)this.C[2], (int)0, (int)C2.length);
    }

    @Override
    public void reset(Memoable memoable) {
        GOST3411Digest gOST3411Digest = (GOST3411Digest)memoable;
        this.sBox = gOST3411Digest.sBox;
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
        System.arraycopy((Object)gOST3411Digest.H, (int)0, (Object)this.H, (int)0, (int)gOST3411Digest.H.length);
        System.arraycopy((Object)gOST3411Digest.L, (int)0, (Object)this.L, (int)0, (int)gOST3411Digest.L.length);
        System.arraycopy((Object)gOST3411Digest.M, (int)0, (Object)this.M, (int)0, (int)gOST3411Digest.M.length);
        System.arraycopy((Object)gOST3411Digest.Sum, (int)0, (Object)this.Sum, (int)0, (int)gOST3411Digest.Sum.length);
        System.arraycopy((Object)gOST3411Digest.C[1], (int)0, (Object)this.C[1], (int)0, (int)gOST3411Digest.C[1].length);
        System.arraycopy((Object)gOST3411Digest.C[2], (int)0, (Object)this.C[2], (int)0, (int)gOST3411Digest.C[2].length);
        System.arraycopy((Object)gOST3411Digest.C[3], (int)0, (Object)this.C[3], (int)0, (int)gOST3411Digest.C[3].length);
        System.arraycopy((Object)gOST3411Digest.xBuf, (int)0, (Object)this.xBuf, (int)0, (int)gOST3411Digest.xBuf.length);
        this.xBufOff = gOST3411Digest.xBufOff;
        this.byteCount = gOST3411Digest.byteCount;
    }

    @Override
    public void update(byte by) {
        byte[] arrby = this.xBuf;
        int n2 = this.xBufOff;
        this.xBufOff = n2 + 1;
        arrby[n2] = by;
        if (this.xBufOff == this.xBuf.length) {
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            this.xBufOff = 0;
        }
        this.byteCount = 1L + this.byteCount;
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        while (this.xBufOff != 0 && n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
        while (n3 > this.xBuf.length) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.xBuf, (int)0, (int)this.xBuf.length);
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            n2 += this.xBuf.length;
            n3 -= this.xBuf.length;
            this.byteCount += (long)this.xBuf.length;
        }
        while (n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
    }
}

