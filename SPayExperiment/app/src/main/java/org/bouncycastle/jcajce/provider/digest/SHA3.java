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
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;

public class SHA3 {
    private SHA3() {
    }

    public static class Digest224
    extends DigestSHA3 {
        public Digest224() {
            super(224);
        }
    }

    public static class Digest256
    extends DigestSHA3 {
        public Digest256() {
            super(256);
        }
    }

    public static class Digest384
    extends DigestSHA3 {
        public Digest384() {
            super(384);
        }
    }

    public static class Digest512
    extends DigestSHA3 {
        public Digest512() {
            super(512);
        }
    }

    public static class DigestSHA3
    extends BCMessageDigest
    implements Cloneable {
        public DigestSHA3(int n) {
            super(new SHA3Digest(n));
        }

        public Object clone() {
            BCMessageDigest bCMessageDigest = (BCMessageDigest)((Object)super.clone());
            bCMessageDigest.digest = new SHA3Digest((SHA3Digest)this.digest);
            return bCMessageDigest;
        }
    }

    public static class HashMac224
    extends BaseMac {
        public HashMac224() {
            super(new HMac(new SHA3Digest(224)));
        }
    }

    public static class HashMac256
    extends BaseMac {
        public HashMac256() {
            super(new HMac(new SHA3Digest(256)));
        }
    }

    public static class HashMac384
    extends BaseMac {
        public HashMac384() {
            super(new HMac(new SHA3Digest(384)));
        }
    }

    public static class HashMac512
    extends BaseMac {
        public HashMac512() {
            super(new HMac(new SHA3Digest(512)));
        }
    }

    public static class KeyGenerator224
    extends BaseKeyGenerator {
        public KeyGenerator224() {
            super("HMACSHA3-224", 224, new CipherKeyGenerator());
        }
    }

    public static class KeyGenerator256
    extends BaseKeyGenerator {
        public KeyGenerator256() {
            super("HMACSHA3-256", 256, new CipherKeyGenerator());
        }
    }

    public static class KeyGenerator384
    extends BaseKeyGenerator {
        public KeyGenerator384() {
            super("HMACSHA3-384", 384, new CipherKeyGenerator());
        }
    }

    public static class KeyGenerator512
    extends BaseKeyGenerator {
        public KeyGenerator512() {
            super("HMACSHA3-512", 512, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = SHA3.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.SHA3-224", PREFIX + "$Digest224");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-256", PREFIX + "$Digest256");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-384", PREFIX + "$Digest384");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-512", PREFIX + "$Digest512");
            this.addHMACAlgorithm(configurableProvider, "SHA3-224", PREFIX + "$HashMac224", PREFIX + "$KeyGenerator224");
            this.addHMACAlgorithm(configurableProvider, "SHA3-256", PREFIX + "$HashMac256", PREFIX + "$KeyGenerator256");
            this.addHMACAlgorithm(configurableProvider, "SHA3-384", PREFIX + "$HashMac384", PREFIX + "$KeyGenerator384");
            this.addHMACAlgorithm(configurableProvider, "SHA3-512", PREFIX + "$HashMac512", PREFIX + "$KeyGenerator512");
        }
    }

}

