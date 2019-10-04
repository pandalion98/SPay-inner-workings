/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.digest;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;

public class MD4 {
    private MD4() {
    }

    public static class Digest
    extends BCMessageDigest
    implements Cloneable {
        public Digest() {
            super(new MD4Digest());
        }

        public Object clone() {
            Digest digest = (Digest)((Object)super.clone());
            digest.digest = new MD4Digest((MD4Digest)this.digest);
            return digest;
        }
    }

    public static class HashMac
    extends BaseMac {
        public HashMac() {
            super(new HMac(new MD4Digest()));
        }
    }

    public static class KeyGenerator
    extends BaseKeyGenerator {
        public KeyGenerator() {
            super("HMACMD4", 128, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = MD4.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.MD4", PREFIX + "$Digest");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest." + PKCSObjectIdentifiers.md4, "MD4");
            this.addHMACAlgorithm(configurableProvider, "MD4", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }

}

