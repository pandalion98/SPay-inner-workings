/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.EOFException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Integers
 *  org.bouncycastle.util.Strings
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
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
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CombinedHash;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsDSSSigner;
import org.bouncycastle.crypto.tls.TlsECDSASigner;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsRSASigner;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsSessionImpl;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.io.Streams;

public class TlsUtils {
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final int[] EMPTY_INTS;
    public static final long[] EMPTY_LONGS;
    public static final short[] EMPTY_SHORTS;
    public static final Integer EXT_signature_algorithms;
    static final byte[][] SSL3_CONST;
    static final byte[] SSL_CLIENT;
    static final byte[] SSL_SERVER;

    static {
        EMPTY_SHORTS = new short[0];
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EXT_signature_algorithms = Integers.valueOf((int)13);
        SSL_CLIENT = new byte[]{67, 76, 78, 84};
        SSL_SERVER = new byte[]{83, 82, 86, 82};
        SSL3_CONST = TlsUtils.genSSL3Const();
    }

    public static byte[] PRF(TlsContext tlsContext, byte[] arrby, String string, byte[] arrby2, int n2) {
        if (tlsContext.getServerVersion().isSSL()) {
            throw new IllegalStateException("No PRF available for SSLv3 session");
        }
        byte[] arrby3 = Strings.toByteArray((String)string);
        byte[] arrby4 = TlsUtils.concat(arrby3, arrby2);
        int n3 = tlsContext.getSecurityParameters().getPrfAlgorithm();
        if (n3 == 0) {
            return TlsUtils.PRF_legacy(arrby, arrby3, arrby4, n2);
        }
        Digest digest = TlsUtils.createPRFHash(n3);
        byte[] arrby5 = new byte[n2];
        TlsUtils.hmac_hash(digest, arrby, arrby4, arrby5);
        return arrby5;
    }

    public static byte[] PRF_legacy(byte[] arrby, String string, byte[] arrby2, int n2) {
        byte[] arrby3 = Strings.toByteArray((String)string);
        return TlsUtils.PRF_legacy(arrby, arrby3, TlsUtils.concat(arrby3, arrby2), n2);
    }

    static byte[] PRF_legacy(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2) {
        int n3 = (1 + arrby.length) / 2;
        byte[] arrby4 = new byte[n3];
        byte[] arrby5 = new byte[n3];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby4, (int)0, (int)n3);
        System.arraycopy((Object)arrby, (int)(arrby.length - n3), (Object)arrby5, (int)0, (int)n3);
        byte[] arrby6 = new byte[n2];
        byte[] arrby7 = new byte[n2];
        TlsUtils.hmac_hash(TlsUtils.createHash((short)1), arrby4, arrby3, arrby6);
        TlsUtils.hmac_hash(TlsUtils.createHash((short)2), arrby5, arrby3, arrby7);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby6[i2] = (byte)(arrby6[i2] ^ arrby7[i2]);
        }
        return arrby6;
    }

    public static void addSignatureAlgorithmsExtension(Hashtable hashtable, Vector vector) {
        hashtable.put((Object)EXT_signature_algorithms, (Object)TlsUtils.createSignatureAlgorithmsExtension(vector));
    }

    static byte[] calculateKeyBlock(TlsContext tlsContext, int n2) {
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        byte[] arrby = securityParameters.getMasterSecret();
        byte[] arrby2 = TlsUtils.concat(securityParameters.getServerRandom(), securityParameters.getClientRandom());
        if (TlsUtils.isSSL(tlsContext)) {
            return TlsUtils.calculateKeyBlock_SSL(arrby, arrby2, n2);
        }
        return TlsUtils.PRF(tlsContext, arrby, "key expansion", arrby2, n2);
    }

    static byte[] calculateKeyBlock_SSL(byte[] arrby, byte[] arrby2, int n2) {
        Digest digest = TlsUtils.createHash((short)1);
        Digest digest2 = TlsUtils.createHash((short)2);
        int n3 = digest.getDigestSize();
        byte[] arrby3 = new byte[digest2.getDigestSize()];
        byte[] arrby4 = new byte[n2 + n3];
        int n4 = 0;
        int n5 = 0;
        while (n4 < n2) {
            byte[] arrby5 = SSL3_CONST[n5];
            digest2.update(arrby5, 0, arrby5.length);
            digest2.update(arrby, 0, arrby.length);
            digest2.update(arrby2, 0, arrby2.length);
            digest2.doFinal(arrby3, 0);
            digest.update(arrby, 0, arrby.length);
            digest.update(arrby3, 0, arrby3.length);
            digest.doFinal(arrby4, n4);
            n4 += n3;
            ++n5;
        }
        return Arrays.copyOfRange((byte[])arrby4, (int)0, (int)n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    static byte[] calculateMasterSecret(TlsContext tlsContext, byte[] arrby) {
        String string;
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        byte[] arrby2 = securityParameters.extendedMasterSecret ? securityParameters.getSessionHash() : TlsUtils.concat(securityParameters.getClientRandom(), securityParameters.getServerRandom());
        if (TlsUtils.isSSL(tlsContext)) {
            return TlsUtils.calculateMasterSecret_SSL(arrby, arrby2);
        }
        if (securityParameters.extendedMasterSecret) {
            string = "extended master secret";
            return TlsUtils.PRF(tlsContext, arrby, string, arrby2, 48);
        }
        string = "master secret";
        return TlsUtils.PRF(tlsContext, arrby, string, arrby2, 48);
    }

    static byte[] calculateMasterSecret_SSL(byte[] arrby, byte[] arrby2) {
        Digest digest = TlsUtils.createHash((short)1);
        Digest digest2 = TlsUtils.createHash((short)2);
        int n2 = digest.getDigestSize();
        byte[] arrby3 = new byte[digest2.getDigestSize()];
        byte[] arrby4 = new byte[n2 * 3];
        int n3 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            byte[] arrby5 = SSL3_CONST[i2];
            digest2.update(arrby5, 0, arrby5.length);
            digest2.update(arrby, 0, arrby.length);
            digest2.update(arrby2, 0, arrby2.length);
            digest2.doFinal(arrby3, 0);
            digest.update(arrby, 0, arrby.length);
            digest.update(arrby3, 0, arrby3.length);
            digest.doFinal(arrby4, n3);
            n3 += n2;
        }
        return arrby4;
    }

    static byte[] calculateVerifyData(TlsContext tlsContext, String string, byte[] arrby) {
        if (TlsUtils.isSSL(tlsContext)) {
            return arrby;
        }
        SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        return TlsUtils.PRF(tlsContext, securityParameters.getMasterSecret(), string, arrby, securityParameters.getVerifyDataLength());
    }

    public static void checkUint16(int n2) {
        if (!TlsUtils.isValidUint16(n2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint16(long l2) {
        if (!TlsUtils.isValidUint16(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint24(int n2) {
        if (!TlsUtils.isValidUint24(n2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint24(long l2) {
        if (!TlsUtils.isValidUint24(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint32(long l2) {
        if (!TlsUtils.isValidUint32(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint48(long l2) {
        if (!TlsUtils.isValidUint48(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint64(long l2) {
        if (!TlsUtils.isValidUint64(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint8(int n2) {
        if (!TlsUtils.isValidUint8(n2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint8(long l2) {
        if (!TlsUtils.isValidUint8(l2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static void checkUint8(short s2) {
        if (!TlsUtils.isValidUint8(s2)) {
            throw new TlsFatalAlert(80);
        }
    }

    public static Digest cloneHash(short s2, Digest digest) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 1: {
                return new MD5Digest((MD5Digest)digest);
            }
            case 2: {
                return new SHA1Digest((SHA1Digest)digest);
            }
            case 3: {
                return new SHA224Digest((SHA224Digest)digest);
            }
            case 4: {
                return new SHA256Digest((SHA256Digest)digest);
            }
            case 5: {
                return new SHA384Digest((SHA384Digest)digest);
            }
            case 6: 
        }
        return new SHA512Digest((SHA512Digest)digest);
    }

    public static Digest clonePRFHash(int n2, Digest digest) {
        switch (n2) {
            default: {
                return TlsUtils.cloneHash(TlsUtils.getHashAlgorithmForPRFAlgorithm(n2), digest);
            }
            case 0: 
        }
        return new CombinedHash((CombinedHash)digest);
    }

    static byte[] concat(byte[] arrby, byte[] arrby2) {
        byte[] arrby3 = new byte[arrby.length + arrby2.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)arrby.length);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)arrby.length, (int)arrby2.length);
        return arrby3;
    }

    public static Digest createHash(SignatureAndHashAlgorithm signatureAndHashAlgorithm) {
        if (signatureAndHashAlgorithm == null) {
            return new CombinedHash();
        }
        return TlsUtils.createHash(signatureAndHashAlgorithm.getHash());
    }

    public static Digest createHash(short s2) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 1: {
                return new MD5Digest();
            }
            case 2: {
                return new SHA1Digest();
            }
            case 3: {
                return new SHA224Digest();
            }
            case 4: {
                return new SHA256Digest();
            }
            case 5: {
                return new SHA384Digest();
            }
            case 6: 
        }
        return new SHA512Digest();
    }

    public static Digest createPRFHash(int n2) {
        switch (n2) {
            default: {
                return TlsUtils.createHash(TlsUtils.getHashAlgorithmForPRFAlgorithm(n2));
            }
            case 0: 
        }
        return new CombinedHash();
    }

    public static byte[] createSignatureAlgorithmsExtension(Vector vector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.encodeSupportedSignatureAlgorithms(vector, false, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static TlsSigner createTlsSigner(short s2) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("'clientCertificateType' is not a type with signing capability");
            }
            case 2: {
                return new TlsDSSSigner();
            }
            case 64: {
                return new TlsECDSASigner();
            }
            case 1: 
        }
        return new TlsRSASigner();
    }

    public static byte[] encodeOpaque8(byte[] arrby) {
        TlsUtils.checkUint8(arrby.length);
        return Arrays.prepend((byte[])arrby, (byte)((byte)arrby.length));
    }

    public static void encodeSupportedSignatureAlgorithms(Vector vector, boolean bl, OutputStream outputStream) {
        if (vector == null || vector.size() < 1 || vector.size() >= 32768) {
            throw new IllegalArgumentException("'supportedSignatureAlgorithms' must have length from 1 to (2^15 - 1)");
        }
        int n2 = 2 * vector.size();
        TlsUtils.checkUint16(n2);
        TlsUtils.writeUint16(n2, outputStream);
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = (SignatureAndHashAlgorithm)vector.elementAt(i2);
            if (!bl && signatureAndHashAlgorithm.getSignature() == 0) {
                throw new IllegalArgumentException("SignatureAlgorithm.anonymous MUST NOT appear in the signature_algorithms extension");
            }
            signatureAndHashAlgorithm.encode(outputStream);
        }
    }

    public static byte[] encodeUint16ArrayWithUint16Length(int[] arrn) {
        byte[] arrby = new byte[2 + 2 * arrn.length];
        TlsUtils.writeUint16ArrayWithUint16Length(arrn, arrby, 0);
        return arrby;
    }

    public static byte[] encodeUint8ArrayWithUint8Length(short[] arrs) {
        byte[] arrby = new byte[1 + arrs.length];
        TlsUtils.writeUint8ArrayWithUint8Length(arrs, arrby, 0);
        return arrby;
    }

    private static byte[][] genSSL3Const() {
        byte[][] arrarrby = new byte[10][];
        for (int i2 = 0; i2 < 10; ++i2) {
            byte[] arrby = new byte[i2 + 1];
            Arrays.fill((byte[])arrby, (byte)((byte)(i2 + 65)));
            arrarrby[i2] = arrby;
        }
        return arrarrby;
    }

    public static int getCipherType(int n2) {
        switch (TlsUtils.getEncryptionAlgorithm(n2)) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 10: 
            case 11: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 102: {
                return 2;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 12: 
            case 13: 
            case 14: {
                return 1;
            }
            case 1: 
            case 2: 
            case 100: 
            case 101: 
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static short getClientCertificateType(Certificate certificate, Certificate certificate2) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (certificate.isEmpty()) {
            return -1;
        }
        org.bouncycastle.asn1.x509.Certificate certificate3 = certificate.getCertificateAt(0);
        SubjectPublicKeyInfo subjectPublicKeyInfo = certificate3.getSubjectPublicKeyInfo();
        try {
            asymmetricKeyParameter = PublicKeyFactory.createKey(subjectPublicKeyInfo);
            if (asymmetricKeyParameter.isPrivate()) {
                throw new TlsFatalAlert(80);
            }
            if (asymmetricKeyParameter instanceof RSAKeyParameters) {
                TlsUtils.validateKeyUsage(certificate3, 128);
                return 1;
            }
        }
        catch (Exception exception) {
            throw new TlsFatalAlert(43, exception);
        }
        if (asymmetricKeyParameter instanceof DSAPublicKeyParameters) {
            TlsUtils.validateKeyUsage(certificate3, 128);
            return 2;
        }
        if (asymmetricKeyParameter instanceof ECPublicKeyParameters) {
            TlsUtils.validateKeyUsage(certificate3, 128);
            return 64;
        }
        throw new TlsFatalAlert(43);
    }

    public static Vector getDefaultDSSSignatureAlgorithms() {
        return TlsUtils.vectorOfOne(new SignatureAndHashAlgorithm(2, 2));
    }

    public static Vector getDefaultECDSASignatureAlgorithms() {
        return TlsUtils.vectorOfOne(new SignatureAndHashAlgorithm(2, 3));
    }

    public static Vector getDefaultRSASignatureAlgorithms() {
        return TlsUtils.vectorOfOne(new SignatureAndHashAlgorithm(2, 1));
    }

    public static Vector getDefaultSupportedSignatureAlgorithms() {
        short[] arrs = new short[]{2, 3, 4, 5, 6};
        short[] arrs2 = new short[]{1, 2, 3};
        Vector vector = new Vector();
        for (int i2 = 0; i2 < arrs2.length; ++i2) {
            for (int i3 = 0; i3 < arrs.length; ++i3) {
                vector.addElement((Object)new SignatureAndHashAlgorithm(arrs[i3], arrs2[i2]));
            }
        }
        return vector;
    }

    public static int getEncryptionAlgorithm(int n2) {
        int n3 = 8;
        switch (n2) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 10: 
            case 13: 
            case 16: 
            case 19: 
            case 22: 
            case 139: 
            case 143: 
            case 147: 
            case 49155: 
            case 49160: 
            case 49165: 
            case 49170: 
            case 49178: 
            case 49179: 
            case 49180: 
            case 49204: {
                n3 = 7;
            }
            case 47: 
            case 48: 
            case 49: 
            case 50: 
            case 51: 
            case 60: 
            case 62: 
            case 63: 
            case 64: 
            case 103: 
            case 140: 
            case 144: 
            case 148: 
            case 174: 
            case 178: 
            case 182: 
            case 49156: 
            case 49161: 
            case 49166: 
            case 49171: 
            case 49181: 
            case 49182: 
            case 49183: 
            case 49187: 
            case 49189: 
            case 49191: 
            case 49193: 
            case 49205: 
            case 49207: {
                return n3;
            }
            case 52243: 
            case 52244: 
            case 52245: {
                return 102;
            }
            case 49308: 
            case 49310: 
            case 49316: 
            case 49318: 
            case 49324: {
                return 15;
            }
            case 49312: 
            case 49314: 
            case 49320: 
            case 49322: 
            case 49326: {
                return 16;
            }
            case 156: 
            case 158: 
            case 160: 
            case 162: 
            case 164: 
            case 168: 
            case 170: 
            case 172: 
            case 49195: 
            case 49197: 
            case 49199: 
            case 49201: {
                return 10;
            }
            case 53: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 141: 
            case 145: 
            case 149: 
            case 49157: 
            case 49162: 
            case 49167: 
            case 49172: 
            case 49184: 
            case 49185: 
            case 49186: 
            case 49206: {
                return 9;
            }
            case 61: 
            case 104: 
            case 105: 
            case 106: 
            case 107: {
                return 9;
            }
            case 175: 
            case 179: 
            case 183: 
            case 49188: 
            case 49190: 
            case 49192: 
            case 49194: 
            case 49208: {
                return 9;
            }
            case 49309: 
            case 49311: 
            case 49317: 
            case 49319: 
            case 49325: {
                return 17;
            }
            case 49313: 
            case 49315: 
            case 49321: 
            case 49323: 
            case 49327: {
                return 18;
            }
            case 157: 
            case 159: 
            case 161: 
            case 163: 
            case 165: 
            case 169: 
            case 171: 
            case 173: 
            case 49196: 
            case 49198: 
            case 49200: 
            case 49202: {
                return 11;
            }
            case 65: 
            case 66: 
            case 67: 
            case 68: 
            case 69: {
                return 12;
            }
            case 186: 
            case 187: 
            case 188: 
            case 189: 
            case 190: 
            case 49266: 
            case 49268: 
            case 49270: 
            case 49272: 
            case 49300: 
            case 49302: 
            case 49304: 
            case 49306: {
                return 12;
            }
            case 49274: 
            case 49276: 
            case 49278: 
            case 49280: 
            case 49282: 
            case 49286: 
            case 49288: 
            case 49290: 
            case 49292: 
            case 49294: 
            case 49296: 
            case 49298: {
                return 19;
            }
            case 132: 
            case 133: 
            case 134: 
            case 135: 
            case 136: {
                return 13;
            }
            case 192: 
            case 193: 
            case 194: 
            case 195: 
            case 196: {
                return 13;
            }
            case 49267: 
            case 49269: 
            case 49271: 
            case 49273: 
            case 49301: 
            case 49303: 
            case 49305: 
            case 49307: {
                return 13;
            }
            case 49275: 
            case 49277: 
            case 49279: 
            case 49281: 
            case 49283: 
            case 49287: 
            case 49289: 
            case 49291: 
            case 49293: 
            case 49295: 
            case 49297: 
            case 49299: {
                return 20;
            }
            case 58384: 
            case 58386: 
            case 58388: 
            case 58390: 
            case 58392: 
            case 58394: 
            case 58396: 
            case 58398: {
                return 100;
            }
            case 1: {
                return 0;
            }
            case 2: 
            case 44: 
            case 45: 
            case 46: 
            case 49153: 
            case 49158: 
            case 49163: 
            case 49168: 
            case 49209: {
                return 0;
            }
            case 59: 
            case 176: 
            case 180: 
            case 184: 
            case 49210: {
                return 0;
            }
            case 177: 
            case 181: 
            case 185: 
            case 49211: {
                return 0;
            }
            case 4: 
            case 24: {
                return 2;
            }
            case 5: 
            case 138: 
            case 142: 
            case 146: 
            case 49154: 
            case 49159: 
            case 49164: 
            case 49169: 
            case 49174: 
            case 49203: {
                return 2;
            }
            case 58385: 
            case 58387: 
            case 58389: 
            case 58391: 
            case 58393: 
            case 58395: 
            case 58397: 
            case 58399: {
                return 101;
            }
            case 150: 
            case 151: 
            case 152: 
            case 153: 
            case 154: 
        }
        return 14;
    }

    public static byte[] getExtensionData(Hashtable hashtable, Integer n2) {
        if (hashtable == null) {
            return null;
        }
        return (byte[])hashtable.get((Object)n2);
    }

    public static short getHashAlgorithmForPRFAlgorithm(int n2) {
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown PRFAlgorithm");
            }
            case 0: {
                throw new IllegalArgumentException("legacy PRF not a valid algorithm");
            }
            case 1: {
                return 4;
            }
            case 2: 
        }
        return 5;
    }

    public static ProtocolVersion getMinimumVersion(int n2) {
        switch (n2) {
            default: {
                return ProtocolVersion.SSLv3;
            }
            case 59: 
            case 60: 
            case 61: 
            case 62: 
            case 63: 
            case 64: 
            case 103: 
            case 104: 
            case 105: 
            case 106: 
            case 107: 
            case 156: 
            case 157: 
            case 158: 
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 168: 
            case 169: 
            case 170: 
            case 171: 
            case 172: 
            case 173: 
            case 186: 
            case 187: 
            case 188: 
            case 189: 
            case 190: 
            case 191: 
            case 192: 
            case 193: 
            case 194: 
            case 195: 
            case 196: 
            case 197: 
            case 49187: 
            case 49188: 
            case 49189: 
            case 49190: 
            case 49191: 
            case 49192: 
            case 49193: 
            case 49194: 
            case 49195: 
            case 49196: 
            case 49197: 
            case 49198: 
            case 49199: 
            case 49200: 
            case 49201: 
            case 49202: 
            case 49266: 
            case 49267: 
            case 49268: 
            case 49269: 
            case 49270: 
            case 49271: 
            case 49272: 
            case 49273: 
            case 49274: 
            case 49275: 
            case 49276: 
            case 49277: 
            case 49278: 
            case 49279: 
            case 49280: 
            case 49281: 
            case 49282: 
            case 49283: 
            case 49284: 
            case 49285: 
            case 49286: 
            case 49287: 
            case 49288: 
            case 49289: 
            case 49290: 
            case 49291: 
            case 49292: 
            case 49293: 
            case 49294: 
            case 49295: 
            case 49296: 
            case 49297: 
            case 49298: 
            case 49299: 
            case 49308: 
            case 49309: 
            case 49310: 
            case 49311: 
            case 49312: 
            case 49313: 
            case 49314: 
            case 49315: 
            case 49316: 
            case 49317: 
            case 49318: 
            case 49319: 
            case 49320: 
            case 49321: 
            case 49322: 
            case 49323: 
            case 49324: 
            case 49325: 
            case 49326: 
            case 49327: 
            case 52243: 
            case 52244: 
            case 52245: 
        }
        return ProtocolVersion.TLSv12;
    }

    public static ASN1ObjectIdentifier getOIDForHashAlgorithm(short s2) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 1: {
                return PKCSObjectIdentifiers.md5;
            }
            case 2: {
                return X509ObjectIdentifiers.id_SHA1;
            }
            case 3: {
                return NISTObjectIdentifiers.id_sha224;
            }
            case 4: {
                return NISTObjectIdentifiers.id_sha256;
            }
            case 5: {
                return NISTObjectIdentifiers.id_sha384;
            }
            case 6: 
        }
        return NISTObjectIdentifiers.id_sha512;
    }

    public static Vector getSignatureAlgorithmsExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_signature_algorithms);
        if (arrby == null) {
            return null;
        }
        return TlsUtils.readSignatureAlgorithmsExtension(arrby);
    }

    public static SignatureAndHashAlgorithm getSignatureAndHashAlgorithm(TlsContext tlsContext, TlsSignerCredentials tlsSignerCredentials) {
        boolean bl = TlsUtils.isTLSv12(tlsContext);
        SignatureAndHashAlgorithm signatureAndHashAlgorithm = null;
        if (bl && (signatureAndHashAlgorithm = tlsSignerCredentials.getSignatureAndHashAlgorithm()) == null) {
            throw new TlsFatalAlert(80);
        }
        return signatureAndHashAlgorithm;
    }

    public static boolean hasExpectedEmptyExtensionData(Hashtable hashtable, Integer n2, short s2) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, n2);
        if (arrby == null) {
            return false;
        }
        if (arrby.length != 0) {
            throw new TlsFatalAlert(s2);
        }
        return true;
    }

    public static boolean hasSigningCapability(short s2) {
        switch (s2) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 64: 
        }
        return true;
    }

    static void hmac_hash(Digest digest, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        HMac hMac = new HMac(digest);
        hMac.init(new KeyParameter(arrby));
        int n2 = digest.getDigestSize();
        int n3 = (-1 + (n2 + arrby3.length)) / n2;
        byte[] arrby4 = new byte[hMac.getMacSize()];
        byte[] arrby5 = new byte[hMac.getMacSize()];
        byte[] arrby6 = arrby2;
        for (int i2 = 0; i2 < n3; ++i2) {
            hMac.update(arrby6, 0, arrby6.length);
            hMac.doFinal(arrby4, 0);
            hMac.update(arrby4, 0, arrby4.length);
            hMac.update(arrby2, 0, arrby2.length);
            hMac.doFinal(arrby5, 0);
            System.arraycopy((Object)arrby5, (int)0, (Object)arrby3, (int)(n2 * i2), (int)Math.min((int)n2, (int)(arrby3.length - n2 * i2)));
            arrby6 = arrby4;
        }
    }

    public static TlsSession importSession(byte[] arrby, SessionParameters sessionParameters) {
        return new TlsSessionImpl(arrby, sessionParameters);
    }

    public static boolean isAEADCipherSuite(int n2) {
        return 2 == TlsUtils.getCipherType(n2);
    }

    public static boolean isBlockCipherSuite(int n2) {
        return 1 == TlsUtils.getCipherType(n2);
    }

    public static boolean isSSL(TlsContext tlsContext) {
        return tlsContext.getServerVersion().isSSL();
    }

    public static boolean isSignatureAlgorithmsExtensionAllowed(ProtocolVersion protocolVersion) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }

    public static boolean isStreamCipherSuite(int n2) {
        return TlsUtils.getCipherType(n2) == 0;
    }

    public static boolean isTLSv11(TlsContext tlsContext) {
        return ProtocolVersion.TLSv11.isEqualOrEarlierVersionOf(tlsContext.getServerVersion().getEquivalentTLSVersion());
    }

    public static boolean isTLSv12(TlsContext tlsContext) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(tlsContext.getServerVersion().getEquivalentTLSVersion());
    }

    public static boolean isValidCipherSuiteForVersion(int n2, ProtocolVersion protocolVersion) {
        return TlsUtils.getMinimumVersion(n2).isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }

    public static boolean isValidUint16(int n2) {
        return (65535 & n2) == n2;
    }

    public static boolean isValidUint16(long l2) {
        return (65535L & l2) == l2;
    }

    public static boolean isValidUint24(int n2) {
        return (16777215 & n2) == n2;
    }

    public static boolean isValidUint24(long l2) {
        return (0xFFFFFFL & l2) == l2;
    }

    public static boolean isValidUint32(long l2) {
        return (0xFFFFFFFFL & l2) == l2;
    }

    public static boolean isValidUint48(long l2) {
        return (0xFFFFFFFFFFFFL & l2) == l2;
    }

    public static boolean isValidUint64(long l2) {
        return true;
    }

    public static boolean isValidUint8(int n2) {
        return (n2 & 255) == n2;
    }

    public static boolean isValidUint8(long l2) {
        return (255L & l2) == l2;
    }

    public static boolean isValidUint8(short s2) {
        return (s2 & 255) == s2;
    }

    public static Vector parseSupportedSignatureAlgorithms(boolean bl, InputStream inputStream) {
        int n2 = TlsUtils.readUint16(inputStream);
        if (n2 < 2 || (n2 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        int n3 = n2 / 2;
        Vector vector = new Vector(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = SignatureAndHashAlgorithm.parse(inputStream);
            if (!bl && signatureAndHashAlgorithm.getSignature() == 0) {
                throw new TlsFatalAlert(47);
            }
            vector.addElement((Object)signatureAndHashAlgorithm);
        }
        return vector;
    }

    public static ASN1Primitive readASN1Object(byte[] arrby) {
        ASN1InputStream aSN1InputStream = new ASN1InputStream(arrby);
        ASN1Primitive aSN1Primitive = aSN1InputStream.readObject();
        if (aSN1Primitive == null) {
            throw new TlsFatalAlert(50);
        }
        if (aSN1InputStream.readObject() != null) {
            throw new TlsFatalAlert(50);
        }
        return aSN1Primitive;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] readAllOrNothing(int n2, InputStream inputStream) {
        if (n2 < 1) {
            return EMPTY_BYTES;
        }
        byte[] arrby = new byte[n2];
        int n3 = Streams.readFully((InputStream)inputStream, (byte[])arrby);
        if (n3 == 0) {
            return null;
        }
        if (n3 == n2) return arrby;
        throw new EOFException();
    }

    public static ASN1Primitive readDERObject(byte[] arrby) {
        ASN1Primitive aSN1Primitive = TlsUtils.readASN1Object(arrby);
        if (!Arrays.areEqual((byte[])aSN1Primitive.getEncoded("DER"), (byte[])arrby)) {
            throw new TlsFatalAlert(50);
        }
        return aSN1Primitive;
    }

    public static void readFully(byte[] arrby, InputStream inputStream) {
        int n2 = arrby.length;
        if (n2 > 0 && n2 != Streams.readFully((InputStream)inputStream, (byte[])arrby)) {
            throw new EOFException();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] readFully(int n2, InputStream inputStream) {
        if (n2 < 1) {
            return EMPTY_BYTES;
        }
        byte[] arrby = new byte[n2];
        if (n2 == Streams.readFully((InputStream)inputStream, (byte[])arrby)) return arrby;
        throw new EOFException();
    }

    public static byte[] readOpaque16(InputStream inputStream) {
        return TlsUtils.readFully(TlsUtils.readUint16(inputStream), inputStream);
    }

    public static byte[] readOpaque24(InputStream inputStream) {
        return TlsUtils.readFully(TlsUtils.readUint24(inputStream), inputStream);
    }

    public static byte[] readOpaque8(InputStream inputStream) {
        return TlsUtils.readFully(TlsUtils.readUint8(inputStream), inputStream);
    }

    public static Vector readSignatureAlgorithmsExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        Vector vector = TlsUtils.parseSupportedSignatureAlgorithms(false, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return vector;
    }

    public static int readUint16(InputStream inputStream) {
        int n2 = inputStream.read();
        int n3 = inputStream.read();
        if (n3 < 0) {
            throw new EOFException();
        }
        return n3 | n2 << 8;
    }

    public static int readUint16(byte[] arrby, int n2) {
        return (255 & arrby[n2]) << 8 | 255 & arrby[n2 + 1];
    }

    public static int[] readUint16Array(int n2, InputStream inputStream) {
        int[] arrn = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrn[i2] = TlsUtils.readUint16(inputStream);
        }
        return arrn;
    }

    public static int readUint24(InputStream inputStream) {
        int n2 = inputStream.read();
        int n3 = inputStream.read();
        int n4 = inputStream.read();
        if (n4 < 0) {
            throw new EOFException();
        }
        return n4 | (n2 << 16 | n3 << 8);
    }

    public static int readUint24(byte[] arrby, int n2) {
        int n3 = (255 & arrby[n2]) << 16;
        int n4 = n2 + 1;
        return n3 | (255 & arrby[n4]) << 8 | 255 & arrby[n4 + 1];
    }

    public static long readUint32(InputStream inputStream) {
        int n2 = inputStream.read();
        int n3 = inputStream.read();
        int n4 = inputStream.read();
        int n5 = inputStream.read();
        if (n5 < 0) {
            throw new EOFException();
        }
        return 0xFFFFFFFFL & (long)(n5 | (n2 << 2 | n3 << 16 | n4 << 8));
    }

    public static long readUint32(byte[] arrby, int n2) {
        int n3 = (255 & arrby[n2]) << 24;
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n4]) << 16;
        int n6 = n4 + 1;
        return 0xFFFFFFFFL & (long)(n5 | (255 & arrby[n6]) << 8 | 255 & arrby[n6 + 1]);
    }

    public static long readUint48(InputStream inputStream) {
        int n2 = TlsUtils.readUint24(inputStream);
        int n3 = TlsUtils.readUint24(inputStream);
        return (0xFFFFFFFFL & (long)n2) << 24 | 0xFFFFFFFFL & (long)n3;
    }

    public static long readUint48(byte[] arrby, int n2) {
        int n3 = TlsUtils.readUint24(arrby, n2);
        int n4 = TlsUtils.readUint24(arrby, n2 + 3);
        return (0xFFFFFFFFL & (long)n3) << 24 | 0xFFFFFFFFL & (long)n4;
    }

    public static short readUint8(InputStream inputStream) {
        int n2 = inputStream.read();
        if (n2 < 0) {
            throw new EOFException();
        }
        return (short)n2;
    }

    public static short readUint8(byte[] arrby, int n2) {
        return arrby[n2];
    }

    public static short[] readUint8Array(int n2, InputStream inputStream) {
        short[] arrs = new short[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrs[i2] = TlsUtils.readUint8(inputStream);
        }
        return arrs;
    }

    public static ProtocolVersion readVersion(InputStream inputStream) {
        int n2 = inputStream.read();
        int n3 = inputStream.read();
        if (n3 < 0) {
            throw new EOFException();
        }
        return ProtocolVersion.get(n2, n3);
    }

    public static ProtocolVersion readVersion(byte[] arrby, int n2) {
        return ProtocolVersion.get(255 & arrby[n2], 255 & arrby[n2 + 1]);
    }

    public static int readVersionRaw(InputStream inputStream) {
        int n2 = inputStream.read();
        int n3 = inputStream.read();
        if (n3 < 0) {
            throw new EOFException();
        }
        return n3 | n2 << 8;
    }

    public static int readVersionRaw(byte[] arrby, int n2) {
        return arrby[n2] << 8 | arrby[n2 + 1];
    }

    static void trackHashAlgorithms(TlsHandshakeHash tlsHandshakeHash, Vector vector) {
        if (vector != null) {
            for (int i2 = 0; i2 < vector.size(); ++i2) {
                tlsHandshakeHash.trackHashAlgorithm(((SignatureAndHashAlgorithm)vector.elementAt(i2)).getHash());
            }
        }
    }

    static void validateKeyUsage(org.bouncycastle.asn1.x509.Certificate certificate, int n2) {
        KeyUsage keyUsage;
        Extensions extensions = certificate.getTBSCertificate().getExtensions();
        if (extensions != null && (keyUsage = KeyUsage.fromExtensions(extensions)) != null && (n2 & (255 & keyUsage.getBytes()[0])) != n2) {
            throw new TlsFatalAlert(46);
        }
    }

    private static Vector vectorOfOne(Object object) {
        Vector vector = new Vector(1);
        vector.addElement(object);
        return vector;
    }

    public static void writeGMTUnixTime(byte[] arrby, int n2) {
        int n3 = (int)(System.currentTimeMillis() / 1000L);
        arrby[n2] = (byte)(n3 >>> 24);
        arrby[n2 + 1] = (byte)(n3 >>> 16);
        arrby[n2 + 2] = (byte)(n3 >>> 8);
        arrby[n2 + 3] = (byte)n3;
    }

    public static void writeOpaque16(byte[] arrby, OutputStream outputStream) {
        TlsUtils.checkUint16(arrby.length);
        TlsUtils.writeUint16(arrby.length, outputStream);
        outputStream.write(arrby);
    }

    public static void writeOpaque24(byte[] arrby, OutputStream outputStream) {
        TlsUtils.checkUint24(arrby.length);
        TlsUtils.writeUint24(arrby.length, outputStream);
        outputStream.write(arrby);
    }

    public static void writeOpaque8(byte[] arrby, OutputStream outputStream) {
        TlsUtils.checkUint8(arrby.length);
        TlsUtils.writeUint8(arrby.length, outputStream);
        outputStream.write(arrby);
    }

    public static void writeUint16(int n2, OutputStream outputStream) {
        outputStream.write(n2 >>> 8);
        outputStream.write(n2);
    }

    public static void writeUint16(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)(n2 >>> 8);
        arrby[n3 + 1] = (byte)n2;
    }

    public static void writeUint16Array(int[] arrn, OutputStream outputStream) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            TlsUtils.writeUint16(arrn[i2], outputStream);
        }
    }

    public static void writeUint16Array(int[] arrn, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            TlsUtils.writeUint16(arrn[i2], arrby, n2);
            n2 += 2;
        }
    }

    public static void writeUint16ArrayWithUint16Length(int[] arrn, OutputStream outputStream) {
        int n2 = 2 * arrn.length;
        TlsUtils.checkUint16(n2);
        TlsUtils.writeUint16(n2, outputStream);
        TlsUtils.writeUint16Array(arrn, outputStream);
    }

    public static void writeUint16ArrayWithUint16Length(int[] arrn, byte[] arrby, int n2) {
        int n3 = 2 * arrn.length;
        TlsUtils.checkUint16(n3);
        TlsUtils.writeUint16(n3, arrby, n2);
        TlsUtils.writeUint16Array(arrn, arrby, n2 + 2);
    }

    public static void writeUint24(int n2, OutputStream outputStream) {
        outputStream.write((int)((byte)(n2 >>> 16)));
        outputStream.write((int)((byte)(n2 >>> 8)));
        outputStream.write((int)((byte)n2));
    }

    public static void writeUint24(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)(n2 >>> 16);
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3 + 2] = (byte)n2;
    }

    public static void writeUint32(long l2, OutputStream outputStream) {
        outputStream.write((int)((byte)(l2 >>> 24)));
        outputStream.write((int)((byte)(l2 >>> 16)));
        outputStream.write((int)((byte)(l2 >>> 8)));
        outputStream.write((int)((byte)l2));
    }

    public static void writeUint32(long l2, byte[] arrby, int n2) {
        arrby[n2] = (byte)(l2 >>> 24);
        arrby[n2 + 1] = (byte)(l2 >>> 16);
        arrby[n2 + 2] = (byte)(l2 >>> 8);
        arrby[n2 + 3] = (byte)l2;
    }

    public static void writeUint48(long l2, OutputStream outputStream) {
        outputStream.write((int)((byte)(l2 >>> 40)));
        outputStream.write((int)((byte)(l2 >>> 32)));
        outputStream.write((int)((byte)(l2 >>> 24)));
        outputStream.write((int)((byte)(l2 >>> 16)));
        outputStream.write((int)((byte)(l2 >>> 8)));
        outputStream.write((int)((byte)l2));
    }

    public static void writeUint48(long l2, byte[] arrby, int n2) {
        arrby[n2] = (byte)(l2 >>> 40);
        arrby[n2 + 1] = (byte)(l2 >>> 32);
        arrby[n2 + 2] = (byte)(l2 >>> 24);
        arrby[n2 + 3] = (byte)(l2 >>> 16);
        arrby[n2 + 4] = (byte)(l2 >>> 8);
        arrby[n2 + 5] = (byte)l2;
    }

    public static void writeUint64(long l2, OutputStream outputStream) {
        outputStream.write((int)((byte)(l2 >>> 56)));
        outputStream.write((int)((byte)(l2 >>> 48)));
        outputStream.write((int)((byte)(l2 >>> 40)));
        outputStream.write((int)((byte)(l2 >>> 32)));
        outputStream.write((int)((byte)(l2 >>> 24)));
        outputStream.write((int)((byte)(l2 >>> 16)));
        outputStream.write((int)((byte)(l2 >>> 8)));
        outputStream.write((int)((byte)l2));
    }

    public static void writeUint64(long l2, byte[] arrby, int n2) {
        arrby[n2] = (byte)(l2 >>> 56);
        arrby[n2 + 1] = (byte)(l2 >>> 48);
        arrby[n2 + 2] = (byte)(l2 >>> 40);
        arrby[n2 + 3] = (byte)(l2 >>> 32);
        arrby[n2 + 4] = (byte)(l2 >>> 24);
        arrby[n2 + 5] = (byte)(l2 >>> 16);
        arrby[n2 + 6] = (byte)(l2 >>> 8);
        arrby[n2 + 7] = (byte)l2;
    }

    public static void writeUint8(int n2, OutputStream outputStream) {
        outputStream.write(n2);
    }

    public static void writeUint8(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
    }

    public static void writeUint8(short s2, OutputStream outputStream) {
        outputStream.write((int)s2);
    }

    public static void writeUint8(short s2, byte[] arrby, int n2) {
        arrby[n2] = (byte)s2;
    }

    public static void writeUint8Array(short[] arrs, OutputStream outputStream) {
        for (int i2 = 0; i2 < arrs.length; ++i2) {
            TlsUtils.writeUint8(arrs[i2], outputStream);
        }
    }

    public static void writeUint8Array(short[] arrs, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrs.length; ++i2) {
            TlsUtils.writeUint8(arrs[i2], arrby, n2);
            ++n2;
        }
    }

    public static void writeUint8ArrayWithUint8Length(short[] arrs, OutputStream outputStream) {
        TlsUtils.checkUint8(arrs.length);
        TlsUtils.writeUint8(arrs.length, outputStream);
        TlsUtils.writeUint8Array(arrs, outputStream);
    }

    public static void writeUint8ArrayWithUint8Length(short[] arrs, byte[] arrby, int n2) {
        TlsUtils.checkUint8(arrs.length);
        TlsUtils.writeUint8(arrs.length, arrby, n2);
        TlsUtils.writeUint8Array(arrs, arrby, n2 + 1);
    }

    public static void writeVersion(ProtocolVersion protocolVersion, OutputStream outputStream) {
        outputStream.write(protocolVersion.getMajorVersion());
        outputStream.write(protocolVersion.getMinorVersion());
    }

    public static void writeVersion(ProtocolVersion protocolVersion, byte[] arrby, int n2) {
        arrby[n2] = (byte)protocolVersion.getMajorVersion();
        arrby[n2 + 1] = (byte)protocolVersion.getMinorVersion();
    }
}

