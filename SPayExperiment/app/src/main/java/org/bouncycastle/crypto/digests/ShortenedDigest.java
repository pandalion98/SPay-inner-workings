/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public class ShortenedDigest
implements ExtendedDigest {
    private ExtendedDigest baseDigest;
    private int length;

    public ShortenedDigest(ExtendedDigest extendedDigest, int n2) {
        if (extendedDigest == null) {
            throw new IllegalArgumentException("baseDigest must not be null");
        }
        if (n2 > extendedDigest.getDigestSize()) {
            throw new IllegalArgumentException("baseDigest output not large enough to support length");
        }
        this.baseDigest = extendedDigest;
        this.length = n2;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte[] arrby2 = new byte[this.baseDigest.getDigestSize()];
        this.baseDigest.doFinal(arrby2, 0);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)this.length);
        return this.length;
    }

    @Override
    public String getAlgorithmName() {
        return this.baseDigest.getAlgorithmName() + "(" + 8 * this.length + ")";
    }

    @Override
    public int getByteLength() {
        return this.baseDigest.getByteLength();
    }

    @Override
    public int getDigestSize() {
        return this.length;
    }

    @Override
    public void reset() {
        this.baseDigest.reset();
    }

    @Override
    public void update(byte by) {
        this.baseDigest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.baseDigest.update(arrby, n2, n3);
    }
}

