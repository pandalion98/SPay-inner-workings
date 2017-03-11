package org.bouncycastle.crypto.digests;

import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Arrays;

public class SHA3Digest implements ExtendedDigest {
    private static int[] KeccakRhoOffsets;
    private static long[] KeccakRoundConstants;
    long[] f134C;
    private int bitsAvailableForSqueezing;
    private int bitsInQueue;
    long[] chiC;
    private byte[] chunk;
    private byte[] dataQueue;
    private int fixedOutputLength;
    private byte[] oneByte;
    private int rate;
    private boolean squeezing;
    private byte[] state;
    long[] tempA;

    static {
        KeccakRoundConstants = keccakInitializeRoundConstants();
        KeccakRhoOffsets = keccakInitializeRhoOffsets();
    }

    public SHA3Digest() {
        this.state = new byte[DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE];
        this.dataQueue = new byte[CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256];
        this.f134C = new long[5];
        this.tempA = new long[25];
        this.chiC = new long[5];
        init(0);
    }

    public SHA3Digest(int i) {
        this.state = new byte[DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE];
        this.dataQueue = new byte[CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256];
        this.f134C = new long[5];
        this.tempA = new long[25];
        this.chiC = new long[5];
        init(i);
    }

    public SHA3Digest(SHA3Digest sHA3Digest) {
        this.state = new byte[DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE];
        this.dataQueue = new byte[CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256];
        this.f134C = new long[5];
        this.tempA = new long[25];
        this.chiC = new long[5];
        System.arraycopy(sHA3Digest.state, 0, this.state, 0, sHA3Digest.state.length);
        System.arraycopy(sHA3Digest.dataQueue, 0, this.dataQueue, 0, sHA3Digest.dataQueue.length);
        this.rate = sHA3Digest.rate;
        this.bitsInQueue = sHA3Digest.bitsInQueue;
        this.fixedOutputLength = sHA3Digest.fixedOutputLength;
        this.squeezing = sHA3Digest.squeezing;
        this.bitsAvailableForSqueezing = sHA3Digest.bitsAvailableForSqueezing;
        this.chunk = Arrays.clone(sHA3Digest.chunk);
        this.oneByte = Arrays.clone(sHA3Digest.oneByte);
    }

    private void KeccakAbsorb(byte[] bArr, byte[] bArr2, int i) {
        keccakPermutationAfterXor(bArr, bArr2, i);
    }

    private void KeccakExtract(byte[] bArr, byte[] bArr2, int i) {
        System.arraycopy(bArr, 0, bArr2, 0, i * 8);
    }

    private void KeccakExtract1024bits(byte[] bArr, byte[] bArr2) {
        System.arraycopy(bArr, 0, bArr2, 0, X509KeyUsage.digitalSignature);
    }

    private static boolean LFSR86540(byte[] bArr) {
        boolean z = (bArr[0] & 1) != 0;
        if ((bArr[0] & X509KeyUsage.digitalSignature) != 0) {
            bArr[0] = (byte) ((bArr[0] << 1) ^ 113);
        } else {
            bArr[0] = (byte) (bArr[0] << 1);
        }
        return z;
    }

    private void absorb(byte[] bArr, int i, long j) {
        if (this.bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue.");
        } else if (this.squeezing) {
            throw new IllegalStateException("attempt to absorb while squeezing.");
        } else {
            long j2 = 0;
            while (j2 < j) {
                long j3;
                if (this.bitsInQueue != 0 || j < ((long) this.rate) || j2 > j - ((long) this.rate)) {
                    int i2 = (int) (j - j2);
                    if (this.bitsInQueue + i2 > this.rate) {
                        i2 = this.rate - this.bitsInQueue;
                    }
                    int i3 = i2 % 8;
                    i2 -= i3;
                    System.arraycopy(bArr, ((int) (j2 / 8)) + i, this.dataQueue, this.bitsInQueue / 8, i2 / 8);
                    this.bitsInQueue += i2;
                    j3 = ((long) i2) + j2;
                    if (this.bitsInQueue == this.rate) {
                        absorbQueue();
                    }
                    if (i3 > 0) {
                        this.dataQueue[this.bitsInQueue / 8] = (byte) (((1 << i3) - 1) & bArr[((int) (j3 / 8)) + i]);
                        this.bitsInQueue += i3;
                        j3 += (long) i3;
                    }
                    j2 = j3;
                } else {
                    long j4 = (j - j2) / ((long) this.rate);
                    for (j3 = 0; j3 < j4; j3++) {
                        System.arraycopy(bArr, (int) ((((long) i) + (j2 / 8)) + (((long) this.chunk.length) * j3)), this.chunk, 0, this.chunk.length);
                        KeccakAbsorb(this.state, this.chunk, this.chunk.length);
                    }
                    j2 = (((long) this.rate) * j4) + j2;
                }
            }
        }
    }

    private void absorbQueue() {
        KeccakAbsorb(this.state, this.dataQueue, this.rate / 8);
        this.bitsInQueue = 0;
    }

    private void chi(long[] jArr) {
        for (int i = 0; i < 5; i++) {
            int i2;
            for (i2 = 0; i2 < 5; i2++) {
                this.chiC[i2] = jArr[(i * 5) + i2] ^ ((jArr[((i2 + 1) % 5) + (i * 5)] ^ -1) & jArr[((i2 + 2) % 5) + (i * 5)]);
            }
            for (i2 = 0; i2 < 5; i2++) {
                jArr[(i * 5) + i2] = this.chiC[i2];
            }
        }
    }

    private void clearDataQueueSection(int i, int i2) {
        for (int i3 = i; i3 != i + i2; i3++) {
            this.dataQueue[i3] = (byte) 0;
        }
    }

    private void doUpdate(byte[] bArr, int i, long j) {
        if (j % 8 == 0) {
            absorb(bArr, i, j);
            return;
        }
        absorb(bArr, i, j - (j % 8));
        absorb(new byte[]{(byte) (bArr[((int) (j / 8)) + i] >> ((int) (8 - (j % 8))))}, i, j % 8);
    }

    private void fromBytesToWords(long[] jArr, byte[] bArr) {
        for (int i = 0; i < 25; i++) {
            jArr[i] = 0;
            int i2 = i * 8;
            for (int i3 = 0; i3 < 8; i3++) {
                jArr[i] = jArr[i] | ((((long) bArr[i2 + i3]) & 255) << (i3 * 8));
            }
        }
    }

    private void fromWordsToBytes(byte[] bArr, long[] jArr) {
        for (int i = 0; i < 25; i++) {
            int i2 = i * 8;
            for (int i3 = 0; i3 < 8; i3++) {
                bArr[i2 + i3] = (byte) ((int) ((jArr[i] >>> (i3 * 8)) & 255));
            }
        }
    }

    private void init(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
            case 288:
                initSponge(SkeinMac.SKEIN_1024, 576);
            case 224:
                initSponge(1152, 448);
            case SkeinMac.SKEIN_256 /*256*/:
                initSponge(1088, SkeinMac.SKEIN_512);
            case 384:
                initSponge(832, McTACommands.MOP_MC_TA_CMD_CASD_BASE);
            case SkeinMac.SKEIN_512 /*512*/:
                initSponge(576, SkeinMac.SKEIN_1024);
            default:
                throw new IllegalArgumentException("bitLength must be one of 224, 256, 384, or 512.");
        }
    }

    private void initSponge(int i, int i2) {
        if (i + i2 != 1600) {
            throw new IllegalStateException("rate + capacity != 1600");
        } else if (i <= 0 || i >= 1600 || i % 64 != 0) {
            throw new IllegalStateException("invalid rate value");
        } else {
            this.rate = i;
            this.fixedOutputLength = 0;
            Arrays.fill(this.state, (byte) 0);
            Arrays.fill(this.dataQueue, (byte) 0);
            this.bitsInQueue = 0;
            this.squeezing = false;
            this.bitsAvailableForSqueezing = 0;
            this.fixedOutputLength = i2 / 2;
            this.chunk = new byte[(i / 8)];
            this.oneByte = new byte[1];
        }
    }

    private void iota(long[] jArr, int i) {
        jArr[0] = jArr[0] ^ KeccakRoundConstants[i];
    }

    private static int[] keccakInitializeRhoOffsets() {
        int i = 0;
        int[] iArr = new int[25];
        iArr[0] = 0;
        int i2 = 1;
        int i3 = 0;
        while (i < 24) {
            iArr[(i2 % 5) + ((i3 % 5) * 5)] = (((i + 1) * (i + 2)) / 2) % 64;
            i3 = ((i3 * 3) + (i2 * 2)) % 5;
            i++;
            i2 = ((i2 * 0) + (i3 * 1)) % 5;
        }
        return iArr;
    }

    private static long[] keccakInitializeRoundConstants() {
        long[] jArr = new long[24];
        byte[] bArr = new byte[]{(byte) 1};
        for (int i = 0; i < 24; i++) {
            jArr[i] = 0;
            for (int i2 = 0; i2 < 7; i2++) {
                int i3 = (1 << i2) - 1;
                if (LFSR86540(bArr)) {
                    jArr[i] = jArr[i] ^ (1 << i3);
                }
            }
        }
        return jArr;
    }

    private void keccakPermutation(byte[] bArr) {
        long[] jArr = new long[(bArr.length / 8)];
        fromBytesToWords(jArr, bArr);
        keccakPermutationOnWords(jArr);
        fromWordsToBytes(bArr, jArr);
    }

    private void keccakPermutationAfterXor(byte[] bArr, byte[] bArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) (bArr[i2] ^ bArr2[i2]);
        }
        keccakPermutation(bArr);
    }

    private void keccakPermutationOnWords(long[] jArr) {
        for (int i = 0; i < 24; i++) {
            theta(jArr);
            rho(jArr);
            pi(jArr);
            chi(jArr);
            iota(jArr, i);
        }
    }

    private void padAndSwitchToSqueezingPhase() {
        byte[] bArr;
        int i;
        if (this.bitsInQueue + 1 == this.rate) {
            bArr = this.dataQueue;
            i = this.bitsInQueue / 8;
            bArr[i] = (byte) (bArr[i] | (1 << (this.bitsInQueue % 8)));
            absorbQueue();
            clearDataQueueSection(0, this.rate / 8);
        } else {
            clearDataQueueSection((this.bitsInQueue + 7) / 8, (this.rate / 8) - ((this.bitsInQueue + 7) / 8));
            bArr = this.dataQueue;
            i = this.bitsInQueue / 8;
            bArr[i] = (byte) (bArr[i] | (1 << (this.bitsInQueue % 8)));
        }
        bArr = this.dataQueue;
        i = (this.rate - 1) / 8;
        bArr[i] = (byte) (bArr[i] | (1 << ((this.rate - 1) % 8)));
        absorbQueue();
        if (this.rate == SkeinMac.SKEIN_1024) {
            KeccakExtract1024bits(this.state, this.dataQueue);
            this.bitsAvailableForSqueezing = SkeinMac.SKEIN_1024;
        } else {
            KeccakExtract(this.state, this.dataQueue, this.rate / 64);
            this.bitsAvailableForSqueezing = this.rate;
        }
        this.squeezing = true;
    }

    private void pi(long[] jArr) {
        System.arraycopy(jArr, 0, this.tempA, 0, this.tempA.length);
        for (int i = 0; i < 5; i++) {
            for (int i2 = 0; i2 < 5; i2++) {
                jArr[((((i * 2) + (i2 * 3)) % 5) * 5) + i2] = this.tempA[(i2 * 5) + i];
            }
        }
    }

    private void rho(long[] jArr) {
        for (int i = 0; i < 5; i++) {
            for (int i2 = 0; i2 < 5; i2++) {
                int i3 = i + (i2 * 5);
                jArr[i3] = KeccakRhoOffsets[i3] != 0 ? (jArr[i3] << KeccakRhoOffsets[i3]) ^ (jArr[i3] >>> (64 - KeccakRhoOffsets[i3])) : jArr[i3];
            }
        }
    }

    private void squeeze(byte[] bArr, int i, long j) {
        if (!this.squeezing) {
            padAndSwitchToSqueezingPhase();
        }
        if (j % 8 != 0) {
            throw new IllegalStateException("outputLength not a multiple of 8");
        }
        long j2 = 0;
        while (j2 < j) {
            if (this.bitsAvailableForSqueezing == 0) {
                keccakPermutation(this.state);
                if (this.rate == SkeinMac.SKEIN_1024) {
                    KeccakExtract1024bits(this.state, this.dataQueue);
                    this.bitsAvailableForSqueezing = SkeinMac.SKEIN_1024;
                } else {
                    KeccakExtract(this.state, this.dataQueue, this.rate / 64);
                    this.bitsAvailableForSqueezing = this.rate;
                }
            }
            int i2 = this.bitsAvailableForSqueezing;
            if (((long) i2) > j - j2) {
                i2 = (int) (j - j2);
            }
            System.arraycopy(this.dataQueue, (this.rate - this.bitsAvailableForSqueezing) / 8, bArr, ((int) (j2 / 8)) + i, i2 / 8);
            this.bitsAvailableForSqueezing -= i2;
            j2 = ((long) i2) + j2;
        }
    }

    private void theta(long[] jArr) {
        int i;
        int i2;
        for (i = 0; i < 5; i++) {
            this.f134C[i] = 0;
            for (i2 = 0; i2 < 5; i2++) {
                long[] jArr2 = this.f134C;
                jArr2[i] = jArr2[i] ^ jArr[(i2 * 5) + i];
            }
        }
        for (i = 0; i < 5; i++) {
            long j = ((this.f134C[(i + 1) % 5] << 1) ^ (this.f134C[(i + 1) % 5] >>> 63)) ^ this.f134C[(i + 4) % 5];
            for (i2 = 0; i2 < 5; i2++) {
                int i3 = (i2 * 5) + i;
                jArr[i3] = jArr[i3] ^ j;
            }
        }
    }

    public int doFinal(byte[] bArr, int i) {
        squeeze(bArr, i, (long) this.fixedOutputLength);
        reset();
        return getDigestSize();
    }

    public String getAlgorithmName() {
        return "SHA3-" + this.fixedOutputLength;
    }

    public int getByteLength() {
        return this.rate / 8;
    }

    public int getDigestSize() {
        return this.fixedOutputLength / 8;
    }

    public void reset() {
        init(this.fixedOutputLength);
    }

    public void update(byte b) {
        this.oneByte[0] = b;
        doUpdate(this.oneByte, 0, 8);
    }

    public void update(byte[] bArr, int i, int i2) {
        doUpdate(bArr, i, ((long) i2) * 8);
    }
}
