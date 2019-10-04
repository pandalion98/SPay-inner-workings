/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.DRBGProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.SP800SecureRandom;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;
import org.bouncycastle.crypto.prng.drbg.DualECPoints;
import org.bouncycastle.crypto.prng.drbg.DualECSP800DRBG;
import org.bouncycastle.crypto.prng.drbg.HMacSP800DRBG;
import org.bouncycastle.crypto.prng.drbg.HashSP800DRBG;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;

public class SP800SecureRandomBuilder {
    private int entropyBitsRequired = 256;
    private final EntropySourceProvider entropySourceProvider;
    private byte[] personalizationString;
    private final SecureRandom random;
    private int securityStrength = 256;

    public SP800SecureRandomBuilder() {
        this(new SecureRandom(), false);
    }

    public SP800SecureRandomBuilder(SecureRandom secureRandom, boolean bl) {
        this.random = secureRandom;
        this.entropySourceProvider = new BasicEntropySourceProvider(this.random, bl);
    }

    public SP800SecureRandomBuilder(EntropySourceProvider entropySourceProvider) {
        this.random = null;
        this.entropySourceProvider = entropySourceProvider;
    }

    public SP800SecureRandom buildCTR(BlockCipher blockCipher, int n2, byte[] arrby, boolean bl) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new CTRDRBGProvider(blockCipher, n2, arrby, this.personalizationString, this.securityStrength), bl);
    }

    public SP800SecureRandom buildDualEC(Digest digest, byte[] arrby, boolean bl) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new DualECDRBGProvider(digest, arrby, this.personalizationString, this.securityStrength), bl);
    }

    public SP800SecureRandom buildDualEC(DualECPoints[] arrdualECPoints, Digest digest, byte[] arrby, boolean bl) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new ConfigurableDualECDRBGProvider(arrdualECPoints, digest, arrby, this.personalizationString, this.securityStrength), bl);
    }

    public SP800SecureRandom buildHMAC(Mac mac, byte[] arrby, boolean bl) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new HMacDRBGProvider(mac, arrby, this.personalizationString, this.securityStrength), bl);
    }

    public SP800SecureRandom buildHash(Digest digest, byte[] arrby, boolean bl) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new HashDRBGProvider(digest, arrby, this.personalizationString, this.securityStrength), bl);
    }

    public SP800SecureRandomBuilder setEntropyBitsRequired(int n2) {
        this.entropyBitsRequired = n2;
        return this;
    }

    public SP800SecureRandomBuilder setPersonalizationString(byte[] arrby) {
        this.personalizationString = arrby;
        return this;
    }

    public SP800SecureRandomBuilder setSecurityStrength(int n2) {
        this.securityStrength = n2;
        return this;
    }

    private static class CTRDRBGProvider
    implements DRBGProvider {
        private final BlockCipher blockCipher;
        private final int keySizeInBits;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;

        public CTRDRBGProvider(BlockCipher blockCipher, int n2, byte[] arrby, byte[] arrby2, int n3) {
            this.blockCipher = blockCipher;
            this.keySizeInBits = n2;
            this.nonce = arrby;
            this.personalizationString = arrby2;
            this.securityStrength = n3;
        }

        @Override
        public SP80090DRBG get(EntropySource entropySource) {
            return new CTRSP800DRBG(this.blockCipher, this.keySizeInBits, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }

    private static class ConfigurableDualECDRBGProvider
    implements DRBGProvider {
        private final Digest digest;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final DualECPoints[] pointSet;
        private final int securityStrength;

        public ConfigurableDualECDRBGProvider(DualECPoints[] arrdualECPoints, Digest digest, byte[] arrby, byte[] arrby2, int n2) {
            this.pointSet = new DualECPoints[arrdualECPoints.length];
            System.arraycopy((Object)arrdualECPoints, (int)0, (Object)this.pointSet, (int)0, (int)arrdualECPoints.length);
            this.digest = digest;
            this.nonce = arrby;
            this.personalizationString = arrby2;
            this.securityStrength = n2;
        }

        @Override
        public SP80090DRBG get(EntropySource entropySource) {
            return new DualECSP800DRBG(this.pointSet, this.digest, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }

    private static class DualECDRBGProvider
    implements DRBGProvider {
        private final Digest digest;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;

        public DualECDRBGProvider(Digest digest, byte[] arrby, byte[] arrby2, int n2) {
            this.digest = digest;
            this.nonce = arrby;
            this.personalizationString = arrby2;
            this.securityStrength = n2;
        }

        @Override
        public SP80090DRBG get(EntropySource entropySource) {
            return new DualECSP800DRBG(this.digest, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }

    private static class HMacDRBGProvider
    implements DRBGProvider {
        private final Mac hMac;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;

        public HMacDRBGProvider(Mac mac, byte[] arrby, byte[] arrby2, int n2) {
            this.hMac = mac;
            this.nonce = arrby;
            this.personalizationString = arrby2;
            this.securityStrength = n2;
        }

        @Override
        public SP80090DRBG get(EntropySource entropySource) {
            return new HMacSP800DRBG(this.hMac, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }

    private static class HashDRBGProvider
    implements DRBGProvider {
        private final Digest digest;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;

        public HashDRBGProvider(Digest digest, byte[] arrby, byte[] arrby2, int n2) {
            this.digest = digest;
            this.nonce = arrby;
            this.personalizationString = arrby2;
            this.securityStrength = n2;
        }

        @Override
        public SP80090DRBG get(EntropySource entropySource) {
            return new HashSP800DRBG(this.digest, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }

}

