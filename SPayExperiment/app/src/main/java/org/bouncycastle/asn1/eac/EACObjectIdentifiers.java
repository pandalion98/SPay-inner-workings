/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.eac;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface EACObjectIdentifiers {
    public static final ASN1ObjectIdentifier bsi_de = new ASN1ObjectIdentifier("0.4.0.127.0.7");
    public static final ASN1ObjectIdentifier id_CA;
    public static final ASN1ObjectIdentifier id_CA_DH;
    public static final ASN1ObjectIdentifier id_CA_DH_3DES_CBC_CBC;
    public static final ASN1ObjectIdentifier id_CA_ECDH;
    public static final ASN1ObjectIdentifier id_CA_ECDH_3DES_CBC_CBC;
    public static final ASN1ObjectIdentifier id_EAC_ePassport;
    public static final ASN1ObjectIdentifier id_PK;
    public static final ASN1ObjectIdentifier id_PK_DH;
    public static final ASN1ObjectIdentifier id_PK_ECDH;
    public static final ASN1ObjectIdentifier id_TA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_224;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_384;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_512;
    public static final ASN1ObjectIdentifier id_TA_RSA;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_512;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_512;

    static {
        id_PK = bsi_de.branch("2.2.1");
        id_PK_DH = id_PK.branch("1");
        id_PK_ECDH = id_PK.branch("2");
        id_CA = bsi_de.branch("2.2.3");
        id_CA_DH = id_CA.branch("1");
        id_CA_DH_3DES_CBC_CBC = id_CA_DH.branch("1");
        id_CA_ECDH = id_CA.branch("2");
        id_CA_ECDH_3DES_CBC_CBC = id_CA_ECDH.branch("1");
        id_TA = bsi_de.branch("2.2.2");
        id_TA_RSA = id_TA.branch("1");
        id_TA_RSA_v1_5_SHA_1 = id_TA_RSA.branch("1");
        id_TA_RSA_v1_5_SHA_256 = id_TA_RSA.branch("2");
        id_TA_RSA_PSS_SHA_1 = id_TA_RSA.branch("3");
        id_TA_RSA_PSS_SHA_256 = id_TA_RSA.branch("4");
        id_TA_RSA_v1_5_SHA_512 = id_TA_RSA.branch("5");
        id_TA_RSA_PSS_SHA_512 = id_TA_RSA.branch("6");
        id_TA_ECDSA = id_TA.branch("2");
        id_TA_ECDSA_SHA_1 = id_TA_ECDSA.branch("1");
        id_TA_ECDSA_SHA_224 = id_TA_ECDSA.branch("2");
        id_TA_ECDSA_SHA_256 = id_TA_ECDSA.branch("3");
        id_TA_ECDSA_SHA_384 = id_TA_ECDSA.branch("4");
        id_TA_ECDSA_SHA_512 = id_TA_ECDSA.branch("5");
        id_EAC_ePassport = bsi_de.branch("3.1.2.1");
    }
}

