/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashSet
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509CertPairStoreSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509CertificatePair;
import org.bouncycastle.x509.X509StoreParameters;
import org.bouncycastle.x509.X509StoreSpi;
import org.bouncycastle.x509.util.LDAPStoreHelper;

public class X509StoreLDAPCerts
extends X509StoreSpi {
    private LDAPStoreHelper helper;

    private Collection getCertificatesFromCrossCertificatePairs(X509CertStoreSelector x509CertStoreSelector) {
        HashSet hashSet = new HashSet();
        X509CertPairStoreSelector x509CertPairStoreSelector = new X509CertPairStoreSelector();
        x509CertPairStoreSelector.setForwardSelector(x509CertStoreSelector);
        x509CertPairStoreSelector.setReverseSelector(new X509CertStoreSelector());
        HashSet hashSet2 = new HashSet(this.helper.getCrossCertificatePairs(x509CertPairStoreSelector));
        HashSet hashSet3 = new HashSet();
        HashSet hashSet4 = new HashSet();
        for (X509CertificatePair x509CertificatePair : hashSet2) {
            if (x509CertificatePair.getForward() != null) {
                hashSet3.add((Object)x509CertificatePair.getForward());
            }
            if (x509CertificatePair.getReverse() == null) continue;
            hashSet4.add((Object)x509CertificatePair.getReverse());
        }
        hashSet.addAll((Collection)hashSet3);
        hashSet.addAll((Collection)hashSet4);
        return hashSet;
    }

    @Override
    public Collection engineGetMatches(Selector selector) {
        if (!(selector instanceof X509CertStoreSelector)) {
            return Collections.EMPTY_SET;
        }
        X509CertStoreSelector x509CertStoreSelector = (X509CertStoreSelector)selector;
        HashSet hashSet = new HashSet();
        if (x509CertStoreSelector.getBasicConstraints() > 0) {
            hashSet.addAll(this.helper.getCACertificates(x509CertStoreSelector));
            hashSet.addAll(this.getCertificatesFromCrossCertificatePairs(x509CertStoreSelector));
            return hashSet;
        }
        if (x509CertStoreSelector.getBasicConstraints() == -2) {
            hashSet.addAll(this.helper.getUserCertificates(x509CertStoreSelector));
            return hashSet;
        }
        hashSet.addAll(this.helper.getUserCertificates(x509CertStoreSelector));
        hashSet.addAll(this.helper.getCACertificates(x509CertStoreSelector));
        hashSet.addAll(this.getCertificatesFromCrossCertificatePairs(x509CertStoreSelector));
        return hashSet;
    }

    @Override
    public void engineInit(X509StoreParameters x509StoreParameters) {
        if (!(x509StoreParameters instanceof X509LDAPCertStoreParameters)) {
            throw new IllegalArgumentException("Initialization parameters must be an instance of " + X509LDAPCertStoreParameters.class.getName() + ".");
        }
        this.helper = new LDAPStoreHelper((X509LDAPCertStoreParameters)x509StoreParameters);
    }
}

