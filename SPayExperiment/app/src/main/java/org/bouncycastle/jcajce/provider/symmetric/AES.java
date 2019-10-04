/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.Method
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.IvParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.bc.BCObjectIdentifiers;
import org.bouncycastle.asn1.cms.CCMParameters;
import org.bouncycastle.asn1.cms.GCMParameters;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle.crypto.engines.RFC5649WrapEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.macs.GMac;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.SymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;
import org.bouncycastle.util.Integers;

public final class AES {
    private static final Class gcmSpecClass = AES.lookup("javax.crypto.spec.GCMParameterSpec");

    private AES() {
    }

    private static Class lookup(String string) {
        try {
            Class class_ = AES.class.getClassLoader().loadClass(string);
            return class_;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static class AESCMAC
    extends BaseMac {
        public AESCMAC() {
            super(new CMac(new AESFastEngine()));
        }
    }

    public static class AESGMAC
    extends BaseMac {
        public AESGMAC() {
            super(new GMac(new GCMBlockCipher(new AESFastEngine())));
        }
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
                AlgorithmParameters algorithmParameters = this.createParametersInstance("AES");
                algorithmParameters.init((AlgorithmParameterSpec)new IvParameterSpec(arrby));
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }

    public static class AlgParamGenCCM
    extends BaseAlgorithmParameterGenerator {
        protected AlgorithmParameters engineGenerateParameters() {
            byte[] arrby = new byte[12];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(arrby);
            try {
                AlgorithmParameters algorithmParameters = this.createParametersInstance("CCM");
                algorithmParameters.init(new CCMParameters(arrby, 12).getEncoded());
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }

    public static class AlgParamGenGCM
    extends BaseAlgorithmParameterGenerator {
        protected AlgorithmParameters engineGenerateParameters() {
            byte[] arrby = new byte[12];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(arrby);
            try {
                AlgorithmParameters algorithmParameters = this.createParametersInstance("GCM");
                algorithmParameters.init(new GCMParameters(arrby, 12).getEncoded());
                return algorithmParameters;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }

    public static class AlgParams
    extends IvAlgorithmParameters {
        @Override
        protected String engineToString() {
            return "AES IV";
        }
    }

    public static class AlgParamsCCM
    extends BaseAlgorithmParameters {
        private CCMParameters ccmParams;

        protected byte[] engineGetEncoded() {
            return this.ccmParams.getEncoded();
        }

        protected byte[] engineGetEncoded(String string) {
            if (!this.isASN1FormatString(string)) {
                throw new IOException("unknown format specified");
            }
            return this.ccmParams.getEncoded();
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            throw new InvalidParameterSpecException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }

        protected void engineInit(byte[] arrby) {
            this.ccmParams = CCMParameters.getInstance(arrby);
        }

        protected void engineInit(byte[] arrby, String string) {
            if (!this.isASN1FormatString(string)) {
                throw new IOException("unknown format specified");
            }
            this.ccmParams = CCMParameters.getInstance(arrby);
        }

        protected String engineToString() {
            return "CCM";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (gcmSpecClass != null) {
                try {
                    Class class_2 = gcmSpecClass;
                    Class[] arrclass = new Class[]{Integer.TYPE, byte[].class};
                    Constructor constructor = class_2.getConstructor(arrclass);
                    Object[] arrobject = new Object[]{Integers.valueOf(8 * this.ccmParams.getIcvLen()), this.ccmParams.getNonce()};
                    AlgorithmParameterSpec algorithmParameterSpec = (AlgorithmParameterSpec)constructor.newInstance(arrobject);
                    return algorithmParameterSpec;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    throw new InvalidParameterSpecException("no constructor found!");
                }
                catch (Exception exception) {
                    throw new InvalidParameterSpecException("construction failed: " + exception.getMessage());
                }
            }
            throw new InvalidParameterSpecException("unknown parameter spec: " + class_.getName());
        }
    }

    public static class AlgParamsGCM
    extends BaseAlgorithmParameters {
        private GCMParameters gcmParams;

        protected byte[] engineGetEncoded() {
            return this.gcmParams.getEncoded();
        }

        protected byte[] engineGetEncoded(String string) {
            if (!this.isASN1FormatString(string)) {
                throw new IOException("unknown format specified");
            }
            return this.gcmParams.getEncoded();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (gcmSpecClass == null) return;
            try {
                Method method = gcmSpecClass.getDeclaredMethod("getTLen", new Class[0]);
                this.gcmParams = new GCMParameters((byte[])gcmSpecClass.getDeclaredMethod("getIV", new Class[0]).invoke((Object)algorithmParameterSpec, new Object[0]), (Integer)method.invoke((Object)algorithmParameterSpec, new Object[0]) / 8);
                return;
            }
            catch (Exception exception) {
                throw new InvalidParameterSpecException("Cannot process GCMParameterSpec.");
            }
        }

        protected void engineInit(byte[] arrby) {
            this.gcmParams = GCMParameters.getInstance(arrby);
        }

        protected void engineInit(byte[] arrby, String string) {
            if (!this.isASN1FormatString(string)) {
                throw new IOException("unknown format specified");
            }
            this.gcmParams = GCMParameters.getInstance(arrby);
        }

        protected String engineToString() {
            return "GCM";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (gcmSpecClass != null) {
                try {
                    Class class_2 = gcmSpecClass;
                    Class[] arrclass = new Class[]{Integer.TYPE, byte[].class};
                    Constructor constructor = class_2.getConstructor(arrclass);
                    Object[] arrobject = new Object[]{Integers.valueOf(8 * this.gcmParams.getIcvLen()), this.gcmParams.getNonce()};
                    AlgorithmParameterSpec algorithmParameterSpec = (AlgorithmParameterSpec)constructor.newInstance(arrobject);
                    return algorithmParameterSpec;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    throw new InvalidParameterSpecException("no constructor found!");
                }
                catch (Exception exception) {
                    throw new InvalidParameterSpecException("construction failed: " + exception.getMessage());
                }
            }
            throw new InvalidParameterSpecException("unknown parameter spec: " + class_.getName());
        }
    }

    public static class CBC
    extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new AESFastEngine()), 128);
        }
    }

    public static class CCM
    extends BaseBlockCipher {
        public CCM() {
            super(new CCMBlockCipher(new AESFastEngine()));
        }
    }

    public static class CFB
    extends BaseBlockCipher {
        public CFB() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 128)), 128);
        }
    }

    public static class ECB
    extends BaseBlockCipher {
        public ECB() {
            super(new BlockCipherProvider(){

                @Override
                public BlockCipher get() {
                    return new AESFastEngine();
                }
            });
        }

    }

    public static class GCM
    extends BaseBlockCipher {
        public GCM() {
            super(new GCMBlockCipher(new AESFastEngine()));
        }
    }

    public static class KeyGen
    extends BaseKeyGenerator {
        public KeyGen() {
            this(192);
        }

        public KeyGen(int n) {
            super("AES", n, new CipherKeyGenerator());
        }
    }

    public static class KeyGen128
    extends KeyGen {
        public KeyGen128() {
            super(128);
        }
    }

    public static class KeyGen192
    extends KeyGen {
        public KeyGen192() {
            super(192);
        }
    }

    public static class KeyGen256
    extends KeyGen {
        public KeyGen256() {
            super(256);
        }
    }

    public static class Mappings
    extends SymmetricAlgorithmProvider {
        private static final String PREFIX = AES.class.getName();
        private static final String wrongAES128 = "2.16.840.1.101.3.4.2";
        private static final String wrongAES192 = "2.16.840.1.101.3.4.22";
        private static final String wrongAES256 = "2.16.840.1.101.3.4.42";

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.AES", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.42", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes128_CBC, "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes192_CBC, "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes256_CBC, "AES");
            configurableProvider.addAlgorithm("AlgorithmParameters.GCM", PREFIX + "$AlgParamsGCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes128_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes192_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes256_GCM, "GCM");
            configurableProvider.addAlgorithm("AlgorithmParameters.CCM", PREFIX + "$AlgParamsCCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes128_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes192_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + NISTObjectIdentifiers.id_aes256_CCM, "CCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.AES", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.42", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes128_CBC, "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes192_CBC, "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes256_CBC, "AES");
            configurableProvider.addAlgorithm("Cipher.AES", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.42", "AES");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes128_ECB, PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes192_ECB, PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes256_ECB, PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes128_CBC, PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes192_CBC, PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes256_CBC, PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes128_OFB, PREFIX + "$OFB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes192_OFB, PREFIX + "$OFB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes256_OFB, PREFIX + "$OFB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes128_CFB, PREFIX + "$CFB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes192_CFB, PREFIX + "$CFB");
            configurableProvider.addAlgorithm("Cipher." + NISTObjectIdentifiers.id_aes256_CFB, PREFIX + "$CFB");
            configurableProvider.addAlgorithm("Cipher.AESWRAP", PREFIX + "$Wrap");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes128_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes192_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes256_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Cipher.AESRFC3211WRAP", PREFIX + "$RFC3211Wrap");
            configurableProvider.addAlgorithm("Cipher.AESRFC5649WRAP", PREFIX + "$RFC5649Wrap");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.CCM", PREFIX + "$AlgParamGenCCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes128_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes192_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes256_CCM, "CCM");
            configurableProvider.addAlgorithm("Cipher.CCM", PREFIX + "$CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes128_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes192_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes256_CCM, "CCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.GCM", PREFIX + "$AlgParamGenGCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes128_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes192_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator." + NISTObjectIdentifiers.id_aes256_GCM, "GCM");
            configurableProvider.addAlgorithm("Cipher.GCM", PREFIX + "$GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes128_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes192_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + NISTObjectIdentifiers.id_aes256_GCM, "GCM");
            configurableProvider.addAlgorithm("KeyGenerator.AES", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.2", PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.22", PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.42", PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_ECB, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_CBC, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_OFB, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_CFB, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_ECB, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_CBC, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_OFB, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_CFB, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_ECB, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_CBC, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_OFB, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_CFB, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator.AESWRAP", PREFIX + "$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_wrap, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_wrap, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_wrap, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_GCM, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_GCM, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_GCM, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes128_CCM, PREFIX + "$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes192_CCM, PREFIX + "$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator." + NISTObjectIdentifiers.id_aes256_CCM, PREFIX + "$KeyGen256");
            configurableProvider.addAlgorithm("Mac.AESCMAC", PREFIX + "$AESCMAC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc.getId(), "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc.getId(), "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc.getId(), "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc.getId(), "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc.getId(), "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc.getId(), "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND128BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND192BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND256BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND128BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND192BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND256BITAES-CBC-BC", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND128BITAES-CBC-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND192BITAES-CBC-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND256BITAES-CBC-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND128BITAES-CBC-OPENSSL", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND192BITAES-CBC-OPENSSL", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND256BITAES-CBC-OPENSSL", PREFIX + "$PBEWithAESCBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND128BITAES-CBC-OPENSSL", PREFIX + "$PBEWithMD5And128BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND192BITAES-CBC-OPENSSL", PREFIX + "$PBEWithMD5And192BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND256BITAES-CBC-OPENSSL", PREFIX + "$PBEWithMD5And256BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND128BITAES-CBC-BC", PREFIX + "$PBEWithSHAAnd128BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND192BITAES-CBC-BC", PREFIX + "$PBEWithSHAAnd192BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND256BITAES-CBC-BC", PREFIX + "$PBEWithSHAAnd256BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND128BITAES-CBC-BC", PREFIX + "$PBEWithSHA256And128BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND192BITAES-CBC-BC", PREFIX + "$PBEWithSHA256And192BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND256BITAES-CBC-BC", PREFIX + "$PBEWithSHA256And256BitAESBC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND128BITAES-CBC-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND192BITAES-CBC-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND256BITAES-CBC-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc.getId(), "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc.getId(), "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc.getId(), "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc.getId(), "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc.getId(), "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc.getId(), "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc.getId(), "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc.getId(), "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc.getId(), "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc.getId(), "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc.getId(), "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc.getId(), "PKCS12PBE");
            this.addGMacAlgorithm(configurableProvider, "AES", PREFIX + "$AESGMAC", PREFIX + "$KeyGen128");
            this.addPoly1305Algorithm(configurableProvider, "AES", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
        }
    }

    public static class OFB
    extends BaseBlockCipher {
        public OFB() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new AESFastEngine(), 128)), 128);
        }
    }

    public static class PBEWithAESCBC
    extends BaseBlockCipher {
        public PBEWithAESCBC() {
            super(new CBCBlockCipher(new AESFastEngine()));
        }
    }

    public static class PBEWithMD5And128BitAESCBCOpenSSL
    extends PBESecretKeyFactory {
        public PBEWithMD5And128BitAESCBCOpenSSL() {
            super("PBEWithMD5And128BitAES-CBC-OpenSSL", null, true, 3, 0, 128, 128);
        }
    }

    public static class PBEWithMD5And192BitAESCBCOpenSSL
    extends PBESecretKeyFactory {
        public PBEWithMD5And192BitAESCBCOpenSSL() {
            super("PBEWithMD5And192BitAES-CBC-OpenSSL", null, true, 3, 0, 192, 128);
        }
    }

    public static class PBEWithMD5And256BitAESCBCOpenSSL
    extends PBESecretKeyFactory {
        public PBEWithMD5And256BitAESCBCOpenSSL() {
            super("PBEWithMD5And256BitAES-CBC-OpenSSL", null, true, 3, 0, 256, 128);
        }
    }

    public static class PBEWithSHA256And128BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHA256And128BitAESBC() {
            super("PBEWithSHA256And128BitAES-CBC-BC", null, true, 2, 4, 128, 128);
        }
    }

    public static class PBEWithSHA256And192BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHA256And192BitAESBC() {
            super("PBEWithSHA256And192BitAES-CBC-BC", null, true, 2, 4, 192, 128);
        }
    }

    public static class PBEWithSHA256And256BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHA256And256BitAESBC() {
            super("PBEWithSHA256And256BitAES-CBC-BC", null, true, 2, 4, 256, 128);
        }
    }

    public static class PBEWithSHAAnd128BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHAAnd128BitAESBC() {
            super("PBEWithSHA1And128BitAES-CBC-BC", null, true, 2, 1, 128, 128);
        }
    }

    public static class PBEWithSHAAnd192BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHAAnd192BitAESBC() {
            super("PBEWithSHA1And192BitAES-CBC-BC", null, true, 2, 1, 192, 128);
        }
    }

    public static class PBEWithSHAAnd256BitAESBC
    extends PBESecretKeyFactory {
        public PBEWithSHAAnd256BitAESBC() {
            super("PBEWithSHA1And256BitAES-CBC-BC", null, true, 2, 1, 256, 128);
        }
    }

    public static class Poly1305
    extends BaseMac {
        public Poly1305() {
            super(new org.bouncycastle.crypto.macs.Poly1305(new AESFastEngine()));
        }
    }

    public static class Poly1305KeyGen
    extends BaseKeyGenerator {
        public Poly1305KeyGen() {
            super("Poly1305-AES", 256, new Poly1305KeyGenerator());
        }
    }

    public static class RFC3211Wrap
    extends BaseWrapCipher {
        public RFC3211Wrap() {
            super(new RFC3211WrapEngine(new AESFastEngine()), 16);
        }
    }

    public static class RFC5649Wrap
    extends BaseWrapCipher {
        public RFC5649Wrap() {
            super(new RFC5649WrapEngine(new AESFastEngine()));
        }
    }

    public static class Wrap
    extends BaseWrapCipher {
        public Wrap() {
            super(new AESWrapEngine());
        }
    }

}

