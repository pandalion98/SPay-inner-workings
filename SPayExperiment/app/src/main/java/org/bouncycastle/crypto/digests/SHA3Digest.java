/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Arrays;

public class SHA3Digest
implements ExtendedDigest {
    private static int[] KeccakRhoOffsets;
    private static long[] KeccakRoundConstants;
    long[] C = new long[5];
    private int bitsAvailableForSqueezing;
    private int bitsInQueue;
    long[] chiC = new long[5];
    private byte[] chunk;
    private byte[] dataQueue = new byte[192];
    private int fixedOutputLength;
    private byte[] oneByte;
    private int rate;
    private boolean squeezing;
    private byte[] state = new byte[200];
    long[] tempA = new long[25];

    static {
        KeccakRoundConstants = SHA3Digest.keccakInitializeRoundConstants();
        KeccakRhoOffsets = SHA3Digest.keccakInitializeRhoOffsets();
    }

    public SHA3Digest() {
        this.init(0);
    }

    public SHA3Digest(int n2) {
        this.init(n2);
    }

    public SHA3Digest(SHA3Digest sHA3Digest) {
        System.arraycopy((Object)sHA3Digest.state, (int)0, (Object)this.state, (int)0, (int)sHA3Digest.state.length);
        System.arraycopy((Object)sHA3Digest.dataQueue, (int)0, (Object)this.dataQueue, (int)0, (int)sHA3Digest.dataQueue.length);
        this.rate = sHA3Digest.rate;
        this.bitsInQueue = sHA3Digest.bitsInQueue;
        this.fixedOutputLength = sHA3Digest.fixedOutputLength;
        this.squeezing = sHA3Digest.squeezing;
        this.bitsAvailableForSqueezing = sHA3Digest.bitsAvailableForSqueezing;
        this.chunk = Arrays.clone((byte[])sHA3Digest.chunk);
        this.oneByte = Arrays.clone((byte[])sHA3Digest.oneByte);
    }

    private void KeccakAbsorb(byte[] arrby, byte[] arrby2, int n2) {
        this.keccakPermutationAfterXor(arrby, arrby2, n2);
    }

    private void KeccakExtract(byte[] arrby, byte[] arrby2, int n2) {
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)(n2 * 8));
    }

    private void KeccakExtract1024bits(byte[] arrby, byte[] arrby2) {
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)128);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean LFSR86540(byte[] arrby) {
        boolean bl = (1 & arrby[0]) != 0;
        if ((128 & arrby[0]) != 0) {
            arrby[0] = (byte)(113 ^ arrby[0] << 1);
            return bl;
        }
        arrby[0] = (byte)(arrby[0] << 1);
        return bl;
    }

    private void absorb(byte[] arrby, int n2, long l2) {
        if (this.bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue.");
        }
        if (this.squeezing) {
            throw new IllegalStateException("attempt to absorb while squeezing.");
        }
        long l3 = 0L;
        while (l3 < l2) {
            if (this.bitsInQueue == 0 && l2 >= (long)this.rate && l3 <= l2 - (long)this.rate) {
                long l4 = (l2 - l3) / (long)this.rate;
                for (long i2 = 0L; i2 < l4; ++i2) {
                    System.arraycopy((Object)arrby, (int)((int)((long)n2 + l3 / 8L + i2 * (long)this.chunk.length)), (Object)this.chunk, (int)0, (int)this.chunk.length);
                    this.KeccakAbsorb(this.state, this.chunk, this.chunk.length);
                }
                l3 += l4 * (long)this.rate;
                continue;
            }
            int n3 = (int)(l2 - l3);
            if (n3 + this.bitsInQueue > this.rate) {
                n3 = this.rate - this.bitsInQueue;
            }
            int n4 = n3 % 8;
            int n5 = n3 - n4;
            System.arraycopy((Object)arrby, (int)(n2 + (int)(l3 / 8L)), (Object)this.dataQueue, (int)(this.bitsInQueue / 8), (int)(n5 / 8));
            this.bitsInQueue = n5 + this.bitsInQueue;
            long l5 = l3 + (long)n5;
            if (this.bitsInQueue == this.rate) {
                this.absorbQueue();
            }
            if (n4 > 0) {
                int n6 = -1 + (1 << n4);
                this.dataQueue[this.bitsInQueue / 8] = (byte)(n6 & arrby[n2 + (int)(l5 / 8L)]);
                this.bitsInQueue = n4 + this.bitsInQueue;
                l5 += (long)n4;
            }
            l3 = l5;
        }
    }

    private void absorbQueue() {
        this.KeccakAbsorb(this.state, this.dataQueue, this.rate / 8);
        this.bitsInQueue = 0;
    }

    private void chi(long[] arrl) {
        for (int i2 = 0; i2 < 5; ++i2) {
            for (int i3 = 0; i3 < 5; ++i3) {
                this.chiC[i3] = arrl[i3 + i2 * 5] ^ (-1L ^ arrl[(i3 + 1) % 5 + i2 * 5]) & arrl[(i3 + 2) % 5 + i2 * 5];
            }
            for (int i4 = 0; i4 < 5; ++i4) {
                arrl[i4 + i2 * 5] = this.chiC[i4];
            }
        }
    }

    private void clearDataQueueSection(int n2, int n3) {
        for (int i2 = n2; i2 != n2 + n3; ++i2) {
            this.dataQueue[i2] = 0;
        }
    }

    private void doUpdate(byte[] arrby, int n2, long l2) {
        if (l2 % 8L == 0L) {
            this.absorb(arrby, n2, l2);
            return;
        }
        this.absorb(arrby, n2, l2 - l2 % 8L);
        byte[] arrby2 = new byte[]{(byte)(arrby[n2 + (int)(l2 / 8L)] >> (int)(8L - l2 % 8L))};
        this.absorb(arrby2, n2, l2 % 8L);
    }

    private void fromBytesToWords(long[] arrl, byte[] arrby) {
        for (int i2 = 0; i2 < 25; ++i2) {
            arrl[i2] = 0L;
            int n2 = i2 * 8;
            for (int i3 = 0; i3 < 8; ++i3) {
                arrl[i2] = arrl[i2] | (255L & (long)arrby[n2 + i3]) << i3 * 8;
            }
        }
    }

    private void fromWordsToBytes(byte[] arrby, long[] arrl) {
        for (int i2 = 0; i2 < 25; ++i2) {
            int n2 = i2 * 8;
            for (int i3 = 0; i3 < 8; ++i3) {
                arrby[n2 + i3] = (byte)(255L & arrl[i2] >>> i3 * 8);
            }
        }
    }

    private void init(int n2) {
        switch (n2) {
            default: {
                throw new IllegalArgumentException("bitLength must be one of 224, 256, 384, or 512.");
            }
            case 0: 
            case 288: {
                this.initSponge(1024, 576);
                return;
            }
            case 224: {
                this.initSponge(1152, 448);
                return;
            }
            case 256: {
                this.initSponge(1088, 512);
                return;
            }
            case 384: {
                this.initSponge(832, 768);
                return;
            }
            case 512: 
        }
        this.initSponge(576, 1024);
    }

    private void initSponge(int n2, int n3) {
        if (n2 + n3 != 1600) {
            throw new IllegalStateException("rate + capacity != 1600");
        }
        if (n2 <= 0 || n2 >= 1600 || n2 % 64 != 0) {
            throw new IllegalStateException("invalid rate value");
        }
        this.rate = n2;
        this.fixedOutputLength = 0;
        Arrays.fill((byte[])this.state, (byte)0);
        Arrays.fill((byte[])this.dataQueue, (byte)0);
        this.bitsInQueue = 0;
        this.squeezing = false;
        this.bitsAvailableForSqueezing = 0;
        this.fixedOutputLength = n3 / 2;
        this.chunk = new byte[n2 / 8];
        this.oneByte = new byte[1];
    }

    private void iota(long[] arrl, int n2) {
        arrl[0] = arrl[0] ^ KeccakRoundConstants[n2];
    }

    private static int[] keccakInitializeRhoOffsets() {
        int[] arrn = new int[25];
        arrn[0] = 0;
        int n2 = 1;
        int n3 = 0;
        for (int i2 = 0; i2 < 24; ++i2) {
            arrn[n2 % 5 + 5 * (n3 % 5)] = (i2 + 1) * (i2 + 2) / 2 % 64;
            int n4 = (n2 * 0 + n3 * 1) % 5;
            n3 = (n2 * 2 + n3 * 3) % 5;
            n2 = n4;
        }
        return arrn;
    }

    private static long[] keccakInitializeRoundConstants() {
        long[] arrl = new long[24];
        byte[] arrby = new byte[]{1};
        for (int i2 = 0; i2 < 24; ++i2) {
            arrl[i2] = 0L;
            for (int i3 = 0; i3 < 7; ++i3) {
                int n2 = -1 + (1 << i3);
                if (!SHA3Digest.LFSR86540(arrby)) continue;
                arrl[i2] = arrl[i2] ^ 1L << n2;
            }
        }
        return arrl;
    }

    private void keccakPermutation(byte[] arrby) {
        long[] arrl = new long[arrby.length / 8];
        this.fromBytesToWords(arrl, arrby);
        this.keccakPermutationOnWords(arrl);
        this.fromWordsToBytes(arrby, arrl);
    }

    private void keccakPermutationAfterXor(byte[] arrby, byte[] arrby2, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby[i2] = (byte)(arrby[i2] ^ arrby2[i2]);
        }
        this.keccakPermutation(arrby);
    }

    private void keccakPermutationOnWords(long[] arrl) {
        for (int i2 = 0; i2 < 24; ++i2) {
            this.theta(arrl);
            this.rho(arrl);
            this.pi(arrl);
            this.chi(arrl);
            this.iota(arrl, i2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void padAndSwitchToSqueezingPhase() {
        if (1 + this.bitsInQueue == this.rate) {
            byte[] arrby = this.dataQueue;
            int n2 = this.bitsInQueue / 8;
            arrby[n2] = (byte)(arrby[n2] | 1 << this.bitsInQueue % 8);
            this.absorbQueue();
            this.clearDataQueueSection(0, this.rate / 8);
        } else {
            this.clearDataQueueSection((7 + this.bitsInQueue) / 8, this.rate / 8 - (7 + this.bitsInQueue) / 8);
            byte[] arrby = this.dataQueue;
            int n3 = this.bitsInQueue / 8;
            arrby[n3] = (byte)(arrby[n3] | 1 << this.bitsInQueue % 8);
        }
        byte[] arrby = this.dataQueue;
        int n4 = (-1 + this.rate) / 8;
        arrby[n4] = (byte)(arrby[n4] | 1 << (-1 + this.rate) % 8);
        this.absorbQueue();
        if (this.rate == 1024) {
            this.KeccakExtract1024bits(this.state, this.dataQueue);
            this.bitsAvailableForSqueezing = 1024;
        } else {
            this.KeccakExtract(this.state, this.dataQueue, this.rate / 64);
            this.bitsAvailableForSqueezing = this.rate;
        }
        this.squeezing = true;
    }

    private void pi(long[] arrl) {
        System.arraycopy((Object)arrl, (int)0, (Object)this.tempA, (int)0, (int)this.tempA.length);
        for (int i2 = 0; i2 < 5; ++i2) {
            for (int i3 = 0; i3 < 5; ++i3) {
                arrl[i3 + 5 * ((i2 * 2 + i3 * 3) % 5)] = this.tempA[i2 + i3 * 5];
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void rho(long[] arrl) {
        int n2 = 0;
        while (n2 < 5) {
            for (int i2 = 0; i2 < 5; ++i2) {
                int n3 = n2 + i2 * 5;
                long l2 = KeccakRhoOffsets[n3] != 0 ? arrl[n3] << KeccakRhoOffsets[n3] ^ arrl[n3] >>> 64 - KeccakRhoOffsets[n3] : arrl[n3];
                arrl[n3] = l2;
            }
            ++n2;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void squeeze(byte[] arrby, int n2, long l2) {
        if (!this.squeezing) {
            this.padAndSwitchToSqueezingPhase();
        }
        if (l2 % 8L != 0L) {
            throw new IllegalStateException("outputLength not a multiple of 8");
        }
        long l3 = 0L;
        while (l3 < l2) {
            int n3;
            if (this.bitsAvailableForSqueezing == 0) {
                this.keccakPermutation(this.state);
                if (this.rate == 1024) {
                    this.KeccakExtract1024bits(this.state, this.dataQueue);
                    this.bitsAvailableForSqueezing = 1024;
                } else {
                    this.KeccakExtract(this.state, this.dataQueue, this.rate / 64);
                    this.bitsAvailableForSqueezing = this.rate;
                }
            }
            if ((long)(n3 = this.bitsAvailableForSqueezing) > l2 - l3) {
                n3 = (int)(l2 - l3);
            }
            System.arraycopy((Object)this.dataQueue, (int)((this.rate - this.bitsAvailableForSqueezing) / 8), (Object)arrby, (int)(n2 + (int)(l3 / 8L)), (int)(n3 / 8));
            this.bitsAvailableForSqueezing -= n3;
            l3 += (long)n3;
        }
        return;
    }

    private void theta(long[] arrl) {
        for (int i2 = 0; i2 < 5; ++i2) {
            this.C[i2] = 0L;
            for (int i3 = 0; i3 < 5; ++i3) {
                long[] arrl2 = this.C;
                arrl2[i2] = arrl2[i2] ^ arrl[i2 + i3 * 5];
            }
        }
        for (int i4 = 0; i4 < 5; ++i4) {
            long l2 = this.C[(i4 + 1) % 5] << 1 ^ this.C[(i4 + 1) % 5] >>> 63 ^ this.C[(i4 + 4) % 5];
            for (int i5 = 0; i5 < 5; ++i5) {
                int n2 = i4 + i5 * 5;
                arrl[n2] = l2 ^ arrl[n2];
            }
        }
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.squeeze(arrby, n2, this.fixedOutputLength);
        this.reset();
        return this.getDigestSize();
    }

    @Override
    public String getAlgorithmName() {
        return "SHA3-" + this.fixedOutputLength;
    }

    @Override
    public int getByteLength() {
        return this.rate / 8;
    }

    @Override
    public int getDigestSize() {
        return this.fixedOutputLength / 8;
    }

    @Override
    public void reset() {
        this.init(this.fixedOutputLength);
    }

    @Override
    public void update(byte by) {
        this.oneByte[0] = by;
        this.doUpdate(this.oneByte, 0, 8L);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.doUpdate(arrby, n2, 8L * (long)n3);
    }
}

