/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.Certificate
 *  java.util.Collection
 */
package org.bouncycastle.jcajce;

import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.util.Collection;
import org.bouncycastle.util.Selector;

public class PKIXCertStoreSelector<T extends Certificate>
implements Selector<T> {
    private final CertSelector baseSelector;

    private PKIXCertStoreSelector(CertSelector certSelector) {
        this.baseSelector = certSelector;
    }

    public static Collection<? extends Certificate> getCertificates(final PKIXCertStoreSelector pKIXCertStoreSelector, CertStore certStore) {
        return certStore.getCertificates(new CertSelector(){

            public Object clone() {
                return this;
            }

            public boolean match(Certificate certificate) {
                if (pKIXCertStoreSelector == null) {
                    return true;
                }
                return pKIXCertStoreSelector.match(certificate);
            }
        });
    }

    @Override
    public Object clone() {
        return new PKIXCertStoreSelector<T>(this.baseSelector);
    }

    @Override
    public boolean match(Certificate certificate) {
        return this.baseSelector.match(certificate);
    }

    public static class Builder {
        private final CertSelector baseSelector;

        public Builder(CertSelector certSelector) {
            this.baseSelector = (CertSelector)certSelector.clone();
        }

        public PKIXCertStoreSelector<? extends Certificate> build() {
            return new PKIXCertStoreSelector(this.baseSelector);
        }
    }

}

