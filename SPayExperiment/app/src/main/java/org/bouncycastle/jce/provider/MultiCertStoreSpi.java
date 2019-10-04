/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.CertStoreParameters
 *  java.security.cert.CertStoreSpi
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.List
 */
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
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.jce.MultiCertStoreParameters;

public class MultiCertStoreSpi
extends CertStoreSpi {
    private MultiCertStoreParameters params;

    public MultiCertStoreSpi(CertStoreParameters certStoreParameters) {
        super(certStoreParameters);
        if (!(certStoreParameters instanceof MultiCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("org.bouncycastle.jce.provider.MultiCertStoreSpi: parameter must be a MultiCertStoreParameters object\n" + certStoreParameters.toString());
        }
        this.params = (MultiCertStoreParameters)certStoreParameters;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public Collection engineGetCRLs(CRLSelector cRLSelector) {
        boolean bl = this.params.getSearchAllStores();
        Iterator iterator = this.params.getCertStores().iterator();
        Object object = bl ? new ArrayList() : Collections.EMPTY_LIST;
        while (iterator.hasNext()) {
            Collection collection = ((CertStore)iterator.next()).getCRLs(cRLSelector);
            if (bl) {
                object.addAll(collection);
                continue;
            }
            if (!collection.isEmpty()) return collection;
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public Collection engineGetCertificates(CertSelector certSelector) {
        boolean bl = this.params.getSearchAllStores();
        Iterator iterator = this.params.getCertStores().iterator();
        Object object = bl ? new ArrayList() : Collections.EMPTY_LIST;
        while (iterator.hasNext()) {
            Collection collection = ((CertStore)iterator.next()).getCertificates(certSelector);
            if (bl) {
                object.addAll(collection);
                continue;
            }
            if (!collection.isEmpty()) return collection;
        }
        return object;
    }
}

