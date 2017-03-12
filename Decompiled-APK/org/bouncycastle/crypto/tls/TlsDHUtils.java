package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsDHUtils {
    public static final Integer EXT_negotiated_ff_dhe_groups;
    static final BigInteger TWO;
    static final DHParameters draft_ffdhe2432;
    private static final String draft_ffdhe2432_p = "FFFFFFFFFFFFFFFFADF85458A2BB4A9AAFDC5620273D3CF1D8B9C583CE2D3695A9E13641146433FBCC939DCE249B3EF97D2FE363630C75D8F681B202AEC4617AD3DF1ED5D5FD65612433F51F5F066ED0856365553DED1AF3B557135E7F57C935984F0C70E0E68B77E2A689DAF3EFE8721DF158A136ADE73530ACCA4F483A797ABC0AB182B324FB61D108A94BB2C8E3FBB96ADAB760D7F4681D4F42A3DE394DF4AE56EDE76372BB190B07A7C8EE0A6D709E02FCE1CDF7E2ECC03404CD28342F619172FE9CE98583FF8E4F1232EEF28183C3FE3B1B4C6FAD733BB5FCBC2EC22005C58EF1837D1683B2C6F34A26C1B2EFFA886B4238611FCFDCDE355B3B6519035BBC34F4DEF99C023861B46FC9D6E6C9077AD91D2691F7F7EE598CB0FAC186D91CAEFE13098533C8B3FFFFFFFFFFFFFFFF";
    static final DHParameters draft_ffdhe3072;
    private static final String draft_ffdhe3072_p = "FFFFFFFFFFFFFFFFADF85458A2BB4A9AAFDC5620273D3CF1D8B9C583CE2D3695A9E13641146433FBCC939DCE249B3EF97D2FE363630C75D8F681B202AEC4617AD3DF1ED5D5FD65612433F51F5F066ED0856365553DED1AF3B557135E7F57C935984F0C70E0E68B77E2A689DAF3EFE8721DF158A136ADE73530ACCA4F483A797ABC0AB182B324FB61D108A94BB2C8E3FBB96ADAB760D7F4681D4F42A3DE394DF4AE56EDE76372BB190B07A7C8EE0A6D709E02FCE1CDF7E2ECC03404CD28342F619172FE9CE98583FF8E4F1232EEF28183C3FE3B1B4C6FAD733BB5FCBC2EC22005C58EF1837D1683B2C6F34A26C1B2EFFA886B4238611FCFDCDE355B3B6519035BBC34F4DEF99C023861B46FC9D6E6C9077AD91D2691F7F7EE598CB0FAC186D91CAEFE130985139270B4130C93BC437944F4FD4452E2D74DD364F2E21E71F54BFF5CAE82AB9C9DF69EE86D2BC522363A0DABC521979B0DEADA1DBF9A42D5C4484E0ABCD06BFA53DDEF3C1B20EE3FD59D7C25E41D2B66C62E37FFFFFFFFFFFFFFFF";
    static final DHParameters draft_ffdhe4096;
    private static final String draft_ffdhe4096_p = "FFFFFFFFFFFFFFFFADF85458A2BB4A9AAFDC5620273D3CF1D8B9C583CE2D3695A9E13641146433FBCC939DCE249B3EF97D2FE363630C75D8F681B202AEC4617AD3DF1ED5D5FD65612433F51F5F066ED0856365553DED1AF3B557135E7F57C935984F0C70E0E68B77E2A689DAF3EFE8721DF158A136ADE73530ACCA4F483A797ABC0AB182B324FB61D108A94BB2C8E3FBB96ADAB760D7F4681D4F42A3DE394DF4AE56EDE76372BB190B07A7C8EE0A6D709E02FCE1CDF7E2ECC03404CD28342F619172FE9CE98583FF8E4F1232EEF28183C3FE3B1B4C6FAD733BB5FCBC2EC22005C58EF1837D1683B2C6F34A26C1B2EFFA886B4238611FCFDCDE355B3B6519035BBC34F4DEF99C023861B46FC9D6E6C9077AD91D2691F7F7EE598CB0FAC186D91CAEFE130985139270B4130C93BC437944F4FD4452E2D74DD364F2E21E71F54BFF5CAE82AB9C9DF69EE86D2BC522363A0DABC521979B0DEADA1DBF9A42D5C4484E0ABCD06BFA53DDEF3C1B20EE3FD59D7C25E41D2B669E1EF16E6F52C3164DF4FB7930E9E4E58857B6AC7D5F42D69F6D187763CF1D5503400487F55BA57E31CC7A7135C886EFB4318AED6A1E012D9E6832A907600A918130C46DC778F971AD0038092999A333CB8B7A1A1DB93D7140003C2A4ECEA9F98D0ACC0A8291CDCEC97DCF8EC9B55A7F88A46B4DB5A851F44182E1C68A007E5E655F6AFFFFFFFFFFFFFFFF";
    static final DHParameters draft_ffdhe6144;
    private static final String draft_ffdhe6144_p = "FFFFFFFFFFFFFFFFADF85458A2BB4A9AAFDC5620273D3CF1D8B9C583CE2D3695A9E13641146433FBCC939DCE249B3EF97D2FE363630C75D8F681B202AEC4617AD3DF1ED5D5FD65612433F51F5F066ED0856365553DED1AF3B557135E7F57C935984F0C70E0E68B77E2A689DAF3EFE8721DF158A136ADE73530ACCA4F483A797ABC0AB182B324FB61D108A94BB2C8E3FBB96ADAB760D7F4681D4F42A3DE394DF4AE56EDE76372BB190B07A7C8EE0A6D709E02FCE1CDF7E2ECC03404CD28342F619172FE9CE98583FF8E4F1232EEF28183C3FE3B1B4C6FAD733BB5FCBC2EC22005C58EF1837D1683B2C6F34A26C1B2EFFA886B4238611FCFDCDE355B3B6519035BBC34F4DEF99C023861B46FC9D6E6C9077AD91D2691F7F7EE598CB0FAC186D91CAEFE130985139270B4130C93BC437944F4FD4452E2D74DD364F2E21E71F54BFF5CAE82AB9C9DF69EE86D2BC522363A0DABC521979B0DEADA1DBF9A42D5C4484E0ABCD06BFA53DDEF3C1B20EE3FD59D7C25E41D2B669E1EF16E6F52C3164DF4FB7930E9E4E58857B6AC7D5F42D69F6D187763CF1D5503400487F55BA57E31CC7A7135C886EFB4318AED6A1E012D9E6832A907600A918130C46DC778F971AD0038092999A333CB8B7A1A1DB93D7140003C2A4ECEA9F98D0ACC0A8291CDCEC97DCF8EC9B55A7F88A46B4DB5A851F44182E1C68A007E5E0DD9020BFD64B645036C7A4E677D2C38532A3A23BA4442CAF53EA63BB454329B7624C8917BDD64B1C0FD4CB38E8C334C701C3ACDAD0657FCCFEC719B1F5C3E4E46041F388147FB4CFDB477A52471F7A9A96910B855322EDB6340D8A00EF092350511E30ABEC1FFF9E3A26E7FB29F8C183023C3587E38DA0077D9B4763E4E4B94B2BBC194C6651E77CAF992EEAAC0232A281BF6B3A739C1226116820AE8DB5847A67CBEF9C9091B462D538CD72B03746AE77F5E62292C311562A846505DC82DB854338AE49F5235C95B91178CCF2DD5CACEF403EC9D1810C6272B045B3B71F9DC6B80D63FDD4A8E9ADB1E6962A69526D43161C1A41D570D7938DAD4A40E329CD0E40E65FFFFFFFFFFFFFFFF";
    static final DHParameters draft_ffdhe8192;
    private static final String draft_ffdhe8192_p = "FFFFFFFFFFFFFFFFADF85458A2BB4A9AAFDC5620273D3CF1D8B9C583CE2D3695A9E13641146433FBCC939DCE249B3EF97D2FE363630C75D8F681B202AEC4617AD3DF1ED5D5FD65612433F51F5F066ED0856365553DED1AF3B557135E7F57C935984F0C70E0E68B77E2A689DAF3EFE8721DF158A136ADE73530ACCA4F483A797ABC0AB182B324FB61D108A94BB2C8E3FBB96ADAB760D7F4681D4F42A3DE394DF4AE56EDE76372BB190B07A7C8EE0A6D709E02FCE1CDF7E2ECC03404CD28342F619172FE9CE98583FF8E4F1232EEF28183C3FE3B1B4C6FAD733BB5FCBC2EC22005C58EF1837D1683B2C6F34A26C1B2EFFA886B4238611FCFDCDE355B3B6519035BBC34F4DEF99C023861B46FC9D6E6C9077AD91D2691F7F7EE598CB0FAC186D91CAEFE130985139270B4130C93BC437944F4FD4452E2D74DD364F2E21E71F54BFF5CAE82AB9C9DF69EE86D2BC522363A0DABC521979B0DEADA1DBF9A42D5C4484E0ABCD06BFA53DDEF3C1B20EE3FD59D7C25E41D2B669E1EF16E6F52C3164DF4FB7930E9E4E58857B6AC7D5F42D69F6D187763CF1D5503400487F55BA57E31CC7A7135C886EFB4318AED6A1E012D9E6832A907600A918130C46DC778F971AD0038092999A333CB8B7A1A1DB93D7140003C2A4ECEA9F98D0ACC0A8291CDCEC97DCF8EC9B55A7F88A46B4DB5A851F44182E1C68A007E5E0DD9020BFD64B645036C7A4E677D2C38532A3A23BA4442CAF53EA63BB454329B7624C8917BDD64B1C0FD4CB38E8C334C701C3ACDAD0657FCCFEC719B1F5C3E4E46041F388147FB4CFDB477A52471F7A9A96910B855322EDB6340D8A00EF092350511E30ABEC1FFF9E3A26E7FB29F8C183023C3587E38DA0077D9B4763E4E4B94B2BBC194C6651E77CAF992EEAAC0232A281BF6B3A739C1226116820AE8DB5847A67CBEF9C9091B462D538CD72B03746AE77F5E62292C311562A846505DC82DB854338AE49F5235C95B91178CCF2DD5CACEF403EC9D1810C6272B045B3B71F9DC6B80D63FDD4A8E9ADB1E6962A69526D43161C1A41D570D7938DAD4A40E329CCFF46AAA36AD004CF600C8381E425A31D951AE64FDB23FCEC9509D43687FEB69EDD1CC5E0B8CC3BDF64B10EF86B63142A3AB8829555B2F747C932665CB2C0F1CC01BD70229388839D2AF05E454504AC78B7582822846C0BA35C35F5C59160CC046FD8251541FC68C9C86B022BB7099876A460E7451A8A93109703FEE1C217E6C3826E52C51AA691E0E423CFC99E9E31650C1217B624816CDAD9A95F9D5B8019488D9C0A0A1FE3075A577E23183F81D4A3F2FA4571EFC8CE0BA8A4FE8B6855DFE72B0A66EDED2FBABFBE58A30FAFABE1C5D71A87E2F741EF8C1FE86FEA6BBFDE530677F0D97D11D49F7A8443D0822E506A9F4614E011E2A94838FF88CD68C8BB7C5C6424CFFFFFFFFFFFFFFFF";

    static {
        TWO = BigInteger.valueOf(2);
        EXT_negotiated_ff_dhe_groups = Integers.valueOf(ExtensionType.negotiated_ff_dhe_groups);
        draft_ffdhe2432 = fromSafeP(draft_ffdhe2432_p);
        draft_ffdhe3072 = fromSafeP(draft_ffdhe3072_p);
        draft_ffdhe4096 = fromSafeP(draft_ffdhe4096_p);
        draft_ffdhe6144 = fromSafeP(draft_ffdhe6144_p);
        draft_ffdhe8192 = fromSafeP(draft_ffdhe8192_p);
    }

    public static void addNegotiatedDHEGroupsClientExtension(Hashtable hashtable, short[] sArr) {
        hashtable.put(EXT_negotiated_ff_dhe_groups, createNegotiatedDHEGroupsClientExtension(sArr));
    }

    public static void addNegotiatedDHEGroupsServerExtension(Hashtable hashtable, short s) {
        hashtable.put(EXT_negotiated_ff_dhe_groups, createNegotiatedDHEGroupsServerExtension(s));
    }

    public static boolean areCompatibleParameters(DHParameters dHParameters, DHParameters dHParameters2) {
        return dHParameters.getP().equals(dHParameters2.getP()) && dHParameters.getG().equals(dHParameters2.getG());
    }

    public static byte[] calculateDHBasicAgreement(DHPublicKeyParameters dHPublicKeyParameters, DHPrivateKeyParameters dHPrivateKeyParameters) {
        DHBasicAgreement dHBasicAgreement = new DHBasicAgreement();
        dHBasicAgreement.init(dHPrivateKeyParameters);
        return BigIntegers.asUnsignedByteArray(dHBasicAgreement.calculateAgreement(dHPublicKeyParameters));
    }

    public static boolean containsDHECipherSuites(int[] iArr) {
        for (int isDHECipherSuite : iArr) {
            if (isDHECipherSuite(isDHECipherSuite)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] createNegotiatedDHEGroupsClientExtension(short[] sArr) {
        if (sArr != null && sArr.length >= 1 && sArr.length <= GF2Field.MASK) {
            return TlsUtils.encodeUint8ArrayWithUint8Length(sArr);
        }
        throw new TlsFatalAlert((short) 80);
    }

    public static byte[] createNegotiatedDHEGroupsServerExtension(short s) {
        TlsUtils.checkUint8(s);
        byte[] bArr = new byte[1];
        TlsUtils.writeUint8(s, bArr, 0);
        return bArr;
    }

    private static BigInteger fromHex(String str) {
        return new BigInteger(1, Hex.decode(str));
    }

    private static DHParameters fromSafeP(String str) {
        BigInteger fromHex = fromHex(str);
        return new DHParameters(fromHex, TWO, fromHex.shiftRight(1));
    }

    public static AsymmetricCipherKeyPair generateDHKeyPair(SecureRandom secureRandom, DHParameters dHParameters) {
        DHBasicKeyPairGenerator dHBasicKeyPairGenerator = new DHBasicKeyPairGenerator();
        dHBasicKeyPairGenerator.init(new DHKeyGenerationParameters(secureRandom, dHParameters));
        return dHBasicKeyPairGenerator.generateKeyPair();
    }

    public static DHPrivateKeyParameters generateEphemeralClientKeyExchange(SecureRandom secureRandom, DHParameters dHParameters, OutputStream outputStream) {
        AsymmetricCipherKeyPair generateDHKeyPair = generateDHKeyPair(secureRandom, dHParameters);
        writeDHParameter(((DHPublicKeyParameters) generateDHKeyPair.getPublic()).getY(), outputStream);
        return (DHPrivateKeyParameters) generateDHKeyPair.getPrivate();
    }

    public static DHPrivateKeyParameters generateEphemeralServerKeyExchange(SecureRandom secureRandom, DHParameters dHParameters, OutputStream outputStream) {
        AsymmetricCipherKeyPair generateDHKeyPair = generateDHKeyPair(secureRandom, dHParameters);
        new ServerDHParams((DHPublicKeyParameters) generateDHKeyPair.getPublic()).encode(outputStream);
        return (DHPrivateKeyParameters) generateDHKeyPair.getPrivate();
    }

    public static short[] getNegotiatedDHEGroupsClientExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_negotiated_ff_dhe_groups);
        return extensionData == null ? null : readNegotiatedDHEGroupsClientExtension(extensionData);
    }

    public static short getNegotiatedDHEGroupsServerExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_negotiated_ff_dhe_groups);
        return extensionData == null ? (short) -1 : readNegotiatedDHEGroupsServerExtension(extensionData);
    }

    public static DHParameters getParametersForDHEGroup(short s) {
        switch (s) {
            case ECCurve.COORD_AFFINE /*0*/:
                return draft_ffdhe2432;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return draft_ffdhe3072;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return draft_ffdhe4096;
            case F2m.PPB /*3*/:
                return draft_ffdhe6144;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return draft_ffdhe8192;
            default:
                return null;
        }
    }

    public static boolean isDHECipherSuite(int i) {
        switch (i) {
            case NamedCurve.secp160r2 /*17*/:
            case NamedCurve.secp192k1 /*18*/:
            case NamedCurve.secp192r1 /*19*/:
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
            case NamedCurve.secp224r1 /*21*/:
            case NamedCurve.secp256k1 /*22*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA /*56*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA /*57*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA /*135*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA /*136*/:
            case CipherSuite.TLS_DHE_PSK_WITH_RC4_128_SHA /*142*/:
            case CipherSuite.TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA /*143*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA /*144*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA /*145*/:
            case CipherSuite.TLS_DHE_DSS_WITH_SEED_CBC_SHA /*153*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA /*154*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384 /*179*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA256 /*180*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA384 /*181*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49302*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49303*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
            case CipherSuite.TLS_DHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58396*/:
            case CipherSuite.TLS_DHE_PSK_WITH_SALSA20_SHA1 /*58397*/:
            case CipherSuite.TLS_DHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58398*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SALSA20_SHA1 /*58399*/:
                return true;
            default:
                return false;
        }
    }

    public static BigInteger readDHParameter(InputStream inputStream) {
        return new BigInteger(1, TlsUtils.readOpaque16(inputStream));
    }

    public static short[] readNegotiatedDHEGroupsClientExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        short readUint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (readUint8 < (short) 1) {
            throw new TlsFatalAlert((short) 50);
        }
        short[] readUint8Array = TlsUtils.readUint8Array(readUint8, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return readUint8Array;
    }

    public static short readNegotiatedDHEGroupsServerExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        } else if (bArr.length == 1) {
            return TlsUtils.readUint8(bArr, 0);
        } else {
            throw new TlsFatalAlert((short) 50);
        }
    }

    public static DHPublicKeyParameters validateDHPublicKey(DHPublicKeyParameters dHPublicKeyParameters) {
        BigInteger y = dHPublicKeyParameters.getY();
        DHParameters parameters = dHPublicKeyParameters.getParameters();
        BigInteger p = parameters.getP();
        BigInteger g = parameters.getG();
        if (!p.isProbablePrime(2)) {
            throw new TlsFatalAlert((short) 47);
        } else if (g.compareTo(TWO) < 0 || g.compareTo(p.subtract(TWO)) > 0) {
            throw new TlsFatalAlert((short) 47);
        } else if (y.compareTo(TWO) >= 0 && y.compareTo(p.subtract(TWO)) <= 0) {
            return dHPublicKeyParameters;
        } else {
            throw new TlsFatalAlert((short) 47);
        }
    }

    public static void writeDHParameter(BigInteger bigInteger, OutputStream outputStream) {
        TlsUtils.writeOpaque16(BigIntegers.asUnsignedByteArray(bigInteger), outputStream);
    }
}
