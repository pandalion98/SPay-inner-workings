/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.X931RNG;

public class X931SecureRandom
extends SecureRandom {
    private final X931RNG drbg;
    private final boolean predictionResistant;
    private final SecureRandom randomSource;

    X931SecureRandom(SecureRandom secureRandom, X931RNG x931RNG, boolean bl) {
        this.randomSource = secureRandom;
        this.drbg = x931RNG;
        this.predictionResistant = bl;
    }

    public byte[] generateSeed(int n2) {
        byte[] arrby = new byte[n2];
        this.nextBytes(arrby);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void nextBytes(byte[] arrby) {
        X931SecureRandom x931SecureRandom = this;
        synchronized (x931SecureRandom) {
            if (this.drbg.generate(arrby, this.predictionResistant) < 0) {
                this.drbg.reseed();
                this.drbg.generate(arrby, this.predictionResistant);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSeed(long l2) {
        X931SecureRandom x931SecureRandom = this;
        synchronized (x931SecureRandom) {
            if (this.randomSource != null) {
                this.randomSource.setSeed(l2);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSeed(byte[] arrby) {
        X931SecureRandom x931SecureRandom = this;
        synchronized (x931SecureRandom) {
            if (this.randomSource != null) {
                this.randomSource.setSeed(arrby);
            }
            return;
        }
    }
}

