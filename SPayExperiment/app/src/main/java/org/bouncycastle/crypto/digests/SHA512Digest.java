/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.LongDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SHA512Digest
extends LongDigest {
    private static final int DIGEST_LENGTH = 64;

    public SHA512Digest() {
    }

    public SHA512Digest(SHA512Digest sHA512Digest) {
        super(sHA512Digest);
    }

    public SHA512Digest(byte[] arrby) {
        this.restoreState(arrby);
    }

    @Override
    public Memoable copy() {
        return new SHA512Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        Pack.longToBigEndian((long)this.H1, (byte[])arrby, (int)n2);
        Pack.longToBigEndian((long)this.H2, (byte[])arrby, (int)(n2 + 8));
        Pack.longToBigEndian((long)this.H3, (byte[])arrby, (int)(n2 + 16));
        Pack.longToBigEndian((long)this.H4, (byte[])arrby, (int)(n2 + 24));
        Pack.longToBigEndian((long)this.H5, (byte[])arrby, (int)(n2 + 32));
        Pack.longToBigEndian((long)this.H6, (byte[])arrby, (int)(n2 + 40));
        Pack.longToBigEndian((long)this.H7, (byte[])arrby, (int)(n2 + 48));
        Pack.longToBigEndian((long)this.H8, (byte[])arrby, (int)(n2 + 56));
        this.reset();
        return 64;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-512";
    }

    @Override
    public int getDigestSize() {
        return 64;
    }

    @Override
    public byte[] getEncodedState() {
        byte[] arrby = new byte[this.getEncodedStateSize()];
        super.populateState(arrby);
        return arrby;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 7640891576956012808L;
        this.H2 = -4942790177534073029L;
        this.H3 = 4354685564936845355L;
        this.H4 = -6534734903238641935L;
        this.H5 = 5840696475078001361L;
        this.H6 = -7276294671716946913L;
        this.H7 = 2270897969802886507L;
        this.H8 = 6620516959819538809L;
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((SHA512Digest)memoable);
    }
}

