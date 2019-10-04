/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.security.cert.X509Certificate
 */
package org.bouncycastle.x509;

import java.security.cert.X509Certificate;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509CertificatePair;

public class X509CertPairStoreSelector
implements Selector {
    private X509CertificatePair certPair;
    private X509CertStoreSelector forwardSelector;
    private X509CertStoreSelector reverseSelector;

    @Override
    public Object clone() {
        X509CertPairStoreSelector x509CertPairStoreSelector = new X509CertPairStoreSelector();
        x509CertPairStoreSelector.certPair = this.certPair;
        if (this.forwardSelector != null) {
            x509CertPairStoreSelector.setForwardSelector((X509CertStoreSelector)this.forwardSelector.clone());
        }
        if (this.reverseSelector != null) {
            x509CertPairStoreSelector.setReverseSelector((X509CertStoreSelector)this.reverseSelector.clone());
        }
        return x509CertPairStoreSelector;
    }

    public X509CertificatePair getCertPair() {
        return this.certPair;
    }

    public X509CertStoreSelector getForwardSelector() {
        return this.forwardSelector;
    }

    public X509CertStoreSelector getReverseSelector() {
        return this.reverseSelector;
    }

    public boolean match(Object object) {
        block8 : {
            X509CertificatePair x509CertificatePair;
            block7 : {
                block6 : {
                    try {
                        if (object instanceof X509CertificatePair) break block6;
                        return false;
                    }
                    catch (Exception exception) {
                        return false;
                    }
                }
                x509CertificatePair = (X509CertificatePair)object;
                if (this.forwardSelector == null || this.forwardSelector.match((Object)x509CertificatePair.getForward())) break block7;
                return false;
            }
            if (this.reverseSelector == null || this.reverseSelector.match((Object)x509CertificatePair.getReverse())) break block8;
            return false;
        }
        if (this.certPair != null) {
            boolean bl = this.certPair.equals(object);
            return bl;
        }
        return true;
    }

    public void setCertPair(X509CertificatePair x509CertificatePair) {
        this.certPair = x509CertificatePair;
    }

    public void setForwardSelector(X509CertStoreSelector x509CertStoreSelector) {
        this.forwardSelector = x509CertStoreSelector;
    }

    public void setReverseSelector(X509CertStoreSelector x509CertStoreSelector) {
        this.reverseSelector = x509CertStoreSelector;
    }
}

