/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Hashtable
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECCurve$F2m
 *  org.bouncycastle.math.ec.ECCurve$Fp
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.field.FiniteField
 *  org.bouncycastle.math.field.Polynomial
 *  org.bouncycastle.math.field.PolynomialExtensionField
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.tls.ECBasisType;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.Polynomial;
import org.bouncycastle.math.field.PolynomialExtensionField;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public class TlsECCUtils {
    private static final String[] CURVE_NAMES;
    public static final Integer EXT_ec_point_formats;
    public static final Integer EXT_elliptic_curves;

    static {
        EXT_elliptic_curves = Integers.valueOf((int)10);
        EXT_ec_point_formats = Integers.valueOf((int)11);
        CURVE_NAMES = new String[]{"sect163k1", "sect163r1", "sect163r2", "sect193r1", "sect193r2", "sect233k1", "sect233r1", "sect239k1", "sect283k1", "sect283r1", "sect409k1", "sect409r1", "sect571k1", "sect571r1", "secp160k1", "secp160r1", "secp160r2", "secp192k1", "secp192r1", "secp224k1", "secp224r1", "secp256k1", "secp256r1", "secp384r1", "secp521r1", "brainpoolP256r1", "brainpoolP384r1", "brainpoolP512r1"};
    }

    public static void addSupportedEllipticCurvesExtension(Hashtable hashtable, int[] arrn) {
        hashtable.put((Object)EXT_elliptic_curves, (Object)TlsECCUtils.createSupportedEllipticCurvesExtension(arrn));
    }

    public static void addSupportedPointFormatsExtension(Hashtable hashtable, short[] arrs) {
        hashtable.put((Object)EXT_ec_point_formats, (Object)TlsECCUtils.createSupportedPointFormatsExtension(arrs));
    }

    public static boolean areOnSameCurve(ECDomainParameters eCDomainParameters, ECDomainParameters eCDomainParameters2) {
        return eCDomainParameters.getCurve().equals(eCDomainParameters2.getCurve()) && eCDomainParameters.getG().equals(eCDomainParameters2.getG()) && eCDomainParameters.getN().equals((Object)eCDomainParameters2.getN()) && eCDomainParameters.getH().equals((Object)eCDomainParameters2.getH());
    }

    public static byte[] calculateECDHBasicAgreement(ECPublicKeyParameters eCPublicKeyParameters, ECPrivateKeyParameters eCPrivateKeyParameters) {
        ECDHBasicAgreement eCDHBasicAgreement = new ECDHBasicAgreement();
        eCDHBasicAgreement.init(eCPrivateKeyParameters);
        BigInteger bigInteger = eCDHBasicAgreement.calculateAgreement(eCPublicKeyParameters);
        return BigIntegers.asUnsignedByteArray((int)eCDHBasicAgreement.getFieldSize(), (BigInteger)bigInteger);
    }

    private static void checkNamedCurve(int[] arrn, int n2) {
        if (arrn != null && !Arrays.contains((int[])arrn, (int)n2)) {
            throw new TlsFatalAlert(47);
        }
    }

    public static boolean containsECCCipherSuites(int[] arrn) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = arrn.length;
                    bl = false;
                    if (n2 >= n3) break block3;
                    if (!TlsECCUtils.isECCCipherSuite(arrn[n2])) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    public static byte[] createSupportedEllipticCurvesExtension(int[] arrn) {
        if (arrn == null || arrn.length < 1) {
            throw new TlsFatalAlert(80);
        }
        return TlsUtils.encodeUint16ArrayWithUint16Length(arrn);
    }

    public static byte[] createSupportedPointFormatsExtension(short[] arrs) {
        if (arrs == null || !Arrays.contains((short[])arrs, (short)0)) {
            arrs = Arrays.append((short[])arrs, (short)0);
        }
        return TlsUtils.encodeUint8ArrayWithUint8Length(arrs);
    }

    public static BigInteger deserializeECFieldElement(int n2, byte[] arrby) {
        int n3 = (n2 + 7) / 8;
        if (arrby.length != n3) {
            throw new TlsFatalAlert(50);
        }
        return new BigInteger(1, arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ECPoint deserializeECPoint(short[] arrs, ECCurve eCCurve, byte[] arrby) {
        short s2 = 1;
        if (arrby == null || arrby.length < s2) {
            throw new TlsFatalAlert(47);
        }
        switch (arrby[0]) {
            default: {
                throw new TlsFatalAlert(47);
            }
            case 2: 
            case 3: {
                if (ECAlgorithms.isF2mCurve((ECCurve)eCCurve)) {
                    s2 = 2;
                    break;
                }
                if (ECAlgorithms.isFpCurve((ECCurve)eCCurve)) break;
                throw new TlsFatalAlert(47);
            }
            case 4: {
                s2 = 0;
            }
        }
        if (!(s2 == 0 || arrs != null && Arrays.contains((short[])arrs, (short)s2))) {
            throw new TlsFatalAlert(47);
        }
        return eCCurve.decodePoint(arrby);
    }

    public static ECPublicKeyParameters deserializeECPublicKey(short[] arrs, ECDomainParameters eCDomainParameters, byte[] arrby) {
        try {
            ECPublicKeyParameters eCPublicKeyParameters = new ECPublicKeyParameters(TlsECCUtils.deserializeECPoint(arrs, eCDomainParameters.getCurve(), arrby), eCDomainParameters);
            return eCPublicKeyParameters;
        }
        catch (RuntimeException runtimeException) {
            throw new TlsFatalAlert(47, runtimeException);
        }
    }

    public static AsymmetricCipherKeyPair generateECKeyPair(SecureRandom secureRandom, ECDomainParameters eCDomainParameters) {
        ECKeyPairGenerator eCKeyPairGenerator = new ECKeyPairGenerator();
        eCKeyPairGenerator.init(new ECKeyGenerationParameters(eCDomainParameters, secureRandom));
        return eCKeyPairGenerator.generateKeyPair();
    }

    public static ECPrivateKeyParameters generateEphemeralClientKeyExchange(SecureRandom secureRandom, short[] arrs, ECDomainParameters eCDomainParameters, OutputStream outputStream) {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = TlsECCUtils.generateECKeyPair(secureRandom, eCDomainParameters);
        TlsECCUtils.writeECPoint(arrs, ((ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic()).getQ(), outputStream);
        return (ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
    }

    /*
     * Enabled aggressive block sorting
     */
    static ECPrivateKeyParameters generateEphemeralServerKeyExchange(SecureRandom secureRandom, int[] arrn, short[] arrs, OutputStream outputStream) {
        ECDomainParameters eCDomainParameters;
        int n2;
        block11 : {
            if (arrn == null) {
                n2 = 23;
            } else {
                for (int i2 = 0; i2 < arrn.length; ++i2) {
                    n2 = arrn[i2];
                    if (!NamedCurve.isValid(n2) || !TlsECCUtils.isSupportedNamedCurve(n2)) {
                        continue;
                    }
                    break block11;
                }
                n2 = -1;
            }
        }
        if (n2 >= 0) {
            eCDomainParameters = TlsECCUtils.getParametersForNamedCurve(n2);
        } else if (Arrays.contains((int[])arrn, (int)65281)) {
            eCDomainParameters = TlsECCUtils.getParametersForNamedCurve(23);
        } else {
            boolean bl = Arrays.contains((int[])arrn, (int)65282);
            eCDomainParameters = null;
            if (bl) {
                eCDomainParameters = TlsECCUtils.getParametersForNamedCurve(10);
            }
        }
        if (eCDomainParameters == null) {
            throw new TlsFatalAlert(80);
        }
        if (n2 < 0) {
            TlsECCUtils.writeExplicitECParameters(arrs, eCDomainParameters, outputStream);
            return TlsECCUtils.generateEphemeralClientKeyExchange(secureRandom, arrs, eCDomainParameters, outputStream);
        }
        TlsECCUtils.writeNamedECParameters(n2, outputStream);
        return TlsECCUtils.generateEphemeralClientKeyExchange(secureRandom, arrs, eCDomainParameters, outputStream);
    }

    public static String getNameOfNamedCurve(int n2) {
        if (TlsECCUtils.isSupportedNamedCurve(n2)) {
            return CURVE_NAMES[n2 - 1];
        }
        return null;
    }

    public static ECDomainParameters getParametersForNamedCurve(int n2) {
        String string = TlsECCUtils.getNameOfNamedCurve(n2);
        if (string == null) {
            return null;
        }
        X9ECParameters x9ECParameters = CustomNamedCurves.getByName(string);
        if (x9ECParameters == null && (x9ECParameters = ECNamedCurveTable.getByName(string)) == null) {
            return null;
        }
        X9ECParameters x9ECParameters2 = x9ECParameters;
        return new ECDomainParameters(x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH(), x9ECParameters2.getSeed());
    }

    public static int[] getSupportedEllipticCurvesExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_elliptic_curves);
        if (arrby == null) {
            return null;
        }
        return TlsECCUtils.readSupportedEllipticCurvesExtension(arrby);
    }

    public static short[] getSupportedPointFormatsExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_ec_point_formats);
        if (arrby == null) {
            return null;
        }
        return TlsECCUtils.readSupportedPointFormatsExtension(arrby);
    }

    public static boolean hasAnySupportedNamedCurves() {
        return CURVE_NAMES.length > 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isCompressionPreferred(short[] arrs, short s2) {
        if (arrs != null) {
            short s3;
            for (int i2 = 0; i2 < arrs.length && (s3 = arrs[i2]) != 0; ++i2) {
                if (s3 != s2) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isECCCipherSuite(int n2) {
        switch (n2) {
            default: {
                return false;
            }
            case 49153: 
            case 49154: 
            case 49155: 
            case 49156: 
            case 49157: 
            case 49158: 
            case 49159: 
            case 49160: 
            case 49161: 
            case 49162: 
            case 49163: 
            case 49164: 
            case 49165: 
            case 49166: 
            case 49167: 
            case 49168: 
            case 49169: 
            case 49170: 
            case 49171: 
            case 49172: 
            case 49173: 
            case 49174: 
            case 49175: 
            case 49176: 
            case 49177: 
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
            case 49203: 
            case 49204: 
            case 49205: 
            case 49206: 
            case 49207: 
            case 49208: 
            case 49209: 
            case 49210: 
            case 49211: 
            case 49266: 
            case 49267: 
            case 49268: 
            case 49269: 
            case 49270: 
            case 49271: 
            case 49272: 
            case 49273: 
            case 49286: 
            case 49287: 
            case 49288: 
            case 49289: 
            case 49290: 
            case 49291: 
            case 49292: 
            case 49293: 
            case 49306: 
            case 49307: 
            case 49324: 
            case 49325: 
            case 49326: 
            case 49327: 
            case 52243: 
            case 52244: 
            case 58386: 
            case 58387: 
            case 58388: 
            case 58389: 
            case 58392: 
            case 58393: 
        }
        return true;
    }

    public static boolean isSupportedNamedCurve(int n2) {
        return n2 > 0 && n2 <= CURVE_NAMES.length;
    }

    public static int readECExponent(int n2, InputStream inputStream) {
        int n3;
        BigInteger bigInteger = TlsECCUtils.readECParameter(inputStream);
        if (bigInteger.bitLength() < 32 && (n3 = bigInteger.intValue()) > 0 && n3 < n2) {
            return n3;
        }
        throw new TlsFatalAlert(47);
    }

    public static BigInteger readECFieldElement(int n2, InputStream inputStream) {
        return TlsECCUtils.deserializeECFieldElement(n2, TlsUtils.readOpaque8(inputStream));
    }

    public static BigInteger readECParameter(InputStream inputStream) {
        return new BigInteger(1, TlsUtils.readOpaque8(inputStream));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ECDomainParameters readECParameters(int[] arrn, short[] arrs, InputStream inputStream) {
        try {
            switch (TlsUtils.readUint8(inputStream)) {
                default: {
                    throw new TlsFatalAlert(47);
                }
                case 1: {
                    TlsECCUtils.checkNamedCurve(arrn, 65281);
                    BigInteger bigInteger = TlsECCUtils.readECParameter(inputStream);
                    BigInteger bigInteger2 = TlsECCUtils.readECFieldElement(bigInteger.bitLength(), inputStream);
                    BigInteger bigInteger3 = TlsECCUtils.readECFieldElement(bigInteger.bitLength(), inputStream);
                    byte[] arrby = TlsUtils.readOpaque8(inputStream);
                    BigInteger bigInteger4 = TlsECCUtils.readECParameter(inputStream);
                    BigInteger bigInteger5 = TlsECCUtils.readECParameter(inputStream);
                    ECCurve.Fp fp = new ECCurve.Fp(bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5);
                    return new ECDomainParameters((ECCurve)fp, TlsECCUtils.deserializeECPoint(arrs, (ECCurve)fp, arrby), bigInteger4, bigInteger5);
                }
                case 2: {
                    ECCurve.F2m f2m;
                    TlsECCUtils.checkNamedCurve(arrn, 65282);
                    int n2 = TlsUtils.readUint16(inputStream);
                    short s2 = TlsUtils.readUint8(inputStream);
                    if (!ECBasisType.isValid(s2)) {
                        throw new TlsFatalAlert(47);
                    }
                    int n3 = TlsECCUtils.readECExponent(n2, inputStream);
                    int n4 = -1;
                    int n5 = -1;
                    if (s2 == 2) {
                        n4 = TlsECCUtils.readECExponent(n2, inputStream);
                        n5 = TlsECCUtils.readECExponent(n2, inputStream);
                    }
                    BigInteger bigInteger = TlsECCUtils.readECFieldElement(n2, inputStream);
                    BigInteger bigInteger6 = TlsECCUtils.readECFieldElement(n2, inputStream);
                    byte[] arrby = TlsUtils.readOpaque8(inputStream);
                    BigInteger bigInteger7 = TlsECCUtils.readECParameter(inputStream);
                    BigInteger bigInteger8 = TlsECCUtils.readECParameter(inputStream);
                    if (s2 == 2) {
                        f2m = new ECCurve.F2m(n2, n3, n4, n5, bigInteger, bigInteger6, bigInteger7, bigInteger8);
                        return new ECDomainParameters((ECCurve)f2m, TlsECCUtils.deserializeECPoint(arrs, (ECCurve)f2m, arrby), bigInteger7, bigInteger8);
                    }
                    f2m = new ECCurve.F2m(n2, n3, bigInteger, bigInteger6, bigInteger7, bigInteger8);
                    return new ECDomainParameters((ECCurve)f2m, TlsECCUtils.deserializeECPoint(arrs, (ECCurve)f2m, arrby), bigInteger7, bigInteger8);
                }
                case 3: 
            }
            int n6 = TlsUtils.readUint16(inputStream);
            if (!NamedCurve.refersToASpecificNamedCurve(n6)) {
                throw new TlsFatalAlert(47);
            }
            TlsECCUtils.checkNamedCurve(arrn, n6);
            return TlsECCUtils.getParametersForNamedCurve(n6);
        }
        catch (RuntimeException runtimeException) {
            throw new TlsFatalAlert(47, runtimeException);
        }
    }

    public static int[] readSupportedEllipticCurvesExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        int n2 = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (n2 < 2 || (n2 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        int[] arrn = TlsUtils.readUint16Array(n2 / 2, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return arrn;
    }

    public static short[] readSupportedPointFormatsExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        short s2 = TlsUtils.readUint8((InputStream)byteArrayInputStream);
        if (s2 < 1) {
            throw new TlsFatalAlert(50);
        }
        short[] arrs = TlsUtils.readUint8Array(s2, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!Arrays.contains((short[])arrs, (short)0)) {
            throw new TlsFatalAlert(47);
        }
        return arrs;
    }

    public static byte[] serializeECFieldElement(int n2, BigInteger bigInteger) {
        return BigIntegers.asUnsignedByteArray((int)((n2 + 7) / 8), (BigInteger)bigInteger);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] serializeECPoint(short[] arrs, ECPoint eCPoint) {
        boolean bl;
        ECCurve eCCurve = eCPoint.getCurve();
        if (ECAlgorithms.isFpCurve((ECCurve)eCCurve)) {
            bl = TlsECCUtils.isCompressionPreferred(arrs, (short)1);
            return eCPoint.getEncoded(bl);
        }
        boolean bl2 = ECAlgorithms.isF2mCurve((ECCurve)eCCurve);
        bl = false;
        if (!bl2) return eCPoint.getEncoded(bl);
        bl = TlsECCUtils.isCompressionPreferred(arrs, (short)2);
        return eCPoint.getEncoded(bl);
    }

    public static byte[] serializeECPublicKey(short[] arrs, ECPublicKeyParameters eCPublicKeyParameters) {
        return TlsECCUtils.serializeECPoint(arrs, eCPublicKeyParameters.getQ());
    }

    public static ECPublicKeyParameters validateECPublicKey(ECPublicKeyParameters eCPublicKeyParameters) {
        return eCPublicKeyParameters;
    }

    public static void writeECExponent(int n2, OutputStream outputStream) {
        TlsECCUtils.writeECParameter(BigInteger.valueOf((long)n2), outputStream);
    }

    public static void writeECFieldElement(int n2, BigInteger bigInteger, OutputStream outputStream) {
        TlsUtils.writeOpaque8(TlsECCUtils.serializeECFieldElement(n2, bigInteger), outputStream);
    }

    public static void writeECFieldElement(ECFieldElement eCFieldElement, OutputStream outputStream) {
        TlsUtils.writeOpaque8(eCFieldElement.getEncoded(), outputStream);
    }

    public static void writeECParameter(BigInteger bigInteger, OutputStream outputStream) {
        TlsUtils.writeOpaque8(BigIntegers.asUnsignedByteArray((BigInteger)bigInteger), outputStream);
    }

    public static void writeECPoint(short[] arrs, ECPoint eCPoint, OutputStream outputStream) {
        TlsUtils.writeOpaque8(TlsECCUtils.serializeECPoint(arrs, eCPoint), outputStream);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void writeExplicitECParameters(short[] arrs, ECDomainParameters eCDomainParameters, OutputStream outputStream) {
        ECCurve eCCurve = eCDomainParameters.getCurve();
        if (ECAlgorithms.isFpCurve((ECCurve)eCCurve)) {
            TlsUtils.writeUint8((short)1, outputStream);
            TlsECCUtils.writeECParameter(eCCurve.getField().getCharacteristic(), outputStream);
        } else {
            if (!ECAlgorithms.isF2mCurve((ECCurve)eCCurve)) {
                throw new IllegalArgumentException("'ecParameters' not a known curve type");
            }
            int[] arrn = ((PolynomialExtensionField)eCCurve.getField()).getMinimalPolynomial().getExponentsPresent();
            TlsUtils.writeUint8((short)2, outputStream);
            int n2 = arrn[-1 + arrn.length];
            TlsUtils.checkUint16(n2);
            TlsUtils.writeUint16(n2, outputStream);
            if (arrn.length == 3) {
                TlsUtils.writeUint8((short)1, outputStream);
                TlsECCUtils.writeECExponent(arrn[1], outputStream);
            } else {
                if (arrn.length != 5) {
                    throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
                }
                TlsUtils.writeUint8((short)2, outputStream);
                TlsECCUtils.writeECExponent(arrn[1], outputStream);
                TlsECCUtils.writeECExponent(arrn[2], outputStream);
                TlsECCUtils.writeECExponent(arrn[3], outputStream);
            }
        }
        TlsECCUtils.writeECFieldElement(eCCurve.getA(), outputStream);
        TlsECCUtils.writeECFieldElement(eCCurve.getB(), outputStream);
        TlsUtils.writeOpaque8(TlsECCUtils.serializeECPoint(arrs, eCDomainParameters.getG()), outputStream);
        TlsECCUtils.writeECParameter(eCDomainParameters.getN(), outputStream);
        TlsECCUtils.writeECParameter(eCDomainParameters.getH(), outputStream);
    }

    public static void writeNamedECParameters(int n2, OutputStream outputStream) {
        if (!NamedCurve.refersToASpecificNamedCurve(n2)) {
            throw new TlsFatalAlert(80);
        }
        TlsUtils.writeUint8((short)3, outputStream);
        TlsUtils.checkUint16(n2);
        TlsUtils.writeUint16(n2, outputStream);
    }
}

