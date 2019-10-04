/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.cert.CRL
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertStore
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLSelector
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  org.bouncycastle.util.Arrays
 */
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
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Selector;

public class PKIXCRLStoreSelector<T extends CRL>
implements Selector<T> {
    private final CRLSelector baseSelector;
    private final boolean completeCRLEnabled;
    private final boolean deltaCRLIndicator;
    private final byte[] issuingDistributionPoint;
    private final boolean issuingDistributionPointEnabled;
    private final BigInteger maxBaseCRLNumber;

    private PKIXCRLStoreSelector(Builder builder) {
        this.baseSelector = builder.baseSelector;
        this.deltaCRLIndicator = builder.deltaCRLIndicator;
        this.completeCRLEnabled = builder.completeCRLEnabled;
        this.maxBaseCRLNumber = builder.maxBaseCRLNumber;
        this.issuingDistributionPoint = builder.issuingDistributionPoint;
        this.issuingDistributionPointEnabled = builder.issuingDistributionPointEnabled;
    }

    public static Collection<? extends CRL> getCRLs(final PKIXCRLStoreSelector pKIXCRLStoreSelector, CertStore certStore) {
        return certStore.getCRLs(new CRLSelector(){

            public Object clone() {
                return this;
            }

            public boolean match(CRL cRL) {
                return pKIXCRLStoreSelector.match(cRL);
            }
        });
    }

    @Override
    public Object clone() {
        return this;
    }

    public X509Certificate getCertificateChecking() {
        return ((X509CRLSelector)this.baseSelector).getCertificateChecking();
    }

    public byte[] getIssuingDistributionPoint() {
        return Arrays.clone((byte[])this.issuingDistributionPoint);
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

    @Override
    public boolean match(CRL cRL) {
        ASN1Integer aSN1Integer;
        X509CRL x509CRL;
        block9 : {
            byte[] arrby;
            ASN1Integer aSN1Integer2;
            if (!(cRL instanceof X509CRL)) {
                return this.baseSelector.match(cRL);
            }
            x509CRL = (X509CRL)cRL;
            try {
                arrby = x509CRL.getExtensionValue(Extension.deltaCRLIndicator.getId());
                aSN1Integer = null;
                if (arrby == null) break block9;
            }
            catch (Exception exception) {
                return false;
            }
            aSN1Integer = aSN1Integer2 = ASN1Integer.getInstance(ASN1OctetString.getInstance(arrby).getOctets());
        }
        if (this.isDeltaCRLIndicatorEnabled() && aSN1Integer == null) {
            return false;
        }
        if (this.isCompleteCRLEnabled() && aSN1Integer != null) {
            return false;
        }
        if (aSN1Integer != null && this.maxBaseCRLNumber != null && aSN1Integer.getPositiveValue().compareTo(this.maxBaseCRLNumber) == 1) {
            return false;
        }
        if (this.issuingDistributionPointEnabled) {
            byte[] arrby = x509CRL.getExtensionValue(Extension.issuingDistributionPoint.getId());
            if (this.issuingDistributionPoint == null ? arrby != null : !Arrays.areEqual((byte[])arrby, (byte[])this.issuingDistributionPoint)) {
                return false;
            }
        }
        return this.baseSelector.match(cRL);
    }

    public static class Builder {
        private final CRLSelector baseSelector;
        private boolean completeCRLEnabled = false;
        private boolean deltaCRLIndicator = false;
        private byte[] issuingDistributionPoint = null;
        private boolean issuingDistributionPointEnabled = false;
        private BigInteger maxBaseCRLNumber = null;

        public Builder(CRLSelector cRLSelector) {
            this.baseSelector = (CRLSelector)cRLSelector.clone();
        }

        public PKIXCRLStoreSelector<? extends CRL> build() {
            return new PKIXCRLStoreSelector(this);
        }

        public Builder setCompleteCRLEnabled(boolean bl) {
            this.completeCRLEnabled = bl;
            return this;
        }

        public Builder setDeltaCRLIndicatorEnabled(boolean bl) {
            this.deltaCRLIndicator = bl;
            return this;
        }

        public void setIssuingDistributionPoint(byte[] arrby) {
            this.issuingDistributionPoint = Arrays.clone((byte[])arrby);
        }

        public void setIssuingDistributionPointEnabled(boolean bl) {
            this.issuingDistributionPointEnabled = bl;
        }

        public void setMaxBaseCRLNumber(BigInteger bigInteger) {
            this.maxBaseCRLNumber = bigInteger;
        }
    }

}

