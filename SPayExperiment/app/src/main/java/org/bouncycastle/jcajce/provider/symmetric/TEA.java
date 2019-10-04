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
import org.bouncycastle.crypto.engines.TEAEngine;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class TEA {
    private TEA() {
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "TEA IV";
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new TEAEngine());
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("TEA", 128, new CipherKeyGenerator());
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = TEA.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Cipher.TEA", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("KeyGenerator.TEA", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("AlgorithmParameters.TEA", PREFIX + "$AlgParams");
        }
    }

}

