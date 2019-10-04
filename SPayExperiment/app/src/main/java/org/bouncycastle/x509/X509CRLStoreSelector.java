/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.cert.CRL
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLSelector
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  java.util.Date
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CRL;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class X509CRLStoreSelector
extends X509CRLSelector
implements Selector {
    private X509AttributeCertificate attrCertChecking;
    private boolean completeCRLEnabled = false;
    private boolean deltaCRLIndicator = false;
    private byte[] issuingDistributionPoint = null;
    private boolean issuingDistributionPointEnabled = false;
    private BigInteger maxBaseCRLNumber = null;

    public static X509CRLStoreSelector getInstance(X509CRLSelector x509CRLSelector) {
        if (x509CRLSelector == null) {
            throw new IllegalArgumentException("cannot create from null selector");
        }
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        x509CRLStoreSelector.setCertificateChecking(x509CRLSelector.getCertificateChecking());
        x509CRLStoreSelector.setDateAndTime(x509CRLSelector.getDateAndTime());
        try {
            x509CRLStoreSelector.setIssuerNames(x509CRLSelector.getIssuerNames());
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException(iOException.getMessage());
        }
        x509CRLStoreSelector.setIssuers(x509CRLSelector.getIssuers());
        x509CRLStoreSelector.setMaxCRLNumber(x509CRLSelector.getMaxCRL());
        x509CRLStoreSelector.setMinCRLNumber(x509CRLSelector.getMinCRL());
        return x509CRLStoreSelector;
    }

    @Override
    public Object clone() {
        X509CRLStoreSelector x509CRLStoreSelector = X509CRLStoreSelector.getInstance(this);
        x509CRLStoreSelector.deltaCRLIndicator = this.deltaCRLIndicator;
        x509CRLStoreSelector.completeCRLEnabled = this.completeCRLEnabled;
        x509CRLStoreSelector.maxBaseCRLNumber = this.maxBaseCRLNumber;
        x509CRLStoreSelector.attrCertChecking = this.attrCertChecking;
        x509CRLStoreSelector.issuingDistributionPointEnabled = this.issuingDistributionPointEnabled;
        x509CRLStoreSelector.issuingDistributionPoint = Arrays.clone(this.issuingDistributionPoint);
        return x509CRLStoreSelector;
    }

    public X509AttributeCertificate getAttrCertificateChecking() {
        return this.attrCertChecking;
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

    public boolean match(Object object) {
        ASN1Integer aSN1Integer;
        X509CRL x509CRL;
        block9 : {
            byte[] arrby;
            ASN1Integer aSN1Integer2;
            if (!(object instanceof X509CRL)) {
                return false;
            }
            x509CRL = (X509CRL)object;
            try {
                arrby = x509CRL.getExtensionValue(X509Extensions.DeltaCRLIndicator.getId());
                aSN1Integer = null;
                if (arrby == null) break block9;
            }
            catch (Exception exception) {
                return false;
            }
            aSN1Integer = aSN1Integer2 = ASN1Integer.getInstance(X509ExtensionUtil.fromExtensionValue(arrby));
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
            byte[] arrby = x509CRL.getExtensionValue(X509Extensions.IssuingDistributionPoint.getId());
            if (this.issuingDistributionPoint == null ? arrby != null : !Arrays.areEqual(arrby, this.issuingDistributionPoint)) {
                return false;
            }
        }
        return super.match((CRL)((X509CRL)object));
    }

    public boolean match(CRL cRL) {
        return this.match((Object)cRL);
    }

    public void setAttrCertificateChecking(X509AttributeCertificate x509AttributeCertificate) {
        this.attrCertChecking = x509AttributeCertificate;
    }

    public void setCompleteCRLEnabled(boolean bl) {
        this.completeCRLEnabled = bl;
    }

    public void setDeltaCRLIndicatorEnabled(boolean bl) {
        this.deltaCRLIndicator = bl;
    }

    public void setIssuingDistributionPoint(byte[] arrby) {
        this.issuingDistributionPoint = Arrays.clone(arrby);
    }

    public void setIssuingDistributionPointEnabled(boolean bl) {
        this.issuingDistributionPointEnabled = bl;
    }

    public void setMaxBaseCRLNumber(BigInteger bigInteger) {
        this.maxBaseCRLNumber = bigInteger;
    }
}

