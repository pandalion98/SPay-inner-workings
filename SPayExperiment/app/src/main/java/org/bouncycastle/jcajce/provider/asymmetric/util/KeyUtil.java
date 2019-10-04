/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class KeyUtil {
    public static byte[] getEncodedPrivateKeyInfo(PrivateKeyInfo privateKeyInfo) {
        try {
            byte[] arrby = privateKeyInfo.getEncoded("DER");
            return arrby;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static byte[] getEncodedPrivateKeyInfo(AlgorithmIdentifier algorithmIdentifier, ASN1Encodable aSN1Encodable) {
        try {
            byte[] arrby = KeyUtil.getEncodedPrivateKeyInfo(new PrivateKeyInfo(algorithmIdentifier, aSN1Encodable.toASN1Primitive()));
            return arrby;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier algorithmIdentifier, ASN1Encodable aSN1Encodable) {
        try {
            byte[] arrby = KeyUtil.getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algorithmIdentifier, aSN1Encodable));
            return arrby;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier algorithmIdentifier, byte[] arrby) {
        try {
            byte[] arrby2 = KeyUtil.getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algorithmIdentifier, arrby));
            return arrby2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            byte[] arrby = subjectPublicKeyInfo.getEncoded("DER");
            return arrby;
        }
        catch (Exception exception) {
            return null;
        }
    }
}

