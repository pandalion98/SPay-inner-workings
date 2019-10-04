/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.digest;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;

public class Whirlpool {
    private Whirlpool() {
    }

    public static class Digest
    extends BCMessageDigest
    implements Cloneable {
        public Digest() {
            super(new WhirlpoolDigest());
        }

        public Object clone() {
            Digest digest = (Digest)((Object)super.clone());
            digest.digest = new WhirlpoolDigest((WhirlpoolDigest)this.digest);
            return digest;
        }
    }

    public static class HashMac
    extends BaseMac {
        public HashMac() {
            super(new HMac(new WhirlpoolDigest()));
        }
    }

    public static class KeyGenerator
    extends BaseKeyGenerator {
        public KeyGenerator() {
            super("HMACWHIRLPOOL", 512, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = Whirlpool.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.WHIRLPOOL", PREFIX + "$Digest");
            this.addHMACAlgorithm(configurableProvider, "WHIRLPOOL", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }

}

