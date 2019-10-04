/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.RandomGenerator;

public class ReversedWindowGenerator
implements RandomGenerator {
    private final RandomGenerator generator;
    private byte[] window;
    private int windowCount;

    public ReversedWindowGenerator(RandomGenerator randomGenerator, int n2) {
        if (randomGenerator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        if (n2 < 2) {
            throw new IllegalArgumentException("windowSize must be at least 2");
        }
        this.generator = randomGenerator;
        this.window = new byte[n2];
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void doNextBytes(byte[] arrby, int n2, int n3) {
        int n4 = 0;
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            while (n4 < n3) {
                int n5;
                if (this.windowCount < 1) {
                    this.generator.nextBytes(this.window, 0, this.window.length);
                    this.windowCount = this.window.length;
                }
                int n6 = n4 + 1;
                int n7 = n4 + n2;
                byte[] arrby2 = this.window;
                this.windowCount = n5 = -1 + this.windowCount;
                arrby[n7] = arrby2[n5];
                n4 = n6;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void addSeedMaterial(long l2) {
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(l2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void addSeedMaterial(byte[] arrby) {
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(arrby);
            return;
        }
    }

    @Override
    public void nextBytes(byte[] arrby) {
        this.doNextBytes(arrby, 0, arrby.length);
    }

    @Override
    public void nextBytes(byte[] arrby, int n2, int n3) {
        this.doNextBytes(arrby, n2, n3);
    }
}

