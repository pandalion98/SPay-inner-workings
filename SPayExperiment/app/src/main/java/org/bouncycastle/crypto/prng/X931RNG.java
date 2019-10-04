/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.prng.EntropySource;

public class X931RNG {
    private static final int BLOCK128_MAX_BITS_REQUEST = 262144;
    private static final long BLOCK128_RESEED_MAX = 0x800000L;
    private static final int BLOCK64_MAX_BITS_REQUEST = 4096;
    private static final long BLOCK64_RESEED_MAX = 32768L;
    private final byte[] DT;
    private final byte[] I;
    private final byte[] R;
    private byte[] V;
    private final BlockCipher engine;
    private final EntropySource entropySource;
    private long reseedCounter = 1L;

    public X931RNG(BlockCipher blockCipher, byte[] arrby, EntropySource entropySource) {
        this.engine = blockCipher;
        this.entropySource = entropySource;
        this.DT = new byte[blockCipher.getBlockSize()];
        System.arraycopy((Object)arrby, (int)0, (Object)this.DT, (int)0, (int)this.DT.length);
        this.I = new byte[blockCipher.getBlockSize()];
        this.R = new byte[blockCipher.getBlockSize()];
    }

    private void increment(byte[] arrby) {
        int n2 = -1 + arrby.length;
        do {
            block4 : {
                block3 : {
                    byte by;
                    if (n2 < 0) break block3;
                    arrby[n2] = by = (byte)(1 + arrby[n2]);
                    if (by == 0) break block4;
                }
                return;
            }
            --n2;
        } while (true);
    }

    private static boolean isTooLarge(byte[] arrby, int n2) {
        return arrby != null && arrby.length > n2;
    }

    private void process(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            arrby[i2] = (byte)(arrby2[i2] ^ arrby3[i2]);
        }
        this.engine.processBlock(arrby, 0, arrby, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    int generate(byte[] arrby, boolean bl) {
        block11 : {
            block12 : {
                block10 : {
                    block9 : {
                        if (this.R.length != 8) break block9;
                        if (this.reseedCounter > 32768L) break block10;
                        if (X931RNG.isTooLarge(arrby, 512)) {
                            throw new IllegalArgumentException("Number of bits per request limited to 4096");
                        }
                        break block11;
                    }
                    if (this.reseedCounter <= 0x800000L) break block12;
                }
                return -1;
            }
            if (X931RNG.isTooLarge(arrby, 32768)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (bl || this.V == null) {
            this.V = this.entropySource.getEntropy();
        }
        int n2 = arrby.length / this.R.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            System.arraycopy((Object)this.R, (int)0, (Object)arrby, (int)(i2 * this.R.length), (int)this.R.length);
            this.increment(this.DT);
        }
        int n3 = arrby.length - n2 * this.R.length;
        if (n3 > 0) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            System.arraycopy((Object)this.R, (int)0, (Object)arrby, (int)(n2 * this.R.length), (int)n3);
            this.increment(this.DT);
        }
        this.reseedCounter = 1L + this.reseedCounter;
        return arrby.length;
    }

    void reseed() {
        this.V = this.entropySource.getEntropy();
        this.reseedCounter = 1L;
    }
}

