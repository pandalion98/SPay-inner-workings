/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.symmetric;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.GMac;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.SymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;

public final class Serpent {
    private Serpent() {
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "Serpent IV";
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new BlockCipherProvider(){

                @Override
                public BlockCipher get() {
                    return new SerpentEngine();
                }
            });
        }

    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("Serpent", 192, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends SymmetricAlgorithmProvider {
        private static final String PREFIX = Serpent.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Cipher.Serpent", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("KeyGenerator.Serpent", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("AlgorithmParameters.Serpent", PREFIX + "$AlgParams");
            this.addGMacAlgorithm(configurableProvider, "SERPENT", PREFIX + "$SerpentGMAC", PREFIX + "$KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "SERPENT", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
        }
    }

    public static class Poly1305
    extends BaseMac {
        public Poly1305() {
            super(new org.bouncycastle.crypto.macs.Poly1305(new TwofishEngine()));
        }
    }

    public static class Poly1305KeyGen
    extends BaseKeyGenerator {
        public Poly1305KeyGen() {
            super("Poly1305-Serpent", 256, new Poly1305KeyGenerator());
        }
    }

    public static class SerpentGMAC
    extends BaseMac {
        public SerpentGMAC() {
            super(new GMac(new GCMBlockCipher(new SerpentEngine())));
        }
    }

}

