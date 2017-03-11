package org.bouncycastle.x509;

import java.security.cert.CertStore;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.jce.provider.AnnotatedException;

class PKIXCRLUtil {
    PKIXCRLUtil() {
    }

    private final Collection findCRLs(X509CRLStoreSelector x509CRLStoreSelector, List list) {
        Collection hashSet = new HashSet();
        AnnotatedException annotatedException = null;
        Object obj = null;
        for (Object next : list) {
            Object next2;
            AnnotatedException annotatedException2;
            if (next2 instanceof X509Store) {
                try {
                    hashSet.addAll(((X509Store) next2).getMatches(x509CRLStoreSelector));
                    next2 = 1;
                    annotatedException2 = annotatedException;
                } catch (Throwable e) {
                    Object obj2 = obj;
                    annotatedException2 = new AnnotatedException("Exception searching in X.509 CRL store.", e);
                    next2 = obj2;
                }
            } else {
                try {
                    hashSet.addAll(((CertStore) next2).getCRLs(x509CRLStoreSelector));
                    int i = 1;
                    annotatedException2 = annotatedException;
                } catch (Throwable e2) {
                    annotatedException = new AnnotatedException("Exception searching in X.509 CRL store.", e2);
                    next2 = obj;
                    annotatedException2 = annotatedException;
                }
            }
            annotatedException = annotatedException2;
            obj = next2;
        }
        if (obj != null || annotatedException == null) {
            return hashSet;
        }
        throw annotatedException;
    }

    public Set findCRLs(X509CRLStoreSelector x509CRLStoreSelector, PKIXParameters pKIXParameters) {
        Set hashSet = new HashSet();
        try {
            hashSet.addAll(findCRLs(x509CRLStoreSelector, pKIXParameters.getCertStores()));
            return hashSet;
        } catch (Throwable e) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", e);
        }
    }

    public Set findCRLs(X509CRLStoreSelector x509CRLStoreSelector, ExtendedPKIXParameters extendedPKIXParameters, Date date) {
        Set<X509CRL> hashSet = new HashSet();
        try {
            hashSet.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            hashSet.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            hashSet.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
            Set hashSet2 = new HashSet();
            if (extendedPKIXParameters.getDate() != null) {
                date = extendedPKIXParameters.getDate();
            }
            for (X509CRL x509crl : hashSet) {
                if (x509crl.getNextUpdate().after(date)) {
                    X509Certificate certificateChecking = x509CRLStoreSelector.getCertificateChecking();
                    if (certificateChecking == null) {
                        hashSet2.add(x509crl);
                    } else if (x509crl.getThisUpdate().before(certificateChecking.getNotAfter())) {
                        hashSet2.add(x509crl);
                    }
                }
            }
            return hashSet2;
        } catch (Throwable e) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", e);
        }
    }
}
