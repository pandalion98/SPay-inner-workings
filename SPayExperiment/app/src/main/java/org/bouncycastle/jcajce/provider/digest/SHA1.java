/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  javax.crypto.SecretKey
 *  javax.crypto.spec.PBEKeySpec
 */
package org.bouncycastle.jcajce.provider.digest;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.iana.IANAObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest;
import org.bouncycastle.jcajce.provider.digest.DigestAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseSecretKeyFactory;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;

public class SHA1 {
    private SHA1() {
    }

    public static class BasePBKDF2WithHmacSHA1
    extends BaseSecretKeyFactory {
        private int scheme;

        public BasePBKDF2WithHmacSHA1(String string, int n) {
            super(string, PKCSObjectIdentifiers.id_PBKDF2);
            this.scheme = n;
        }

        @Override
        protected SecretKey engineGenerateSecret(KeySpec keySpec) {
            if (keySpec instanceof PBEKeySpec) {
                PBEKeySpec pBEKeySpec = (PBEKeySpec)keySpec;
                if (pBEKeySpec.getSalt() == null) {
                    throw new InvalidKeySpecException("missing required salt");
                }
                if (pBEKeySpec.getIterationCount() <= 0) {
                    throw new InvalidKeySpecException("positive iteration count required: " + pBEKeySpec.getIterationCount());
                }
                if (pBEKeySpec.getKeyLength() <= 0) {
                    throw new InvalidKeySpecException("positive key length required: " + pBEKeySpec.getKeyLength());
                }
                if (pBEKeySpec.getPassword().length == 0) {
                    throw new IllegalArgumentException("password empty");
                }
                int n = pBEKeySpec.getKeyLength();
                CipherParameters cipherParameters = PBE.Util.makePBEMacParameters(pBEKeySpec, this.scheme, 1, n);
                return new BCPBEKey(this.algName, this.algOid, this.scheme, 1, n, -1, pBEKeySpec, cipherParameters);
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }

    public static class Digest
    extends BCMessageDigest
    implements Cloneable {
        public Digest() {
            super(new SHA1Digest());
        }

        public Object clone() {
            Digest digest = (Digest)((Object)super.clone());
            digest.digest = new SHA1Digest((SHA1Digest)this.digest);
            return digest;
        }
    }

    public static class HashMac
    extends BaseMac {
        public HashMac() {
            super(new HMac(new SHA1Digest()));
        }
    }

    public static class KeyGenerator
    extends BaseKeyGenerator {
        public KeyGenerator() {
            super("HMACSHA1", 160, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends DigestAlgorithmProvider {
        private static final String PREFIX = SHA1.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.SHA-1", PREFIX + "$Digest");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SHA1", "SHA-1");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SHA", "SHA-1");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest." + OIWObjectIdentifiers.idSHA1, "SHA-1");
            this.addHMACAlgorithm(configurableProvider, "SHA1", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
            this.addHMACAlias(configurableProvider, "SHA1", PKCSObjectIdentifiers.id_hmacWithSHA1);
            this.addHMACAlias(configurableProvider, "SHA1", IANAObjectIdentifiers.hmacSHA1);
            configurableProvider.addAlgorithm("Mac.PBEWITHHMACSHA", PREFIX + "$SHA1Mac");
            configurableProvider.addAlgorithm("Mac.PBEWITHHMACSHA1", PREFIX + "$SHA1Mac");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHHMACSHA", "PBEWITHHMACSHA1");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + OIWObjectIdentifiers.idSHA1, "PBEWITHHMACSHA1");
            configurableProvider.addAlgorithm("Alg.Alias.Mac." + OIWObjectIdentifiers.idSHA1, "PBEWITHHMACSHA");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHHMACSHA1", PREFIX + "$PBEWithMacKeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WithHmacSHA1", PREFIX + "$PBKDF2WithHmacSHA1UTF8");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBKDF2WithHmacSHA1AndUTF8", "PBKDF2WithHmacSHA1");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WithHmacSHA1And8BIT", PREFIX + "$PBKDF2WithHmacSHA18BIT");
        }
    }

    public static class PBEWithMacKeyFactory
    extends PBESecretKeyFactory {
        public PBEWithMacKeyFactory() {
            super("PBEwithHmacSHA", null, false, 2, 1, 160, 0);
        }
    }

    public static class PBKDF2WithHmacSHA18BIT
    extends BasePBKDF2WithHmacSHA1 {
        public PBKDF2WithHmacSHA18BIT() {
            super("PBKDF2WithHmacSHA1And8bit", 1);
        }
    }

    public static class PBKDF2WithHmacSHA1UTF8
    extends BasePBKDF2WithHmacSHA1 {
        public PBKDF2WithHmacSHA1UTF8() {
            super("PBKDF2WithHmacSHA1", 5);
        }
    }

    public static class SHA1Mac
    extends BaseMac {
        public SHA1Mac() {
            super(new HMac(new SHA1Digest()));
        }
    }

}

