/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.SkeinEngine;
import org.bouncycastle.crypto.params.SkeinParameters;
import org.bouncycastle.util.Memoable;

public class SkeinDigest
implements ExtendedDigest,
Memoable {
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    private SkeinEngine engine;

    public SkeinDigest(int n2, int n3) {
        this.engine = new SkeinEngine(n2, n3);
        this.init(null);
    }

    public SkeinDigest(SkeinDigest skeinDigest) {
        this.engine = new SkeinEngine(skeinDigest.engine);
    }

    @Override
    public Memoable copy() {
        return new SkeinDigest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        return this.engine.doFinal(arrby, n2);
    }

    @Override
    public String getAlgorithmName() {
        return "Skein-" + 8 * this.engine.getBlockSize() + "-" + 8 * this.engine.getOutputSize();
    }

    @Override
    public int getByteLength() {
        return this.engine.getBlockSize();
    }

    @Override
    public int getDigestSize() {
        return this.engine.getOutputSize();
    }

    public void init(SkeinParameters skeinParameters) {
        this.engine.init(skeinParameters);
    }

    @Override
    public void reset() {
        this.engine.reset();
    }

    @Override
    public void reset(Memoable memoable) {
        SkeinDigest skeinDigest = (SkeinDigest)memoable;
        this.engine.reset(skeinDigest.engine);
    }

    @Override
    public void update(byte by) {
        this.engine.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.engine.update(arrby, n2, n3);
    }
}

