/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.PublicKey
 *  java.security.cert.Certificate
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  java.util.Date
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.util.Selector;

public class X509CertStoreSelector
extends X509CertSelector
implements Selector {
    public static X509CertStoreSelector getInstance(X509CertSelector x509CertSelector) {
        if (x509CertSelector == null) {
            throw new IllegalArgumentException("cannot create from null selector");
        }
        X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
        x509CertStoreSelector.setAuthorityKeyIdentifier(x509CertSelector.getAuthorityKeyIdentifier());
        x509CertStoreSelector.setBasicConstraints(x509CertSelector.getBasicConstraints());
        x509CertStoreSelector.setCertificate(x509CertSelector.getCertificate());
        x509CertStoreSelector.setCertificateValid(x509CertSelector.getCertificateValid());
        x509CertStoreSelector.setMatchAllSubjectAltNames(x509CertSelector.getMatchAllSubjectAltNames());
        try {
            x509CertStoreSelector.setPathToNames(x509CertSelector.getPathToNames());
            x509CertStoreSelector.setExtendedKeyUsage(x509CertSelector.getExtendedKeyUsage());
            x509CertStoreSelector.setNameConstraints(x509CertSelector.getNameConstraints());
            x509CertStoreSelector.setPolicy(x509CertSelector.getPolicy());
            x509CertStoreSelector.setSubjectPublicKeyAlgID(x509CertSelector.getSubjectPublicKeyAlgID());
            x509CertStoreSelector.setSubjectAlternativeNames(x509CertSelector.getSubjectAlternativeNames());
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error in passed in selector: " + (Object)((Object)iOException));
        }
        x509CertStoreSelector.setIssuer(x509CertSelector.getIssuer());
        x509CertStoreSelector.setKeyUsage(x509CertSelector.getKeyUsage());
        x509CertStoreSelector.setPrivateKeyValid(x509CertSelector.getPrivateKeyValid());
        x509CertStoreSelector.setSerialNumber(x509CertSelector.getSerialNumber());
        x509CertStoreSelector.setSubject(x509CertSelector.getSubject());
        x509CertStoreSelector.setSubjectKeyIdentifier(x509CertSelector.getSubjectKeyIdentifier());
        x509CertStoreSelector.setSubjectPublicKey(x509CertSelector.getSubjectPublicKey());
        return x509CertStoreSelector;
    }

    @Override
    public Object clone() {
        return (X509CertStoreSelector)super.clone();
    }

    public boolean match(Object object) {
        if (!(object instanceof X509Certificate)) {
            return false;
        }
        return super.match((Certificate)((X509Certificate)object));
    }

    public boolean match(Certificate certificate) {
        return this.match((Object)certificate);
    }
}

