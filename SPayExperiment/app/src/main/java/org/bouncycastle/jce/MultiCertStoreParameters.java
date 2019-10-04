/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.cert.CertStoreParameters
 *  java.util.Collection
 */
package org.bouncycastle.jce;

import java.security.cert.CertStoreParameters;
import java.util.Collection;

public class MultiCertStoreParameters
implements CertStoreParameters {
    private Collection certStores;
    private boolean searchAllStores;

    public MultiCertStoreParameters(Collection collection) {
        this(collection, true);
    }

    public MultiCertStoreParameters(Collection collection, boolean bl) {
        this.certStores = collection;
        this.searchAllStores = bl;
    }

    public Object clone() {
        return this;
    }

    public Collection getCertStores() {
        return this.certStores;
    }

    public boolean getSearchAllStores() {
        return this.searchAllStores;
    }
}

