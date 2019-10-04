/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface X9ObjectIdentifiers {
    public static final ASN1ObjectIdentifier ansi_X9_42;
    public static final ASN1ObjectIdentifier ansi_X9_62;
    public static final ASN1ObjectIdentifier c2onb191v4;
    public static final ASN1ObjectIdentifier c2onb191v5;
    public static final ASN1ObjectIdentifier c2onb239v4;
    public static final ASN1ObjectIdentifier c2onb239v5;
    public static final ASN1ObjectIdentifier c2pnb163v1;
    public static final ASN1ObjectIdentifier c2pnb163v2;
    public static final ASN1ObjectIdentifier c2pnb163v3;
    public static final ASN1ObjectIdentifier c2pnb176w1;
    public static final ASN1ObjectIdentifier c2pnb208w1;
    public static final ASN1ObjectIdentifier c2pnb272w1;
    public static final ASN1ObjectIdentifier c2pnb304w1;
    public static final ASN1ObjectIdentifier c2pnb368w1;
    public static final ASN1ObjectIdentifier c2tnb191v1;
    public static final ASN1ObjectIdentifier c2tnb191v2;
    public static final ASN1ObjectIdentifier c2tnb191v3;
    public static final ASN1ObjectIdentifier c2tnb239v1;
    public static final ASN1ObjectIdentifier c2tnb239v2;
    public static final ASN1ObjectIdentifier c2tnb239v3;
    public static final ASN1ObjectIdentifier c2tnb359v1;
    public static final ASN1ObjectIdentifier c2tnb431r1;
    public static final ASN1ObjectIdentifier cTwoCurve;
    public static final ASN1ObjectIdentifier characteristic_two_field;
    public static final ASN1ObjectIdentifier dhEphem;
    public static final ASN1ObjectIdentifier dhHybrid1;
    public static final ASN1ObjectIdentifier dhHybrid2;
    public static final ASN1ObjectIdentifier dhHybridOneFlow;
    public static final ASN1ObjectIdentifier dhOneFlow;
    public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha1kdf_scheme;
    public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha1kdf_scheme;
    public static final ASN1ObjectIdentifier dhStatic;
    public static final ASN1ObjectIdentifier dhpublicnumber;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA1;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA2;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA224;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA256;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA384;
    public static final ASN1ObjectIdentifier ecdsa_with_SHA512;
    public static final ASN1ObjectIdentifier ellipticCurve;
    public static final ASN1ObjectIdentifier gnBasis;
    public static final ASN1ObjectIdentifier id_dsa;
    public static final ASN1ObjectIdentifier id_dsa_with_sha1;
    public static final ASN1ObjectIdentifier id_ecPublicKey;
    public static final ASN1ObjectIdentifier id_ecSigType;
    public static final ASN1ObjectIdentifier id_fieldType;
    public static final ASN1ObjectIdentifier id_publicKeyType;
    public static final ASN1ObjectIdentifier mqv1;
    public static final ASN1ObjectIdentifier mqv2;
    public static final ASN1ObjectIdentifier mqvSinglePass_sha1kdf_scheme;
    public static final ASN1ObjectIdentifier ppBasis;
    public static final ASN1ObjectIdentifier prime192v1;
    public static final ASN1ObjectIdentifier prime192v2;
    public static final ASN1ObjectIdentifier prime192v3;
    public static final ASN1ObjectIdentifier prime239v1;
    public static final ASN1ObjectIdentifier prime239v2;
    public static final ASN1ObjectIdentifier prime239v3;
    public static final ASN1ObjectIdentifier prime256v1;
    public static final ASN1ObjectIdentifier primeCurve;
    public static final ASN1ObjectIdentifier prime_field;
    public static final ASN1ObjectIdentifier tpBasis;
    public static final ASN1ObjectIdentifier x9_42_schemes;
    public static final ASN1ObjectIdentifier x9_63_scheme;

    static {
        ansi_X9_62 = new ASN1ObjectIdentifier("1.2.840.10045");
        id_fieldType = ansi_X9_62.branch("1");
        prime_field = id_fieldType.branch("1");
        characteristic_two_field = id_fieldType.branch("2");
        gnBasis = characteristic_two_field.branch("3.1");
        tpBasis = characteristic_two_field.branch("3.2");
        ppBasis = characteristic_two_field.branch("3.3");
        id_ecSigType = ansi_X9_62.branch("4");
        ecdsa_with_SHA1 = id_ecSigType.branch("1");
        id_publicKeyType = ansi_X9_62.branch("2");
        id_ecPublicKey = id_publicKeyType.branch("1");
        ecdsa_with_SHA2 = id_ecSigType.branch("3");
        ecdsa_with_SHA224 = ecdsa_with_SHA2.branch("1");
        ecdsa_with_SHA256 = ecdsa_with_SHA2.branch("2");
        ecdsa_with_SHA384 = ecdsa_with_SHA2.branch("3");
        ecdsa_with_SHA512 = ecdsa_with_SHA2.branch("4");
        ellipticCurve = ansi_X9_62.branch("3");
        cTwoCurve = ellipticCurve.branch("0");
        c2pnb163v1 = cTwoCurve.branch("1");
        c2pnb163v2 = cTwoCurve.branch("2");
        c2pnb163v3 = cTwoCurve.branch("3");
        c2pnb176w1 = cTwoCurve.branch("4");
        c2tnb191v1 = cTwoCurve.branch("5");
        c2tnb191v2 = cTwoCurve.branch("6");
        c2tnb191v3 = cTwoCurve.branch("7");
        c2onb191v4 = cTwoCurve.branch("8");
        c2onb191v5 = cTwoCurve.branch("9");
        c2pnb208w1 = cTwoCurve.branch("10");
        c2tnb239v1 = cTwoCurve.branch("11");
        c2tnb239v2 = cTwoCurve.branch("12");
        c2tnb239v3 = cTwoCurve.branch("13");
        c2onb239v4 = cTwoCurve.branch("14");
        c2onb239v5 = cTwoCurve.branch("15");
        c2pnb272w1 = cTwoCurve.branch("16");
        c2pnb304w1 = cTwoCurve.branch("17");
        c2tnb359v1 = cTwoCurve.branch("18");
        c2pnb368w1 = cTwoCurve.branch("19");
        c2tnb431r1 = cTwoCurve.branch("20");
        primeCurve = ellipticCurve.branch("1");
        prime192v1 = primeCurve.branch("1");
        prime192v2 = primeCurve.branch("2");
        prime192v3 = primeCurve.branch("3");
        prime239v1 = primeCurve.branch("4");
        prime239v2 = primeCurve.branch("5");
        prime239v3 = primeCurve.branch("6");
        prime256v1 = primeCurve.branch("7");
        id_dsa = new ASN1ObjectIdentifier("1.2.840.10040.4.1");
        id_dsa_with_sha1 = new ASN1ObjectIdentifier("1.2.840.10040.4.3");
        x9_63_scheme = new ASN1ObjectIdentifier("1.3.133.16.840.63.0");
        dhSinglePass_stdDH_sha1kdf_scheme = x9_63_scheme.branch("2");
        dhSinglePass_cofactorDH_sha1kdf_scheme = x9_63_scheme.branch("3");
        mqvSinglePass_sha1kdf_scheme = x9_63_scheme.branch("16");
        ansi_X9_42 = new ASN1ObjectIdentifier("1.2.840.10046");
        dhpublicnumber = ansi_X9_42.branch("2.1");
        x9_42_schemes = ansi_X9_42.branch("3");
        dhStatic = x9_42_schemes.branch("1");
        dhEphem = x9_42_schemes.branch("2");
        dhOneFlow = x9_42_schemes.branch("3");
        dhHybrid1 = x9_42_schemes.branch("4");
        dhHybrid2 = x9_42_schemes.branch("5");
        dhHybridOneFlow = x9_42_schemes.branch("6");
        mqv2 = x9_42_schemes.branch("7");
        mqv1 = x9_42_schemes.branch("8");
    }
}

