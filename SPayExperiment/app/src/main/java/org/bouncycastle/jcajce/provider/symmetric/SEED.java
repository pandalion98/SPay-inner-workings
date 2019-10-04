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
import org.bouncycastle.asn1.kisa.KISAObjectIdentifiers;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.engines.SEEDWrapEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.GMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.SymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;

public final class SEED {
    private SEED() {
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
                AlgorithmParameters algorithmParameters = this.createParametersInstance("SEED");
                algorithmParameters.init((AlgorithmParameterSpec)new IvParameterSpec(arrby));
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for SEED parameter generation.");
        }
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "SEED IV";
        }
    }

    public static class CBC
    extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new SEEDEngine()), 128);
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new BlockCipherProvider(){

                @Override
                public BlockCipher get() {
                    return new SEEDEngine();
                }
            });
        }

    }

    public static class GMAC
    extends BaseMac {
        public GMAC() {
            super(new GMac(new GCMBlockCipher(new SEEDEngine())));
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("SEED", 128, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends SymmetricAlgorithmProvider {
        private static final String PREFIX = SEED.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.SEED", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + KISAObjectIdentifiers.id_seedCBC, "SEED");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.SEED", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + KISAObjectIdentifiers.id_seedCBC, "SEED");
            configurableProvider.addAlgorithm("Cipher.SEED", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher." + KISAObjectIdentifiers.id_seedCBC, PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Cipher.SEEDWRAP", PREFIX + "$Wrap");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "SEEDWRAP");
            configurableProvider.addAlgorithm("KeyGenerator.SEED", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator." + KISAObjectIdentifiers.id_seedCBC, PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, PREFIX + "$KeyGen");
            this.addGMacAlgorithm(configurableProvider, "SEED", PREFIX + "$GMAC", PREFIX + "$KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "SEED", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
        }
    }

    public static class Poly1305
    extends BaseMac {
        public Poly1305() {
            super(new org.bouncycastle.crypto.macs.Poly1305(new SEEDEngine()));
        }
    }

    public static class Poly1305KeyGen
    extends BaseKeyGenerator {
        public Poly1305KeyGen() {
            super("Poly1305-SEED", 256, new Poly1305KeyGenerator());
        }
    }

    public static class Wrap
    extends BaseWrapCipher {
        public Wrap() {
            super(new SEEDWrapEngine());
        }
    }

}

