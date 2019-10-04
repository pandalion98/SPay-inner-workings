/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.String
 *  org.bouncycastle.util.MemoableResetException
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.LongDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.MemoableResetException;
import org.bouncycastle.util.Pack;

public class SHA512tDigest
extends LongDigest {
    private long H1t;
    private long H2t;
    private long H3t;
    private long H4t;
    private long H5t;
    private long H6t;
    private long H7t;
    private long H8t;
    private int digestLength;

    public SHA512tDigest(int n2) {
        if (n2 >= 512) {
            throw new IllegalArgumentException("bitLength cannot be >= 512");
        }
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("bitLength needs to be a multiple of 8");
        }
        if (n2 == 384) {
            throw new IllegalArgumentException("bitLength cannot be 384 use SHA384 instead");
        }
        this.digestLength = n2 / 8;
        this.tIvGenerate(8 * this.digestLength);
        this.reset();
    }

    public SHA512tDigest(SHA512tDigest sHA512tDigest) {
        super(sHA512tDigest);
        this.digestLength = sHA512tDigest.digestLength;
        this.reset(sHA512tDigest);
    }

    public SHA512tDigest(byte[] arrby) {
        this(SHA512tDigest.readDigestLength(arrby));
        this.restoreState(arrby);
    }

    private static void intToBigEndian(int n2, byte[] arrby, int n3, int n4) {
        int n5 = Math.min((int)4, (int)n4);
        while (--n5 >= 0) {
            int n6 = 8 * (3 - n5);
            arrby[n3 + n5] = (byte)(n2 >>> n6);
        }
    }

    private static void longToBigEndian(long l2, byte[] arrby, int n2, int n3) {
        if (n3 > 0) {
            SHA512tDigest.intToBigEndian((int)(l2 >>> 32), arrby, n2, n3);
            if (n3 > 4) {
                SHA512tDigest.intToBigEndian((int)(0xFFFFFFFFL & l2), arrby, n2 + 4, n3 - 4);
            }
        }
    }

    private static int readDigestLength(byte[] arrby) {
        return Pack.bigEndianToInt((byte[])arrby, (int)(-4 + arrby.length));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void tIvGenerate(int n2) {
        this.H1 = -3482333909917012819L;
        this.H2 = 2216346199247487646L;
        this.H3 = -7364697282686394994L;
        this.H4 = 65953792586715988L;
        this.H5 = -816286391624063116L;
        this.H6 = 4512832404995164602L;
        this.H7 = -5033199132376557362L;
        this.H8 = -124578254951840548L;
        this.update((byte)83);
        this.update((byte)72);
        this.update((byte)65);
        this.update((byte)45);
        this.update((byte)53);
        this.update((byte)49);
        this.update((byte)50);
        this.update((byte)47);
        if (n2 > 100) {
            this.update((byte)(48 + n2 / 100));
            int n3 = n2 % 100;
            this.update((byte)(48 + n3 / 10));
            this.update((byte)(48 + n3 % 10));
        } else if (n2 > 10) {
            this.update((byte)(48 + n2 / 10));
            this.update((byte)(48 + n2 % 10));
        } else {
            this.update((byte)(n2 + 48));
        }
        this.finish();
        this.H1t = this.H1;
        this.H2t = this.H2;
        this.H3t = this.H3;
        this.H4t = this.H4;
        this.H5t = this.H5;
        this.H6t = this.H6;
        this.H7t = this.H7;
        this.H8t = this.H8;
    }

    @Override
    public Memoable copy() {
        return new SHA512tDigest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        SHA512tDigest.longToBigEndian(this.H1, arrby, n2, this.digestLength);
        SHA512tDigest.longToBigEndian(this.H2, arrby, n2 + 8, -8 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H3, arrby, n2 + 16, -16 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H4, arrby, n2 + 24, -24 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H5, arrby, n2 + 32, -32 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H6, arrby, n2 + 40, -40 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H7, arrby, n2 + 48, -48 + this.digestLength);
        SHA512tDigest.longToBigEndian(this.H8, arrby, n2 + 56, -56 + this.digestLength);
        this.reset();
        return this.digestLength;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-512/" + Integer.toString((int)(8 * this.digestLength));
    }

    @Override
    public int getDigestSize() {
        return this.digestLength;
    }

    @Override
    public byte[] getEncodedState() {
        int n2 = this.getEncodedStateSize();
        byte[] arrby = new byte[n2 + 4];
        this.populateState(arrby);
        Pack.intToBigEndian((int)(8 * this.digestLength), (byte[])arrby, (int)n2);
        return arrby;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = this.H1t;
        this.H2 = this.H2t;
        this.H3 = this.H3t;
        this.H4 = this.H4t;
        this.H5 = this.H5t;
        this.H6 = this.H6t;
        this.H7 = this.H7t;
        this.H8 = this.H8t;
    }

    @Override
    public void reset(Memoable memoable) {
        SHA512tDigest sHA512tDigest = (SHA512tDigest)memoable;
        if (this.digestLength != sHA512tDigest.digestLength) {
            throw new MemoableResetException("digestLength inappropriate in other");
        }
        super.copyIn(sHA512tDigest);
        this.H1t = sHA512tDigest.H1t;
        this.H2t = sHA512tDigest.H2t;
        this.H3t = sHA512tDigest.H3t;
        this.H4t = sHA512tDigest.H4t;
        this.H5t = sHA512tDigest.H5t;
        this.H6t = sHA512tDigest.H6t;
        this.H7t = sHA512tDigest.H7t;
        this.H8t = sHA512tDigest.H8t;
    }
}

