package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters.Builder;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;

public class PKIXAttrCertPathValidatorSpi extends CertPathValidatorSpi {
    private final JcaJceHelper helper;

    public PKIXAttrCertPathValidatorSpi() {
        this.helper = new BCJcaJceHelper();
    }

    public CertPathValidatorResult engineValidate(CertPath certPath, CertPathParameters certPathParameters) {
        if ((certPathParameters instanceof ExtendedPKIXParameters) || (certPathParameters instanceof PKIXExtendedParameters)) {
            Set set;
            Set set2;
            Set set3;
            PKIXExtendedParameters pKIXExtendedParameters;
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            HashSet hashSet3 = new HashSet();
            Set hashSet4 = new HashSet();
            if (certPathParameters instanceof PKIXParameters) {
                Set attrCertCheckers;
                Set prohibitedACAttributes;
                Set necessaryACAttributes;
                Builder builder = new Builder((PKIXParameters) certPathParameters);
                if (certPathParameters instanceof ExtendedPKIXParameters) {
                    ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters) certPathParameters;
                    builder.setUseDeltasEnabled(extendedPKIXParameters.isUseDeltasEnabled());
                    builder.setValidityModel(extendedPKIXParameters.getValidityModel());
                    attrCertCheckers = extendedPKIXParameters.getAttrCertCheckers();
                    prohibitedACAttributes = extendedPKIXParameters.getProhibitedACAttributes();
                    necessaryACAttributes = extendedPKIXParameters.getNecessaryACAttributes();
                } else {
                    Object obj = hashSet3;
                    Object obj2 = hashSet2;
                    Object obj3 = hashSet;
                }
                PKIXExtendedParameters build = builder.build();
                set = necessaryACAttributes;
                set2 = prohibitedACAttributes;
                set3 = attrCertCheckers;
                pKIXExtendedParameters = build;
            } else {
                Object obj4 = hashSet2;
                Object obj5 = hashSet;
                Object obj6 = hashSet3;
                pKIXExtendedParameters = (PKIXExtendedParameters) certPathParameters;
            }
            PKIXCertStoreSelector targetConstraints = pKIXExtendedParameters.getTargetConstraints();
            if (targetConstraints instanceof X509AttributeCertStoreSelector) {
                X509AttributeCertificate attributeCert = ((X509AttributeCertStoreSelector) targetConstraints).getAttributeCert();
                CertPath processAttrCert1 = RFC3281CertPathUtilities.processAttrCert1(attributeCert, pKIXExtendedParameters);
                CertPathValidatorResult processAttrCert2 = RFC3281CertPathUtilities.processAttrCert2(certPath, pKIXExtendedParameters);
                X509Certificate x509Certificate = (X509Certificate) certPath.getCertificates().get(0);
                RFC3281CertPathUtilities.processAttrCert3(x509Certificate, pKIXExtendedParameters);
                RFC3281CertPathUtilities.processAttrCert4(x509Certificate, hashSet4);
                RFC3281CertPathUtilities.processAttrCert5(attributeCert, pKIXExtendedParameters);
                RFC3281CertPathUtilities.processAttrCert7(attributeCert, certPath, processAttrCert1, pKIXExtendedParameters, set3);
                RFC3281CertPathUtilities.additionalChecks(attributeCert, set2, set);
                try {
                    RFC3281CertPathUtilities.checkCRLs(attributeCert, pKIXExtendedParameters, x509Certificate, CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, null, -1), certPath.getCertificates(), this.helper);
                    return processAttrCert2;
                } catch (Throwable e) {
                    throw new ExtCertPathValidatorException("Could not get validity date from attribute certificate.", e);
                }
            }
            throw new InvalidAlgorithmParameterException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + getClass().getName() + " class.");
        }
        throw new InvalidAlgorithmParameterException("Parameters must be a " + ExtendedPKIXParameters.class.getName() + " instance.");
    }
}
