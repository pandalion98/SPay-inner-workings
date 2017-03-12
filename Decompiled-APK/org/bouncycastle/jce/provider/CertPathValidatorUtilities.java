package org.bouncycastle.jce.provider;

import android.support.v4.os.EnvironmentCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CRLSelector;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCRLStoreSelector;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXCertStoreSelector.Builder;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.X509AttributeCertificate;

class CertPathValidatorUtilities {
    protected static final String ANY_POLICY = "2.5.29.32.0";
    protected static final String AUTHORITY_KEY_IDENTIFIER;
    protected static final String BASIC_CONSTRAINTS;
    protected static final String CERTIFICATE_POLICIES;
    protected static final String CRL_DISTRIBUTION_POINTS;
    protected static final String CRL_NUMBER;
    protected static final int CRL_SIGN = 6;
    protected static final PKIXCRLUtil CRL_UTIL;
    protected static final String DELTA_CRL_INDICATOR;
    protected static final String FRESHEST_CRL;
    protected static final String INHIBIT_ANY_POLICY;
    protected static final String ISSUING_DISTRIBUTION_POINT;
    protected static final int KEY_CERT_SIGN = 5;
    protected static final String KEY_USAGE;
    protected static final String NAME_CONSTRAINTS;
    protected static final String POLICY_CONSTRAINTS;
    protected static final String POLICY_MAPPINGS;
    protected static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String[] crlReasons;

    static {
        CRL_UTIL = new PKIXCRLUtil();
        CERTIFICATE_POLICIES = Extension.certificatePolicies.getId();
        BASIC_CONSTRAINTS = Extension.basicConstraints.getId();
        POLICY_MAPPINGS = Extension.policyMappings.getId();
        SUBJECT_ALTERNATIVE_NAME = Extension.subjectAlternativeName.getId();
        NAME_CONSTRAINTS = Extension.nameConstraints.getId();
        KEY_USAGE = Extension.keyUsage.getId();
        INHIBIT_ANY_POLICY = Extension.inhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = Extension.issuingDistributionPoint.getId();
        DELTA_CRL_INDICATOR = Extension.deltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = Extension.policyConstraints.getId();
        FRESHEST_CRL = Extension.freshestCRL.getId();
        CRL_DISTRIBUTION_POINTS = Extension.cRLDistributionPoints.getId();
        AUTHORITY_KEY_IDENTIFIER = Extension.authorityKeyIdentifier.getId();
        CRL_NUMBER = Extension.cRLNumber.getId();
        crlReasons = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", EnvironmentCompat.MEDIA_UNKNOWN, "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    }

    CertPathValidatorUtilities() {
    }

    static void checkCRLsNotEmpty(Set set, Object obj) {
        if (!set.isEmpty()) {
            return;
        }
        if (obj instanceof X509AttributeCertificate) {
            throw new AnnotatedException("No CRLs found for issuer \"" + ((X509AttributeCertificate) obj).getIssuer().getPrincipals()[0] + "\"");
        } else {
            throw new AnnotatedException("No CRLs found for issuer \"" + RFC4519Style.INSTANCE.toString(PrincipalUtils.getIssuerPrincipal((X509Certificate) obj)) + "\"");
        }
    }

    protected static Collection findCertificates(PKIXCertStoreSelector pKIXCertStoreSelector, List list) {
        Collection linkedHashSet = new LinkedHashSet();
        for (Object next : list) {
            if (next instanceof Store) {
                try {
                    linkedHashSet.addAll(((Store) next).getMatches(pKIXCertStoreSelector));
                } catch (Throwable e) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", e);
                }
            }
            try {
                linkedHashSet.addAll(PKIXCertStoreSelector.getCertificates(pKIXCertStoreSelector, (CertStore) next));
            } catch (Throwable e2) {
                throw new AnnotatedException("Problem while picking certificates from certificate store.", e2);
            }
        }
        return linkedHashSet;
    }

    static Collection findIssuerCerts(X509Certificate x509Certificate, List<CertStore> list, List<PKIXCertStore> list2) {
        CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PrincipalUtils.getIssuerPrincipal(x509Certificate).getEncoded());
            try {
                Object extensionValue = x509Certificate.getExtensionValue(AUTHORITY_KEY_IDENTIFIER);
                if (extensionValue != null) {
                    byte[] keyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets()).getKeyIdentifier();
                    if (keyIdentifier != null) {
                        x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                    }
                }
            } catch (Exception e) {
            }
            PKIXCertStoreSelector build = new Builder(x509CertSelector).build();
            Collection linkedHashSet = new LinkedHashSet();
            try {
                List<X509Certificate> arrayList = new ArrayList();
                arrayList.addAll(findCertificates(build, list));
                arrayList.addAll(findCertificates(build, list2));
                for (X509Certificate add : arrayList) {
                    linkedHashSet.add(add);
                }
                return linkedHashSet;
            } catch (Throwable e2) {
                throw new AnnotatedException("Issuer certificate cannot be searched.", e2);
            }
        } catch (Throwable e22) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate could not be set.", e22);
        }
    }

    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set) {
        return findTrustAnchor(x509Certificate, set, null);
    }

    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set, String str) {
        X509CertSelector x509CertSelector = new X509CertSelector();
        X500Name encodedIssuerPrincipal = PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate);
        try {
            x509CertSelector.setSubject(encodedIssuerPrincipal.getEncoded());
            Iterator it = set.iterator();
            Throwable th = null;
            PublicKey publicKey = null;
            TrustAnchor trustAnchor = null;
            while (it.hasNext() && trustAnchor == null) {
                TrustAnchor trustAnchor2;
                PublicKey publicKey2;
                PublicKey publicKey3;
                trustAnchor = (TrustAnchor) it.next();
                if (trustAnchor.getTrustedCert() != null) {
                    if (x509CertSelector.match(trustAnchor.getTrustedCert())) {
                        trustAnchor2 = trustAnchor;
                        publicKey2 = trustAnchor.getTrustedCert().getPublicKey();
                    } else {
                        publicKey2 = publicKey;
                        trustAnchor2 = null;
                    }
                } else if (trustAnchor.getCAName() == null || trustAnchor.getCAPublicKey() == null) {
                    publicKey2 = publicKey;
                    trustAnchor2 = null;
                } else {
                    try {
                        if (encodedIssuerPrincipal.equals(PrincipalUtils.getCA(trustAnchor))) {
                            publicKey = trustAnchor.getCAPublicKey();
                        } else {
                            trustAnchor = null;
                        }
                        publicKey3 = publicKey;
                        trustAnchor2 = trustAnchor;
                        publicKey2 = publicKey3;
                    } catch (IllegalArgumentException e) {
                        publicKey2 = publicKey;
                        trustAnchor2 = null;
                    }
                }
                if (publicKey2 != null) {
                    try {
                        verifyX509Certificate(x509Certificate, publicKey2, str);
                        publicKey3 = publicKey2;
                        trustAnchor = trustAnchor2;
                        publicKey = publicKey3;
                    } catch (Throwable e2) {
                        th = e2;
                        publicKey = null;
                        trustAnchor = null;
                    }
                } else {
                    publicKey3 = publicKey2;
                    trustAnchor = trustAnchor2;
                    publicKey = publicKey3;
                }
            }
            if (trustAnchor != null || th == null) {
                return trustAnchor;
            }
            throw new AnnotatedException("TrustAnchor found but certificate validation failed.", th);
        } catch (Throwable e22) {
            throw new AnnotatedException("Cannot set subject search criteria for trust anchor.", e22);
        }
    }

    static List<PKIXCertStore> getAdditionalStoresFromAltNames(byte[] bArr, Map<GeneralName, PKIXCertStore> map) {
        if (bArr == null) {
            return Collections.EMPTY_LIST;
        }
        GeneralName[] names = GeneralNames.getInstance(ASN1OctetString.getInstance(bArr).getOctets()).getNames();
        List<PKIXCertStore> arrayList = new ArrayList();
        for (int i = 0; i != names.length; i++) {
            PKIXCertStore pKIXCertStore = (PKIXCertStore) map.get(names[i]);
            if (pKIXCertStore != null) {
                arrayList.add(pKIXCertStore);
            }
        }
        return arrayList;
    }

    static List<PKIXCRLStore> getAdditionalStoresFromCRLDistributionPoint(CRLDistPoint cRLDistPoint, Map<GeneralName, PKIXCRLStore> map) {
        if (cRLDistPoint == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            DistributionPoint[] distributionPoints = cRLDistPoint.getDistributionPoints();
            List<PKIXCRLStore> arrayList = new ArrayList();
            for (int i = 0; i < distributionPoints.length; i++) {
                DistributionPointName distributionPoint = distributionPoints[i].getDistributionPoint();
                if (distributionPoint != null && distributionPoint.getType() == 0) {
                    GeneralName[] names = GeneralNames.getInstance(distributionPoint.getName()).getNames();
                    for (int i2 = 0; i2 < names.length; i2++) {
                        PKIXCRLStore pKIXCRLStore = (PKIXCRLStore) map.get(names[i]);
                        if (pKIXCRLStore != null) {
                            arrayList.add(pKIXCRLStore);
                        }
                    }
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new AnnotatedException("Distribution points could not be read.", e);
        }
    }

    protected static AlgorithmIdentifier getAlgorithmIdentifier(PublicKey publicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getAlgorithm();
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Subject public key cannot be decoded.", e);
        }
    }

    protected static void getCRLIssuersFromDistributionPoint(DistributionPoint distributionPoint, Collection collection, X509CRLSelector x509CRLSelector) {
        List<X500Name> arrayList = new ArrayList();
        if (distributionPoint.getCRLIssuer() != null) {
            GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < names.length; i++) {
                if (names[i].getTagNo() == 4) {
                    try {
                        arrayList.add(X500Name.getInstance(names[i].getName().toASN1Primitive().getEncoded()));
                    } catch (Throwable e) {
                        throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", e);
                    }
                }
            }
        } else if (distributionPoint.getDistributionPoint() == null) {
            throw new AnnotatedException("CRL issuer is omitted from distribution point but no distributionPoint field present.");
        } else {
            for (Object add : collection) {
                arrayList.add(add);
            }
        }
        for (X500Name encoded : arrayList) {
            try {
                x509CRLSelector.addIssuerName(encoded.getEncoded());
            } catch (Throwable e2) {
                throw new AnnotatedException("Cannot decode CRL issuer information.", e2);
            }
        }
    }

    protected static void getCertStatus(Date date, X509CRL x509crl, Object obj, CertStatus certStatus) {
        try {
            X509CRLEntry x509CRLEntry;
            if (X509CRLObject.isIndirectCRL(x509crl)) {
                X509CRLEntry revokedCertificate = x509crl.getRevokedCertificate(getSerialNumber(obj));
                if (revokedCertificate != null) {
                    Object instance = X500Name.getInstance(revokedCertificate.getCertificateIssuer().getEncoded());
                    if (instance == null) {
                        instance = PrincipalUtils.getIssuerPrincipal(x509crl);
                    }
                    if (PrincipalUtils.getEncodedIssuerPrincipal(obj).equals(instance)) {
                        x509CRLEntry = revokedCertificate;
                    } else {
                        return;
                    }
                }
                return;
            } else if (PrincipalUtils.getEncodedIssuerPrincipal(obj).equals(PrincipalUtils.getIssuerPrincipal(x509crl))) {
                x509CRLEntry = x509crl.getRevokedCertificate(getSerialNumber(obj));
                if (x509CRLEntry == null) {
                    return;
                }
            } else {
                return;
            }
            ASN1Enumerated aSN1Enumerated = null;
            if (x509CRLEntry.hasExtensions()) {
                try {
                    aSN1Enumerated = ASN1Enumerated.getInstance(getExtensionValue(x509CRLEntry, Extension.reasonCode.getId()));
                } catch (Throwable e) {
                    throw new AnnotatedException("Reason code CRL entry extension could not be decoded.", e);
                }
            }
            if (date.getTime() >= x509CRLEntry.getRevocationDate().getTime() || aSN1Enumerated == null || aSN1Enumerated.getValue().intValue() == 0 || aSN1Enumerated.getValue().intValue() == 1 || aSN1Enumerated.getValue().intValue() == 2 || aSN1Enumerated.getValue().intValue() == 8) {
                if (aSN1Enumerated != null) {
                    certStatus.setCertStatus(aSN1Enumerated.getValue().intValue());
                } else {
                    certStatus.setCertStatus(0);
                }
                certStatus.setRevocationDate(x509CRLEntry.getRevocationDate());
            }
        } catch (Throwable e2) {
            throw new AnnotatedException("Failed check for indirect CRL.", e2);
        }
    }

    protected static Set getCompleteCRLs(DistributionPoint distributionPoint, Object obj, Date date, PKIXExtendedParameters pKIXExtendedParameters) {
        CRLSelector x509CRLSelector = new X509CRLSelector();
        try {
            Collection hashSet = new HashSet();
            hashSet.add(PrincipalUtils.getEncodedIssuerPrincipal(obj));
            getCRLIssuersFromDistributionPoint(distributionPoint, hashSet, x509CRLSelector);
            if (obj instanceof X509Certificate) {
                x509CRLSelector.setCertificateChecking((X509Certificate) obj);
            }
            PKIXCRLStoreSelector build = new PKIXCRLStoreSelector.Builder(x509CRLSelector).setCompleteCRLEnabled(true).build();
            if (pKIXExtendedParameters.getDate() != null) {
                date = pKIXExtendedParameters.getDate();
            }
            Set findCRLs = CRL_UTIL.findCRLs(build, date, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores());
            checkCRLsNotEmpty(findCRLs, obj);
            return findCRLs;
        } catch (Throwable e) {
            throw new AnnotatedException("Could not get issuer information from distribution point.", e);
        }
    }

    protected static Set getDeltaCRLs(Date date, X509CRL x509crl, List<CertStore> list, List<PKIXCRLStore> list2) {
        BigInteger bigInteger = null;
        CRLSelector x509CRLSelector = new X509CRLSelector();
        try {
            x509CRLSelector.addIssuerName(PrincipalUtils.getIssuerPrincipal(x509crl).getEncoded());
            try {
                ASN1Primitive extensionValue = getExtensionValue(x509crl, CRL_NUMBER);
                BigInteger positiveValue = extensionValue != null ? ASN1Integer.getInstance(extensionValue).getPositiveValue() : null;
                try {
                    byte[] extensionValue2 = x509crl.getExtensionValue(ISSUING_DISTRIBUTION_POINT);
                    if (positiveValue != null) {
                        bigInteger = positiveValue.add(BigInteger.valueOf(1));
                    }
                    x509CRLSelector.setMinCRLNumber(bigInteger);
                    PKIXCRLStoreSelector.Builder builder = new PKIXCRLStoreSelector.Builder(x509CRLSelector);
                    builder.setIssuingDistributionPoint(extensionValue2);
                    builder.setIssuingDistributionPointEnabled(true);
                    builder.setMaxBaseCRLNumber(positiveValue);
                    Set<X509CRL> findCRLs = CRL_UTIL.findCRLs(builder.build(), date, list, list2);
                    Set hashSet = new HashSet();
                    for (X509CRL x509crl2 : findCRLs) {
                        if (isDeltaCRL(x509crl2)) {
                            hashSet.add(x509crl2);
                        }
                    }
                    return hashSet;
                } catch (Throwable e) {
                    throw new AnnotatedException("Issuing distribution point extension value could not be read.", e);
                }
            } catch (Throwable e2) {
                throw new AnnotatedException("CRL number extension could not be extracted from CRL.", e2);
            }
        } catch (Throwable e22) {
            throw new AnnotatedException("Cannot extract issuer from CRL.", e22);
        }
    }

    protected static ASN1Primitive getExtensionValue(X509Extension x509Extension, String str) {
        byte[] extensionValue = x509Extension.getExtensionValue(str);
        return extensionValue == null ? null : getObject(str, extensionValue);
    }

    protected static PublicKey getNextWorkingKey(List list, int i, JcaJceHelper jcaJceHelper) {
        PublicKey publicKey = ((Certificate) list.get(i)).getPublicKey();
        if (publicKey instanceof DSAPublicKey) {
            DSAPublicKey dSAPublicKey = (DSAPublicKey) publicKey;
            if (dSAPublicKey.getParams() == null) {
                int i2 = i + 1;
                while (i2 < list.size()) {
                    PublicKey publicKey2 = ((X509Certificate) list.get(i2)).getPublicKey();
                    if (publicKey2 instanceof DSAPublicKey) {
                        DSAPublicKey dSAPublicKey2 = (DSAPublicKey) publicKey2;
                        if (dSAPublicKey2.getParams() == null) {
                            i2++;
                        } else {
                            DSAParams params = dSAPublicKey2.getParams();
                            try {
                                publicKey = jcaJceHelper.createKeyFactory("DSA").generatePublic(new DSAPublicKeySpec(dSAPublicKey.getY(), params.getP(), params.getQ(), params.getG()));
                            } catch (Exception e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    }
                    throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
                }
                throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
            }
        }
        return publicKey;
    }

    private static ASN1Primitive getObject(String str, byte[] bArr) {
        try {
            return new ASN1InputStream(((ASN1OctetString) new ASN1InputStream(bArr).readObject()).getOctets()).readObject();
        } catch (Throwable e) {
            throw new AnnotatedException("exception processing extension " + str, e);
        }
    }

    protected static final Set getQualifierSet(ASN1Sequence aSN1Sequence) {
        Set hashSet = new HashSet();
        if (aSN1Sequence == null) {
            return hashSet;
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ASN1OutputStream aSN1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            try {
                aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
                hashSet.add(new PolicyQualifierInfo(byteArrayOutputStream.toByteArray()));
                byteArrayOutputStream.reset();
            } catch (Throwable e) {
                throw new ExtCertPathValidatorException("Policy qualifier info cannot be decoded.", e);
            }
        }
        return hashSet;
    }

    private static BigInteger getSerialNumber(Object obj) {
        return ((X509Certificate) obj).getSerialNumber();
    }

    protected static Date getValidCertDateFromValidityModel(PKIXExtendedParameters pKIXExtendedParameters, CertPath certPath, int i) {
        if (pKIXExtendedParameters.getValidityModel() != 1) {
            return getValidDate(pKIXExtendedParameters);
        }
        if (i <= 0) {
            return getValidDate(pKIXExtendedParameters);
        }
        if (i - 1 != 0) {
            return ((X509Certificate) certPath.getCertificates().get(i - 1)).getNotBefore();
        }
        try {
            byte[] extensionValue = ((X509Certificate) certPath.getCertificates().get(i - 1)).getExtensionValue(ISISMTTObjectIdentifiers.id_isismtt_at_dateOfCertGen.getId());
            ASN1GeneralizedTime instance = extensionValue != null ? ASN1GeneralizedTime.getInstance(ASN1Primitive.fromByteArray(extensionValue)) : null;
            if (instance == null) {
                return ((X509Certificate) certPath.getCertificates().get(i - 1)).getNotBefore();
            }
            try {
                return instance.getDate();
            } catch (Throwable e) {
                throw new AnnotatedException("Date from date of cert gen extension could not be parsed.", e);
            }
        } catch (IOException e2) {
            throw new AnnotatedException("Date of cert gen extension could not be read.");
        } catch (IllegalArgumentException e3) {
            throw new AnnotatedException("Date of cert gen extension could not be read.");
        }
    }

    protected static Date getValidDate(PKIXExtendedParameters pKIXExtendedParameters) {
        Date date = pKIXExtendedParameters.getDate();
        return date == null ? new Date() : date;
    }

    protected static boolean isAnyPolicy(Set set) {
        return set == null || set.contains(ANY_POLICY) || set.isEmpty();
    }

    private static boolean isDeltaCRL(X509CRL x509crl) {
        Set criticalExtensionOIDs = x509crl.getCriticalExtensionOIDs();
        return criticalExtensionOIDs == null ? false : criticalExtensionOIDs.contains(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
    }

    protected static boolean isSelfIssued(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectDN().equals(x509Certificate.getIssuerDN());
    }

    protected static void prepareNextCertB1(int i, List[] listArr, String str, Map map, X509Certificate x509Certificate) {
        boolean z;
        boolean z2 = false;
        for (PKIXPolicyNode pKIXPolicyNode : listArr[i]) {
            PKIXPolicyNode pKIXPolicyNode2;
            if (pKIXPolicyNode2.getValidPolicy().equals(str)) {
                pKIXPolicyNode2.expectedPolicies = (Set) map.get(str);
                z = true;
                break;
            }
        }
        z = false;
        if (!z) {
            for (PKIXPolicyNode pKIXPolicyNode22 : listArr[i]) {
                if (ANY_POLICY.equals(pKIXPolicyNode22.getValidPolicy())) {
                    Set set = null;
                    try {
                        Enumeration objects = ASN1Sequence.getInstance(getExtensionValue(x509Certificate, CERTIFICATE_POLICIES)).getObjects();
                        while (objects.hasMoreElements()) {
                            try {
                                PolicyInformation instance = PolicyInformation.getInstance(objects.nextElement());
                                if (ANY_POLICY.equals(instance.getPolicyIdentifier().getId())) {
                                    try {
                                        set = getQualifierSet(instance.getPolicyQualifiers());
                                        break;
                                    } catch (Throwable e) {
                                        throw new ExtCertPathValidatorException("Policy qualifier info set could not be built.", e);
                                    }
                                }
                            } catch (Throwable e2) {
                                throw new AnnotatedException("Policy information cannot be decoded.", e2);
                            }
                        }
                        if (x509Certificate.getCriticalExtensionOIDs() != null) {
                            z2 = x509Certificate.getCriticalExtensionOIDs().contains(CERTIFICATE_POLICIES);
                        }
                        PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode) pKIXPolicyNode22.getParent();
                        if (ANY_POLICY.equals(pKIXPolicyNode3.getValidPolicy())) {
                            pKIXPolicyNode22 = new PKIXPolicyNode(new ArrayList(), i, (Set) map.get(str), pKIXPolicyNode3, set, str, z2);
                            pKIXPolicyNode3.addChild(pKIXPolicyNode22);
                            listArr[i].add(pKIXPolicyNode22);
                            return;
                        }
                        return;
                    } catch (Throwable e22) {
                        throw new AnnotatedException("Certificate policies cannot be decoded.", e22);
                    }
                }
            }
        }
    }

    protected static PKIXPolicyNode prepareNextCertB2(int i, List[] listArr, String str, PKIXPolicyNode pKIXPolicyNode) {
        Iterator it = listArr[i].iterator();
        while (it.hasNext()) {
            PKIXPolicyNode pKIXPolicyNode2 = (PKIXPolicyNode) it.next();
            if (pKIXPolicyNode2.getValidPolicy().equals(str)) {
                ((PKIXPolicyNode) pKIXPolicyNode2.getParent()).removeChild(pKIXPolicyNode2);
                it.remove();
                int i2 = i - 1;
                PKIXPolicyNode pKIXPolicyNode3 = pKIXPolicyNode;
                while (i2 >= 0) {
                    List list = listArr[i2];
                    PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNode3;
                    for (int i3 = 0; i3 < list.size(); i3++) {
                        pKIXPolicyNode2 = (PKIXPolicyNode) list.get(i3);
                        if (!pKIXPolicyNode2.hasChildren()) {
                            pKIXPolicyNode4 = removePolicyNode(pKIXPolicyNode4, listArr, pKIXPolicyNode2);
                            if (pKIXPolicyNode4 == null) {
                                break;
                            }
                        }
                    }
                    i2--;
                    pKIXPolicyNode3 = pKIXPolicyNode4;
                }
                pKIXPolicyNode = pKIXPolicyNode3;
            }
        }
        return pKIXPolicyNode;
    }

    protected static boolean processCertD1i(int i, List[] listArr, ASN1ObjectIdentifier aSN1ObjectIdentifier, Set set) {
        List list = listArr[i - 1];
        for (int i2 = 0; i2 < list.size(); i2++) {
            PKIXPolicyNode pKIXPolicyNode = (PKIXPolicyNode) list.get(i2);
            if (pKIXPolicyNode.getExpectedPolicies().contains(aSN1ObjectIdentifier.getId())) {
                Set hashSet = new HashSet();
                hashSet.add(aSN1ObjectIdentifier.getId());
                PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode(new ArrayList(), i, hashSet, pKIXPolicyNode, set, aSN1ObjectIdentifier.getId(), false);
                pKIXPolicyNode.addChild(pKIXPolicyNode2);
                listArr[i].add(pKIXPolicyNode2);
                return true;
            }
        }
        return false;
    }

    protected static void processCertD1ii(int i, List[] listArr, ASN1ObjectIdentifier aSN1ObjectIdentifier, Set set) {
        List list = listArr[i - 1];
        for (int i2 = 0; i2 < list.size(); i2++) {
            PKIXPolicyNode pKIXPolicyNode = (PKIXPolicyNode) list.get(i2);
            if (ANY_POLICY.equals(pKIXPolicyNode.getValidPolicy())) {
                Set hashSet = new HashSet();
                hashSet.add(aSN1ObjectIdentifier.getId());
                PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode(new ArrayList(), i, hashSet, pKIXPolicyNode, set, aSN1ObjectIdentifier.getId(), false);
                pKIXPolicyNode.addChild(pKIXPolicyNode2);
                listArr[i].add(pKIXPolicyNode2);
                return;
            }
        }
    }

    protected static PKIXPolicyNode removePolicyNode(PKIXPolicyNode pKIXPolicyNode, List[] listArr, PKIXPolicyNode pKIXPolicyNode2) {
        PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode) pKIXPolicyNode2.getParent();
        if (pKIXPolicyNode == null) {
            return null;
        }
        if (pKIXPolicyNode3 == null) {
            for (int i = 0; i < listArr.length; i++) {
                listArr[i] = new ArrayList();
            }
            return null;
        }
        pKIXPolicyNode3.removeChild(pKIXPolicyNode2);
        removePolicyNodeRecurse(listArr, pKIXPolicyNode2);
        return pKIXPolicyNode;
    }

    private static void removePolicyNodeRecurse(List[] listArr, PKIXPolicyNode pKIXPolicyNode) {
        listArr[pKIXPolicyNode.getDepth()].remove(pKIXPolicyNode);
        if (pKIXPolicyNode.hasChildren()) {
            Iterator children = pKIXPolicyNode.getChildren();
            while (children.hasNext()) {
                removePolicyNodeRecurse(listArr, (PKIXPolicyNode) children.next());
            }
        }
    }

    protected static void verifyX509Certificate(X509Certificate x509Certificate, PublicKey publicKey, String str) {
        if (str == null) {
            x509Certificate.verify(publicKey);
        } else {
            x509Certificate.verify(publicKey, str);
        }
    }
}
