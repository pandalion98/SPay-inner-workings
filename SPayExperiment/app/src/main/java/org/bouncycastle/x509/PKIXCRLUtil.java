/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertStore
 *  java.security.cert.CertStoreException
 *  java.security.cert.PKIXParameters
 *  java.security.cert.X509CRL
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.x509;

import java.security.cert.CRLSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.X509Store;

class PKIXCRLUtil {
    PKIXCRLUtil() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final Collection findCRLs(X509CRLStoreSelector x509CRLStoreSelector, List list) {
        HashSet hashSet = new HashSet();
        Iterator iterator = list.iterator();
        AnnotatedException annotatedException = null;
        boolean bl = false;
        while (iterator.hasNext()) {
            AnnotatedException annotatedException2;
            boolean bl2;
            Object object = iterator.next();
            if (object instanceof X509Store) {
                X509Store x509Store = (X509Store)object;
                try {
                    hashSet.addAll(x509Store.getMatches(x509CRLStoreSelector));
                    bl2 = true;
                    annotatedException2 = annotatedException;
                }
                catch (StoreException storeException) {
                    AnnotatedException annotatedException3 = new AnnotatedException("Exception searching in X.509 CRL store.", (Throwable)storeException);
                    boolean bl3 = bl;
                    annotatedException2 = annotatedException3;
                    bl2 = bl3;
                }
            } else {
                CertStore certStore = (CertStore)object;
                try {
                    hashSet.addAll(certStore.getCRLs((CRLSelector)x509CRLStoreSelector));
                    bl2 = true;
                    annotatedException2 = annotatedException;
                }
                catch (CertStoreException certStoreException) {
                    AnnotatedException annotatedException4 = new AnnotatedException("Exception searching in X.509 CRL store.", certStoreException);
                    bl2 = bl;
                    annotatedException2 = annotatedException4;
                }
            }
            annotatedException = annotatedException2;
            bl = bl2;
        }
        if (!bl && annotatedException != null) {
            throw annotatedException;
        }
        return hashSet;
    }

    public Set findCRLs(X509CRLStoreSelector x509CRLStoreSelector, PKIXParameters pKIXParameters) {
        HashSet hashSet = new HashSet();
        try {
            hashSet.addAll(this.findCRLs(x509CRLStoreSelector, pKIXParameters.getCertStores()));
            return hashSet;
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", (Throwable)annotatedException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Set findCRLs(X509CRLStoreSelector x509CRLStoreSelector, ExtendedPKIXParameters extendedPKIXParameters, Date date) {
        HashSet hashSet = new HashSet();
        try {
            hashSet.addAll(this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            hashSet.addAll(this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            hashSet.addAll(this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", (Throwable)annotatedException);
        }
        HashSet hashSet2 = new HashSet();
        if (extendedPKIXParameters.getDate() != null) {
            date = extendedPKIXParameters.getDate();
        }
        Iterator iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            X509CRL x509CRL = (X509CRL)iterator.next();
            if (!x509CRL.getNextUpdate().after(date)) continue;
            X509Certificate x509Certificate = x509CRLStoreSelector.getCertificateChecking();
            if (x509Certificate != null) {
                if (!x509CRL.getThisUpdate().before(x509Certificate.getNotAfter())) continue;
                hashSet2.add((Object)x509CRL);
                continue;
            }
            hashSet2.add((Object)x509CRL);
        }
        return hashSet2;
    }
}

