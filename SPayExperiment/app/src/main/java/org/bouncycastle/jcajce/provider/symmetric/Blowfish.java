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
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class Blowfish {
    private Blowfish() {
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "Blowfish IV";
        }
    }

    public static class CBC
    extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new BlowfishEngine()), 64);
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new BlowfishEngine());
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("Blowfish", 128, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = Blowfish.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Cipher.BLOWFISH", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher.1.3.6.1.4.1.3029.1.2", PREFIX + "$CBC");
            configurableProvider.addAlgorithm("KeyGenerator.BLOWFISH", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.1.3.6.1.4.1.3029.1.2", "BLOWFISH");
            configurableProvider.addAlgorithm("AlgorithmParameters.BLOWFISH", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.1.3.6.1.4.1.3029.1.2", "BLOWFISH");
        }
    }

}

