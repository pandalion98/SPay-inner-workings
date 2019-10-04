/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface X509ObjectIdentifiers {
    public static final ASN1ObjectIdentifier commonName = new ASN1ObjectIdentifier("2.5.4.3");
    public static final ASN1ObjectIdentifier countryName = new ASN1ObjectIdentifier("2.5.4.6");
    public static final ASN1ObjectIdentifier crlAccessMethod;
    public static final ASN1ObjectIdentifier id_SHA1;
    public static final ASN1ObjectIdentifier id_ad;
    public static final ASN1ObjectIdentifier id_ad_caIssuers;
    public static final ASN1ObjectIdentifier id_ad_ocsp;
    public static final ASN1ObjectIdentifier id_at_name;
    public static final ASN1ObjectIdentifier id_at_telephoneNumber;
    public static final ASN1ObjectIdentifier id_ce;
    public static final ASN1ObjectIdentifier id_ea_rsa;
    public static final ASN1ObjectIdentifier id_pe;
    public static final ASN1ObjectIdentifier id_pkix;
    public static final ASN1ObjectIdentifier localityName;
    public static final ASN1ObjectIdentifier ocspAccessMethod;
    public static final ASN1ObjectIdentifier organization;
    public static final ASN1ObjectIdentifier organizationalUnitName;
    public static final ASN1ObjectIdentifier ripemd160;
    public static final ASN1ObjectIdentifier ripemd160WithRSAEncryption;
    public static final ASN1ObjectIdentifier stateOrProvinceName;

    static {
        localityName = new ASN1ObjectIdentifier("2.5.4.7");
        stateOrProvinceName = new ASN1ObjectIdentifier("2.5.4.8");
        organization = new ASN1ObjectIdentifier("2.5.4.10");
        organizationalUnitName = new ASN1ObjectIdentifier("2.5.4.11");
        id_at_telephoneNumber = new ASN1ObjectIdentifier("2.5.4.20");
        id_at_name = new ASN1ObjectIdentifier("2.5.4.41");
        id_SHA1 = new ASN1ObjectIdentifier("1.3.14.3.2.26");
        ripemd160 = new ASN1ObjectIdentifier("1.3.36.3.2.1");
        ripemd160WithRSAEncryption = new ASN1ObjectIdentifier("1.3.36.3.3.1.2");
        id_ea_rsa = new ASN1ObjectIdentifier("2.5.8.1.1");
        id_pkix = new ASN1ObjectIdentifier("1.3.6.1.5.5.7");
        id_pe = id_pkix.branch("1");
        id_ce = new ASN1ObjectIdentifier("2.5.29");
        id_ad = id_pkix.branch("48");
        id_ad_caIssuers = id_ad.branch("2");
        ocspAccessMethod = id_ad_ocsp = id_ad.branch("1");
        crlAccessMethod = id_ad_caIssuers;
    }
}

