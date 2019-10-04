/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashSet
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509CertPairStoreSelector;
import org.bouncycastle.x509.X509StoreParameters;
import org.bouncycastle.x509.X509StoreSpi;
import org.bouncycastle.x509.util.LDAPStoreHelper;

public class X509StoreLDAPCertPairs
extends X509StoreSpi {
    private LDAPStoreHelper helper;

    @Override
    public Collection engineGetMatches(Selector selector) {
        if (!(selector instanceof X509CertPairStoreSelector)) {
            return Collections.EMPTY_SET;
        }
        X509CertPairStoreSelector x509CertPairStoreSelector = (X509CertPairStoreSelector)selector;
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.helper.getCrossCertificatePairs(x509CertPairStoreSelector));
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

