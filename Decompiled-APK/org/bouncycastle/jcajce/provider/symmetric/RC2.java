package org.bouncycastle.jcajce.provider.symmetric;

import com.mastercard.mcbp.core.mpplite.states.StatesConstants;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RC2CBCParameter;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.engines.RC2WrapEngine;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.macs.CFBBlockCipherMac;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.tls.AlertDescription;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Arrays;

public final class RC2 {

    public static class AlgParamGen extends BaseAlgorithmParameterGenerator {
        RC2ParameterSpec spec;

        public AlgParamGen() {
            this.spec = null;
        }

        protected AlgorithmParameters engineGenerateParameters() {
            if (this.spec == null) {
                byte[] bArr = new byte[8];
                if (this.random == null) {
                    this.random = new SecureRandom();
                }
                this.random.nextBytes(bArr);
                try {
                    AlgorithmParameters createParametersInstance = createParametersInstance("RC2");
                    createParametersInstance.init(new IvParameterSpec(bArr));
                    return createParametersInstance;
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            try {
                createParametersInstance = createParametersInstance("RC2");
                createParametersInstance.init(this.spec);
                return createParametersInstance;
            } catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                this.spec = (RC2ParameterSpec) algorithmParameterSpec;
                return;
            }
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC2 parameter generation.");
        }
    }

    public static class AlgParams extends BaseAlgorithmParameters {
        private static final short[] ekb;
        private static final short[] table;
        private byte[] iv;
        private int parameterVersion;

        static {
            table = new short[]{(short) 189, (short) 86, (short) 234, (short) 242, (short) 162, (short) 241, (short) 172, (short) 42, (short) 176, (short) 147, (short) 209, (short) 156, (short) 27, (short) 51, (short) 253, (short) 208, (short) 48, (short) 4, (short) 182, (short) 220, (short) 125, (short) 223, (short) 50, (short) 75, (short) 247, (short) 203, (short) 69, (short) 155, (short) 49, (short) 187, (short) 33, (short) 90, (short) 65, (short) 159, (short) 225, (short) 217, (short) 74, (short) 77, (short) 158, (short) 218, (short) 160, (short) 104, (short) 44, (short) 195, (short) 39, (short) 95, (short) 128, (short) 54, (short) 62, (short) 238, (short) 251, (short) 149, (short) 26, (short) 254, (short) 206, (short) 168, (short) 52, (short) 169, (short) 19, (short) 240, (short) 166, (short) 63, (short) 216, (short) 12, (short) 120, (short) 36, (short) 175, (short) 35, (short) 82, (short) 193, (short) 103, (short) 23, (short) 245, (short) 102, (short) 144, (short) 231, (short) 232, (short) 7, (short) 184, (short) 96, (short) 72, (short) 230, (short) 30, (short) 83, (short) 243, (short) 146, ISO7816.SELECT, AlertDescription.bad_certificate_hash_value, (short) 140, (short) 8, (short) 21, AlertDescription.unsupported_extension, (short) 134, (short) 0, MCFCITemplate.TAG_DF_NAME, (short) 250, (short) 244, (short) 127, (short) 138, (short) 66, (short) 25, (short) 246, (short) 219, (short) 205, (short) 20, (short) 141, (short) 80, (short) 18, (short) 186, (short) 60, (short) 6, (short) 78, (short) 236, (short) 179, (short) 53, (short) 17, (short) 161, (short) 136, (short) 142, (short) 43, (short) 148, (short) 153, (short) 183, AlertDescription.bad_certificate_status_response, (short) 116, (short) 211, (short) 228, (short) 191, (short) 58, (short) 222, (short) 150, (short) 14, (short) 188, (short) 10, (short) 237, (short) 119, (short) 252, (short) 55, (short) 107, (short) 3, (short) 121, (short) 137, (short) 98, (short) 198, (short) 215, (short) 192, (short) 210, (short) 124, (short) 106, (short) 139, (short) 34, (short) 163, (short) 91, (short) 5, (short) 93, (short) 2, (short) 117, (short) 213, (short) 97, (short) 227, (short) 24, (short) 143, (short) 85, (short) 81, (short) 173, (short) 31, (short) 11, (short) 94, (short) 133, (short) 229, (short) 194, (short) 87, (short) 99, (short) 202, (short) 61, (short) 108, (short) 180, (short) 197, (short) 204, AlertDescription.unrecognized_name, ISO7816.READ_RECORD, (short) 145, (short) 89, (short) 13, (short) 71, (short) 32, (short) 200, (short) 79, (short) 88, (short) 224, (short) 1, (short) 226, (short) 22, (short) 56, (short) 196, AlertDescription.certificate_unobtainable, (short) 59, (short) 15, (short) 101, (short) 70, (short) 190, (short) 126, (short) 45, (short) 123, (short) 130, (short) 249, (short) 64, (short) 181, (short) 29, AlertDescription.unknown_psk_identity, (short) 248, (short) 235, (short) 38, (short) 199, MCFCITemplate.TAG_APP_PRIORITY_INDICATOR, (short) 151, (short) 37, (short) 84, (short) 177, (short) 40, (short) 170, (short) 152, (short) 157, MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE, (short) 100, (short) 109, (short) 122, (short) 212, (short) 16, (short) 129, (short) 68, (short) 239, (short) 73, (short) 214, (short) 174, (short) 46, (short) 221, (short) 118, (short) 92, (short) 47, (short) 167, (short) 28, (short) 201, (short) 9, (short) 105, (short) 154, (short) 131, (short) 207, (short) 41, (short) 57, (short) 185, (short) 233, (short) 76, StatesConstants.LENGTH_MASK, (short) 67, (short) 171};
            ekb = new short[]{(short) 93, (short) 190, (short) 155, (short) 139, (short) 17, (short) 153, AlertDescription.unsupported_extension, (short) 77, (short) 89, (short) 243, (short) 133, (short) 166, (short) 63, (short) 183, (short) 131, (short) 197, (short) 228, AlertDescription.unknown_psk_identity, (short) 107, (short) 58, (short) 104, (short) 90, (short) 192, (short) 71, (short) 160, (short) 100, (short) 52, (short) 12, (short) 241, (short) 208, (short) 82, MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE, (short) 185, (short) 30, (short) 150, (short) 67, (short) 65, (short) 216, (short) 212, (short) 44, (short) 219, (short) 248, (short) 7, (short) 119, (short) 42, (short) 202, (short) 235, (short) 239, (short) 16, (short) 28, (short) 22, (short) 13, (short) 56, AlertDescription.bad_certificate_hash_value, (short) 47, (short) 137, (short) 193, (short) 249, (short) 128, (short) 196, (short) 109, (short) 174, (short) 48, (short) 61, (short) 206, (short) 32, (short) 99, (short) 254, (short) 230, (short) 26, (short) 199, (short) 184, (short) 80, (short) 232, (short) 36, (short) 23, (short) 252, (short) 37, AlertDescription.certificate_unobtainable, (short) 187, (short) 106, (short) 163, (short) 68, (short) 83, (short) 217, (short) 162, (short) 1, (short) 171, (short) 188, (short) 182, (short) 31, (short) 152, (short) 238, (short) 154, (short) 167, (short) 45, (short) 79, (short) 158, (short) 142, (short) 172, (short) 224, (short) 198, (short) 73, (short) 70, (short) 41, (short) 244, (short) 148, (short) 138, (short) 175, (short) 225, (short) 91, (short) 195, (short) 179, (short) 123, (short) 87, (short) 209, (short) 124, (short) 156, (short) 237, MCFCITemplate.TAG_APP_PRIORITY_INDICATOR, (short) 64, (short) 140, (short) 226, (short) 203, (short) 147, (short) 20, (short) 201, (short) 97, (short) 46, (short) 229, (short) 204, (short) 246, (short) 94, (short) 168, (short) 92, (short) 214, (short) 117, (short) 141, (short) 98, (short) 149, (short) 88, (short) 105, (short) 118, (short) 161, (short) 74, (short) 181, (short) 85, (short) 9, (short) 120, (short) 51, (short) 130, (short) 215, (short) 221, (short) 121, (short) 245, (short) 27, (short) 11, (short) 222, (short) 38, (short) 33, (short) 40, (short) 116, (short) 4, (short) 151, (short) 86, (short) 223, (short) 60, (short) 240, (short) 55, (short) 57, (short) 220, StatesConstants.LENGTH_MASK, (short) 6, ISO7816.SELECT, (short) 234, (short) 66, (short) 8, (short) 218, (short) 180, AlertDescription.bad_certificate_status_response, (short) 176, (short) 207, (short) 18, (short) 122, (short) 78, (short) 250, (short) 108, (short) 29, MCFCITemplate.TAG_DF_NAME, (short) 0, (short) 200, (short) 127, (short) 145, (short) 69, (short) 170, (short) 43, (short) 194, (short) 177, (short) 143, (short) 213, (short) 186, (short) 242, (short) 173, (short) 25, ISO7816.READ_RECORD, (short) 103, (short) 54, (short) 247, (short) 15, (short) 10, (short) 146, (short) 125, (short) 227, (short) 157, (short) 233, (short) 144, (short) 62, (short) 35, (short) 39, (short) 102, (short) 19, (short) 236, (short) 129, (short) 21, (short) 189, (short) 34, (short) 191, (short) 159, (short) 126, (short) 169, (short) 81, (short) 75, (short) 76, (short) 251, (short) 2, (short) 211, AlertDescription.unrecognized_name, (short) 134, (short) 49, (short) 231, (short) 59, (short) 5, (short) 3, (short) 84, (short) 96, (short) 72, (short) 101, (short) 24, (short) 210, (short) 205, (short) 95, (short) 50, (short) 136, (short) 14, (short) 53, (short) 253};
        }

        public AlgParams() {
            this.parameterVersion = 58;
        }

        protected byte[] engineGetEncoded() {
            return Arrays.clone(this.iv);
        }

        protected byte[] engineGetEncoded(String str) {
            return isASN1FormatString(str) ? this.parameterVersion == -1 ? new RC2CBCParameter(engineGetEncoded()).getEncoded() : new RC2CBCParameter(this.parameterVersion, engineGetEncoded()).getEncoded() : str.equals("RAW") ? engineGetEncoded() : null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
            } else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                int effectiveKeyBits = ((RC2ParameterSpec) algorithmParameterSpec).getEffectiveKeyBits();
                if (effectiveKeyBits != -1) {
                    if (effectiveKeyBits < SkeinMac.SKEIN_256) {
                        this.parameterVersion = table[effectiveKeyBits];
                    } else {
                        this.parameterVersion = effectiveKeyBits;
                    }
                }
                this.iv = ((RC2ParameterSpec) algorithmParameterSpec).getIV();
            } else {
                throw new InvalidParameterSpecException("IvParameterSpec or RC2ParameterSpec required to initialise a RC2 parameters algorithm parameters object");
            }
        }

        protected void engineInit(byte[] bArr) {
            this.iv = Arrays.clone(bArr);
        }

        protected void engineInit(byte[] bArr, String str) {
            if (isASN1FormatString(str)) {
                RC2CBCParameter instance = RC2CBCParameter.getInstance(ASN1Primitive.fromByteArray(bArr));
                if (instance.getRC2ParameterVersion() != null) {
                    this.parameterVersion = instance.getRC2ParameterVersion().intValue();
                }
                this.iv = instance.getIV();
            } else if (str.equals("RAW")) {
                engineInit(bArr);
            } else {
                throw new IOException("Unknown parameters format in IV parameters object");
            }
        }

        protected String engineToString() {
            return "RC2 Parameters";
        }

        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class cls) {
            if (cls == RC2ParameterSpec.class && this.parameterVersion != -1) {
                return this.parameterVersion < SkeinMac.SKEIN_256 ? new RC2ParameterSpec(ekb[this.parameterVersion], this.iv) : new RC2ParameterSpec(this.parameterVersion, this.iv);
            } else {
                if (cls == IvParameterSpec.class) {
                    return new IvParameterSpec(this.iv);
                }
                throw new InvalidParameterSpecException("unknown parameter spec passed to RC2 parameters object.");
            }
        }
    }

    public static class CBC extends BaseBlockCipher {
        public CBC() {
            super(new CBCBlockCipher(new RC2Engine()), 64);
        }
    }

    public static class CBCMAC extends BaseMac {
        public CBCMAC() {
            super(new CBCBlockCipherMac(new RC2Engine()));
        }
    }

    public static class CFB8MAC extends BaseMac {
        public CFB8MAC() {
            super(new CFBBlockCipherMac(new RC2Engine()));
        }
    }

    public static class ECB extends BaseBlockCipher {
        public ECB() {
            super(new RC2Engine());
        }
    }

    public static class KeyGenerator extends BaseKeyGenerator {
        public KeyGenerator() {
            super("RC2", X509KeyUsage.digitalSignature, new CipherKeyGenerator());
        }
    }

    public static class Mappings extends AlgorithmProvider {
        private static final String PREFIX;

        static {
            PREFIX = RC2.class.getName();
        }

        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.RC2", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.1.2.840.113549.3.2", PREFIX + "$AlgParamGen");
            configurableProvider.addAlgorithm("KeyGenerator.RC2", PREFIX + "$KeyGenerator");
            configurableProvider.addAlgorithm("KeyGenerator.1.2.840.113549.3.2", PREFIX + "$KeyGenerator");
            configurableProvider.addAlgorithm("AlgorithmParameters.RC2", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.1.2.840.113549.3.2", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Cipher.RC2", PREFIX + "$ECB");
            configurableProvider.addAlgorithm("Cipher.RC2WRAP", PREFIX + "$Wrap");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + PKCSObjectIdentifiers.id_alg_CMSRC2wrap, "RC2WRAP");
            configurableProvider.addAlgorithm("Cipher.1.2.840.113549.3.2", PREFIX + "$CBC");
            configurableProvider.addAlgorithm("Mac.RC2MAC", PREFIX + "$CBCMAC");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC2", "RC2MAC");
            configurableProvider.addAlgorithm("Mac.RC2MAC/CFB8", PREFIX + "$CFB8MAC");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC2/CFB8", "RC2MAC/CFB8");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD2ANDRC2-CBC", "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD5ANDRC2-CBC", "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1ANDRC2-CBC", "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.5", "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.6", "PBEWITHSHAAND40BITRC2-CBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD2ANDRC2", PREFIX + "$PBEWithMD2KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5ANDRC2", PREFIX + "$PBEWithMD5KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA1ANDRC2", PREFIX + "$PBEWithSHA1KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND128BITRC2-CBC", PREFIX + "$PBEWithSHAAnd128BitKeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND40BITRC2-CBC", PREFIX + "$PBEWithSHAAnd40BitKeyFactory");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher." + PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.5", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.6", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWithSHAAnd3KeyTripleDES", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.1.2.840.113549.1.12.1.5", "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.1.2.840.113549.1.12.1.6", "PBEWITHSHAAND40BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND128BITRC2-CBC", "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND40BITRC2-CBC", "PBEWITHSHAAND40BITRC2-CBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA1ANDRC2", PREFIX + "$PBEWithSHA1AndRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND128BITRC2-CBC", PREFIX + "$PBEWithSHAAnd128BitRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND40BITRC2-CBC", PREFIX + "$PBEWithSHAAnd40BitRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5ANDRC2", PREFIX + "$PBEWithMD5AndRC2");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1ANDRC2", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDRC2", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1ANDRC2-CBC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND40BITRC2-CBC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND128BITRC2-CBC", "PKCS12PBE");
        }
    }

    public static class PBEWithMD2KeyFactory extends PBESecretKeyFactory {
        public PBEWithMD2KeyFactory() {
            super("PBEwithMD2andRC2", PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, true, 0, 5, 64, 64);
        }
    }

    public static class PBEWithMD5AndRC2 extends BaseBlockCipher {
        public PBEWithMD5AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }

    public static class PBEWithMD5KeyFactory extends PBESecretKeyFactory {
        public PBEWithMD5KeyFactory() {
            super("PBEwithMD5andRC2", PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, true, 0, 0, 64, 64);
        }
    }

    public static class PBEWithSHA1AndRC2 extends BaseBlockCipher {
        public PBEWithSHA1AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }

    public static class PBEWithSHA1KeyFactory extends PBESecretKeyFactory {
        public PBEWithSHA1KeyFactory() {
            super("PBEwithSHA1andRC2", PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, true, 0, 1, 64, 64);
        }
    }

    public static class PBEWithSHAAnd128BitKeyFactory extends PBESecretKeyFactory {
        public PBEWithSHAAnd128BitKeyFactory() {
            super("PBEwithSHAand128BitRC2-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, true, 2, 1, X509KeyUsage.digitalSignature, 64);
        }
    }

    public static class PBEWithSHAAnd128BitRC2 extends BaseBlockCipher {
        public PBEWithSHAAnd128BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }

    public static class PBEWithSHAAnd40BitKeyFactory extends PBESecretKeyFactory {
        public PBEWithSHAAnd40BitKeyFactory() {
            super("PBEwithSHAand40BitRC2-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, true, 2, 1, 40, 64);
        }
    }

    public static class PBEWithSHAAnd40BitRC2 extends BaseBlockCipher {
        public PBEWithSHAAnd40BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }

    public static class Wrap extends BaseWrapCipher {
        public Wrap() {
            super(new RC2WrapEngine());
        }
    }

    private RC2() {
    }
}
