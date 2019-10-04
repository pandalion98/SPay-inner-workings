/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.nist;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface NISTObjectIdentifiers {
    public static final ASN1ObjectIdentifier aes;
    public static final ASN1ObjectIdentifier dsa_with_sha224;
    public static final ASN1ObjectIdentifier dsa_with_sha256;
    public static final ASN1ObjectIdentifier dsa_with_sha384;
    public static final ASN1ObjectIdentifier dsa_with_sha512;
    public static final ASN1ObjectIdentifier hashAlgs;
    public static final ASN1ObjectIdentifier id_aes128_CBC;
    public static final ASN1ObjectIdentifier id_aes128_CCM;
    public static final ASN1ObjectIdentifier id_aes128_CFB;
    public static final ASN1ObjectIdentifier id_aes128_ECB;
    public static final ASN1ObjectIdentifier id_aes128_GCM;
    public static final ASN1ObjectIdentifier id_aes128_OFB;
    public static final ASN1ObjectIdentifier id_aes128_wrap;
    public static final ASN1ObjectIdentifier id_aes192_CBC;
    public static final ASN1ObjectIdentifier id_aes192_CCM;
    public static final ASN1ObjectIdentifier id_aes192_CFB;
    public static final ASN1ObjectIdentifier id_aes192_ECB;
    public static final ASN1ObjectIdentifier id_aes192_GCM;
    public static final ASN1ObjectIdentifier id_aes192_OFB;
    public static final ASN1ObjectIdentifier id_aes192_wrap;
    public static final ASN1ObjectIdentifier id_aes256_CBC;
    public static final ASN1ObjectIdentifier id_aes256_CCM;
    public static final ASN1ObjectIdentifier id_aes256_CFB;
    public static final ASN1ObjectIdentifier id_aes256_ECB;
    public static final ASN1ObjectIdentifier id_aes256_GCM;
    public static final ASN1ObjectIdentifier id_aes256_OFB;
    public static final ASN1ObjectIdentifier id_aes256_wrap;
    public static final ASN1ObjectIdentifier id_dsa_with_sha2;
    public static final ASN1ObjectIdentifier id_sha224;
    public static final ASN1ObjectIdentifier id_sha256;
    public static final ASN1ObjectIdentifier id_sha384;
    public static final ASN1ObjectIdentifier id_sha512;
    public static final ASN1ObjectIdentifier id_sha512_224;
    public static final ASN1ObjectIdentifier id_sha512_256;
    public static final ASN1ObjectIdentifier nistAlgorithm;

    static {
        nistAlgorithm = new ASN1ObjectIdentifier("2.16.840.1.101.3.4");
        hashAlgs = nistAlgorithm.branch("2");
        id_sha256 = hashAlgs.branch("1");
        id_sha384 = hashAlgs.branch("2");
        id_sha512 = hashAlgs.branch("3");
        id_sha224 = hashAlgs.branch("4");
        id_sha512_224 = hashAlgs.branch("5");
        id_sha512_256 = hashAlgs.branch("6");
        aes = nistAlgorithm.branch("1");
        id_aes128_ECB = aes.branch("1");
        id_aes128_CBC = aes.branch("2");
        id_aes128_OFB = aes.branch("3");
        id_aes128_CFB = aes.branch("4");
        id_aes128_wrap = aes.branch("5");
        id_aes128_GCM = aes.branch("6");
        id_aes128_CCM = aes.branch("7");
        id_aes192_ECB = aes.branch("21");
        id_aes192_CBC = aes.branch("22");
        id_aes192_OFB = aes.branch("23");
        id_aes192_CFB = aes.branch("24");
        id_aes192_wrap = aes.branch("25");
        id_aes192_GCM = aes.branch("26");
        id_aes192_CCM = aes.branch("27");
        id_aes256_ECB = aes.branch("41");
        id_aes256_CBC = aes.branch("42");
        id_aes256_OFB = aes.branch("43");
        id_aes256_CFB = aes.branch("44");
        id_aes256_wrap = aes.branch("45");
        id_aes256_GCM = aes.branch("46");
        id_aes256_CCM = aes.branch("47");
        id_dsa_with_sha2 = nistAlgorithm.branch("3");
        dsa_with_sha224 = id_dsa_with_sha2.branch("1");
        dsa_with_sha256 = id_dsa_with_sha2.branch("2");
        dsa_with_sha384 = id_dsa_with_sha2.branch("3");
        dsa_with_sha512 = id_dsa_with_sha2.branch("4");
    }
}

