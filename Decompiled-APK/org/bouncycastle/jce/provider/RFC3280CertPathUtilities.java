package org.bouncycastle.jce.provider;

import android.support.v4.os.EnvironmentCompat;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CRLSelector;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCRLStoreSelector;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters.Builder;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Arrays;

class RFC3280CertPathUtilities {
    public static final String ANY_POLICY = "2.5.29.32.0";
    public static final String AUTHORITY_KEY_IDENTIFIER;
    public static final String BASIC_CONSTRAINTS;
    public static final String CERTIFICATE_POLICIES;
    public static final String CRL_DISTRIBUTION_POINTS;
    public static final String CRL_NUMBER;
    protected static final int CRL_SIGN = 6;
    private static final PKIXCRLUtil CRL_UTIL;
    public static final String DELTA_CRL_INDICATOR;
    public static final String FRESHEST_CRL;
    public static final String INHIBIT_ANY_POLICY;
    public static final String ISSUING_DISTRIBUTION_POINT;
    protected static final int KEY_CERT_SIGN = 5;
    public static final String KEY_USAGE;
    public static final String NAME_CONSTRAINTS;
    public static final String POLICY_CONSTRAINTS;
    public static final String POLICY_MAPPINGS;
    public static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String[] crlReasons;

    static {
        CRL_UTIL = new PKIXCRLUtil();
        CERTIFICATE_POLICIES = Extension.certificatePolicies.getId();
        POLICY_MAPPINGS = Extension.policyMappings.getId();
        INHIBIT_ANY_POLICY = Extension.inhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = Extension.issuingDistributionPoint.getId();
        FRESHEST_CRL = Extension.freshestCRL.getId();
        DELTA_CRL_INDICATOR = Extension.deltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = Extension.policyConstraints.getId();
        BASIC_CONSTRAINTS = Extension.basicConstraints.getId();
        CRL_DISTRIBUTION_POINTS = Extension.cRLDistributionPoints.getId();
        SUBJECT_ALTERNATIVE_NAME = Extension.subjectAlternativeName.getId();
        NAME_CONSTRAINTS = Extension.nameConstraints.getId();
        AUTHORITY_KEY_IDENTIFIER = Extension.authorityKeyIdentifier.getId();
        KEY_USAGE = Extension.keyUsage.getId();
        CRL_NUMBER = Extension.cRLNumber.getId();
        crlReasons = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", EnvironmentCompat.MEDIA_UNKNOWN, "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    }

    RFC3280CertPathUtilities() {
    }

    private static void checkCRL(DistributionPoint distributionPoint, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, CertStatus certStatus, ReasonsMask reasonsMask, List list, JcaJceHelper jcaJceHelper) {
        Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        Iterator it = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509Certificate, date2, pKIXExtendedParameters).iterator();
        AnnotatedException annotatedException = null;
        Object obj = null;
        while (it.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
            try {
                X509CRL x509crl = (X509CRL) it.next();
                ReasonsMask processCRLD = processCRLD(x509crl, distributionPoint);
                if (processCRLD.hasNewReasons(reasonsMask)) {
                    PublicKey processCRLG = processCRLG(x509crl, processCRLF(x509crl, x509Certificate, x509Certificate2, publicKey, pKIXExtendedParameters, list, jcaJceHelper));
                    X509CRL x509crl2 = null;
                    Date date3 = pKIXExtendedParameters.getDate() != null ? pKIXExtendedParameters.getDate() : date2;
                    if (pKIXExtendedParameters.isUseDeltasEnabled()) {
                        x509crl2 = processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date3, x509crl, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()), processCRLG);
                    }
                    if (pKIXExtendedParameters.getValidityModel() == 1 || x509Certificate.getNotAfter().getTime() >= x509crl.getThisUpdate().getTime()) {
                        processCRLB1(distributionPoint, x509Certificate, x509crl);
                        processCRLB2(distributionPoint, x509Certificate, x509crl);
                        processCRLC(x509crl2, x509crl, pKIXExtendedParameters);
                        processCRLI(date, x509crl2, x509Certificate, certStatus, pKIXExtendedParameters);
                        processCRLJ(date, x509crl, x509Certificate, certStatus);
                        if (certStatus.getCertStatus() == 8) {
                            certStatus.setCertStatus(11);
                        }
                        reasonsMask.addReasons(processCRLD);
                        Collection criticalExtensionOIDs = x509crl.getCriticalExtensionOIDs();
                        if (criticalExtensionOIDs != null) {
                            Set hashSet = new HashSet(criticalExtensionOIDs);
                            hashSet.remove(Extension.issuingDistributionPoint.getId());
                            hashSet.remove(Extension.deltaCRLIndicator.getId());
                            if (!hashSet.isEmpty()) {
                                throw new AnnotatedException("CRL contains unsupported critical extensions.");
                            }
                        }
                        if (x509crl2 != null) {
                            criticalExtensionOIDs = x509crl2.getCriticalExtensionOIDs();
                            if (criticalExtensionOIDs != null) {
                                Set hashSet2 = new HashSet(criticalExtensionOIDs);
                                hashSet2.remove(Extension.issuingDistributionPoint.getId());
                                hashSet2.remove(Extension.deltaCRLIndicator.getId());
                                if (!hashSet2.isEmpty()) {
                                    throw new AnnotatedException("Delta CRL contains unsupported critical extension.");
                                }
                            }
                        }
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

    protected static void checkCRLs(PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, List list, JcaJceHelper jcaJceHelper) {
        Throwable th = null;
        try {
            CRLDistPoint instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, CRL_DISTRIBUTION_POINTS));
            Builder builder = new Builder(pKIXExtendedParameters);
            try {
                for (PKIXCRLStore addCRLStore : CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(instance, pKIXExtendedParameters.getNamedCRLStoreMap())) {
                    builder.addCRLStore(addCRLStore);
                }
                CertStatus certStatus = new CertStatus();
                ReasonsMask reasonsMask = new ReasonsMask();
                PKIXExtendedParameters build = builder.build();
                Object obj = null;
                if (instance != null) {
                    try {
                        DistributionPoint[] distributionPoints = instance.getDistributionPoints();
                        if (distributionPoints != null) {
                            int i = 0;
                            while (i < distributionPoints.length && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                                Object obj2;
                                Throwable th2;
                                try {
                                    checkCRL(distributionPoints[i], build, x509Certificate, date, x509Certificate2, publicKey, certStatus, reasonsMask, list, jcaJceHelper);
                                    obj2 = 1;
                                    th2 = th;
                                } catch (Throwable e) {
                                    th2 = e;
                                    obj2 = obj;
                                }
                                i++;
                                obj = obj2;
                                th = th2;
                            }
                        }
                    } catch (Throwable e2) {
                        throw new AnnotatedException("Distribution points could not be read.", e2);
                    }
                }
                if (certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                    try {
                        checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, new ASN1InputStream(PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate).getEncoded()).readObject()))), null, null), (PKIXExtendedParameters) pKIXExtendedParameters.clone(), x509Certificate, date, x509Certificate2, publicKey, certStatus, reasonsMask, list, jcaJceHelper);
                        obj = 1;
                    } catch (Throwable e22) {
                        throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", e22);
                    } catch (Throwable e222) {
                        th = e222;
                    }
                }
                if (obj == null) {
                    if (th instanceof AnnotatedException) {
                        throw th;
                    }
                    throw new AnnotatedException("No valid CRL found.", th);
                } else if (certStatus.getCertStatus() != 11) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    throw new AnnotatedException(("Certificate revocation after " + simpleDateFormat.format(certStatus.getRevocationDate())) + ", reason: " + crlReasons[certStatus.getCertStatus()]);
                } else {
                    if (!reasonsMask.isAllReasons() && certStatus.getCertStatus() == 11) {
                        certStatus.setCertStatus(12);
                    }
                    if (certStatus.getCertStatus() == 12) {
                        throw new AnnotatedException("Certificate status could not be determined.");
                    }
                }
            } catch (Throwable e2222) {
                throw new AnnotatedException("No additional CRL locations could be decoded from CRL distribution point extension.", e2222);
            }
        } catch (Throwable e22222) {
            throw new AnnotatedException("CRL distribution point extension could not be read.", e22222);
        }
    }

    protected static PKIXPolicyNode prepareCertB(CertPath certPath, int i, List[] listArr, PKIXPolicyNode pKIXPolicyNode, int i2) {
        List certificates = certPath.getCertificates();
        X509Certificate x509Certificate = (X509Certificate) certificates.get(i);
        int size = certificates.size() - i;
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, POLICY_MAPPINGS));
            if (instance != null) {
                String id;
                Map hashMap = new HashMap();
                Set<String> hashSet = new HashSet();
                for (int i3 = 0; i3 < instance.size(); i3++) {
                    ASN1Sequence aSN1Sequence = (ASN1Sequence) instance.getObjectAt(i3);
                    String id2 = ((ASN1ObjectIdentifier) aSN1Sequence.getObjectAt(0)).getId();
                    id = ((ASN1ObjectIdentifier) aSN1Sequence.getObjectAt(1)).getId();
                    if (hashMap.containsKey(id2)) {
                        ((Set) hashMap.get(id2)).add(id);
                    } else {
                        Set hashSet2 = new HashSet();
                        hashSet2.add(id);
                        hashMap.put(id2, hashSet2);
                        hashSet.add(id2);
                    }
                }
                for (String id3 : hashSet) {
                    PKIXPolicyNode pKIXPolicyNode2;
                    if (i2 > 0) {
                        Object obj;
                        for (PKIXPolicyNode pKIXPolicyNode22 : listArr[size]) {
                            if (pKIXPolicyNode22.getValidPolicy().equals(id3)) {
                                pKIXPolicyNode22.expectedPolicies = (Set) hashMap.get(id3);
                                obj = 1;
                                break;
                            }
                        }
                        obj = null;
                        if (obj == null) {
                            for (PKIXPolicyNode pKIXPolicyNode222 : listArr[size]) {
                                if (ANY_POLICY.equals(pKIXPolicyNode222.getValidPolicy())) {
                                    Set set = null;
                                    try {
                                        Enumeration objects = ((ASN1Sequence) CertPathValidatorUtilities.getExtensionValue(x509Certificate, CERTIFICATE_POLICIES)).getObjects();
                                        while (objects.hasMoreElements()) {
                                            try {
                                                PolicyInformation instance2 = PolicyInformation.getInstance(objects.nextElement());
                                                if (ANY_POLICY.equals(instance2.getPolicyIdentifier().getId())) {
                                                    try {
                                                        set = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                                                        break;
                                                    } catch (Throwable e) {
                                                        throw new ExtCertPathValidatorException("Policy qualifier info set could not be decoded.", e, certPath, i);
                                                    }
                                                }
                                            } catch (Throwable e2) {
                                                throw new CertPathValidatorException("Policy information could not be decoded.", e2, certPath, i);
                                            }
                                        }
                                        boolean z = false;
                                        if (x509Certificate.getCriticalExtensionOIDs() != null) {
                                            z = x509Certificate.getCriticalExtensionOIDs().contains(CERTIFICATE_POLICIES);
                                        }
                                        PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode) pKIXPolicyNode222.getParent();
                                        if (ANY_POLICY.equals(pKIXPolicyNode3.getValidPolicy())) {
                                            pKIXPolicyNode222 = new PKIXPolicyNode(new ArrayList(), size, (Set) hashMap.get(id3), pKIXPolicyNode3, set, id3, z);
                                            pKIXPolicyNode3.addChild(pKIXPolicyNode222);
                                            listArr[size].add(pKIXPolicyNode222);
                                        }
                                    } catch (Throwable e22) {
                                        throw new ExtCertPathValidatorException("Certificate policies extension could not be decoded.", e22, certPath, i);
                                    }
                                }
                            }
                            continue;
                        } else {
                            continue;
                        }
                    } else if (i2 <= 0) {
                        Iterator it = listArr[size].iterator();
                        while (it.hasNext()) {
                            pKIXPolicyNode222 = (PKIXPolicyNode) it.next();
                            if (pKIXPolicyNode222.getValidPolicy().equals(id3)) {
                                ((PKIXPolicyNode) pKIXPolicyNode222.getParent()).removeChild(pKIXPolicyNode222);
                                it.remove();
                                int i4 = size - 1;
                                PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNode;
                                while (i4 >= 0) {
                                    List list = listArr[i4];
                                    PKIXPolicyNode pKIXPolicyNode5 = pKIXPolicyNode4;
                                    for (int i5 = 0; i5 < list.size(); i5++) {
                                        pKIXPolicyNode222 = (PKIXPolicyNode) list.get(i5);
                                        if (!pKIXPolicyNode222.hasChildren()) {
                                            pKIXPolicyNode5 = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode5, listArr, pKIXPolicyNode222);
                                            if (pKIXPolicyNode5 == null) {
                                                break;
                                            }
                                        }
                                    }
                                    i4--;
                                    pKIXPolicyNode4 = pKIXPolicyNode5;
                                }
                                pKIXPolicyNode = pKIXPolicyNode4;
                            }
                        }
                    }
                }
            }
            return pKIXPolicyNode;
        } catch (Throwable e222) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", e222, certPath, i);
        }
    }

    protected static void prepareNextCertA(CertPath certPath, int i) {
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), POLICY_MAPPINGS));
            if (instance != null) {
                int i2 = 0;
                while (i2 < instance.size()) {
                    try {
                        ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObjectAt(i2));
                        ASN1ObjectIdentifier instance3 = ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(0));
                        ASN1ObjectIdentifier instance4 = ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(1));
                        if (ANY_POLICY.equals(instance3.getId())) {
                            throw new CertPathValidatorException("IssuerDomainPolicy is anyPolicy", null, certPath, i);
                        } else if (ANY_POLICY.equals(instance4.getId())) {
                            throw new CertPathValidatorException("SubjectDomainPolicy is anyPolicy,", null, certPath, i);
                        } else {
                            i2++;
                        }
                    } catch (Throwable e) {
                        throw new ExtCertPathValidatorException("Policy mappings extension contents could not be decoded.", e, certPath, i);
                    }
                }
            }
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", e2, certPath, i);
        }
    }

    protected static void prepareNextCertG(CertPath certPath, int i, PKIXNameConstraintValidator pKIXNameConstraintValidator) {
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), NAME_CONSTRAINTS));
            NameConstraints instance2 = instance != null ? NameConstraints.getInstance(instance) : null;
            if (instance2 != null) {
                GeneralSubtree[] permittedSubtrees = instance2.getPermittedSubtrees();
                if (permittedSubtrees != null) {
                    try {
                        pKIXNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                    } catch (Throwable e) {
                        throw new ExtCertPathValidatorException("Permitted subtrees cannot be build from name constraints extension.", e, certPath, i);
                    }
                }
                permittedSubtrees = instance2.getExcludedSubtrees();
                if (permittedSubtrees != null) {
                    int i2 = 0;
                    while (i2 != permittedSubtrees.length) {
                        try {
                            pKIXNameConstraintValidator.addExcludedSubtree(permittedSubtrees[i2]);
                            i2++;
                        } catch (Throwable e2) {
                            throw new ExtCertPathValidatorException("Excluded subtrees cannot be build from name constraints extension.", e2, certPath, i);
                        }
                    }
                }
            }
        } catch (Throwable e22) {
            throw new ExtCertPathValidatorException("Name constraints extension could not be decoded.", e22, certPath, i);
        }
    }

    protected static int prepareNextCertH1(CertPath certPath, int i, int i2) {
        return (CertPathValidatorUtilities.isSelfIssued((X509Certificate) certPath.getCertificates().get(i)) || i2 == 0) ? i2 : i2 - 1;
    }

    protected static int prepareNextCertH2(CertPath certPath, int i, int i2) {
        return (CertPathValidatorUtilities.isSelfIssued((X509Certificate) certPath.getCertificates().get(i)) || i2 == 0) ? i2 : i2 - 1;
    }

    protected static int prepareNextCertH3(CertPath certPath, int i, int i2) {
        return (CertPathValidatorUtilities.isSelfIssued((X509Certificate) certPath.getCertificates().get(i)) || i2 == 0) ? i2 : i2 - 1;
    }

    protected static int prepareNextCertI1(CertPath certPath, int i, int i2) {
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), POLICY_CONSTRAINTS));
            if (instance == null) {
                return i2;
            }
            Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                try {
                    ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                    if (instance2.getTagNo() == 0) {
                        int intValue = ASN1Integer.getInstance(instance2, false).getValue().intValue();
                        return intValue < i2 ? intValue : i2;
                    }
                } catch (Throwable e) {
                    throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", e, certPath, i);
                }
            }
            return i2;
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", e2, certPath, i);
        }
    }

    protected static int prepareNextCertI2(CertPath certPath, int i, int i2) {
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), POLICY_CONSTRAINTS));
            if (instance == null) {
                return i2;
            }
            Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                try {
                    ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                    if (instance2.getTagNo() == 1) {
                        int intValue = ASN1Integer.getInstance(instance2, false).getValue().intValue();
                        return intValue < i2 ? intValue : i2;
                    }
                } catch (Throwable e) {
                    throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", e, certPath, i);
                }
            }
            return i2;
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", e2, certPath, i);
        }
    }

    protected static int prepareNextCertJ(CertPath certPath, int i, int i2) {
        try {
            ASN1Integer instance = ASN1Integer.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), INHIBIT_ANY_POLICY));
            if (instance == null) {
                return i2;
            }
            int intValue = instance.getValue().intValue();
            return intValue < i2 ? intValue : i2;
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Inhibit any-policy extension cannot be decoded.", e, certPath, i);
        }
    }

    protected static void prepareNextCertK(CertPath certPath, int i) {
        try {
            BasicConstraints instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), BASIC_CONSTRAINTS));
            if (instance == null) {
                throw new CertPathValidatorException("Intermediate certificate lacks BasicConstraints");
            } else if (!instance.isCA()) {
                throw new CertPathValidatorException("Not a CA certificate");
            }
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", e, certPath, i);
        }
    }

    protected static int prepareNextCertL(CertPath certPath, int i, int i2) {
        if (CertPathValidatorUtilities.isSelfIssued((X509Certificate) certPath.getCertificates().get(i))) {
            return i2;
        }
        if (i2 > 0) {
            return i2 - 1;
        }
        throw new ExtCertPathValidatorException("Max path length not greater than zero", null, certPath, i);
    }

    protected static int prepareNextCertM(CertPath certPath, int i, int i2) {
        try {
            BasicConstraints instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), BASIC_CONSTRAINTS));
            if (instance == null) {
                return i2;
            }
            BigInteger pathLenConstraint = instance.getPathLenConstraint();
            if (pathLenConstraint == null) {
                return i2;
            }
            int intValue = pathLenConstraint.intValue();
            return intValue < i2 ? intValue : i2;
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", e, certPath, i);
        }
    }

    protected static void prepareNextCertN(CertPath certPath, int i) {
        boolean[] keyUsage = ((X509Certificate) certPath.getCertificates().get(i)).getKeyUsage();
        if (keyUsage != null && !keyUsage[KEY_CERT_SIGN]) {
            throw new ExtCertPathValidatorException("Issuer certificate keyusage extension is critical and does not permit key signing.", null, certPath, i);
        }
    }

    protected static void prepareNextCertO(CertPath certPath, int i, Set set, List list) {
        X509Certificate x509Certificate = (X509Certificate) certPath.getCertificates().get(i);
        for (PKIXCertPathChecker check : list) {
            try {
                check.check(x509Certificate, set);
            } catch (CertPathValidatorException e) {
                throw new CertPathValidatorException(e.getMessage(), e.getCause(), certPath, i);
            }
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension: " + set, null, certPath, i);
        }
    }

    protected static Set processCRLA1i(Date date, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, X509CRL x509crl) {
        Set hashSet = new HashSet();
        if (pKIXExtendedParameters.isUseDeltasEnabled()) {
            try {
                CRLDistPoint instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, FRESHEST_CRL));
                if (instance == null) {
                    try {
                        instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509crl, FRESHEST_CRL));
                    } catch (Throwable e) {
                        throw new AnnotatedException("Freshest CRL extension could not be decoded from CRL.", e);
                    }
                }
                if (instance != null) {
                    List arrayList = new ArrayList();
                    arrayList.addAll(pKIXExtendedParameters.getCRLStores());
                    try {
                        arrayList.addAll(CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(instance, pKIXExtendedParameters.getNamedCRLStoreMap()));
                        try {
                            hashSet.addAll(CertPathValidatorUtilities.getDeltaCRLs(date, x509crl, pKIXExtendedParameters.getCertStores(), arrayList));
                        } catch (Throwable e2) {
                            throw new AnnotatedException("Exception obtaining delta CRLs.", e2);
                        }
                    } catch (Throwable e22) {
                        throw new AnnotatedException("No new delta CRL locations could be added from Freshest CRL extension.", e22);
                    }
                }
            } catch (Throwable e222) {
                throw new AnnotatedException("Freshest CRL extension could not be decoded from certificate.", e222);
            }
        }
        return hashSet;
    }

    protected static Set[] processCRLA1ii(Date date, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, X509CRL x509crl) {
        Set hashSet = new HashSet();
        CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.setCertificateChecking(x509Certificate);
        try {
            x509CRLSelector.addIssuerName(PrincipalUtils.getIssuerPrincipal(x509crl).getEncoded());
            PKIXCRLStoreSelector build = new PKIXCRLStoreSelector.Builder(x509CRLSelector).setCompleteCRLEnabled(true).build();
            if (pKIXExtendedParameters.getDate() != null) {
                date = pKIXExtendedParameters.getDate();
            }
            Set findCRLs = CRL_UTIL.findCRLs(build, date, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores());
            if (pKIXExtendedParameters.isUseDeltasEnabled()) {
                try {
                    hashSet.addAll(CertPathValidatorUtilities.getDeltaCRLs(date, x509crl, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()));
                } catch (Throwable e) {
                    throw new AnnotatedException("Exception obtaining delta CRLs.", e);
                }
            }
            return new Set[]{findCRLs, hashSet};
        } catch (Throwable e2) {
            throw new AnnotatedException("Cannot extract issuer from CRL." + e2, e2);
        }
    }

    protected static void processCRLB1(DistributionPoint distributionPoint, Object obj, X509CRL x509crl) {
        int i = 0;
        ASN1Primitive extensionValue = CertPathValidatorUtilities.getExtensionValue(x509crl, ISSUING_DISTRIBUTION_POINT);
        int i2 = (extensionValue == null || !IssuingDistributionPoint.getInstance(extensionValue).isIndirectCRL()) ? 0 : 1;
        try {
            int i3;
            byte[] encoded = PrincipalUtils.getIssuerPrincipal(x509crl).getEncoded();
            if (distributionPoint.getCRLIssuer() != null) {
                GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
                i3 = 0;
                while (i < names.length) {
                    if (names[i].getTagNo() == 4) {
                        try {
                            if (Arrays.areEqual(names[i].getName().toASN1Primitive().getEncoded(), encoded)) {
                                i3 = 1;
                            }
                        } catch (Throwable e) {
                            throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", e);
                        }
                    }
                    i++;
                }
                if (i3 != 0 && i2 == 0) {
                    throw new AnnotatedException("Distribution point contains cRLIssuer field but CRL is not indirect.");
                } else if (i3 == 0) {
                    throw new AnnotatedException("CRL issuer of CRL does not match CRL issuer of distribution point.");
                }
            }
            i3 = PrincipalUtils.getIssuerPrincipal(x509crl).equals(PrincipalUtils.getEncodedIssuerPrincipal(obj)) ? 1 : 0;
            if (i3 == 0) {
                throw new AnnotatedException("Cannot find matching CRL issuer for certificate.");
            }
        } catch (Throwable e2) {
            throw new AnnotatedException("Exception encoding CRL issuer: " + e2.getMessage(), e2);
        }
    }

    protected static void processCRLB2(DistributionPoint distributionPoint, Object obj, X509CRL x509crl) {
        int i = 0;
        try {
            IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509crl, ISSUING_DISTRIBUTION_POINT));
            if (instance != null) {
                if (instance.getDistributionPoint() != null) {
                    DistributionPointName distributionPoint2 = IssuingDistributionPoint.getInstance(instance).getDistributionPoint();
                    List arrayList = new ArrayList();
                    if (distributionPoint2.getType() == 0) {
                        GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                        for (Object add : names) {
                            arrayList.add(add);
                        }
                    }
                    if (distributionPoint2.getType() == 1) {
                        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
                        try {
                            Enumeration objects = ASN1Sequence.getInstance(PrincipalUtils.getIssuerPrincipal(x509crl)).getObjects();
                            while (objects.hasMoreElements()) {
                                aSN1EncodableVector.add((ASN1Encodable) objects.nextElement());
                            }
                            aSN1EncodableVector.add(distributionPoint2.getName());
                            arrayList.add(new GeneralName(X500Name.getInstance(new DERSequence(aSN1EncodableVector))));
                        } catch (Throwable e) {
                            throw new AnnotatedException("Could not read CRL issuer.", e);
                        }
                    }
                    GeneralName[] names2;
                    if (distributionPoint.getDistributionPoint() != null) {
                        DistributionPointName distributionPoint3 = distributionPoint.getDistributionPoint();
                        GeneralName[] generalNameArr = null;
                        if (distributionPoint3.getType() == 0) {
                            generalNameArr = GeneralNames.getInstance(distributionPoint3.getName()).getNames();
                        }
                        if (distributionPoint3.getType() == 1) {
                            if (distributionPoint.getCRLIssuer() != null) {
                                names2 = distributionPoint.getCRLIssuer().getNames();
                            } else {
                                generalNameArr = new GeneralName[1];
                                try {
                                    generalNameArr[0] = new GeneralName(X500Name.getInstance(PrincipalUtils.getEncodedIssuerPrincipal(obj).getEncoded()));
                                    names2 = generalNameArr;
                                } catch (Throwable e2) {
                                    throw new AnnotatedException("Could not read certificate issuer.", e2);
                                }
                            }
                            for (int i2 = 0; i2 < names2.length; i2++) {
                                Enumeration objects2 = ASN1Sequence.getInstance(names2[i2].getName().toASN1Primitive()).getObjects();
                                ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
                                while (objects2.hasMoreElements()) {
                                    aSN1EncodableVector2.add((ASN1Encodable) objects2.nextElement());
                                }
                                aSN1EncodableVector2.add(distributionPoint3.getName());
                                names2[i2] = new GeneralName(X500Name.getInstance(new DERSequence(aSN1EncodableVector2)));
                            }
                        } else {
                            names2 = generalNameArr;
                        }
                        if (names2 != null) {
                            for (Object contains : names2) {
                                if (arrayList.contains(contains)) {
                                    i = 1;
                                    break;
                                }
                            }
                        }
                        if (i == 0) {
                            throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                        }
                    } else if (distributionPoint.getCRLIssuer() == null) {
                        throw new AnnotatedException("Either the cRLIssuer or the distributionPoint field must be contained in DistributionPoint.");
                    } else {
                        names2 = distributionPoint.getCRLIssuer().getNames();
                        for (Object contains2 : names2) {
                            if (arrayList.contains(contains2)) {
                                i = 1;
                                break;
                            }
                        }
                        if (i == 0) {
                            throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                        }
                    }
                }
                try {
                    BasicConstraints instance2 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension) obj, BASIC_CONSTRAINTS));
                    if (obj instanceof X509Certificate) {
                        if (instance.onlyContainsUserCerts() && instance2 != null && instance2.isCA()) {
                            throw new AnnotatedException("CA Cert CRL only contains user certificates.");
                        } else if (instance.onlyContainsCACerts() && (instance2 == null || !instance2.isCA())) {
                            throw new AnnotatedException("End CRL only contains CA certificates.");
                        }
                    }
                    if (instance.onlyContainsAttributeCerts()) {
                        throw new AnnotatedException("onlyContainsAttributeCerts boolean is asserted.");
                    }
                } catch (Throwable e22) {
                    throw new AnnotatedException("Basic constraints extension could not be decoded.", e22);
                }
            }
        } catch (Throwable e222) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", e222);
        }
    }

    protected static void processCRLC(X509CRL x509crl, X509CRL x509crl2, PKIXExtendedParameters pKIXExtendedParameters) {
        Object obj = 1;
        if (x509crl != null) {
            try {
                IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509crl2, ISSUING_DISTRIBUTION_POINT));
                if (!pKIXExtendedParameters.isUseDeltasEnabled()) {
                    return;
                }
                if (PrincipalUtils.getIssuerPrincipal(x509crl).equals(PrincipalUtils.getIssuerPrincipal(x509crl2))) {
                    try {
                        IssuingDistributionPoint instance2 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509crl, ISSUING_DISTRIBUTION_POINT));
                        if (instance != null ? instance.equals(instance2) : instance2 == null) {
                            obj = null;
                        }
                        if (obj == null) {
                            throw new AnnotatedException("Issuing distribution point extension from delta CRL and complete CRL does not match.");
                        }
                        try {
                            ASN1Primitive extensionValue = CertPathValidatorUtilities.getExtensionValue(x509crl2, AUTHORITY_KEY_IDENTIFIER);
                            try {
                                ASN1Primitive extensionValue2 = CertPathValidatorUtilities.getExtensionValue(x509crl, AUTHORITY_KEY_IDENTIFIER);
                                if (extensionValue == null) {
                                    throw new AnnotatedException("CRL authority key identifier is null.");
                                } else if (extensionValue2 == null) {
                                    throw new AnnotatedException("Delta CRL authority key identifier is null.");
                                } else if (!extensionValue.equals(extensionValue2)) {
                                    throw new AnnotatedException("Delta CRL authority key identifier does not match complete CRL authority key identifier.");
                                } else {
                                    return;
                                }
                            } catch (Throwable e) {
                                throw new AnnotatedException("Authority key identifier extension could not be extracted from delta CRL.", e);
                            }
                        } catch (Throwable e2) {
                            throw new AnnotatedException("Authority key identifier extension could not be extracted from complete CRL.", e2);
                        }
                    } catch (Throwable e22) {
                        throw new AnnotatedException("Issuing distribution point extension from delta CRL could not be decoded.", e22);
                    }
                }
                throw new AnnotatedException("Complete CRL issuer does not match delta CRL issuer.");
            } catch (Throwable e222) {
                throw new AnnotatedException("Issuing distribution point extension could not be decoded.", e222);
            }
        }
    }

    protected static ReasonsMask processCRLD(X509CRL x509crl, DistributionPoint distributionPoint) {
        try {
            IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509crl, ISSUING_DISTRIBUTION_POINT));
            if (instance != null && instance.getOnlySomeReasons() != null && distributionPoint.getReasons() != null) {
                return new ReasonsMask(distributionPoint.getReasons()).intersect(new ReasonsMask(instance.getOnlySomeReasons()));
            }
            if ((instance == null || instance.getOnlySomeReasons() == null) && distributionPoint.getReasons() == null) {
                return ReasonsMask.allReasons;
            }
            return (distributionPoint.getReasons() == null ? ReasonsMask.allReasons : new ReasonsMask(distributionPoint.getReasons())).intersect(instance == null ? ReasonsMask.allReasons : new ReasonsMask(instance.getOnlySomeReasons()));
        } catch (Throwable e) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", e);
        }
    }

    protected static Set processCRLF(X509CRL x509crl, Object obj, X509Certificate x509Certificate, PublicKey publicKey, PKIXExtendedParameters pKIXExtendedParameters, List list, JcaJceHelper jcaJceHelper) {
        CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PrincipalUtils.getIssuerPrincipal(x509crl).getEncoded());
            PKIXCertStoreSelector build = new PKIXCertStoreSelector.Builder(x509CertSelector).build();
            try {
                Collection<X509Certificate> findCertificates = CertPathValidatorUtilities.findCertificates(build, pKIXExtendedParameters.getCertificateStores());
                findCertificates.addAll(CertPathValidatorUtilities.findCertificates(build, pKIXExtendedParameters.getCertStores()));
                findCertificates.add(x509Certificate);
                List arrayList = new ArrayList();
                List arrayList2 = new ArrayList();
                for (X509Certificate x509Certificate2 : findCertificates) {
                    if (x509Certificate2.equals(x509Certificate)) {
                        arrayList.add(x509Certificate2);
                        arrayList2.add(publicKey);
                    } else {
                        try {
                            PKIXCertPathBuilderSpi pKIXCertPathBuilderSpi = new PKIXCertPathBuilderSpi();
                            CertSelector x509CertSelector2 = new X509CertSelector();
                            x509CertSelector2.setCertificate(x509Certificate2);
                            Builder targetConstraints = new Builder(pKIXExtendedParameters).setTargetConstraints(new PKIXCertStoreSelector.Builder(x509CertSelector2).build());
                            if (list.contains(x509Certificate2)) {
                                targetConstraints.setRevocationEnabled(false);
                            } else {
                                targetConstraints.setRevocationEnabled(true);
                            }
                            List certificates = pKIXCertPathBuilderSpi.engineBuild(new PKIXExtendedBuilderParameters.Builder(targetConstraints.build()).build()).getCertPath().getCertificates();
                            arrayList.add(x509Certificate2);
                            arrayList2.add(CertPathValidatorUtilities.getNextWorkingKey(certificates, 0, jcaJceHelper));
                        } catch (Throwable e) {
                            throw new AnnotatedException("CertPath for CRL signer failed to validate.", e);
                        } catch (Throwable e2) {
                            throw new AnnotatedException("Public key of issuer certificate of CRL could not be retrieved.", e2);
                        } catch (Exception e3) {
                            throw new AnnotatedException(e3.getMessage());
                        }
                    }
                }
                Set hashSet = new HashSet();
                int i = 0;
                AnnotatedException annotatedException = null;
                while (i < arrayList.size()) {
                    AnnotatedException annotatedException2;
                    boolean[] keyUsage = ((X509Certificate) arrayList.get(i)).getKeyUsage();
                    if (keyUsage == null || (keyUsage.length >= 7 && keyUsage[CRL_SIGN])) {
                        hashSet.add(arrayList2.get(i));
                        annotatedException2 = annotatedException;
                    } else {
                        annotatedException2 = new AnnotatedException("Issuer certificate key usage extension does not permit CRL signing.");
                    }
                    i++;
                    annotatedException = annotatedException2;
                }
                if (hashSet.isEmpty() && annotatedException == null) {
                    throw new AnnotatedException("Cannot find a valid issuer certificate.");
                } else if (!hashSet.isEmpty() || annotatedException == null) {
                    return hashSet;
                } else {
                    throw annotatedException;
                }
            } catch (Throwable e22) {
                throw new AnnotatedException("Issuer certificate for CRL cannot be searched.", e22);
            }
        } catch (Throwable e222) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate for CRL could not be set.", e222);
        }
    }

    protected static PublicKey processCRLG(X509CRL x509crl, Set set) {
        Throwable th = null;
        for (PublicKey publicKey : set) {
            try {
                x509crl.verify(publicKey);
                return publicKey;
            } catch (Exception e) {
                th = e;
            }
        }
        throw new AnnotatedException("Cannot verify CRL.", th);
    }

    protected static X509CRL processCRLH(Set set, PublicKey publicKey) {
        Throwable th = null;
        for (X509CRL x509crl : set) {
            try {
                x509crl.verify(publicKey);
                return x509crl;
            } catch (Exception e) {
                th = e;
            }
        }
        if (th == null) {
            return null;
        }
        throw new AnnotatedException("Cannot verify delta CRL.", th);
    }

    protected static void processCRLI(Date date, X509CRL x509crl, Object obj, CertStatus certStatus, PKIXExtendedParameters pKIXExtendedParameters) {
        if (pKIXExtendedParameters.isUseDeltasEnabled() && x509crl != null) {
            CertPathValidatorUtilities.getCertStatus(date, x509crl, obj, certStatus);
        }
    }

    protected static void processCRLJ(Date date, X509CRL x509crl, Object obj, CertStatus certStatus) {
        if (certStatus.getCertStatus() == 11) {
            CertPathValidatorUtilities.getCertStatus(date, x509crl, obj, certStatus);
        }
    }

    protected static void processCertA(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters, int i, PublicKey publicKey, boolean z, X500Name x500Name, X509Certificate x509Certificate, JcaJceHelper jcaJceHelper) {
        List certificates = certPath.getCertificates();
        X509Certificate x509Certificate2 = (X509Certificate) certificates.get(i);
        if (!z) {
            try {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, publicKey, pKIXExtendedParameters.getSigProvider());
            } catch (Throwable e) {
                throw new ExtCertPathValidatorException("Could not validate certificate signature.", e, certPath, i);
            }
        }
        try {
            x509Certificate2.checkValidity(CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, certPath, i));
            if (pKIXExtendedParameters.isRevocationEnabled()) {
                try {
                    checkCRLs(pKIXExtendedParameters, x509Certificate2, CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, certPath, i), x509Certificate, publicKey, certificates, jcaJceHelper);
                } catch (Throwable e2) {
                    throw new ExtCertPathValidatorException(e2.getMessage(), e2.getCause() != null ? e2.getCause() : e2, certPath, i);
                }
            }
            if (!PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate2).equals(x500Name)) {
                throw new ExtCertPathValidatorException("IssuerName(" + PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate2) + ") does not match SubjectName(" + x500Name + ") of signing certificate.", null, certPath, i);
            }
        } catch (Throwable e3) {
            throw new ExtCertPathValidatorException("Could not validate certificate: " + e3.getMessage(), e3, certPath, i);
        } catch (Throwable e32) {
            throw new ExtCertPathValidatorException("Could not validate certificate: " + e32.getMessage(), e32, certPath, i);
        } catch (Throwable e322) {
            throw new ExtCertPathValidatorException("Could not validate time of certificate.", e322, certPath, i);
        }
    }

    protected static void processCertBC(CertPath certPath, int i, PKIXNameConstraintValidator pKIXNameConstraintValidator) {
        int i2 = 0;
        List certificates = certPath.getCertificates();
        X509Certificate x509Certificate = (X509Certificate) certificates.get(i);
        int size = certificates.size();
        int i3 = size - i;
        if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate) || i3 >= size) {
            try {
                ASN1Sequence instance = ASN1Sequence.getInstance(PrincipalUtils.getSubjectPrincipal(x509Certificate).getEncoded());
                try {
                    pKIXNameConstraintValidator.checkPermittedDN(instance);
                    pKIXNameConstraintValidator.checkExcludedDN(instance);
                    try {
                        GeneralNames instance2 = GeneralNames.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, SUBJECT_ALTERNATIVE_NAME));
                        RDN[] rDNs = X500Name.getInstance(instance).getRDNs(BCStyle.EmailAddress);
                        size = 0;
                        while (size != rDNs.length) {
                            GeneralName generalName = new GeneralName(1, ((ASN1String) rDNs[size].getFirst().getValue()).getString());
                            try {
                                pKIXNameConstraintValidator.checkPermitted(generalName);
                                pKIXNameConstraintValidator.checkExcluded(generalName);
                                size++;
                            } catch (Throwable e) {
                                throw new CertPathValidatorException("Subtree check for certificate subject alternative email failed.", e, certPath, i);
                            }
                        }
                        if (instance2 != null) {
                            try {
                                GeneralName[] names = instance2.getNames();
                                while (i2 < names.length) {
                                    try {
                                        pKIXNameConstraintValidator.checkPermitted(names[i2]);
                                        pKIXNameConstraintValidator.checkExcluded(names[i2]);
                                        i2++;
                                    } catch (Throwable e2) {
                                        throw new CertPathValidatorException("Subtree check for certificate subject alternative name failed.", e2, certPath, i);
                                    }
                                }
                            } catch (Throwable e22) {
                                throw new CertPathValidatorException("Subject alternative name contents could not be decoded.", e22, certPath, i);
                            }
                        }
                    } catch (Throwable e222) {
                        throw new CertPathValidatorException("Subject alternative name extension could not be decoded.", e222, certPath, i);
                    }
                } catch (Throwable e2222) {
                    throw new CertPathValidatorException("Subtree check for certificate subject failed.", e2222, certPath, i);
                }
            } catch (Throwable e22222) {
                throw new CertPathValidatorException("Exception extracting subject name when checking subtrees.", e22222, certPath, i);
            }
        }
    }

    protected static PKIXPolicyNode processCertD(CertPath certPath, int i, Set set, PKIXPolicyNode pKIXPolicyNode, List[] listArr, int i2) {
        List certificates = certPath.getCertificates();
        X509Certificate x509Certificate = (X509Certificate) certificates.get(i);
        int size = certificates.size();
        int i3 = size - i;
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, CERTIFICATE_POLICIES));
            if (instance == null || pKIXPolicyNode == null) {
                return null;
            }
            Set qualifierSet;
            Iterator children;
            PKIXPolicyNode pKIXPolicyNode2;
            Enumeration objects = instance.getObjects();
            Collection hashSet = new HashSet();
            while (objects.hasMoreElements()) {
                PolicyInformation instance2 = PolicyInformation.getInstance(objects.nextElement());
                ASN1ObjectIdentifier policyIdentifier = instance2.getPolicyIdentifier();
                hashSet.add(policyIdentifier.getId());
                if (!ANY_POLICY.equals(policyIdentifier.getId())) {
                    try {
                        qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                        if (!CertPathValidatorUtilities.processCertD1i(i3, listArr, policyIdentifier, qualifierSet)) {
                            CertPathValidatorUtilities.processCertD1ii(i3, listArr, policyIdentifier, qualifierSet);
                        }
                    } catch (Throwable e) {
                        throw new ExtCertPathValidatorException("Policy qualifier info set could not be build.", e, certPath, i);
                    }
                }
            }
            if (set.isEmpty() || set.contains(ANY_POLICY)) {
                set.clear();
                set.addAll(hashSet);
            } else {
                Collection hashSet2 = new HashSet();
                for (Object next : set) {
                    if (hashSet.contains(next)) {
                        hashSet2.add(next);
                    }
                }
                set.clear();
                set.addAll(hashSet2);
            }
            if (i2 > 0 || (i3 < size && CertPathValidatorUtilities.isSelfIssued(x509Certificate))) {
                Enumeration objects2 = instance.getObjects();
                while (objects2.hasMoreElements()) {
                    PolicyInformation instance3 = PolicyInformation.getInstance(objects2.nextElement());
                    if (ANY_POLICY.equals(instance3.getPolicyIdentifier().getId())) {
                        qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance3.getPolicyQualifiers());
                        List list = listArr[i3 - 1];
                        for (int i4 = 0; i4 < list.size(); i4++) {
                            PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode) list.get(i4);
                            for (Object next2 : pKIXPolicyNode3.getExpectedPolicies()) {
                                String str;
                                if (next2 instanceof String) {
                                    str = (String) next2;
                                } else if (next2 instanceof ASN1ObjectIdentifier) {
                                    str = ((ASN1ObjectIdentifier) next2).getId();
                                }
                                children = pKIXPolicyNode3.getChildren();
                                Object obj = null;
                                while (children.hasNext()) {
                                    obj = str.equals(((PKIXPolicyNode) children.next()).getValidPolicy()) ? 1 : obj;
                                }
                                if (obj == null) {
                                    Set hashSet3 = new HashSet();
                                    hashSet3.add(str);
                                    pKIXPolicyNode2 = new PKIXPolicyNode(new ArrayList(), i3, hashSet3, pKIXPolicyNode3, qualifierSet, str, false);
                                    pKIXPolicyNode3.addChild(pKIXPolicyNode2);
                                    listArr[i3].add(pKIXPolicyNode2);
                                }
                            }
                        }
                    }
                }
            }
            int i5 = i3 - 1;
            PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNode;
            while (i5 >= 0) {
                List list2 = listArr[i5];
                PKIXPolicyNode pKIXPolicyNode5 = pKIXPolicyNode4;
                for (int i6 = 0; i6 < list2.size(); i6++) {
                    pKIXPolicyNode2 = (PKIXPolicyNode) list2.get(i6);
                    if (!pKIXPolicyNode2.hasChildren()) {
                        pKIXPolicyNode5 = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode5, listArr, pKIXPolicyNode2);
                        if (pKIXPolicyNode5 == null) {
                            break;
                        }
                    }
                }
                i5--;
                pKIXPolicyNode4 = pKIXPolicyNode5;
            }
            Set criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs == null) {
                return pKIXPolicyNode4;
            }
            boolean contains = criticalExtensionOIDs.contains(CERTIFICATE_POLICIES);
            List list3 = listArr[i3];
            for (i3 = 0; i3 < list3.size(); i3++) {
                ((PKIXPolicyNode) list3.get(i3)).setCritical(contains);
            }
            return pKIXPolicyNode4;
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", e2, certPath, i);
        }
    }

    protected static PKIXPolicyNode processCertE(CertPath certPath, int i, PKIXPolicyNode pKIXPolicyNode) {
        try {
            return ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), CERTIFICATE_POLICIES)) == null ? null : pKIXPolicyNode;
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", e, certPath, i);
        }
    }

    protected static void processCertF(CertPath certPath, int i, PKIXPolicyNode pKIXPolicyNode, int i2) {
        if (i2 <= 0 && pKIXPolicyNode == null) {
            throw new ExtCertPathValidatorException("No valid policy tree found when one expected.", null, certPath, i);
        }
    }

    protected static int wrapupCertA(int i, X509Certificate x509Certificate) {
        return (CertPathValidatorUtilities.isSelfIssued(x509Certificate) || i == 0) ? i : i - 1;
    }

    protected static int wrapupCertB(CertPath certPath, int i, int i2) {
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Certificate) certPath.getCertificates().get(i), POLICY_CONSTRAINTS));
            if (instance == null) {
                return i2;
            }
            Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objects.nextElement();
                switch (aSN1TaggedObject.getTagNo()) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        try {
                            if (ASN1Integer.getInstance(aSN1TaggedObject, false).getValue().intValue() != 0) {
                                break;
                            }
                            return 0;
                        } catch (Throwable e) {
                            throw new ExtCertPathValidatorException("Policy constraints requireExplicitPolicy field could not be decoded.", e, certPath, i);
                        }
                    default:
                        break;
                }
            }
            return i2;
        } catch (Throwable e2) {
            throw new ExtCertPathValidatorException("Policy constraints could not be decoded.", e2, certPath, i);
        }
    }

    protected static void wrapupCertF(CertPath certPath, int i, List list, Set set) {
        X509Certificate x509Certificate = (X509Certificate) certPath.getCertificates().get(i);
        for (PKIXCertPathChecker check : list) {
            try {
                check.check(x509Certificate, set);
            } catch (Throwable e) {
                throw new ExtCertPathValidatorException("Additional certificate path checker failed.", e, certPath, i);
            }
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension: " + set, null, certPath, i);
        }
    }

    protected static PKIXPolicyNode wrapupCertG(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters, Set set, int i, List[] listArr, PKIXPolicyNode pKIXPolicyNode, Set set2) {
        int size = certPath.getCertificates().size();
        if (pKIXPolicyNode == null) {
            if (!pKIXExtendedParameters.isExplicitPolicyRequired()) {
                return null;
            }
            throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, i);
        } else if (!CertPathValidatorUtilities.isAnyPolicy(set)) {
            r4 = new HashSet();
            for (List list : listArr) {
                for (r2 = 0; r2 < list.size(); r2++) {
                    r0 = (PKIXPolicyNode) list.get(r2);
                    if (ANY_POLICY.equals(r0.getValidPolicy())) {
                        Iterator children = r0.getChildren();
                        while (children.hasNext()) {
                            r0 = (PKIXPolicyNode) children.next();
                            if (!ANY_POLICY.equals(r0.getValidPolicy())) {
                                r4.add(r0);
                            }
                        }
                    }
                }
            }
            for (PKIXPolicyNode pKIXPolicyNode2 : r4) {
                if (!set.contains(pKIXPolicyNode2.getValidPolicy())) {
                    pKIXPolicyNode = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode, listArr, pKIXPolicyNode2);
                }
            }
            if (pKIXPolicyNode != null) {
                size--;
                r1 = pKIXPolicyNode;
                while (size >= 0) {
                    r4 = listArr[size];
                    r2 = r1;
                    for (r1 = 0; r1 < r4.size(); r1++) {
                        pKIXPolicyNode2 = (PKIXPolicyNode) r4.get(r1);
                        if (!pKIXPolicyNode2.hasChildren()) {
                            r2 = CertPathValidatorUtilities.removePolicyNode(r2, listArr, pKIXPolicyNode2);
                        }
                    }
                    size--;
                    r1 = r2;
                }
            } else {
                r1 = pKIXPolicyNode;
            }
            return r1;
        } else if (!pKIXExtendedParameters.isExplicitPolicyRequired()) {
            return pKIXPolicyNode;
        } else {
            if (set2.isEmpty()) {
                throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, i);
            }
            r4 = new HashSet();
            for (List list2 : listArr) {
                for (r2 = 0; r2 < list2.size(); r2++) {
                    pKIXPolicyNode2 = (PKIXPolicyNode) list2.get(r2);
                    if (ANY_POLICY.equals(pKIXPolicyNode2.getValidPolicy())) {
                        Iterator children2 = pKIXPolicyNode2.getChildren();
                        while (children2.hasNext()) {
                            r4.add(children2.next());
                        }
                    }
                }
            }
            for (PKIXPolicyNode pKIXPolicyNode22 : r4) {
                if (set2.contains(pKIXPolicyNode22.getValidPolicy())) {
                }
            }
            if (pKIXPolicyNode == null) {
                return pKIXPolicyNode;
            }
            size--;
            r1 = pKIXPolicyNode;
            while (size >= 0) {
                r4 = listArr[size];
                r2 = r1;
                for (r1 = 0; r1 < r4.size(); r1++) {
                    pKIXPolicyNode22 = (PKIXPolicyNode) r4.get(r1);
                    if (!pKIXPolicyNode22.hasChildren()) {
                        r2 = CertPathValidatorUtilities.removePolicyNode(r2, listArr, pKIXPolicyNode22);
                    }
                }
                size--;
                r1 = r2;
            }
            return r1;
        }
    }
}
