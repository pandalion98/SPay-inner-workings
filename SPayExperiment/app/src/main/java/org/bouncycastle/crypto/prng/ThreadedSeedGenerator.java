/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Thread
 */
package org.bouncycastle.crypto.prng;

public class ThreadedSeedGenerator {
    public byte[] generateSeed(int n2, boolean bl) {
        return new SeedGenerator().generateSeed(n2, bl);
    }

    private class SeedGenerator
    implements Runnable {
        private volatile int counter = 0;
        private volatile boolean stop = false;

        private SeedGenerator() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public byte[] generateSeed(int n2, boolean bl) {
            int n3 = 0;
            Thread thread = new Thread((Runnable)this);
            byte[] arrby = new byte[n2];
            this.counter = 0;
            this.stop = false;
            thread.start();
            if (!bl) {
                n2 *= 8;
            }
            int n4 = 0;
            do {
                if (n3 >= n2) {
                    this.stop = true;
                    return arrby;
                }
                while (this.counter == n4) {
                    try {
                        Thread.sleep((long)1L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
                n4 = this.counter;
                if (bl) {
                    arrby[n3] = (byte)(n4 & 255);
                } else {
                    int n5 = n3 / 8;
                    arrby[n5] = (byte)(arrby[n5] << 1 | n4 & 1);
                }
                ++n3;
            } while (true);
        }

        public void run() {
            while (!this.stop) {
                this.counter = 1 + this.counter;
            }
        }
    }

}

