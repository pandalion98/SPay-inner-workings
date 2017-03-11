package org.bouncycastle.jcajce;

import java.math.BigInteger;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertStore;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Selector;

public class PKIXCRLStoreSelector<T extends CRL> implements Selector<T> {
    private final CRLSelector baseSelector;
    private final boolean completeCRLEnabled;
    private final boolean deltaCRLIndicator;
    private final byte[] issuingDistributionPoint;
    private final boolean issuingDistributionPointEnabled;
    private final BigInteger maxBaseCRLNumber;

    /* renamed from: org.bouncycastle.jcajce.PKIXCRLStoreSelector.1 */
    static class C07511 implements CRLSelector {
        final /* synthetic */ PKIXCRLStoreSelector val$selector;

        C07511(PKIXCRLStoreSelector pKIXCRLStoreSelector) {
            this.val$selector = pKIXCRLStoreSelector;
        }

        public Object clone() {
            return this;
        }

        public boolean match(CRL crl) {
            return this.val$selector.match(crl);
        }
    }

    public static class Builder {
        private final CRLSelector baseSelector;
        private boolean completeCRLEnabled;
        private boolean deltaCRLIndicator;
        private byte[] issuingDistributionPoint;
        private boolean issuingDistributionPointEnabled;
        private BigInteger maxBaseCRLNumber;

        public Builder(CRLSelector cRLSelector) {
            this.deltaCRLIndicator = false;
            this.completeCRLEnabled = false;
            this.maxBaseCRLNumber = null;
            this.issuingDistributionPoint = null;
            this.issuingDistributionPointEnabled = false;
            this.baseSelector = (CRLSelector) cRLSelector.clone();
        }

        public PKIXCRLStoreSelector<? extends CRL> build() {
            return new PKIXCRLStoreSelector();
        }

        public Builder setCompleteCRLEnabled(boolean z) {
            this.completeCRLEnabled = z;
            return this;
        }

        public Builder setDeltaCRLIndicatorEnabled(boolean z) {
            this.deltaCRLIndicator = z;
            return this;
        }

        public void setIssuingDistributionPoint(byte[] bArr) {
            this.issuingDistributionPoint = Arrays.clone(bArr);
        }

        public void setIssuingDistributionPointEnabled(boolean z) {
            this.issuingDistributionPointEnabled = z;
        }

        public void setMaxBaseCRLNumber(BigInteger bigInteger) {
            this.maxBaseCRLNumber = bigInteger;
        }
    }

    private PKIXCRLStoreSelector(Builder builder) {
        this.baseSelector = builder.baseSelector;
        this.deltaCRLIndicator = builder.deltaCRLIndicator;
        this.completeCRLEnabled = builder.completeCRLEnabled;
        this.maxBaseCRLNumber = builder.maxBaseCRLNumber;
        this.issuingDistributionPoint = builder.issuingDistributionPoint;
        this.issuingDistributionPointEnabled = builder.issuingDistributionPointEnabled;
    }

    public static Collection<? extends CRL> getCRLs(PKIXCRLStoreSelector pKIXCRLStoreSelector, CertStore certStore) {
        return certStore.getCRLs(new C07511(pKIXCRLStoreSelector));
    }

    public Object clone() {
        return this;
    }

    public X509Certificate getCertificateChecking() {
        return ((X509CRLSelector) this.baseSelector).getCertificateChecking();
    }

    public byte[] getIssuingDistributionPoint() {
        return Arrays.clone(this.issuingDistributionPoint);
    }

    public BigInteger getMaxBaseCRLNumber() {
        return this.maxBaseCRLNumber;
    }

    public boolean isCompleteCRLEnabled() {
        return this.completeCRLEnabled;
    }

    public boolean isDeltaCRLIndicatorEnabled() {
        return this.deltaCRLIndicator;
    }

    public boolean isIssuingDistributionPointEnabled() {
        return this.issuingDistributionPointEnabled;
    }

    public boolean match(CRL crl) {
        if (!(crl instanceof X509CRL)) {
            return this.baseSelector.match(crl);
        }
        X509CRL x509crl = (X509CRL) crl;
        ASN1Integer aSN1Integer = null;
        try {
            Object extensionValue = x509crl.getExtensionValue(Extension.deltaCRLIndicator.getId());
            if (extensionValue != null) {
                aSN1Integer = ASN1Integer.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets());
            }
            if (isDeltaCRLIndicatorEnabled() && aSN1Integer == null) {
                return false;
            }
            if (isCompleteCRLEnabled() && aSN1Integer != null) {
                return false;
            }
            if (aSN1Integer != null && this.maxBaseCRLNumber != null && aSN1Integer.getPositiveValue().compareTo(this.maxBaseCRLNumber) == 1) {
                return false;
            }
            if (this.issuingDistributionPointEnabled) {
                byte[] extensionValue2 = x509crl.getExtensionValue(Extension.issuingDistributionPoint.getId());
                if (this.issuingDistributionPoint == null) {
                    if (extensionValue2 != null) {
                        return false;
                    }
                } else if (!Arrays.areEqual(extensionValue2, this.issuingDistributionPoint)) {
                    return false;
                }
            }
            return this.baseSelector.match(crl);
        } catch (Exception e) {
            return false;
        }
    }
}
