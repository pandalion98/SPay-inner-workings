package org.bouncycastle.crypto.prng;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class ThreadedSeedGenerator {

    private class SeedGenerator implements Runnable {
        private volatile int counter;
        private volatile boolean stop;

        private SeedGenerator() {
            this.counter = 0;
            this.stop = false;
        }

        public byte[] generateSeed(int i, boolean z) {
            int i2 = 0;
            Thread thread = new Thread(this);
            byte[] bArr = new byte[i];
            this.counter = 0;
            this.stop = false;
            thread.start();
            if (!z) {
                i *= 8;
            }
            int i3 = 0;
            while (i2 < i) {
                while (this.counter == i3) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
                i3 = this.counter;
                if (z) {
                    bArr[i2] = (byte) (i3 & GF2Field.MASK);
                } else {
                    int i4 = i2 / 8;
                    bArr[i4] = (byte) ((bArr[i4] << 1) | (i3 & 1));
                }
                i2++;
            }
            this.stop = true;
            return bArr;
        }

        public void run() {
            while (!this.stop) {
                this.counter++;
            }
        }
    }

    public byte[] generateSeed(int i, boolean z) {
        return new SeedGenerator().generateSeed(i, z);
    }
}
