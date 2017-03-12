package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.bouncycastle.jce.MultiCertStoreParameters;

public class MultiCertStoreSpi extends CertStoreSpi {
    private MultiCertStoreParameters params;

    public MultiCertStoreSpi(CertStoreParameters certStoreParameters) {
        super(certStoreParameters);
        if (certStoreParameters instanceof MultiCertStoreParameters) {
            this.params = (MultiCertStoreParameters) certStoreParameters;
            return;
        }
        throw new InvalidAlgorithmParameterException("org.bouncycastle.jce.provider.MultiCertStoreSpi: parameter must be a MultiCertStoreParameters object\n" + certStoreParameters.toString());
    }

    public Collection engineGetCRLs(CRLSelector cRLSelector) {
        Collection arrayList;
        boolean searchAllStores = this.params.getSearchAllStores();
        if (searchAllStores) {
            arrayList = new ArrayList();
        } else {
            Object obj = Collections.EMPTY_LIST;
        }
        for (CertStore cRLs : this.params.getCertStores()) {
            Collection cRLs2 = cRLs.getCRLs(cRLSelector);
            if (searchAllStores) {
                arrayList.addAll(cRLs2);
            } else if (!cRLs2.isEmpty()) {
                return cRLs2;
            }
        }
        return arrayList;
    }

    public Collection engineGetCertificates(CertSelector certSelector) {
        Collection arrayList;
        boolean searchAllStores = this.params.getSearchAllStores();
        if (searchAllStores) {
            arrayList = new ArrayList();
        } else {
            Object obj = Collections.EMPTY_LIST;
        }
        for (CertStore certificates : this.params.getCertStores()) {
            Collection certificates2 = certificates.getCertificates(certSelector);
            if (searchAllStores) {
                arrayList.addAll(certificates2);
            } else if (!certificates2.isEmpty()) {
                return certificates2;
            }
        }
        return arrayList;
    }
}
