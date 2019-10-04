/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.teletrust;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface TeleTrusTObjectIdentifiers {
    public static final ASN1ObjectIdentifier brainpoolP160r1;
    public static final ASN1ObjectIdentifier brainpoolP160t1;
    public static final ASN1ObjectIdentifier brainpoolP192r1;
    public static final ASN1ObjectIdentifier brainpoolP192t1;
    public static final ASN1ObjectIdentifier brainpoolP224r1;
    public static final ASN1ObjectIdentifier brainpoolP224t1;
    public static final ASN1ObjectIdentifier brainpoolP256r1;
    public static final ASN1ObjectIdentifier brainpoolP256t1;
    public static final ASN1ObjectIdentifier brainpoolP320r1;
    public static final ASN1ObjectIdentifier brainpoolP320t1;
    public static final ASN1ObjectIdentifier brainpoolP384r1;
    public static final ASN1ObjectIdentifier brainpoolP384t1;
    public static final ASN1ObjectIdentifier brainpoolP512r1;
    public static final ASN1ObjectIdentifier brainpoolP512t1;
    public static final ASN1ObjectIdentifier ecSign;
    public static final ASN1ObjectIdentifier ecSignWithRipemd160;
    public static final ASN1ObjectIdentifier ecSignWithSha1;
    public static final ASN1ObjectIdentifier ecc_brainpool;
    public static final ASN1ObjectIdentifier ellipticCurve;
    public static final ASN1ObjectIdentifier ripemd128;
    public static final ASN1ObjectIdentifier ripemd160;
    public static final ASN1ObjectIdentifier ripemd256;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd128;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd160;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd256;
    public static final ASN1ObjectIdentifier teleTrusTAlgorithm;
    public static final ASN1ObjectIdentifier teleTrusTRSAsignatureAlgorithm;
    public static final ASN1ObjectIdentifier versionOne;

    static {
        teleTrusTAlgorithm = new ASN1ObjectIdentifier("1.3.36.3");
        ripemd160 = teleTrusTAlgorithm.branch("2.1");
        ripemd128 = teleTrusTAlgorithm.branch("2.2");
        ripemd256 = teleTrusTAlgorithm.branch("2.3");
        teleTrusTRSAsignatureAlgorithm = teleTrusTAlgorithm.branch("3.1");
        rsaSignatureWithripemd160 = teleTrusTRSAsignatureAlgorithm.branch("2");
        rsaSignatureWithripemd128 = teleTrusTRSAsignatureAlgorithm.branch("3");
        rsaSignatureWithripemd256 = teleTrusTRSAsignatureAlgorithm.branch("4");
        ecSign = teleTrusTAlgorithm.branch("3.2");
        ecSignWithSha1 = ecSign.branch("1");
        ecSignWithRipemd160 = ecSign.branch("2");
        ecc_brainpool = teleTrusTAlgorithm.branch("3.2.8");
        ellipticCurve = ecc_brainpool.branch("1");
        versionOne = ellipticCurve.branch("1");
        brainpoolP160r1 = versionOne.branch("1");
        brainpoolP160t1 = versionOne.branch("2");
        brainpoolP192r1 = versionOne.branch("3");
        brainpoolP192t1 = versionOne.branch("4");
        brainpoolP224r1 = versionOne.branch("5");
        brainpoolP224t1 = versionOne.branch("6");
        brainpoolP256r1 = versionOne.branch("7");
        brainpoolP256t1 = versionOne.branch("8");
        brainpoolP320r1 = versionOne.branch("9");
        brainpoolP320t1 = versionOne.branch("10");
        brainpoolP384r1 = versionOne.branch("11");
        brainpoolP384t1 = versionOne.branch("12");
        brainpoolP512r1 = versionOne.branch("13");
        brainpoolP512t1 = versionOne.branch("14");
    }
}

