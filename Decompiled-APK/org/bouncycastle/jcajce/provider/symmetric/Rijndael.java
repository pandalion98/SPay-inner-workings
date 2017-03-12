package org.bouncycastle.jcajce.provider.symmetric;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class Rijndael {

    public static class AlgParams extends IvAlgorithmParameters {
        protected String engineToString() {
            return "Rijndael IV";
        }
    }

    public static class ECB extends BaseBlockCipher {

        /* renamed from: org.bouncycastle.jcajce.provider.symmetric.Rijndael.ECB.1 */
        class C07641 implements BlockCipherProvider {
            C07641() {
            }

            public BlockCipher get() {
                return new RijndaelEngine();
            }
        }

        public ECB() {
            super(new C07641());
        }
    }

    public static class KeyGen extends BaseKeyGenerator {
        public KeyGen() {
            super("Rijndael", CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256, new CipherKeyGenerator());
        }
    }

    public static class Mappings extends AlgorithmProvider {
        private static final String PREFIX;

        static {
            PREFIX = Rijndael.class.getName();
        }

        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Cipher.RIJNDAEL", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("KeyGenerator.RIJNDAEL", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("AlgorithmParameters.RIJNDAEL", PREFIX + "$AlgParams");
        }
    }

    private Rijndael() {
    }
}
