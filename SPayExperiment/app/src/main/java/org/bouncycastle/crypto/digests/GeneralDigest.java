/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public abstract class GeneralDigest
implements ExtendedDigest,
Memoable {
    private static final int BYTE_LENGTH = 64;
    private long byteCount;
    private final byte[] xBuf = new byte[4];
    private int xBufOff;

    protected GeneralDigest() {
        this.xBufOff = 0;
    }

    protected GeneralDigest(GeneralDigest generalDigest) {
        this.copyIn(generalDigest);
    }

    protected GeneralDigest(byte[] arrby) {
        System.arraycopy((Object)arrby, (int)0, (Object)this.xBuf, (int)0, (int)this.xBuf.length);
        this.xBufOff = Pack.bigEndianToInt((byte[])arrby, (int)4);
        this.byteCount = Pack.bigEndianToLong((byte[])arrby, (int)8);
    }

    protected void copyIn(GeneralDigest generalDigest) {
        System.arraycopy((Object)generalDigest.xBuf, (int)0, (Object)this.xBuf, (int)0, (int)generalDigest.xBuf.length);
        this.xBufOff = generalDigest.xBufOff;
        this.byteCount = generalDigest.byteCount;
    }

    public void finish() {
        long l2 = this.byteCount << 3;
        this.update((byte)-128);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processLength(l2);
        this.processBlock();
    }

    @Override
    public int getByteLength() {
        return 64;
    }

    protected void populateState(byte[] arrby) {
        System.arraycopy((Object)this.xBuf, (int)0, (Object)arrby, (int)0, (int)this.xBufOff);
        Pack.intToBigEndian((int)this.xBufOff, (byte[])arrby, (int)4);
        Pack.longToBigEndian((long)this.byteCount, (byte[])arrby, (int)8);
    }

    protected abstract void processBlock();

    protected abstract void processLength(long var1);

    protected abstract void processWord(byte[] var1, int var2);

    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        for (int i2 = 0; i2 < this.xBuf.length; ++i2) {
            this.xBuf[i2] = 0;
        }
    }

    @Override
    public void update(byte by) {
        byte[] arrby = this.xBuf;
        int n2 = this.xBufOff;
        this.xBufOff = n2 + 1;
        arrby[n2] = by;
        if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
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
            this.processWord(arrby, n2);
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

