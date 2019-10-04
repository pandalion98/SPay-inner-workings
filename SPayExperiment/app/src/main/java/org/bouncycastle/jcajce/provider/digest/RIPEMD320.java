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
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;

public class RIPEMD320 {
    private RIPEMD320() {
    }

    public static class Digest
    extends BCMessageDigest
    implements Cloneable {
        public Digest() {
            super(new RIPEMD320Digest());
        }

        public Object clone() {
            Digest digest = (Digest)((Object)super.clone());
            digest.digest = new RIPEMD320Digest((RIPEMD320Digest)this.digest);
            return digest;
        }
    }

    public static class HashMac
    extends BaseMac {
        public HashMac() {
            super(new HMac(new RIPEMD320Digest()));
        }
    }

    public static class KeyGenerator
    extends BaseKeyGenerator {
        public KeyGenerator() {
            super("HMACRIPEMD320", 320, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = RIPEMD320.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.RIPEMD320", PREFIX + "$Digest");
            this.addHMACAlgorithm(configurableProvider, "RIPEMD320", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }

}

