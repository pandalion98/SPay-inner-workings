/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.jcajce.provider.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface AsymmetricKeyInfoConverter {
    public PrivateKey generatePrivate(PrivateKeyInfo var1);

    public PublicKey generatePublic(SubjectPublicKeyInfo var1);
}

