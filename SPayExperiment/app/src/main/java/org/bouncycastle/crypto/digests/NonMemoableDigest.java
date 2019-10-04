/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public class NonMemoableDigest
implements ExtendedDigest {
    private ExtendedDigest baseDigest;

    public NonMemoableDigest(ExtendedDigest extendedDigest) {
        if (extendedDigest == null) {
            throw new IllegalArgumentException("baseDigest must not be null");
        }
        this.baseDigest = extendedDigest;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        return this.baseDigest.doFinal(arrby, n2);
    }

    @Override
    public String getAlgorithmName() {
        return this.baseDigest.getAlgorithmName();
    }

    @Override
    public int getByteLength() {
        return this.baseDigest.getByteLength();
    }

    @Override
    public int getDigestSize() {
        return this.baseDigest.getDigestSize();
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

