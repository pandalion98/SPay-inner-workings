/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertPathValidatorResult
 *  java.security.cert.CertPathValidatorSpi
 *  java.security.cert.PKIXParameters
 *  java.security.cert.X509Certificate
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.jce.provider.RFC3281CertPathUtilities;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;

public class PKIXAttrCertPathValidatorSpi
extends CertPathValidatorSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CertPathValidatorResult engineValidate(CertPath certPath, CertPathParameters certPathParameters) {
        PKIXExtendedParameters pKIXExtendedParameters;
        PKIXCertStoreSelector pKIXCertStoreSelector;
        HashSet hashSet;
        HashSet hashSet2;
        Date date;
        HashSet hashSet3;
        if (!(certPathParameters instanceof ExtendedPKIXParameters) && !(certPathParameters instanceof PKIXExtendedParameters)) {
            throw new InvalidAlgorithmParameterException("Parameters must be a " + ExtendedPKIXParameters.class.getName() + " instance.");
        }
        HashSet hashSet4 = new HashSet();
        HashSet hashSet5 = new HashSet();
        HashSet hashSet6 = new HashSet();
        HashSet hashSet7 = new HashSet();
        if (certPathParameters instanceof PKIXParameters) {
            HashSet hashSet8;
            HashSet hashSet9;
            HashSet hashSet10;
            PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder((PKIXParameters)certPathParameters);
            if (certPathParameters instanceof ExtendedPKIXParameters) {
                ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)certPathParameters;
                builder.setUseDeltasEnabled(extendedPKIXParameters.isUseDeltasEnabled());
                builder.setValidityModel(extendedPKIXParameters.getValidityModel());
                hashSet8 = extendedPKIXParameters.getAttrCertCheckers();
                hashSet10 = extendedPKIXParameters.getProhibitedACAttributes();
                hashSet9 = extendedPKIXParameters.getNecessaryACAttributes();
            } else {
                hashSet9 = hashSet6;
                hashSet10 = hashSet5;
                hashSet8 = hashSet4;
            }
            PKIXExtendedParameters pKIXExtendedParameters2 = builder.build();
            hashSet2 = hashSet9;
            hashSet = hashSet10;
            hashSet3 = hashSet8;
            pKIXExtendedParameters = pKIXExtendedParameters2;
        } else {
            PKIXExtendedParameters pKIXExtendedParameters3 = (PKIXExtendedParameters)certPathParameters;
            hashSet = hashSet5;
            hashSet3 = hashSet4;
            hashSet2 = hashSet6;
            pKIXExtendedParameters = pKIXExtendedParameters3;
        }
        if (!((pKIXCertStoreSelector = pKIXExtendedParameters.getTargetConstraints()) instanceof X509AttributeCertStoreSelector)) {
            throw new InvalidAlgorithmParameterException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + this.getClass().getName() + " class.");
        }
        X509AttributeCertificate x509AttributeCertificate = ((X509AttributeCertStoreSelector)((Object)pKIXCertStoreSelector)).getAttributeCert();
        CertPath certPath2 = RFC3281CertPathUtilities.processAttrCert1(x509AttributeCertificate, pKIXExtendedParameters);
        CertPathValidatorResult certPathValidatorResult = RFC3281CertPathUtilities.processAttrCert2(certPath, pKIXExtendedParameters);
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(0);
        RFC3281CertPathUtilities.processAttrCert3(x509Certificate, pKIXExtendedParameters);
        RFC3281CertPathUtilities.processAttrCert4(x509Certificate, (Set)hashSet7);
        RFC3281CertPathUtilities.processAttrCert5(x509AttributeCertificate, pKIXExtendedParameters);
        RFC3281CertPathUtilities.processAttrCert7(x509AttributeCertificate, certPath, certPath2, pKIXExtendedParameters, (Set)hashSet3);
        RFC3281CertPathUtilities.additionalChecks(x509AttributeCertificate, (Set)hashSet, (Set)hashSet2);
        try {
            date = CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, null, -1);
        }
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathValidatorException("Could not get validity date from attribute certificate.", (Throwable)annotatedException);
        }
        RFC3281CertPathUtilities.checkCRLs(x509AttributeCertificate, pKIXExtendedParameters, x509Certificate, date, certPath.getCertificates(), this.helper);
        return certPathValidatorResult;
    }
}

