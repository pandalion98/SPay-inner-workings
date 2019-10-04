/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.spec.IvParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.macs.GOST28147Mac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.GCFBBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class GOST28147 {
    private GOST28147() {
    }

    public static class AlgParamGen
    extends BaseAlgorithmParameterGenerator {
        protected AlgorithmParameters engineGenerateParameters() {
            byte[] arrby = new byte[16];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(arrby);
            try {
                AlgorithmParameters algorithmParameters = this.createParametersInstance("GOST28147");
                algorithmParameters.init((AlgorithmParameterSpec)new IvParameterSpec(arrby));
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for GOST28147 parameter generation.");
        }
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "GOST IV";
        }
    }

    public static class CBC
    extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new GOST28147Engine()), 64);
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new GOST28147Engine());
        }
    }

    public static class GCFB
    extends BaseBlockCipher {
        public GCFB() {
            super(new BufferedBlockCipher(new GCFBBlockCipher(new GOST28147Engine())), 64);
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            this(256);
        }

        public KeyGen(int n) {
            super("GOST28147", n, new CipherKeyGenerator());
        }
    }

    public static class Mac
    extends BaseMac {
        public Mac() {
            super(new GOST28147Mac());
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = GOST28147.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Cipher.GOST28147", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.GOST", "GOST28147");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.GOST-28147", "GOST28147");
            configurableProvider.addAlgorithm("Cipher." + CryptoProObjectIdentifiers.gostR28147_gcfb, PREFIX + "$GCFB");
            configurableProvider.addAlgorithm("KeyGenerator.GOST28147", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.GOST", "GOST28147");
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.GOST-28147", "GOST28147");
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator." + CryptoProObjectIdentifiers.gostR28147_gcfb, "GOST28147");
            configurableProvider.addAlgorithm("Mac.GOST28147MAC", PREFIX + "$Mac");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.GOST28147", "GOST28147MAC");
        }
    }

}

