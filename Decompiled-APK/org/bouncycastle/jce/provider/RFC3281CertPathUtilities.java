package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertSelector;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.TargetInformation;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters.Builder;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.x509.PKIXAttrCertChecker;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CertStoreSelector;

class RFC3281CertPathUtilities {
    private static final String AUTHORITY_INFO_ACCESS;
    private static final String CRL_DISTRIBUTION_POINTS;
    private static final String NO_REV_AVAIL;
    private static final String TARGET_INFORMATION;

    static {
        TARGET_INFORMATION = X509Extensions.TargetInformation.getId();
        NO_REV_AVAIL = X509Extensions.NoRevAvail.getId();
        CRL_DISTRIBUTION_POINTS = X509Extensions.CRLDistributionPoints.getId();
        AUTHORITY_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
    }

    RFC3281CertPathUtilities() {
    }

    protected static void additionalChecks(X509AttributeCertificate x509AttributeCertificate, Set set, Set set2) {
        for (String str : set) {
            if (x509AttributeCertificate.getAttributes(str) != null) {
                throw new CertPathValidatorException("Attribute certificate contains prohibited attribute: " + str + ".");
            }
        }
        for (String str2 : set2) {
            if (x509AttributeCertificate.getAttributes(str2) == null) {
                throw new CertPathValidatorException("Attribute certificate does not contain necessary attribute: " + str2 + ".");
            }
        }
    }

    private static void checkCRL(DistributionPoint distributionPoint, X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters, Date date, X509Certificate x509Certificate, CertStatus certStatus, ReasonsMask reasonsMask, List list, JcaJceHelper jcaJceHelper) {
        if (x509AttributeCertificate.getExtensionValue(X509Extensions.NoRevAvail.getId()) == null) {
            Date date2 = new Date(System.currentTimeMillis());
            if (date.getTime() > date2.getTime()) {
                throw new AnnotatedException("Validation time is in future.");
            }
            Iterator it = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509AttributeCertificate, date2, pKIXExtendedParameters).iterator();
            AnnotatedException annotatedException = null;
            Object obj = null;
            while (it.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                try {
                    X509CRL x509crl = (X509CRL) it.next();
                    ReasonsMask processCRLD = RFC3280CertPathUtilities.processCRLD(x509crl, distributionPoint);
                    if (processCRLD.hasNewReasons(reasonsMask)) {
                        PublicKey processCRLG = RFC3280CertPathUtilities.processCRLG(x509crl, RFC3280CertPathUtilities.processCRLF(x509crl, x509AttributeCertificate, null, null, pKIXExtendedParameters, list, jcaJceHelper));
                        X509CRL x509crl2 = null;
                        if (pKIXExtendedParameters.isUseDeltasEnabled()) {
                            x509crl2 = RFC3280CertPathUtilities.processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date2, x509crl, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()), processCRLG);
                        }
                        if (pKIXExtendedParameters.getValidityModel() == 1 || x509AttributeCertificate.getNotAfter().getTime() >= x509crl.getThisUpdate().getTime()) {
                            RFC3280CertPathUtilities.processCRLB1(distributionPoint, x509AttributeCertificate, x509crl);
                            RFC3280CertPathUtilities.processCRLB2(distributionPoint, x509AttributeCertificate, x509crl);
                            RFC3280CertPathUtilities.processCRLC(x509crl2, x509crl, pKIXExtendedParameters);
                            RFC3280CertPathUtilities.processCRLI(date, x509crl2, x509AttributeCertificate, certStatus, pKIXExtendedParameters);
                            RFC3280CertPathUtilities.processCRLJ(date, x509crl, x509AttributeCertificate, certStatus);
                            if (certStatus.getCertStatus() == 8) {
                                certStatus.setCertStatus(11);
                            }
                            reasonsMask.addReasons(processCRLD);
                            obj = 1;
                        } else {
                            throw new AnnotatedException("No valid CRL for current time found.");
                        }
                    }
                    continue;
                } catch (AnnotatedException e) {
                    annotatedException = e;
                }
            }
            if (obj == null) {
                throw annotatedException;
            }
        }
    }

    protected static void checkCRLs(X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, Date date, List list, JcaJceHelper jcaJceHelper) {
        if (!pKIXExtendedParameters.isRevocationEnabled()) {
            return;
        }
        if (x509AttributeCertificate.getExtensionValue(NO_REV_AVAIL) == null) {
            try {
                CRLDistPoint instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, CRL_DISTRIBUTION_POINTS));
                List arrayList = new ArrayList();
                try {
                    Object obj;
                    arrayList.addAll(CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(instance, pKIXExtendedParameters.getNamedCRLStoreMap()));
                    Builder builder = new Builder(pKIXExtendedParameters);
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        builder.addCRLStore((PKIXCRLStore) arrayList);
                    }
                    PKIXExtendedParameters build = builder.build();
                    CertStatus certStatus = new CertStatus();
                    ReasonsMask reasonsMask = new ReasonsMask();
                    Throwable th = null;
                    if (instance != null) {
                        try {
                            DistributionPoint[] distributionPoints = instance.getDistributionPoints();
                            int i = 0;
                            obj = null;
                            while (i < distributionPoints.length && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                                try {
                                    checkCRL(distributionPoints[i], x509AttributeCertificate, (PKIXExtendedParameters) build.clone(), date, x509Certificate, certStatus, reasonsMask, list, jcaJceHelper);
                                    obj = 1;
                                    i++;
                                } catch (Throwable e) {
                                    th = new AnnotatedException("No valid CRL for distribution point found.", e);
                                }
                            }
                        } catch (Throwable e2) {
                            throw new ExtCertPathValidatorException("Distribution points could not be read.", e2);
                        }
                    }
                    obj = null;
                    if (certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                        try {
                            checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, new ASN1InputStream(((X500Principal) x509AttributeCertificate.getIssuer().getPrincipals()[0]).getEncoded()).readObject()))), null, null), x509AttributeCertificate, (PKIXExtendedParameters) build.clone(), date, x509Certificate, certStatus, reasonsMask, list, jcaJceHelper);
                            obj = 1;
                        } catch (Throwable e22) {
                            throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", e22);
                        } catch (Throwable e222) {
                            th = new AnnotatedException("No valid CRL for distribution point found.", e222);
                        }
                    }
                    if (obj == null) {
                        throw new ExtCertPathValidatorException("No valid CRL found.", th);
                    } else if (certStatus.getCertStatus() != 11) {
                        throw new CertPathValidatorException(("Attribute certificate revocation after " + certStatus.getRevocationDate()) + ", reason: " + RFC3280CertPathUtilities.crlReasons[certStatus.getCertStatus()]);
                    } else {
                        if (!reasonsMask.isAllReasons() && certStatus.getCertStatus() == 11) {
                            certStatus.setCertStatus(12);
                        }
                        if (certStatus.getCertStatus() == 12) {
                            throw new CertPathValidatorException("Attribute certificate status could not be determined.");
                        }
                    }
                } catch (Throwable e2222) {
                    throw new CertPathValidatorException("No additional CRL locations could be decoded from CRL distribution point extension.", e2222);
                }
            } catch (Throwable e22222) {
                throw new CertPathValidatorException("CRL distribution point extension could not be read.", e22222);
            }
        } else if (x509AttributeCertificate.getExtensionValue(CRL_DISTRIBUTION_POINTS) != null || x509AttributeCertificate.getExtensionValue(AUTHORITY_INFO_ACCESS) != null) {
            throw new CertPathValidatorException("No rev avail extension is set, but also an AC revocation pointer.");
        }
    }

    protected static CertPath processAttrCert1(X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters) {
        int i = 0;
        Set<X509Certificate> hashSet = new HashSet();
        if (x509AttributeCertificate.getHolder().getIssuer() != null) {
            CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setSerialNumber(x509AttributeCertificate.getHolder().getSerialNumber());
            Principal[] issuer = x509AttributeCertificate.getHolder().getIssuer();
            int i2 = 0;
            while (i2 < issuer.length) {
                try {
                    if (issuer[i2] instanceof X500Principal) {
                        x509CertSelector.setIssuer(((X500Principal) issuer[i2]).getEncoded());
                    }
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder(x509CertSelector).build(), pKIXExtendedParameters.getCertStores()));
                    i2++;
                } catch (Throwable e) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", e);
                } catch (Throwable e2) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", e2);
                }
            }
            if (hashSet.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in base certificate ID for attribute certificate cannot be found.");
            }
        }
        if (x509AttributeCertificate.getHolder().getEntityNames() != null) {
            CertSelector x509CertStoreSelector = new X509CertStoreSelector();
            Principal[] entityNames = x509AttributeCertificate.getHolder().getEntityNames();
            while (i < entityNames.length) {
                try {
                    if (entityNames[i] instanceof X500Principal) {
                        x509CertStoreSelector.setIssuer(((X500Principal) entityNames[i]).getEncoded());
                    }
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder(x509CertStoreSelector).build(), pKIXExtendedParameters.getCertStores()));
                    i++;
                } catch (Throwable e22) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", e22);
                } catch (Throwable e222) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", e222);
                }
            }
            if (hashSet.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in entity name for attribute certificate cannot be found.");
            }
        }
        Builder builder = new Builder(pKIXExtendedParameters);
        ExtCertPathValidatorException extCertPathValidatorException = null;
        CertPathBuilderResult certPathBuilderResult = null;
        for (X509Certificate certificate : hashSet) {
            CertSelector x509CertStoreSelector2 = new X509CertStoreSelector();
            x509CertStoreSelector2.setCertificate(certificate);
            builder.setTargetConstraints(new PKIXCertStoreSelector.Builder(x509CertStoreSelector2).build());
            try {
                CertPathBuilderResult build;
                ExtCertPathValidatorException extCertPathValidatorException2;
                try {
                    ExtCertPathValidatorException extCertPathValidatorException3 = extCertPathValidatorException;
                    build = CertPathBuilder.getInstance("PKIX", BouncyCastleProvider.PROVIDER_NAME).build(new PKIXExtendedBuilderParameters.Builder(builder.build()).build());
                    extCertPathValidatorException2 = extCertPathValidatorException3;
                } catch (Throwable e2222) {
                    extCertPathValidatorException2 = new ExtCertPathValidatorException("Certification path for public key certificate of attribute certificate could not be build.", e2222);
                    build = certPathBuilderResult;
                } catch (InvalidAlgorithmParameterException e3) {
                    throw new RuntimeException(e3.getMessage());
                }
                certPathBuilderResult = build;
                extCertPathValidatorException = extCertPathValidatorException2;
            } catch (Throwable e22222) {
                throw new ExtCertPathValidatorException("Support class could not be created.", e22222);
            } catch (Throwable e222222) {
                throw new ExtCertPathValidatorException("Support class could not be created.", e222222);
            }
        }
        if (extCertPathValidatorException == null) {
            return certPathBuilderResult.getCertPath();
        }
        throw extCertPathValidatorException;
    }

    protected static CertPathValidatorResult processAttrCert2(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters) {
        try {
            try {
                return CertPathValidator.getInstance("PKIX", BouncyCastleProvider.PROVIDER_NAME).validate(certPath, pKIXExtendedParameters);
            } catch (Throwable e) {
                throw new ExtCertPathValidatorException("Certification path for issuer certificate of attribute certificate could not be validated.", e);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        } catch (Throwable e3) {
            throw new ExtCertPathValidatorException("Support class could not be created.", e3);
        } catch (Throwable e32) {
            throw new ExtCertPathValidatorException("Support class could not be created.", e32);
        }
    }

    protected static void processAttrCert3(X509Certificate x509Certificate, PKIXExtendedParameters pKIXExtendedParameters) {
        if (x509Certificate.getKeyUsage() != null && !x509Certificate.getKeyUsage()[0] && !x509Certificate.getKeyUsage()[1]) {
            throw new CertPathValidatorException("Attribute certificate issuer public key cannot be used to validate digital signatures.");
        } else if (x509Certificate.getBasicConstraints() != -1) {
            throw new CertPathValidatorException("Attribute certificate issuer is also a public key certificate issuer.");
        }
    }

    protected static void processAttrCert4(X509Certificate x509Certificate, Set set) {
        Object obj = null;
        for (TrustAnchor trustAnchor : set) {
            Object obj2 = (x509Certificate.getSubjectX500Principal().getName("RFC2253").equals(trustAnchor.getCAName()) || x509Certificate.equals(trustAnchor.getTrustedCert())) ? 1 : obj;
            obj = obj2;
        }
        if (obj == null) {
            throw new CertPathValidatorException("Attribute certificate issuer is not directly trusted.");
        }
    }

    protected static void processAttrCert5(X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters) {
        try {
            x509AttributeCertificate.checkValidity(CertPathValidatorUtilities.getValidDate(pKIXExtendedParameters));
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", e);
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", e2);
        }
    }

    protected static void processAttrCert7(X509AttributeCertificate x509AttributeCertificate, CertPath certPath, CertPath certPath2, PKIXExtendedParameters pKIXExtendedParameters, Set set) {
        Object criticalExtensionOIDs = x509AttributeCertificate.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs.contains(TARGET_INFORMATION)) {
            try {
                TargetInformation.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, TARGET_INFORMATION));
            } catch (Throwable e) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", e);
            } catch (Throwable e2) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", e2);
            }
        }
        criticalExtensionOIDs.remove(TARGET_INFORMATION);
        for (PKIXAttrCertChecker check : set) {
            check.check(x509AttributeCertificate, certPath, certPath2, criticalExtensionOIDs);
        }
        if (!criticalExtensionOIDs.isEmpty()) {
            throw new CertPathValidatorException("Attribute certificate contains unsupported critical extensions: " + criticalExtensionOIDs);
        }
    }
}
