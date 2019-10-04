/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.cert.CRL
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStoreParameters
 *  java.security.cert.CertStoreSpi
 *  java.security.cert.Certificate
 *  java.security.cert.CollectionCertStoreParameters
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 */
package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CertStoreCollectionSpi
extends CertStoreSpi {
    private CollectionCertStoreParameters params;

    public CertStoreCollectionSpi(CertStoreParameters certStoreParameters) {
        super(certStoreParameters);
        if (!(certStoreParameters instanceof CollectionCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("org.bouncycastle.jce.provider.CertStoreCollectionSpi: parameter must be a CollectionCertStoreParameters object\n" + certStoreParameters.toString());
        }
        this.params = (CollectionCertStoreParameters)certStoreParameters;
    }

    public Collection engineGetCRLs(CRLSelector cRLSelector) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        Iterator iterator = this.params.getCollection().iterator();
        if (cRLSelector == null) {
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (!(object instanceof CRL)) continue;
                arrayList.add(object);
            }
        } else {
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (!(object instanceof CRL) || !cRLSelector.match((CRL)object)) continue;
                arrayList.add(object);
            }
        }
        return arrayList;
    }

    public Collection engineGetCertificates(CertSelector certSelector) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        Iterator iterator = this.params.getCollection().iterator();
        if (certSelector == null) {
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (!(object instanceof Certificate)) continue;
                arrayList.add(object);
            }
        } else {
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (!(object instanceof Certificate) || !certSelector.match((Certificate)object)) continue;
                arrayList.add(object);
            }
        }
        return arrayList;
    }
}

