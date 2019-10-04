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

public class SHA384Digest
extends LongDigest {
    private static final int DIGEST_LENGTH = 48;

    public SHA384Digest() {
    }

    public SHA384Digest(SHA384Digest sHA384Digest) {
        super(sHA384Digest);
    }

    public SHA384Digest(byte[] arrby) {
        this.restoreState(arrby);
    }

    @Override
    public Memoable copy() {
        return new SHA384Digest(this);
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
        this.reset();
        return 48;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-384";
    }

    @Override
    public int getDigestSize() {
        return 48;
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
        this.H1 = -3766243637369397544L;
        this.H2 = 7105036623409894663L;
        this.H3 = -7973340178411365097L;
        this.H4 = 1526699215303891257L;
        this.H5 = 7436329637833083697L;
        this.H6 = -8163818279084223215L;
        this.H7 = -2662702644619276377L;
        this.H8 = 5167115440072839076L;
    }

    @Override
    public void reset(Memoable memoable) {
        super.copyIn((SHA384Digest)memoable);
    }
}

