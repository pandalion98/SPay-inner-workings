package org.bouncycastle.jcajce;

import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.util.Collection;
import org.bouncycastle.util.Selector;

public class PKIXCertStoreSelector<T extends Certificate> implements Selector<T> {
    private final CertSelector baseSelector;

    /* renamed from: org.bouncycastle.jcajce.PKIXCertStoreSelector.1 */
    static class C07521 implements CertSelector {
        final /* synthetic */ PKIXCertStoreSelector val$selector;

        C07521(PKIXCertStoreSelector pKIXCertStoreSelector) {
            this.val$selector = pKIXCertStoreSelector;
        }

        public Object clone() {
            return this;
        }

        public boolean match(Certificate certificate) {
            return this.val$selector == null ? true : this.val$selector.match(certificate);
        }
    }

    public static class Builder {
        private final CertSelector baseSelector;

        public Builder(CertSelector certSelector) {
            this.baseSelector = (CertSelector) certSelector.clone();
        }

        public PKIXCertStoreSelector<? extends Certificate> build() {
            return new PKIXCertStoreSelector(null);
        }
    }

    private PKIXCertStoreSelector(CertSelector certSelector) {
        this.baseSelector = certSelector;
    }

    public static Collection<? extends Certificate> getCertificates(PKIXCertStoreSelector pKIXCertStoreSelector, CertStore certStore) {
        return certStore.getCertificates(new C07521(pKIXCertStoreSelector));
    }

    public Object clone() {
        return new PKIXCertStoreSelector(this.baseSelector);
    }

    public boolean match(Certificate certificate) {
        return this.baseSelector.match(certificate);
    }
}
