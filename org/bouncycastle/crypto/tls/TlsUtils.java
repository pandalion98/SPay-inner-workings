package org.bouncycastle.crypto.tls;

import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsUtils {
    public static final byte[] EMPTY_BYTES;
    public static final int[] EMPTY_INTS;
    public static final long[] EMPTY_LONGS;
    public static final short[] EMPTY_SHORTS;
    public static final Integer EXT_signature_algorithms;
    static final byte[][] SSL3_CONST;
    static final byte[] SSL_CLIENT;
    static final byte[] SSL_SERVER;

    static {
        EMPTY_BYTES = new byte[0];
        EMPTY_SHORTS = new short[0];
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EXT_signature_algorithms = Integers.valueOf(13);
        SSL_CLIENT = new byte[]{(byte) 67, (byte) 76, (byte) 78, (byte) 84};
        SSL_SERVER = new byte[]{ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 82, (byte) 86, (byte) 82};
        SSL3_CONST = genSSL3Const();
    }

    public static byte[] PRF(TlsContext tlsContext, byte[] bArr, String str, byte[] bArr2, int i) {
        if (tlsContext.getServerVersion().isSSL()) {
            throw new IllegalStateException("No PRF available for SSLv3 session");
        }
        byte[] toByteArray = Strings.toByteArray(str);
        byte[] concat = concat(toByteArray, bArr2);
        int prfAlgorithm = tlsContext.getSecurityParameters().getPrfAlgorithm();
        if (prfAlgorithm == 0) {
            return PRF_legacy(bArr, toByteArray, concat, i);
        }
        toByteArray = new byte[i];
        hmac_hash(createPRFHash(prfAlgorithm), bArr, concat, toByteArray);
        return toByteArray;
    }

    public static byte[] PRF_legacy(byte[] bArr, String str, byte[] bArr2, int i) {
        byte[] toByteArray = Strings.toByteArray(str);
        return PRF_legacy(bArr, toByteArray, concat(toByteArray, bArr2), i);
    }

    static byte[] PRF_legacy(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        int i2 = 0;
        int length = (bArr.length + 1) / 2;
        Object obj = new byte[length];
        Object obj2 = new byte[length];
        System.arraycopy(bArr, 0, obj, 0, length);
        System.arraycopy(bArr, bArr.length - length, obj2, 0, length);
        byte[] bArr4 = new byte[i];
        byte[] bArr5 = new byte[i];
        hmac_hash(createHash((short) 1), obj, bArr3, bArr4);
        hmac_hash(createHash((short) 2), obj2, bArr3, bArr5);
        while (i2 < i) {
            bArr4[i2] = (byte) (bArr4[i2] ^ bArr5[i2]);
            i2++;
        }
        return bArr4;
    }

    public static void addSignatureAlgorithmsExtension(Hashtable hashtable, Vector vector) {
        hashtable.put(EXT_signature_algorithms, createSignatureAlgorithmsExtension(vector));
    }

    static byte[] calculateKeyBlock(TlsContext tlsContext, int i) {
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        byte[] masterSecret = securityParameters.getMasterSecret();
        byte[] concat = concat(securityParameters.getServerRandom(), securityParameters.getClientRandom());
        return isSSL(tlsContext) ? calculateKeyBlock_SSL(masterSecret, concat, i) : PRF(tlsContext, masterSecret, ExporterLabel.key_expansion, concat, i);
    }

    static byte[] calculateKeyBlock_SSL(byte[] bArr, byte[] bArr2, int i) {
        Digest createHash = createHash((short) 1);
        Digest createHash2 = createHash((short) 2);
        int digestSize = createHash.getDigestSize();
        byte[] bArr3 = new byte[createHash2.getDigestSize()];
        byte[] bArr4 = new byte[(i + digestSize)];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            byte[] bArr5 = SSL3_CONST[i3];
            createHash2.update(bArr5, 0, bArr5.length);
            createHash2.update(bArr, 0, bArr.length);
            createHash2.update(bArr2, 0, bArr2.length);
            createHash2.doFinal(bArr3, 0);
            createHash.update(bArr, 0, bArr.length);
            createHash.update(bArr3, 0, bArr3.length);
            createHash.doFinal(bArr4, i2);
            i2 += digestSize;
            i3++;
        }
        return Arrays.copyOfRange(bArr4, 0, i);
    }

    static byte[] calculateMasterSecret(TlsContext tlsContext, byte[] bArr) {
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        byte[] sessionHash = securityParameters.extendedMasterSecret ? securityParameters.getSessionHash() : concat(securityParameters.getClientRandom(), securityParameters.getServerRandom());
        if (isSSL(tlsContext)) {
            return calculateMasterSecret_SSL(bArr, sessionHash);
        }
        return PRF(tlsContext, bArr, securityParameters.extendedMasterSecret ? ExporterLabel.extended_master_secret : ExporterLabel.master_secret, sessionHash, 48);
    }

    static byte[] calculateMasterSecret_SSL(byte[] bArr, byte[] bArr2) {
        Digest createHash = createHash((short) 1);
        Digest createHash2 = createHash((short) 2);
        int digestSize = createHash.getDigestSize();
        byte[] bArr3 = new byte[createHash2.getDigestSize()];
        byte[] bArr4 = new byte[(digestSize * 3)];
        int i = 0;
        for (int i2 = 0; i2 < 3; i2++) {
            byte[] bArr5 = SSL3_CONST[i2];
            createHash2.update(bArr5, 0, bArr5.length);
            createHash2.update(bArr, 0, bArr.length);
            createHash2.update(bArr2, 0, bArr2.length);
            createHash2.doFinal(bArr3, 0);
            createHash.update(bArr, 0, bArr.length);
            createHash.update(bArr3, 0, bArr3.length);
            createHash.doFinal(bArr4, i);
            i += digestSize;
        }
        return bArr4;
    }

    static byte[] calculateVerifyData(TlsContext tlsContext, String str, byte[] bArr) {
        if (isSSL(tlsContext)) {
            return bArr;
        }
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        return PRF(tlsContext, securityParameters.getMasterSecret(), str, bArr, securityParameters.getVerifyDataLength());
    }

    public static void checkUint16(int i) {
        if (!isValidUint16(i)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint16(long j) {
        if (!isValidUint16(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint24(int i) {
        if (!isValidUint24(i)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint24(long j) {
        if (!isValidUint24(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint32(long j) {
        if (!isValidUint32(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint48(long j) {
        if (!isValidUint48(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint64(long j) {
        if (!isValidUint64(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint8(int i) {
        if (!isValidUint8(i)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint8(long j) {
        if (!isValidUint8(j)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static void checkUint8(short s) {
        if (!isValidUint8(s)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public static Digest cloneHash(short s, Digest digest) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new MD5Digest((MD5Digest) digest);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new SHA1Digest((SHA1Digest) digest);
            case F2m.PPB /*3*/:
                return new SHA224Digest((SHA224Digest) digest);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new SHA256Digest((SHA256Digest) digest);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new SHA384Digest((SHA384Digest) digest);
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new SHA512Digest((SHA512Digest) digest);
            default:
                throw new IllegalArgumentException("unknown HashAlgorithm");
        }
    }

    public static Digest clonePRFHash(int i, Digest digest) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return new CombinedHash((CombinedHash) digest);
            default:
                return cloneHash(getHashAlgorithmForPRFAlgorithm(i), digest);
        }
    }

    static byte[] concat(byte[] bArr, byte[] bArr2) {
        Object obj = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return obj;
    }

    public static Digest createHash(SignatureAndHashAlgorithm signatureAndHashAlgorithm) {
        return signatureAndHashAlgorithm == null ? new CombinedHash() : createHash(signatureAndHashAlgorithm.getHash());
    }

    public static Digest createHash(short s) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new MD5Digest();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new SHA1Digest();
            case F2m.PPB /*3*/:
                return new SHA224Digest();
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new SHA256Digest();
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new SHA384Digest();
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new SHA512Digest();
            default:
                throw new IllegalArgumentException("unknown HashAlgorithm");
        }
    }

    public static Digest createPRFHash(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return new CombinedHash();
            default:
                return createHash(getHashAlgorithmForPRFAlgorithm(i));
        }
    }

    public static byte[] createSignatureAlgorithmsExtension(Vector vector) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encodeSupportedSignatureAlgorithms(vector, false, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static TlsSigner createTlsSigner(short s) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new TlsRSASigner();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new TlsDSSSigner();
            case X509KeyUsage.nonRepudiation /*64*/:
                return new TlsECDSASigner();
            default:
                throw new IllegalArgumentException("'clientCertificateType' is not a type with signing capability");
        }
    }

    public static byte[] encodeOpaque8(byte[] bArr) {
        checkUint8(bArr.length);
        return Arrays.prepend(bArr, (byte) bArr.length);
    }

    public static void encodeSupportedSignatureAlgorithms(Vector vector, boolean z, OutputStream outputStream) {
        if (vector == null || vector.size() < 1 || vector.size() >= X509KeyUsage.decipherOnly) {
            throw new IllegalArgumentException("'supportedSignatureAlgorithms' must have length from 1 to (2^15 - 1)");
        }
        int size = vector.size() * 2;
        checkUint16(size);
        writeUint16(size, outputStream);
        int i = 0;
        while (i < vector.size()) {
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = (SignatureAndHashAlgorithm) vector.elementAt(i);
            if (z || signatureAndHashAlgorithm.getSignature() != (short) 0) {
                signatureAndHashAlgorithm.encode(outputStream);
                i++;
            } else {
                throw new IllegalArgumentException("SignatureAlgorithm.anonymous MUST NOT appear in the signature_algorithms extension");
            }
        }
    }

    public static byte[] encodeUint16ArrayWithUint16Length(int[] iArr) {
        byte[] bArr = new byte[((iArr.length * 2) + 2)];
        writeUint16ArrayWithUint16Length(iArr, bArr, 0);
        return bArr;
    }

    public static byte[] encodeUint8ArrayWithUint8Length(short[] sArr) {
        byte[] bArr = new byte[(sArr.length + 1)];
        writeUint8ArrayWithUint8Length(sArr, bArr, 0);
        return bArr;
    }

    private static byte[][] genSSL3Const() {
        byte[][] bArr = new byte[10][];
        for (int i = 0; i < 10; i++) {
            byte[] bArr2 = new byte[(i + 1)];
            Arrays.fill(bArr2, (byte) (i + 65));
            bArr[i] = bArr2;
        }
        return bArr;
    }

    public static int getCipherType(int i) {
        switch (getEncryptionAlgorithm(i)) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case EncryptionAlgorithm.ESTREAM_SALSA20 /*100*/:
            case ExtensionType.negotiated_ff_dhe_groups /*101*/:
                return 0;
            case F2m.PPB /*3*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
            case ECCurve.COORD_SKEWED /*7*/:
            case X509KeyUsage.keyAgreement /*8*/:
            case NamedCurve.sect283k1 /*9*/:
            case CertStatus.UNDETERMINED /*12*/:
            case NamedCurve.sect571k1 /*13*/:
            case NamedCurve.sect571r1 /*14*/:
                return 1;
            case NamedCurve.sect283r1 /*10*/:
            case CertStatus.UNREVOKED /*11*/:
            case NamedCurve.secp160k1 /*15*/:
            case X509KeyUsage.dataEncipherment /*16*/:
            case NamedCurve.secp160r2 /*17*/:
            case NamedCurve.secp192k1 /*18*/:
            case NamedCurve.secp192r1 /*19*/:
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
            case EncryptionAlgorithm.AEAD_CHACHA20_POLY1305 /*102*/:
                return 2;
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    static short getClientCertificateType(Certificate certificate, Certificate certificate2) {
        if (certificate.isEmpty()) {
            return (short) -1;
        }
        Certificate certificateAt = certificate.getCertificateAt(0);
        try {
            AsymmetricKeyParameter createKey = PublicKeyFactory.createKey(certificateAt.getSubjectPublicKeyInfo());
            if (createKey.isPrivate()) {
                throw new TlsFatalAlert((short) 80);
            } else if (createKey instanceof RSAKeyParameters) {
                validateKeyUsage(certificateAt, X509KeyUsage.digitalSignature);
                return (short) 1;
            } else if (createKey instanceof DSAPublicKeyParameters) {
                validateKeyUsage(certificateAt, X509KeyUsage.digitalSignature);
                return (short) 2;
            } else if (createKey instanceof ECPublicKeyParameters) {
                validateKeyUsage(certificateAt, X509KeyUsage.digitalSignature);
                return (short) 64;
            } else {
                throw new TlsFatalAlert((short) 43);
            }
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 43, e);
        }
    }

    public static Vector getDefaultDSSSignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short) 2, (short) 2));
    }

    public static Vector getDefaultECDSASignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short) 2, (short) 3));
    }

    public static Vector getDefaultRSASignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short) 2, (short) 1));
    }

    public static Vector getDefaultSupportedSignatureAlgorithms() {
        short[] sArr = new short[]{(short) 2, (short) 3, (short) 4, (short) 5, (short) 6};
        short[] sArr2 = new short[]{(short) 1, (short) 2, (short) 3};
        Vector vector = new Vector();
        for (short signatureAndHashAlgorithm : sArr2) {
            for (short signatureAndHashAlgorithm2 : sArr) {
                vector.addElement(new SignatureAndHashAlgorithm(signatureAndHashAlgorithm2, signatureAndHashAlgorithm));
            }
        }
        return vector;
    }

    public static int getEncryptionAlgorithm(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return 0;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_NULL_SHA /*49153*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_NULL_SHA /*49158*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_NULL_SHA /*49163*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_NULL_SHA /*49168*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA /*49209*/:
                return 0;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case NamedCurve.secp384r1 /*24*/:
                return 2;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case CipherSuite.TLS_PSK_WITH_RC4_128_SHA /*138*/:
            case CipherSuite.TLS_DHE_PSK_WITH_RC4_128_SHA /*142*/:
            case CipherSuite.TLS_RSA_PSK_WITH_RC4_128_SHA /*146*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_RC4_128_SHA /*49154*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA /*49159*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_RC4_128_SHA /*49164*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA /*49169*/:
            case CipherSuite.TLS_ECDH_anon_WITH_RC4_128_SHA /*49174*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_RC4_128_SHA /*49203*/:
                return 2;
            case NamedCurve.sect283r1 /*10*/:
            case NamedCurve.sect571k1 /*13*/:
            case X509KeyUsage.dataEncipherment /*16*/:
            case NamedCurve.secp192r1 /*19*/:
            case NamedCurve.secp256k1 /*22*/:
            case CipherSuite.TLS_PSK_WITH_3DES_EDE_CBC_SHA /*139*/:
            case CipherSuite.TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA /*143*/:
            case CipherSuite.TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA /*147*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA /*49155*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA /*49160*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA /*49165*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA /*49170*/:
            case CipherSuite.TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA /*49178*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA /*49179*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA /*49180*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_3DES_EDE_CBC_SHA /*49204*/:
                return 7;
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_CBC_SHA /*49*/:
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA /*140*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA /*144*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA /*148*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA256 /*174*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA256 /*182*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA /*49156*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA /*49161*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA /*49166*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA /*49171*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA /*49181*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA /*49182*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA /*49183*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 /*49187*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 /*49189*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 /*49191*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 /*49193*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA /*49205*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA256 /*49207*/:
                return 8;
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA /*53*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA /*54*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA /*56*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA /*57*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA /*141*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA /*145*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA /*149*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA /*49157*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA /*49162*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA /*49167*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA /*49172*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA /*49184*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA /*49185*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA /*49186*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA /*49206*/:
                return 9;
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA256 /*176*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA256 /*180*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA256 /*184*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA256 /*49210*/:
                return 0;
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
                return 9;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA /*65*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA /*67*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
                return 12;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA /*132*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA /*133*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA /*134*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA /*135*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA /*136*/:
                return 13;
            case CipherSuite.TLS_RSA_WITH_SEED_CBC_SHA /*150*/:
            case CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA /*151*/:
            case CipherSuite.TLS_DH_RSA_WITH_SEED_CBC_SHA /*152*/:
            case CipherSuite.TLS_DHE_DSS_WITH_SEED_CBC_SHA /*153*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA /*154*/:
                return 14;
            case CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256 /*156*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256 /*168*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256 /*172*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 /*49195*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 /*49197*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 /*49199*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 /*49201*/:
                return 10;
            case CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384 /*157*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_GCM_SHA384 /*161*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_GCM_SHA384 /*169*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_GCM_SHA384 /*173*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 /*49196*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 /*49198*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 /*49200*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 /*49202*/:
                return 11;
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA384 /*175*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384 /*179*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA384 /*183*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 /*49188*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 /*49190*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 /*49192*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 /*49194*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA384 /*49208*/:
                return 9;
            case CipherSuite.TLS_PSK_WITH_NULL_SHA384 /*177*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA384 /*181*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA384 /*185*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA384 /*49211*/:
                return 0;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*186*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*187*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*188*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49266*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49268*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49270*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49272*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49300*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49302*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49304*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49306*/:
                return 12;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
                return 13;
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49267*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49269*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49271*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49273*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49301*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49303*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49305*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49307*/:
                return 13;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49274*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49278*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49282*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49286*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49288*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49290*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49292*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49294*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49298*/:
                return 19;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49275*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49279*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49283*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49287*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49289*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49291*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49293*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49295*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49299*/:
                return 20;
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM /*49308*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM /*49316*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM /*49324*/:
                return 15;
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM /*49309*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM /*49317*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM /*49325*/:
                return 17;
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM_8 /*49312*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM_8 /*49320*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 /*49326*/:
                return 16;
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM_8 /*49313*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM_8 /*49321*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8 /*49327*/:
                return 18;
            case CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52243*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 /*52244*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
                return EncryptionAlgorithm.AEAD_CHACHA20_POLY1305;
            case CipherSuite.TLS_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58384*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58386*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_ESTREAM_SALSA20_SHA1 /*58388*/:
            case CipherSuite.TLS_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58390*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58392*/:
            case CipherSuite.TLS_RSA_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58394*/:
            case CipherSuite.TLS_DHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58396*/:
            case CipherSuite.TLS_DHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58398*/:
                return 100;
            case CipherSuite.TLS_RSA_WITH_SALSA20_SHA1 /*58385*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_SALSA20_SHA1 /*58387*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_SALSA20_SHA1 /*58389*/:
            case CipherSuite.TLS_PSK_WITH_SALSA20_SHA1 /*58391*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_SALSA20_SHA1 /*58393*/:
            case CipherSuite.TLS_RSA_PSK_WITH_SALSA20_SHA1 /*58395*/:
            case CipherSuite.TLS_DHE_PSK_WITH_SALSA20_SHA1 /*58397*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SALSA20_SHA1 /*58399*/:
                return ExtensionType.negotiated_ff_dhe_groups;
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public static byte[] getExtensionData(Hashtable hashtable, Integer num) {
        return hashtable == null ? null : (byte[]) hashtable.get(num);
    }

    public static short getHashAlgorithmForPRFAlgorithm(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                throw new IllegalArgumentException("legacy PRF not a valid algorithm");
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return (short) 4;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return (short) 5;
            default:
                throw new IllegalArgumentException("unknown PRFAlgorithm");
        }
    }

    public static ProtocolVersion getMinimumVersion(int i) {
        switch (i) {
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256 /*156*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384 /*157*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_GCM_SHA384 /*161*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256 /*168*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_GCM_SHA384 /*169*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256 /*172*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_GCM_SHA384 /*173*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*186*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*187*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*188*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256 /*191*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256 /*197*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 /*49187*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 /*49188*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 /*49189*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 /*49190*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 /*49191*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 /*49192*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 /*49193*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 /*49194*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 /*49195*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 /*49196*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 /*49197*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 /*49198*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 /*49199*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 /*49200*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 /*49201*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 /*49202*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49266*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49267*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49268*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49269*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49270*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49271*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49272*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49273*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49274*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49275*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49278*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49279*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49282*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49283*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_GCM_SHA256 /*49284*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_GCM_SHA384 /*49285*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49286*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49287*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49288*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49289*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49290*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49291*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49292*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49293*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49294*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49295*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49298*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49299*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM /*49308*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM /*49309*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM_8 /*49312*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM_8 /*49313*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM /*49316*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM /*49317*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM_8 /*49320*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM_8 /*49321*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM /*49324*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM /*49325*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 /*49326*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8 /*49327*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52243*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 /*52244*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
                return ProtocolVersion.TLSv12;
            default:
                return ProtocolVersion.SSLv3;
        }
    }

    public static ASN1ObjectIdentifier getOIDForHashAlgorithm(short s) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return PKCSObjectIdentifiers.md5;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return X509ObjectIdentifiers.id_SHA1;
            case F2m.PPB /*3*/:
                return NISTObjectIdentifiers.id_sha224;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return NISTObjectIdentifiers.id_sha256;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return NISTObjectIdentifiers.id_sha384;
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return NISTObjectIdentifiers.id_sha512;
            default:
                throw new IllegalArgumentException("unknown HashAlgorithm");
        }
    }

    public static Vector getSignatureAlgorithmsExtension(Hashtable hashtable) {
        byte[] extensionData = getExtensionData(hashtable, EXT_signature_algorithms);
        return extensionData == null ? null : readSignatureAlgorithmsExtension(extensionData);
    }

    public static SignatureAndHashAlgorithm getSignatureAndHashAlgorithm(TlsContext tlsContext, TlsSignerCredentials tlsSignerCredentials) {
        SignatureAndHashAlgorithm signatureAndHashAlgorithm = null;
        if (isTLSv12(tlsContext)) {
            signatureAndHashAlgorithm = tlsSignerCredentials.getSignatureAndHashAlgorithm();
            if (signatureAndHashAlgorithm == null) {
                throw new TlsFatalAlert((short) 80);
            }
        }
        return signatureAndHashAlgorithm;
    }

    public static boolean hasExpectedEmptyExtensionData(Hashtable hashtable, Integer num, short s) {
        byte[] extensionData = getExtensionData(hashtable, num);
        if (extensionData == null) {
            return false;
        }
        if (extensionData.length == 0) {
            return true;
        }
        throw new TlsFatalAlert(s);
    }

    public static boolean hasSigningCapability(short s) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case X509KeyUsage.nonRepudiation /*64*/:
                return true;
            default:
                return false;
        }
    }

    static void hmac_hash(Digest digest, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        HMac hMac = new HMac(digest);
        hMac.init(new KeyParameter(bArr));
        int digestSize = digest.getDigestSize();
        int length = ((bArr3.length + digestSize) - 1) / digestSize;
        byte[] bArr4 = new byte[hMac.getMacSize()];
        Object obj = new byte[hMac.getMacSize()];
        int i = 0;
        byte[] bArr5 = bArr2;
        while (i < length) {
            hMac.update(bArr5, 0, bArr5.length);
            hMac.doFinal(bArr4, 0);
            hMac.update(bArr4, 0, bArr4.length);
            hMac.update(bArr2, 0, bArr2.length);
            hMac.doFinal(obj, 0);
            System.arraycopy(obj, 0, bArr3, digestSize * i, Math.min(digestSize, bArr3.length - (digestSize * i)));
            i++;
            bArr5 = bArr4;
        }
    }

    public static TlsSession importSession(byte[] bArr, SessionParameters sessionParameters) {
        return new TlsSessionImpl(bArr, sessionParameters);
    }

    public static boolean isAEADCipherSuite(int i) {
        return 2 == getCipherType(i);
    }

    public static boolean isBlockCipherSuite(int i) {
        return 1 == getCipherType(i);
    }

    public static boolean isSSL(TlsContext tlsContext) {
        return tlsContext.getServerVersion().isSSL();
    }

    public static boolean isSignatureAlgorithmsExtensionAllowed(ProtocolVersion protocolVersion) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }

    public static boolean isStreamCipherSuite(int i) {
        return getCipherType(i) == 0;
    }

    public static boolean isTLSv11(TlsContext tlsContext) {
        return ProtocolVersion.TLSv11.isEqualOrEarlierVersionOf(tlsContext.getServerVersion().getEquivalentTLSVersion());
    }

    public static boolean isTLSv12(TlsContext tlsContext) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(tlsContext.getServerVersion().getEquivalentTLSVersion());
    }

    public static boolean isValidCipherSuiteForVersion(int i, ProtocolVersion protocolVersion) {
        return getMinimumVersion(i).isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }

    public static boolean isValidUint16(int i) {
        return (HCEClientConstants.HIGHEST_ATC_DEC_VALUE & i) == i;
    }

    public static boolean isValidUint16(long j) {
        return (65535 & j) == j;
    }

    public static boolean isValidUint24(int i) {
        return (ViewCompat.MEASURED_SIZE_MASK & i) == i;
    }

    public static boolean isValidUint24(long j) {
        return (16777215 & j) == j;
    }

    public static boolean isValidUint32(long j) {
        return (4294967295L & j) == j;
    }

    public static boolean isValidUint48(long j) {
        return (281474976710655L & j) == j;
    }

    public static boolean isValidUint64(long j) {
        return true;
    }

    public static boolean isValidUint8(int i) {
        return (i & GF2Field.MASK) == i;
    }

    public static boolean isValidUint8(long j) {
        return (255 & j) == j;
    }

    public static boolean isValidUint8(short s) {
        return (s & GF2Field.MASK) == s;
    }

    public static Vector parseSupportedSignatureAlgorithms(boolean z, InputStream inputStream) {
        int readUint16 = readUint16(inputStream);
        if (readUint16 < 2 || (readUint16 & 1) != 0) {
            throw new TlsFatalAlert((short) 50);
        }
        int i = readUint16 / 2;
        Vector vector = new Vector(i);
        readUint16 = 0;
        while (readUint16 < i) {
            SignatureAndHashAlgorithm parse = SignatureAndHashAlgorithm.parse(inputStream);
            if (z || parse.getSignature() != (short) 0) {
                vector.addElement(parse);
                readUint16++;
            } else {
                throw new TlsFatalAlert((short) 47);
            }
        }
        return vector;
    }

    public static ASN1Primitive readASN1Object(byte[] bArr) {
        ASN1InputStream aSN1InputStream = new ASN1InputStream(bArr);
        ASN1Primitive readObject = aSN1InputStream.readObject();
        if (readObject == null) {
            throw new TlsFatalAlert((short) 50);
        } else if (aSN1InputStream.readObject() == null) {
            return readObject;
        } else {
            throw new TlsFatalAlert((short) 50);
        }
    }

    public static byte[] readAllOrNothing(int i, InputStream inputStream) {
        if (i < 1) {
            return EMPTY_BYTES;
        }
        byte[] bArr = new byte[i];
        int readFully = Streams.readFully(inputStream, bArr);
        if (readFully == 0) {
            return null;
        }
        if (readFully == i) {
            return bArr;
        }
        throw new EOFException();
    }

    public static ASN1Primitive readDERObject(byte[] bArr) {
        ASN1Primitive readASN1Object = readASN1Object(bArr);
        if (Arrays.areEqual(readASN1Object.getEncoded(ASN1Encoding.DER), bArr)) {
            return readASN1Object;
        }
        throw new TlsFatalAlert((short) 50);
    }

    public static void readFully(byte[] bArr, InputStream inputStream) {
        int length = bArr.length;
        if (length > 0 && length != Streams.readFully(inputStream, bArr)) {
            throw new EOFException();
        }
    }

    public static byte[] readFully(int i, InputStream inputStream) {
        if (i < 1) {
            return EMPTY_BYTES;
        }
        byte[] bArr = new byte[i];
        if (i == Streams.readFully(inputStream, bArr)) {
            return bArr;
        }
        throw new EOFException();
    }

    public static byte[] readOpaque16(InputStream inputStream) {
        return readFully(readUint16(inputStream), inputStream);
    }

    public static byte[] readOpaque24(InputStream inputStream) {
        return readFully(readUint24(inputStream), inputStream);
    }

    public static byte[] readOpaque8(InputStream inputStream) {
        return readFully(readUint8(inputStream), inputStream);
    }

    public static Vector readSignatureAlgorithmsExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Vector parseSupportedSignatureAlgorithms = parseSupportedSignatureAlgorithms(false, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return parseSupportedSignatureAlgorithms;
    }

    public static int readUint16(InputStream inputStream) {
        int read = inputStream.read();
        int read2 = inputStream.read();
        if (read2 >= 0) {
            return (read << 8) | read2;
        }
        throw new EOFException();
    }

    public static int readUint16(byte[] bArr, int i) {
        return ((bArr[i] & GF2Field.MASK) << 8) | (bArr[i + 1] & GF2Field.MASK);
    }

    public static int[] readUint16Array(int i, InputStream inputStream) {
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = readUint16(inputStream);
        }
        return iArr;
    }

    public static int readUint24(InputStream inputStream) {
        int read = inputStream.read();
        int read2 = inputStream.read();
        int read3 = inputStream.read();
        if (read3 >= 0) {
            return ((read << 16) | (read2 << 8)) | read3;
        }
        throw new EOFException();
    }

    public static int readUint24(byte[] bArr, int i) {
        int i2 = i + 1;
        return (((bArr[i] & GF2Field.MASK) << 16) | ((bArr[i2] & GF2Field.MASK) << 8)) | (bArr[i2 + 1] & GF2Field.MASK);
    }

    public static long readUint32(InputStream inputStream) {
        int read = inputStream.read();
        int read2 = inputStream.read();
        int read3 = inputStream.read();
        int read4 = inputStream.read();
        if (read4 >= 0) {
            return ((long) ((((read << 2) | (read2 << 16)) | (read3 << 8)) | read4)) & 4294967295L;
        }
        throw new EOFException();
    }

    public static long readUint32(byte[] bArr, int i) {
        int i2 = i + 1;
        i2++;
        return ((long) (((((bArr[i] & GF2Field.MASK) << 24) | ((bArr[i2] & GF2Field.MASK) << 16)) | ((bArr[i2] & GF2Field.MASK) << 8)) | (bArr[i2 + 1] & GF2Field.MASK))) & 4294967295L;
    }

    public static long readUint48(InputStream inputStream) {
        return (((long) readUint24(inputStream)) & 4294967295L) | ((((long) readUint24(inputStream)) & 4294967295L) << 24);
    }

    public static long readUint48(byte[] bArr, int i) {
        return (((long) readUint24(bArr, i + 3)) & 4294967295L) | ((((long) readUint24(bArr, i)) & 4294967295L) << 24);
    }

    public static short readUint8(InputStream inputStream) {
        int read = inputStream.read();
        if (read >= 0) {
            return (short) read;
        }
        throw new EOFException();
    }

    public static short readUint8(byte[] bArr, int i) {
        return (short) bArr[i];
    }

    public static short[] readUint8Array(int i, InputStream inputStream) {
        short[] sArr = new short[i];
        for (int i2 = 0; i2 < i; i2++) {
            sArr[i2] = readUint8(inputStream);
        }
        return sArr;
    }

    public static ProtocolVersion readVersion(InputStream inputStream) {
        int read = inputStream.read();
        int read2 = inputStream.read();
        if (read2 >= 0) {
            return ProtocolVersion.get(read, read2);
        }
        throw new EOFException();
    }

    public static ProtocolVersion readVersion(byte[] bArr, int i) {
        return ProtocolVersion.get(bArr[i] & GF2Field.MASK, bArr[i + 1] & GF2Field.MASK);
    }

    public static int readVersionRaw(InputStream inputStream) {
        int read = inputStream.read();
        int read2 = inputStream.read();
        if (read2 >= 0) {
            return (read << 8) | read2;
        }
        throw new EOFException();
    }

    public static int readVersionRaw(byte[] bArr, int i) {
        return (bArr[i] << 8) | bArr[i + 1];
    }

    static void trackHashAlgorithms(TlsHandshakeHash tlsHandshakeHash, Vector vector) {
        if (vector != null) {
            for (int i = 0; i < vector.size(); i++) {
                tlsHandshakeHash.trackHashAlgorithm(((SignatureAndHashAlgorithm) vector.elementAt(i)).getHash());
            }
        }
    }

    static void validateKeyUsage(Certificate certificate, int i) {
        Extensions extensions = certificate.getTBSCertificate().getExtensions();
        if (extensions != null) {
            KeyUsage fromExtensions = KeyUsage.fromExtensions(extensions);
            if (fromExtensions != null && ((fromExtensions.getBytes()[0] & GF2Field.MASK) & i) != i) {
                throw new TlsFatalAlert((short) 46);
            }
        }
    }

    private static Vector vectorOfOne(Object obj) {
        Vector vector = new Vector(1);
        vector.addElement(obj);
        return vector;
    }

    public static void writeGMTUnixTime(byte[] bArr, int i) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        bArr[i] = (byte) (currentTimeMillis >>> 24);
        bArr[i + 1] = (byte) (currentTimeMillis >>> 16);
        bArr[i + 2] = (byte) (currentTimeMillis >>> 8);
        bArr[i + 3] = (byte) currentTimeMillis;
    }

    public static void writeOpaque16(byte[] bArr, OutputStream outputStream) {
        checkUint16(bArr.length);
        writeUint16(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    public static void writeOpaque24(byte[] bArr, OutputStream outputStream) {
        checkUint24(bArr.length);
        writeUint24(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    public static void writeOpaque8(byte[] bArr, OutputStream outputStream) {
        checkUint8(bArr.length);
        writeUint8(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    public static void writeUint16(int i, OutputStream outputStream) {
        outputStream.write(i >>> 8);
        outputStream.write(i);
    }

    public static void writeUint16(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >>> 8);
        bArr[i2 + 1] = (byte) i;
    }

    public static void writeUint16Array(int[] iArr, OutputStream outputStream) {
        for (int writeUint16 : iArr) {
            writeUint16(writeUint16, outputStream);
        }
    }

    public static void writeUint16Array(int[] iArr, byte[] bArr, int i) {
        for (int writeUint16 : iArr) {
            writeUint16(writeUint16, bArr, i);
            i += 2;
        }
    }

    public static void writeUint16ArrayWithUint16Length(int[] iArr, OutputStream outputStream) {
        int length = iArr.length * 2;
        checkUint16(length);
        writeUint16(length, outputStream);
        writeUint16Array(iArr, outputStream);
    }

    public static void writeUint16ArrayWithUint16Length(int[] iArr, byte[] bArr, int i) {
        int length = iArr.length * 2;
        checkUint16(length);
        writeUint16(length, bArr, i);
        writeUint16Array(iArr, bArr, i + 2);
    }

    public static void writeUint24(int i, OutputStream outputStream) {
        outputStream.write((byte) (i >>> 16));
        outputStream.write((byte) (i >>> 8));
        outputStream.write((byte) i);
    }

    public static void writeUint24(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >>> 16);
        bArr[i2 + 1] = (byte) (i >>> 8);
        bArr[i2 + 2] = (byte) i;
    }

    public static void writeUint32(long j, OutputStream outputStream) {
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) j));
    }

    public static void writeUint32(long j, byte[] bArr, int i) {
        bArr[i] = (byte) ((int) (j >>> 24));
        bArr[i + 1] = (byte) ((int) (j >>> 16));
        bArr[i + 2] = (byte) ((int) (j >>> 8));
        bArr[i + 3] = (byte) ((int) j);
    }

    public static void writeUint48(long j, OutputStream outputStream) {
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) j));
    }

    public static void writeUint48(long j, byte[] bArr, int i) {
        bArr[i] = (byte) ((int) (j >>> 40));
        bArr[i + 1] = (byte) ((int) (j >>> 32));
        bArr[i + 2] = (byte) ((int) (j >>> 24));
        bArr[i + 3] = (byte) ((int) (j >>> 16));
        bArr[i + 4] = (byte) ((int) (j >>> 8));
        bArr[i + 5] = (byte) ((int) j);
    }

    public static void writeUint64(long j, OutputStream outputStream) {
        outputStream.write((byte) ((int) (j >>> 56)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) j));
    }

    public static void writeUint64(long j, byte[] bArr, int i) {
        bArr[i] = (byte) ((int) (j >>> 56));
        bArr[i + 1] = (byte) ((int) (j >>> 48));
        bArr[i + 2] = (byte) ((int) (j >>> 40));
        bArr[i + 3] = (byte) ((int) (j >>> 32));
        bArr[i + 4] = (byte) ((int) (j >>> 24));
        bArr[i + 5] = (byte) ((int) (j >>> 16));
        bArr[i + 6] = (byte) ((int) (j >>> 8));
        bArr[i + 7] = (byte) ((int) j);
    }

    public static void writeUint8(int i, OutputStream outputStream) {
        outputStream.write(i);
    }

    public static void writeUint8(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
    }

    public static void writeUint8(short s, OutputStream outputStream) {
        outputStream.write(s);
    }

    public static void writeUint8(short s, byte[] bArr, int i) {
        bArr[i] = (byte) s;
    }

    public static void writeUint8Array(short[] sArr, OutputStream outputStream) {
        for (short writeUint8 : sArr) {
            writeUint8(writeUint8, outputStream);
        }
    }

    public static void writeUint8Array(short[] sArr, byte[] bArr, int i) {
        for (short writeUint8 : sArr) {
            writeUint8(writeUint8, bArr, i);
            i++;
        }
    }

    public static void writeUint8ArrayWithUint8Length(short[] sArr, OutputStream outputStream) {
        checkUint8(sArr.length);
        writeUint8(sArr.length, outputStream);
        writeUint8Array(sArr, outputStream);
    }

    public static void writeUint8ArrayWithUint8Length(short[] sArr, byte[] bArr, int i) {
        checkUint8(sArr.length);
        writeUint8(sArr.length, bArr, i);
        writeUint8Array(sArr, bArr, i + 1);
    }

    public static void writeVersion(ProtocolVersion protocolVersion, OutputStream outputStream) {
        outputStream.write(protocolVersion.getMajorVersion());
        outputStream.write(protocolVersion.getMinorVersion());
    }

    public static void writeVersion(ProtocolVersion protocolVersion, byte[] bArr, int i) {
        bArr[i] = (byte) protocolVersion.getMajorVersion();
        bArr[i + 1] = (byte) protocolVersion.getMinorVersion();
    }
}
