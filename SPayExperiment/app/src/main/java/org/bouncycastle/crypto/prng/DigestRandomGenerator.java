/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.RandomGenerator;

public class DigestRandomGenerator
implements RandomGenerator {
    private static long CYCLE_COUNT = 10L;
    private Digest digest;
    private byte[] seed;
    private long seedCounter;
    private byte[] state;
    private long stateCounter;

    public DigestRandomGenerator(Digest digest) {
        this.digest = digest;
        this.seed = new byte[digest.getDigestSize()];
        this.seedCounter = 1L;
        this.state = new byte[digest.getDigestSize()];
        this.stateCounter = 1L;
    }

    private void cycleSeed() {
        this.digestUpdate(this.seed);
        long l2 = this.seedCounter;
        this.seedCounter = 1L + l2;
        this.digestAddCounter(l2);
        this.digestDoFinal(this.seed);
    }

    private void digestAddCounter(long l2) {
        for (int i2 = 0; i2 != 8; ++i2) {
            this.digest.update((byte)l2);
            l2 >>>= 8;
        }
    }

    private void digestDoFinal(byte[] arrby) {
        this.digest.doFinal(arrby, 0);
    }

    private void digestUpdate(byte[] arrby) {
        this.digest.update(arrby, 0, arrby.length);
    }

    private void generateState() {
        long l2 = this.stateCounter;
        this.stateCounter = 1L + l2;
        this.digestAddCounter(l2);
        this.digestUpdate(this.state);
        this.digestUpdate(this.seed);
        this.digestDoFinal(this.state);
        if (this.stateCounter % CYCLE_COUNT == 0L) {
            this.cycleSeed();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void addSeedMaterial(long l2) {
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            this.digestAddCounter(l2);
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
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
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            this.digestUpdate(arrby);
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
            return;
        }
    }

    @Override
    public void nextBytes(byte[] arrby) {
        this.nextBytes(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void nextBytes(byte[] arrby, int n2, int n3) {
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            this.generateState();
            int n4 = n2 + n3;
            int n5 = 0;
            while (n2 != n4) {
                if (n5 == this.state.length) {
                    this.generateState();
                    n5 = 0;
                }
                byte[] arrby2 = this.state;
                int n6 = n5 + 1;
                arrby[n2] = arrby2[n5];
                ++n2;
                n5 = n6;
            }
            return;
        }
    }
}

