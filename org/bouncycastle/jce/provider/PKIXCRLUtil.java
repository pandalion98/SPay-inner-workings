package org.bouncycastle.jce.provider;

import java.security.cert.CertStore;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.jcajce.PKIXCRLStoreSelector;
import org.bouncycastle.util.Store;

class PKIXCRLUtil {
    PKIXCRLUtil() {
    }

    private final Collection findCRLs(PKIXCRLStoreSelector pKIXCRLStoreSelector, List list) {
        Collection hashSet = new HashSet();
        AnnotatedException annotatedException = null;
        Object obj = null;
        for (Object next : list) {
            Object next2;
            AnnotatedException annotatedException2;
            if (next2 instanceof Store) {
                try {
                    hashSet.addAll(((Store) next2).getMatches(pKIXCRLStoreSelector));
                    next2 = 1;
                    annotatedException2 = annotatedException;
                } catch (Throwable e) {
                    Object obj2 = obj;
                    annotatedException2 = new AnnotatedException("Exception searching in X.509 CRL store.", e);
                    next2 = obj2;
                }
            } else {
                try {
                    hashSet.addAll(PKIXCRLStoreSelector.getCRLs(pKIXCRLStoreSelector, (CertStore) next2));
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

    public Set findCRLs(PKIXCRLStoreSelector pKIXCRLStoreSelector, Date date, List list, List list2) {
        Set<X509CRL> hashSet = new HashSet();
        try {
            hashSet.addAll(findCRLs(pKIXCRLStoreSelector, list2));
            hashSet.addAll(findCRLs(pKIXCRLStoreSelector, list));
            Set hashSet2 = new HashSet();
            for (X509CRL x509crl : hashSet) {
                if (x509crl.getNextUpdate().after(date)) {
                    X509Certificate certificateChecking = pKIXCRLStoreSelector.getCertificateChecking();
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
