package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters.Builder;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PKIXCertPathValidatorSpi extends CertPathValidatorSpi {
    private final JcaJceHelper helper;

    public PKIXCertPathValidatorSpi() {
        this.helper = new BCJcaJceHelper();
    }

    public CertPathValidatorResult engineValidate(CertPath certPath, CertPathParameters certPathParameters) {
        if (certPathParameters instanceof CertPathParameters) {
            PKIXExtendedParameters build;
            if (certPathParameters instanceof PKIXParameters) {
                Builder builder = new Builder((PKIXParameters) certPathParameters);
                if (certPathParameters instanceof ExtendedPKIXParameters) {
                    ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters) certPathParameters;
                    builder.setUseDeltasEnabled(extendedPKIXParameters.isUseDeltasEnabled());
                    builder.setValidityModel(extendedPKIXParameters.getValidityModel());
                }
                build = builder.build();
            } else {
                build = certPathParameters instanceof PKIXExtendedBuilderParameters ? ((PKIXExtendedBuilderParameters) certPathParameters).getBaseParameters() : (PKIXExtendedParameters) certPathParameters;
            }
            if (build.getTrustAnchors() == null) {
                throw new InvalidAlgorithmParameterException("trustAnchors is null, this is not allowed for certification path validation.");
            }
            List certificates = certPath.getCertificates();
            int size = certificates.size();
            if (certificates.isEmpty()) {
                throw new CertPathValidatorException("Certification path is empty.", null, certPath, 0);
            }
            Set initialPolicies = build.getInitialPolicies();
            try {
                TrustAnchor findTrustAnchor = CertPathValidatorUtilities.findTrustAnchor((X509Certificate) certificates.get(certificates.size() - 1), build.getTrustAnchors(), build.getSigProvider());
                if (findTrustAnchor == null) {
                    throw new CertPathValidatorException("Trust anchor for certification path not found.", null, certPath, -1);
                }
                int i;
                PublicKey publicKey;
                X500Name subjectPrincipal;
                PKIXExtendedParameters build2 = new Builder(build).setTrustAnchor(findTrustAnchor).build();
                List[] listArr = new ArrayList[(size + 1)];
                for (i = 0; i < listArr.length; i++) {
                    listArr[i] = new ArrayList();
                }
                Set hashSet = new HashSet();
                hashSet.add(RFC3280CertPathUtilities.ANY_POLICY);
                PKIXPolicyNode pKIXPolicyNode = new PKIXPolicyNode(new ArrayList(), 0, hashSet, null, new HashSet(), RFC3280CertPathUtilities.ANY_POLICY, false);
                listArr[0].add(pKIXPolicyNode);
                PKIXNameConstraintValidator pKIXNameConstraintValidator = new PKIXNameConstraintValidator();
                Set hashSet2 = new HashSet();
                int i2 = build2.isExplicitPolicyRequired() ? 0 : size + 1;
                int i3 = build2.isAnyPolicyInhibited() ? 0 : size + 1;
                int i4 = build2.isPolicyMappingInhibited() ? 0 : size + 1;
                X509Certificate trustedCert = findTrustAnchor.getTrustedCert();
                if (trustedCert != null) {
                    try {
                        publicKey = trustedCert.getPublicKey();
                        subjectPrincipal = PrincipalUtils.getSubjectPrincipal(trustedCert);
                    } catch (Throwable e) {
                        throw new ExtCertPathValidatorException("Subject of trust anchor could not be (re)encoded.", e, certPath, -1);
                    }
                }
                publicKey = findTrustAnchor.getCAPublicKey();
                subjectPrincipal = PrincipalUtils.getCA(findTrustAnchor);
                try {
                    AlgorithmIdentifier algorithmIdentifier = CertPathValidatorUtilities.getAlgorithmIdentifier(publicKey);
                    algorithmIdentifier.getAlgorithm();
                    algorithmIdentifier.getParameters();
                    if (build2.getTargetConstraints() == null || build2.getTargetConstraints().match((X509Certificate) certificates.get(0))) {
                        int i5;
                        Collection criticalExtensionOIDs;
                        Set hashSet3;
                        List<PKIXCertPathChecker> certPathCheckers = build2.getCertPathCheckers();
                        for (PKIXCertPathChecker init : certPathCheckers) {
                            init.init(false);
                        }
                        X509Certificate x509Certificate = null;
                        int i6 = size;
                        int i7 = i4;
                        int i8 = i3;
                        int i9 = i2;
                        PKIXPolicyNode pKIXPolicyNode2 = pKIXPolicyNode;
                        PublicKey publicKey2 = publicKey;
                        i2 = certificates.size() - 1;
                        X509Certificate x509Certificate2 = trustedCert;
                        while (i2 >= 0) {
                            PKIXPolicyNode pKIXPolicyNode3;
                            int i10 = size - i2;
                            x509Certificate = (X509Certificate) certificates.get(i2);
                            RFC3280CertPathUtilities.processCertA(certPath, build2, i2, publicKey2, i2 == certificates.size() + -1, subjectPrincipal, x509Certificate2, this.helper);
                            RFC3280CertPathUtilities.processCertBC(certPath, i2, pKIXNameConstraintValidator);
                            pKIXPolicyNode = RFC3280CertPathUtilities.processCertE(certPath, i2, RFC3280CertPathUtilities.processCertD(certPath, i2, hashSet2, pKIXPolicyNode2, listArr, i8));
                            RFC3280CertPathUtilities.processCertF(certPath, i2, pKIXPolicyNode, i9);
                            if (i10 == size) {
                                i5 = i7;
                                i4 = i9;
                                pKIXPolicyNode3 = pKIXPolicyNode;
                                i = i6;
                            } else if (x509Certificate == null || x509Certificate.getVersion() != 1) {
                                RFC3280CertPathUtilities.prepareNextCertA(certPath, i2);
                                pKIXPolicyNode3 = RFC3280CertPathUtilities.prepareCertB(certPath, i2, listArr, pKIXPolicyNode, i7);
                                RFC3280CertPathUtilities.prepareNextCertG(certPath, i2, pKIXNameConstraintValidator);
                                i = RFC3280CertPathUtilities.prepareNextCertH1(certPath, i2, i9);
                                i5 = RFC3280CertPathUtilities.prepareNextCertH2(certPath, i2, i7);
                                i3 = RFC3280CertPathUtilities.prepareNextCertH3(certPath, i2, i8);
                                int prepareNextCertI1 = RFC3280CertPathUtilities.prepareNextCertI1(certPath, i2, i);
                                i4 = RFC3280CertPathUtilities.prepareNextCertI2(certPath, i2, i5);
                                i8 = RFC3280CertPathUtilities.prepareNextCertJ(certPath, i2, i3);
                                RFC3280CertPathUtilities.prepareNextCertK(certPath, i2);
                                i5 = RFC3280CertPathUtilities.prepareNextCertM(certPath, i2, RFC3280CertPathUtilities.prepareNextCertL(certPath, i2, i6));
                                RFC3280CertPathUtilities.prepareNextCertN(certPath, i2);
                                criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                                if (criticalExtensionOIDs != null) {
                                    hashSet3 = new HashSet(criticalExtensionOIDs);
                                    hashSet3.remove(RFC3280CertPathUtilities.KEY_USAGE);
                                    hashSet3.remove(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                    hashSet3.remove(RFC3280CertPathUtilities.POLICY_MAPPINGS);
                                    hashSet3.remove(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                                    hashSet3.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                                    hashSet3.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                                    hashSet3.remove(RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                                    hashSet3.remove(RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                                    hashSet3.remove(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                                    hashSet3.remove(RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                                } else {
                                    hashSet3 = new HashSet();
                                }
                                RFC3280CertPathUtilities.prepareNextCertO(certPath, i2, hashSet3, certPathCheckers);
                                subjectPrincipal = PrincipalUtils.getSubjectPrincipal(x509Certificate);
                                try {
                                    publicKey2 = CertPathValidatorUtilities.getNextWorkingKey(certPath.getCertificates(), i2, this.helper);
                                    AlgorithmIdentifier algorithmIdentifier2 = CertPathValidatorUtilities.getAlgorithmIdentifier(publicKey2);
                                    algorithmIdentifier2.getAlgorithm();
                                    algorithmIdentifier2.getParameters();
                                    i = i5;
                                    i5 = i4;
                                    i4 = prepareNextCertI1;
                                    x509Certificate2 = x509Certificate;
                                } catch (Throwable e2) {
                                    throw new CertPathValidatorException("Next working key could not be retrieved.", e2, certPath, i2);
                                }
                            } else {
                                throw new CertPathValidatorException("Version 1 certificates can't be used as CA ones.", null, certPath, i2);
                            }
                            i2--;
                            i6 = i;
                            i7 = i5;
                            i9 = i4;
                            pKIXPolicyNode2 = pKIXPolicyNode3;
                        }
                        i5 = RFC3280CertPathUtilities.wrapupCertB(certPath, i2 + 1, RFC3280CertPathUtilities.wrapupCertA(i9, x509Certificate));
                        criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                        if (criticalExtensionOIDs != null) {
                            hashSet3 = new HashSet(criticalExtensionOIDs);
                            hashSet3.remove(RFC3280CertPathUtilities.KEY_USAGE);
                            hashSet3.remove(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                            hashSet3.remove(RFC3280CertPathUtilities.POLICY_MAPPINGS);
                            hashSet3.remove(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                            hashSet3.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                            hashSet3.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                            hashSet3.remove(RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                            hashSet3.remove(RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                            hashSet3.remove(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                            hashSet3.remove(RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                            hashSet3.remove(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS);
                        } else {
                            hashSet3 = new HashSet();
                        }
                        RFC3280CertPathUtilities.wrapupCertF(certPath, i2 + 1, certPathCheckers, hashSet3);
                        PolicyNode wrapupCertG = RFC3280CertPathUtilities.wrapupCertG(certPath, build2, initialPolicies, i2 + 1, listArr, pKIXPolicyNode2, hashSet2);
                        if (i5 > 0 || wrapupCertG != null) {
                            return new PKIXCertPathValidatorResult(findTrustAnchor, wrapupCertG, x509Certificate.getPublicKey());
                        }
                        throw new CertPathValidatorException("Path processing failed on policy.", null, certPath, i2);
                    }
                    throw new ExtCertPathValidatorException("Target certificate in certification path does not match targetConstraints.", null, certPath, 0);
                } catch (Throwable e22) {
                    throw new ExtCertPathValidatorException("Algorithm identifier of public key of trust anchor could not be read.", e22, certPath, -1);
                }
            } catch (Throwable e222) {
                throw new CertPathValidatorException(e222.getMessage(), e222, certPath, certificates.size() - 1);
            }
        }
        throw new InvalidAlgorithmParameterException("Parameters must be a " + PKIXParameters.class.getName() + " instance.");
    }
}
