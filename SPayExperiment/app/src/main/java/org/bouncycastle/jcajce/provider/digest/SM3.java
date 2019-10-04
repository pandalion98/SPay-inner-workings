/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.digest;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;

public class SM3 {
    private SM3() {
    }

    public static class Digest
    extends BCMessageDigest
    implements Cloneable {
        public Digest() {
            super(new SM3Digest());
        }

        public Object clone() {
            Digest digest = (Digest)((Object)super.clone());
            digest.digest = new SM3Digest((SM3Digest)this.digest);
            return digest;
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = SM3.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.SM3", PREFIX + "$Digest");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SM3", "SM3");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");
        }
    }

}

