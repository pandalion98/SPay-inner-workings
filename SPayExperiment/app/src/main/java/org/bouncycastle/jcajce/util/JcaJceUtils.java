/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AlgorithmParameters
 */
package org.bouncycastle.jcajce.util;

import java.security.AlgorithmParameters;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;

public class JcaJceUtils {
    private JcaJceUtils() {
    }

    public static ASN1Encodable extractParameters(AlgorithmParameters algorithmParameters) {
        try {
            ASN1Primitive aSN1Primitive = ASN1Primitive.fromByteArray(algorithmParameters.getEncoded("ASN.1"));
            return aSN1Primitive;
        }
        catch (Exception exception) {
            return ASN1Primitive.fromByteArray(algorithmParameters.getEncoded());
        }
    }

    public static String getDigestAlgName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        if (PKCSObjectIdentifiers.md5.equals(aSN1ObjectIdentifier)) {
            return "MD5";
        }
        if (OIWObjectIdentifiers.idSHA1.equals(aSN1ObjectIdentifier)) {
            return "SHA1";
        }
        if (NISTObjectIdentifiers.id_sha224.equals(aSN1ObjectIdentifier)) {
            return "SHA224";
        }
        if (NISTObjectIdentifiers.id_sha256.equals(aSN1ObjectIdentifier)) {
            return "SHA256";
        }
        if (NISTObjectIdentifiers.id_sha384.equals(aSN1ObjectIdentifier)) {
            return "SHA384";
        }
        if (NISTObjectIdentifiers.id_sha512.equals(aSN1ObjectIdentifier)) {
            return "SHA512";
        }
        if (TeleTrusTObjectIdentifiers.ripemd128.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD128";
        }
        if (TeleTrusTObjectIdentifiers.ripemd160.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD160";
        }
        if (TeleTrusTObjectIdentifiers.ripemd256.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD256";
        }
        if (CryptoProObjectIdentifiers.gostR3411.equals(aSN1ObjectIdentifier)) {
            return "GOST3411";
        }
        return aSN1ObjectIdentifier.getId();
    }

    public static void loadParameters(AlgorithmParameters algorithmParameters, ASN1Encodable aSN1Encodable) {
        try {
            algorithmParameters.init(aSN1Encodable.toASN1Primitive().getEncoded(), "ASN.1");
            return;
        }
        catch (Exception exception) {
            algorithmParameters.init(aSN1Encodable.toASN1Primitive().getEncoded());
            return;
        }
    }
}

