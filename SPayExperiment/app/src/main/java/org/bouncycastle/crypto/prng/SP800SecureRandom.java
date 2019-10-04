/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.DRBGProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;

public class SP800SecureRandom
extends SecureRandom {
    private SP80090DRBG drbg;
    private final DRBGProvider drbgProvider;
    private final EntropySource entropySource;
    private final boolean predictionResistant;
    private final SecureRandom randomSource;

    SP800SecureRandom(SecureRandom secureRandom, EntropySource entropySource, DRBGProvider dRBGProvider, boolean bl) {
        this.randomSource = secureRandom;
        this.entropySource = entropySource;
        this.drbgProvider = dRBGProvider;
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
        SP800SecureRandom sP800SecureRandom = this;
        synchronized (sP800SecureRandom) {
            if (this.drbg == null) {
                this.drbg = this.drbgProvider.get(this.entropySource);
            }
            if (this.drbg.generate(arrby, null, this.predictionResistant) < 0) {
                this.drbg.reseed(null);
                this.drbg.generate(arrby, null, this.predictionResistant);
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
        SP800SecureRandom sP800SecureRandom = this;
        synchronized (sP800SecureRandom) {
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
        SP800SecureRandom sP800SecureRandom = this;
        synchronized (sP800SecureRandom) {
            if (this.randomSource != null) {
                this.randomSource.setSeed(arrby);
            }
            return;
        }
    }
}

