/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.bc;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface BCObjectIdentifiers {
    public static final ASN1ObjectIdentifier bc = new ASN1ObjectIdentifier("1.3.6.1.4.1.22554");
    public static final ASN1ObjectIdentifier bc_pbe = bc.branch("1");
    public static final ASN1ObjectIdentifier bc_pbe_sha1 = bc_pbe.branch("1");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12;
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes128_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes192_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes256_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs5;
    public static final ASN1ObjectIdentifier bc_pbe_sha224;
    public static final ASN1ObjectIdentifier bc_pbe_sha256;
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12;
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes128_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes192_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes256_cbc;
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs5;
    public static final ASN1ObjectIdentifier bc_pbe_sha384;
    public static final ASN1ObjectIdentifier bc_pbe_sha512;

    static {
        bc_pbe_sha256 = bc_pbe.branch("2.1");
        bc_pbe_sha384 = bc_pbe.branch("2.2");
        bc_pbe_sha512 = bc_pbe.branch("2.3");
        bc_pbe_sha224 = bc_pbe.branch("2.4");
        bc_pbe_sha1_pkcs5 = bc_pbe_sha1.branch("1");
        bc_pbe_sha1_pkcs12 = bc_pbe_sha1.branch("2");
        bc_pbe_sha256_pkcs5 = bc_pbe_sha256.branch("1");
        bc_pbe_sha256_pkcs12 = bc_pbe_sha256.branch("2");
        bc_pbe_sha1_pkcs12_aes128_cbc = bc_pbe_sha1_pkcs12.branch("1.2");
        bc_pbe_sha1_pkcs12_aes192_cbc = bc_pbe_sha1_pkcs12.branch("1.22");
        bc_pbe_sha1_pkcs12_aes256_cbc = bc_pbe_sha1_pkcs12.branch("1.42");
        bc_pbe_sha256_pkcs12_aes128_cbc = bc_pbe_sha256_pkcs12.branch("1.2");
        bc_pbe_sha256_pkcs12_aes192_cbc = bc_pbe_sha256_pkcs12.branch("1.22");
        bc_pbe_sha256_pkcs12_aes256_cbc = bc_pbe_sha256_pkcs12.branch("1.42");
    }
}

