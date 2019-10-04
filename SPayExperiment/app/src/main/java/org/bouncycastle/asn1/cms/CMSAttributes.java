/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CMSAttributes {
    public static final ASN1ObjectIdentifier contentHint;
    public static final ASN1ObjectIdentifier contentType;
    public static final ASN1ObjectIdentifier counterSignature;
    public static final ASN1ObjectIdentifier messageDigest;
    public static final ASN1ObjectIdentifier signingTime;

    static {
        contentType = PKCSObjectIdentifiers.pkcs_9_at_contentType;
        messageDigest = PKCSObjectIdentifiers.pkcs_9_at_messageDigest;
        signingTime = PKCSObjectIdentifiers.pkcs_9_at_signingTime;
        counterSignature = PKCSObjectIdentifiers.pkcs_9_at_counterSignature;
        contentHint = PKCSObjectIdentifiers.id_aa_contentHint;
    }
}

