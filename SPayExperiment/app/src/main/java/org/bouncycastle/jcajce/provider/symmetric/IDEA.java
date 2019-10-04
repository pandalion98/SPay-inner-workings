/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.IvParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.misc.IDEACBCPar;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.IDEAEngine;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class IDEA {
    private IDEA() {
    }

    public static class AlgParamGen
    extends BaseAlgorithmParameterGenerator {
        protected AlgorithmParameters engineGenerateParameters() {
            byte[] arrby = new byte[8];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(arrby);
            try {
                AlgorithmParameters algorithmParameters = this.createParametersInstance("IDEA");
                algorithmParameters.init((AlgorithmParameterSpec)new IvParameterSpec(arrby));
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for IDEA parameter generation.");
        }
    }

    public static class AlgParams
    extends BaseAlgorithmParameters {
        private byte[] iv;

        protected byte[] engineGetEncoded() {
            return this.engineGetEncoded("ASN.1");
        }

        protected byte[] engineGetEncoded(String string) {
            if (this.isASN1FormatString(string)) {
                return new IDEACBCPar(this.engineGetEncoded("RAW")).getEncoded();
            }
            if (string.equals((Object)"RAW")) {
                byte[] arrby = new byte[this.iv.length];
                System.arraycopy((Object)this.iv, (int)0, (Object)arrby, (int)0, (int)this.iv.length);
                return arrby;
            }
            return null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
                throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
            }
            this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
        }

        protected void engineInit(byte[] arrby) {
            this.iv = new byte[arrby.length];
            System.arraycopy((Object)arrby, (int)0, (Object)this.iv, (int)0, (int)this.iv.length);
        }

        protected void engineInit(byte[] arrby, String string) {
            if (string.equals((Object)"RAW")) {
                this.engineInit(arrby);
                return;
            }
            if (string.equals((Object)"ASN.1")) {
                this.engineInit(new IDEACBCPar((ASN1Sequence)new ASN1InputStream(arrby).readObject()).getIV());
                return;
            }
            throw new IOException("Unknown parameters format in IV parameters object");
        }

        protected String engineToString() {
            return "IDEA Parameters";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (class_ == IvParameterSpec.class) {
                return new IvParameterSpec(this.iv);
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
        }
    }

    public static class CBC
    extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new IDEAEngine()), 64);
        }
    }

    public static class CFB8Mac
    extends BaseMac {
        public CFB8Mac() {
            super(new CFBBlockCipherMac(new IDEAEngine()));
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new IDEAEngine());
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            super("IDEA", 128, new CipherKeyGenerator());
        }
    }

    public static class Mac
    extends BaseMac {
        public Mac() {
            super(new CBCBlockCipherMac(new IDEAEngine()));
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = IDEA.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.IDEA", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameters.IDEA", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA-CBC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Cipher.IDEA", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAANDIDEA-CBC", PREFIX + "$PBEWithSHAAndIDEA");
            configurableProvider.addAlgorithm("KeyGenerator.IDEA", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.1.3.6.1.4.1.188.7.1.1.2", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAANDIDEA-CBC", PREFIX + "$PBEWithSHAAndIDEAKeyGen");
            configurableProvider.addAlgorithm("Mac.IDEAMAC", PREFIX + "$Mac");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.IDEA", "IDEAMAC");
            configurableProvider.addAlgorithm("Mac.IDEAMAC/CFB8", PREFIX + "$CFB8Mac");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.IDEA/CFB8", "IDEAMAC/CFB8");
        }
    }

    public static class PBEWithSHAAndIDEA
    extends BaseBlockCipher {
        public PBEWithSHAAndIDEA() {
            super(new CBCBlockCipher(new IDEAEngine()));
        }
    }

    public static class PBEWithSHAAndIDEAKeyGen
    extends PBESecretKeyFactory {
        public PBEWithSHAAndIDEAKeyGen() {
            super("PBEwithSHAandIDEA-CBC", null, true, 2, 1, 128, 64);
        }
    }

}

