package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters.Builder;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PKIXCertPathBuilderSpi extends CertPathBuilderSpi {
    private Exception certPathException;

    protected CertPathBuilderResult build(X509Certificate x509Certificate, PKIXExtendedBuilderParameters pKIXExtendedBuilderParameters, List list) {
        CertPathBuilderResult certPathBuilderResult = null;
        if (list.contains(x509Certificate)) {
            return null;
        }
        if (pKIXExtendedBuilderParameters.getExcludedCerts().contains(x509Certificate)) {
            return null;
        }
        if (pKIXExtendedBuilderParameters.getMaxPathLength() != -1 && list.size() - 1 > pKIXExtendedBuilderParameters.getMaxPathLength()) {
            return null;
        }
        list.add(x509Certificate);
        try {
            CertificateFactory certificateFactory = new CertificateFactory();
            PKIXCertPathValidatorSpi pKIXCertPathValidatorSpi = new PKIXCertPathValidatorSpi();
            CertPathBuilderResult certPathBuilderResult2;
            try {
                if (CertPathValidatorUtilities.findTrustAnchor(x509Certificate, pKIXExtendedBuilderParameters.getBaseParameters().getTrustAnchors(), pKIXExtendedBuilderParameters.getBaseParameters().getSigProvider()) != null) {
                    CertPath engineGenerateCertPath = certificateFactory.engineGenerateCertPath(list);
                    PKIXCertPathValidatorResult pKIXCertPathValidatorResult = (PKIXCertPathValidatorResult) pKIXCertPathValidatorSpi.engineValidate(engineGenerateCertPath, pKIXExtendedBuilderParameters);
                    return new PKIXCertPathBuilderResult(engineGenerateCertPath, pKIXCertPathValidatorResult.getTrustAnchor(), pKIXCertPathValidatorResult.getPolicyTree(), pKIXCertPathValidatorResult.getPublicKey());
                }
                List arrayList = new ArrayList();
                arrayList.addAll(pKIXExtendedBuilderParameters.getBaseParameters().getCertificateStores());
                arrayList.addAll(CertPathValidatorUtilities.getAdditionalStoresFromAltNames(x509Certificate.getExtensionValue(Extension.issuerAlternativeName.getId()), pKIXExtendedBuilderParameters.getBaseParameters().getNamedCertificateStoreMap()));
                Collection hashSet = new HashSet();
                hashSet.addAll(CertPathValidatorUtilities.findIssuerCerts(x509Certificate, pKIXExtendedBuilderParameters.getBaseParameters().getCertStores(), arrayList));
                if (hashSet.isEmpty()) {
                    throw new AnnotatedException("No issuer certificate for certificate in certification path found.");
                }
                Iterator it = hashSet.iterator();
                while (it.hasNext() && certPathBuilderResult == null) {
                    certPathBuilderResult = build((X509Certificate) it.next(), pKIXExtendedBuilderParameters, list);
                }
                certPathBuilderResult2 = certPathBuilderResult;
                if (certPathBuilderResult2 != null) {
                    return certPathBuilderResult2;
                }
                list.remove(x509Certificate);
                return certPathBuilderResult2;
            } catch (Throwable e) {
                throw new AnnotatedException("Cannot find issuer certificate for certificate in certification path.", e);
            } catch (Throwable e2) {
                throw new AnnotatedException("No additional X.509 stores can be added from certificate locations.", e2);
            } catch (Throwable e22) {
                throw new AnnotatedException("Certification path could not be validated.", e22);
            } catch (Throwable e222) {
                throw new AnnotatedException("Certification path could not be constructed from certificate list.", e222);
            } catch (Exception e3) {
                this.certPathException = e3;
                certPathBuilderResult2 = null;
            }
        } catch (Exception e4) {
            throw new RuntimeException("Exception creating support classes.");
        }
    }

    public CertPathBuilderResult engineBuild(CertPathParameters certPathParameters) {
        if ((certPathParameters instanceof PKIXBuilderParameters) || (certPathParameters instanceof ExtendedPKIXBuilderParameters) || (certPathParameters instanceof PKIXExtendedBuilderParameters)) {
            Iterator it;
            PKIXExtendedBuilderParameters build;
            if (certPathParameters instanceof PKIXBuilderParameters) {
                Builder builder;
                PKIXExtendedParameters.Builder builder2 = new PKIXExtendedParameters.Builder((PKIXBuilderParameters) certPathParameters);
                if (certPathParameters instanceof ExtendedPKIXParameters) {
                    ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters) certPathParameters;
                    for (PKIXCertStore addCertificateStore : extendedPKIXBuilderParameters.getAdditionalStores()) {
                        builder2.addCertificateStore(addCertificateStore);
                    }
                    builder = new Builder(builder2.build());
                    builder.addExcludedCerts(extendedPKIXBuilderParameters.getExcludedCerts());
                    builder.setMaxPathLength(extendedPKIXBuilderParameters.getMaxPathLength());
                } else {
                    builder = new Builder((PKIXBuilderParameters) certPathParameters);
                }
                build = builder.build();
            } else {
                build = (PKIXExtendedBuilderParameters) certPathParameters;
            }
            List arrayList = new ArrayList();
            PKIXCertStoreSelector targetConstraints = build.getBaseParameters().getTargetConstraints();
            try {
                Collection findCertificates = CertPathValidatorUtilities.findCertificates(targetConstraints, build.getBaseParameters().getCertificateStores());
                findCertificates.addAll(CertPathValidatorUtilities.findCertificates(targetConstraints, build.getBaseParameters().getCertStores()));
                if (findCertificates.isEmpty()) {
                    throw new CertPathBuilderException("No certificate found matching targetContraints.");
                }
                CertPathBuilderResult certPathBuilderResult = null;
                it = findCertificates.iterator();
                while (it.hasNext() && certPathBuilderResult == null) {
                    certPathBuilderResult = build((X509Certificate) it.next(), build, arrayList);
                }
                if (certPathBuilderResult != null || this.certPathException == null) {
                    if (certPathBuilderResult != null || this.certPathException != null) {
                        return certPathBuilderResult;
                    }
                    throw new CertPathBuilderException("Unable to find certificate chain.");
                } else if (this.certPathException instanceof AnnotatedException) {
                    throw new CertPathBuilderException(this.certPathException.getMessage(), this.certPathException.getCause());
                } else {
                    throw new CertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
                }
            } catch (Throwable e) {
                throw new ExtCertPathBuilderException("Error finding target certificate.", e);
            }
        }
        throw new InvalidAlgorithmParameterException("Parameters must be an instance of " + PKIXBuilderParameters.class.getName() + " or " + PKIXExtendedBuilderParameters.class.getName() + ".");
    }
}
