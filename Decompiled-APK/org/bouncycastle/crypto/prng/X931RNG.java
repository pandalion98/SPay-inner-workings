package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;

public class X931RNG {
    private static final int BLOCK128_MAX_BITS_REQUEST = 262144;
    private static final long BLOCK128_RESEED_MAX = 8388608;
    private static final int BLOCK64_MAX_BITS_REQUEST = 4096;
    private static final long BLOCK64_RESEED_MAX = 32768;
    private final byte[] DT;
    private final byte[] f255I;
    private final byte[] f256R;
    private byte[] f257V;
    private final BlockCipher engine;
    private final EntropySource entropySource;
    private long reseedCounter;

    public X931RNG(BlockCipher blockCipher, byte[] bArr, EntropySource entropySource) {
        this.reseedCounter = 1;
        this.engine = blockCipher;
        this.entropySource = entropySource;
        this.DT = new byte[blockCipher.getBlockSize()];
        System.arraycopy(bArr, 0, this.DT, 0, this.DT.length);
        this.f255I = new byte[blockCipher.getBlockSize()];
        this.f256R = new byte[blockCipher.getBlockSize()];
    }

    private void increment(byte[] bArr) {
        int length = bArr.length - 1;
        while (length >= 0) {
            byte b = (byte) (bArr[length] + 1);
            bArr[length] = b;
            if (b == null) {
                length--;
            } else {
                return;
            }
        }
    }

    private static boolean isTooLarge(byte[] bArr, int i) {
        return bArr != null && bArr.length > i;
    }

    private void process(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) (bArr2[i] ^ bArr3[i]);
        }
        this.engine.processBlock(bArr, 0, bArr, 0);
    }

    int generate(byte[] bArr, boolean z) {
        int i;
        if (this.f256R.length == 8) {
            if (this.reseedCounter > BLOCK64_RESEED_MAX) {
                return -1;
            }
            if (isTooLarge(bArr, SkeinMac.SKEIN_512)) {
                throw new IllegalArgumentException("Number of bits per request limited to 4096");
            }
        } else if (this.reseedCounter > BLOCK128_RESEED_MAX) {
            return -1;
        } else {
            if (isTooLarge(bArr, X509KeyUsage.decipherOnly)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (z || this.f257V == null) {
            this.f257V = this.entropySource.getEntropy();
        }
        int length = bArr.length / this.f256R.length;
        for (i = 0; i < length; i++) {
            this.engine.processBlock(this.DT, 0, this.f255I, 0);
            process(this.f256R, this.f255I, this.f257V);
            process(this.f257V, this.f256R, this.f255I);
            System.arraycopy(this.f256R, 0, bArr, this.f256R.length * i, this.f256R.length);
            increment(this.DT);
        }
        i = bArr.length - (this.f256R.length * length);
        if (i > 0) {
            this.engine.processBlock(this.DT, 0, this.f255I, 0);
            process(this.f256R, this.f255I, this.f257V);
            process(this.f257V, this.f256R, this.f255I);
            System.arraycopy(this.f256R, 0, bArr, length * this.f256R.length, i);
            increment(this.DT);
        }
        this.reseedCounter++;
        return bArr.length;
    }

    void reseed() {
        this.f257V = this.entropySource.getEntropy();
        this.reseedCounter = 1;
    }
}
