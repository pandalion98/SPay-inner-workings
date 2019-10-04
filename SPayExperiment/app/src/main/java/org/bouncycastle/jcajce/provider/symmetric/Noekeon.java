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
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.NoekeonEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.GMac;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.SymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;

public final class Noekeon {
    private Noekeon() {
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
                AlgorithmParameters algorithmParameters = this.createParametersInstance("Noekeon");
                algorithmParameters.init((AlgorithmParameterSpec)new IvParameterSpec(arrby));
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Noekeon parameter generation.");
        }
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "Noekeon IV";
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new BlockCipherProvider(){

                @Override
                public BlockCipher get() {
                    return new NoekeonEngine();
                }
            });
        }

    }

    public static class GMAC
    extends BaseMac {
        public GMAC() {
            super(new GMac(new GCMBlockCipher(new NoekeonEngine())));
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("Noekeon", 128, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends SymmetricAlgorithmProvider {
        private static final String PREFIX = Noekeon.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.NOEKEON", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.NOEKEON", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("Cipher.NOEKEON", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("KeyGenerator.NOEKEON", PREFIX + "$KeyGen");
            this.addGMacAlgorithm(configurableProvider, "NOEKEON", PREFIX + "$GMAC", PREFIX + "$KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "NOEKEON", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
        }
    }

    public static class Poly1305
    extends BaseMac {
        public Poly1305() {
            super(new org.bouncycastle.crypto.macs.Poly1305(new NoekeonEngine()));
        }
    }

    public static class Poly1305KeyGen
    extends BaseKeyGenerator {
        public Poly1305KeyGen() {
            super("Poly1305-Noekeon", 256, new Poly1305KeyGenerator());
        }
    }

}

