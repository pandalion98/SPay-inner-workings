package org.bouncycastle.x509;

import android.support.v4.os.EnvironmentCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
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
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.X509LDAPCertStoreParameters.Builder;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Store;

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

    protected static void addAdditionalStoreFromLocation(String str, ExtendedPKIXParameters extendedPKIXParameters) {
        if (extendedPKIXParameters.isAdditionalLocationsEnabled()) {
            try {
                if (str.startsWith("ldap://")) {
                    String substring = str.substring(7);
                    String str2 = null;
                    if (substring.indexOf("/") != -1) {
                        str2 = substring.substring(substring.indexOf("/"));
                        substring = "ldap://" + substring.substring(0, substring.indexOf("/"));
                    } else {
                        substring = "ldap://" + substring;
                    }
                    X509StoreParameters build = new Builder(substring, str2).build();
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATE/LDAP", build, BouncyCastleProvider.PROVIDER_NAME));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CRL/LDAP", build, BouncyCastleProvider.PROVIDER_NAME));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("ATTRIBUTECERTIFICATE/LDAP", build, BouncyCastleProvider.PROVIDER_NAME));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATEPAIR/LDAP", build, BouncyCastleProvider.PROVIDER_NAME));
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception adding X.509 stores.");
            }
        }
    }

    protected static void addAdditionalStoresFromAltNames(X509Certificate x509Certificate, ExtendedPKIXParameters extendedPKIXParameters) {
        if (x509Certificate.getIssuerAlternativeNames() != null) {
            for (List list : x509Certificate.getIssuerAlternativeNames()) {
                if (list.get(0).equals(Integers.valueOf(CRL_SIGN))) {
                    addAdditionalStoreFromLocation((String) list.get(1), extendedPKIXParameters);
                }
            }
        }
    }

    protected static void addAdditionalStoresFromCRLDistributionPoint(CRLDistPoint cRLDistPoint, ExtendedPKIXParameters extendedPKIXParameters) {
        if (cRLDistPoint != null) {
            try {
                DistributionPoint[] distributionPoints = cRLDistPoint.getDistributionPoints();
                for (DistributionPoint distributionPoint : distributionPoints) {
                    DistributionPointName distributionPoint2 = distributionPoint.getDistributionPoint();
                    if (distributionPoint2 != null && distributionPoint2.getType() == 0) {
                        GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                        for (int i = 0; i < names.length; i++) {
                            if (names[i].getTagNo() == CRL_SIGN) {
                                addAdditionalStoreFromLocation(DERIA5String.getInstance(names[i].getName()).getString(), extendedPKIXParameters);
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                throw new AnnotatedException("Distribution points could not be read.", e);
            }
        }
    }

    protected static Collection findCertificates(PKIXCertStoreSelector pKIXCertStoreSelector, List list) {
        Collection hashSet = new HashSet();
        for (Object next : list) {
            if (next instanceof Store) {
                try {
                    hashSet.addAll(((Store) next).getMatches(pKIXCertStoreSelector));
                } catch (Throwable e) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", e);
                }
            }
            try {
                hashSet.addAll(PKIXCertStoreSelector.getCertificates(pKIXCertStoreSelector, (CertStore) next));
            } catch (Throwable e2) {
                throw new AnnotatedException("Problem while picking certificates from certificate store.", e2);
            }
        }
        return hashSet;
    }

    protected static Collection findCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector, List list) {
        Collection hashSet = new HashSet();
        for (Object next : list) {
            if (next instanceof X509Store) {
                try {
                    hashSet.addAll(((X509Store) next).getMatches(x509AttributeCertStoreSelector));
                } catch (Throwable e) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", e);
                }
            }
        }
        return hashSet;
    }

    protected static Collection findCertificates(X509CertStoreSelector x509CertStoreSelector, List list) {
        Collection hashSet = new HashSet();
        CertificateFactory certificateFactory = new CertificateFactory();
        for (Object next : list) {
            if (next instanceof Store) {
                try {
                    for (Object next2 : ((Store) next2).getMatches(x509CertStoreSelector)) {
                        if (next2 instanceof Encodable) {
                            hashSet.add(certificateFactory.engineGenerateCertificate(new ByteArrayInputStream(((Encodable) next2).getEncoded())));
                        } else if (next2 instanceof Certificate) {
                            hashSet.add(next2);
                        } else {
                            throw new AnnotatedException("Unknown object found in certificate store.");
                        }
                    }
                    continue;
                } catch (Throwable e) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", e);
                } catch (Throwable e2) {
                    throw new AnnotatedException("Problem while extracting certificates from X.509 store.", e2);
                } catch (Throwable e22) {
                    throw new AnnotatedException("Problem while extracting certificates from X.509 store.", e22);
                }
            }
            try {
                hashSet.addAll(((CertStore) next2).getCertificates(x509CertStoreSelector));
            } catch (Throwable e222) {
                throw new AnnotatedException("Problem while picking certificates from certificate store.", e222);
            }
        }
        return hashSet;
    }

    static Collection findIssuerCerts(X509Certificate x509Certificate, List list, List list2) {
        CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(x509Certificate.getIssuerX500Principal().getEncoded());
            PKIXCertStoreSelector build = new PKIXCertStoreSelector.Builder(x509CertSelector).build();
            Collection hashSet = new HashSet();
            try {
                List<X509Certificate> arrayList = new ArrayList();
                arrayList.addAll(findCertificates(build, list));
                arrayList.addAll(findCertificates(build, list2));
                for (X509Certificate add : arrayList) {
                    hashSet.add(add);
                }
                return hashSet;
            } catch (Throwable e) {
                throw new AnnotatedException("Issuer certificate cannot be searched.", e);
            }
        } catch (Throwable e2) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate could not be set.", e2);
        }
    }

    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set) {
        return findTrustAnchor(x509Certificate, set, null);
    }

    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set, String str) {
        X509CertSelector x509CertSelector = new X509CertSelector();
        X500Principal encodedIssuerPrincipal = getEncodedIssuerPrincipal(x509Certificate);
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
                        if (encodedIssuerPrincipal.equals(new X500Principal(trustAnchor.getCAName()))) {
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

    protected static AlgorithmIdentifier getAlgorithmIdentifier(PublicKey publicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getAlgorithmId();
        } catch (Throwable e) {
            throw new ExtCertPathValidatorException("Subject public key cannot be decoded.", e);
        }
    }

    protected static void getCRLIssuersFromDistributionPoint(DistributionPoint distributionPoint, Collection collection, X509CRLSelector x509CRLSelector, ExtendedPKIXParameters extendedPKIXParameters) {
        List<X500Principal> arrayList = new ArrayList();
        if (distributionPoint.getCRLIssuer() != null) {
            GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < names.length; i++) {
                if (names[i].getTagNo() == 4) {
                    try {
                        arrayList.add(new X500Principal(names[i].getName().toASN1Primitive().getEncoded()));
                    } catch (Throwable e) {
                        throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", e);
                    }
                }
            }
        } else if (distributionPoint.getDistributionPoint() == null) {
            throw new AnnotatedException("CRL issuer is omitted from distribution point but no distributionPoint field present.");
        } else {
            for (X500Principal add : collection) {
                arrayList.add(add);
            }
        }
        for (X500Principal add2 : arrayList) {
            try {
                x509CRLSelector.addIssuerName(add2.getEncoded());
            } catch (Throwable e2) {
                throw new AnnotatedException("Cannot decode CRL issuer information.", e2);
            }
        }
    }

    protected static void getCertStatus(Date date, X509CRL x509crl, Object obj, CertStatus certStatus) {
        try {
            X509CRLEntry x509CRLEntry;
            if (isIndirectCRL(x509crl)) {
                X509CRLEntry revokedCertificate = x509crl.getRevokedCertificate(getSerialNumber(obj));
                if (revokedCertificate != null) {
                    Object certificateIssuer = revokedCertificate.getCertificateIssuer();
                    if (certificateIssuer == null) {
                        certificateIssuer = getIssuerPrincipal(x509crl);
                    }
                    if (getEncodedIssuerPrincipal(obj).equals(certificateIssuer)) {
                        x509CRLEntry = revokedCertificate;
                    } else {
                        return;
                    }
                }
                return;
            } else if (getEncodedIssuerPrincipal(obj).equals(getIssuerPrincipal(x509crl))) {
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
                    aSN1Enumerated = ASN1Enumerated.getInstance(getExtensionValue(x509CRLEntry, X509Extension.reasonCode.getId()));
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

    protected static Set getCompleteCRLs(DistributionPoint distributionPoint, Object obj, Date date, ExtendedPKIXParameters extendedPKIXParameters) {
        X509CRLSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            Object hashSet = new HashSet();
            if (obj instanceof X509AttributeCertificate) {
                hashSet.add(((X509AttributeCertificate) obj).getIssuer().getPrincipals()[0]);
            } else {
                hashSet.add(getEncodedIssuerPrincipal(obj));
            }
            getCRLIssuersFromDistributionPoint(distributionPoint, hashSet, x509CRLStoreSelector, extendedPKIXParameters);
            if (obj instanceof X509Certificate) {
                x509CRLStoreSelector.setCertificateChecking((X509Certificate) obj);
            } else if (obj instanceof X509AttributeCertificate) {
                x509CRLStoreSelector.setAttrCertificateChecking((X509AttributeCertificate) obj);
            }
            x509CRLStoreSelector.setCompleteCRLEnabled(true);
            Set findCRLs = CRL_UTIL.findCRLs(x509CRLStoreSelector, extendedPKIXParameters, date);
            if (!findCRLs.isEmpty()) {
                return findCRLs;
            }
            if (obj instanceof X509AttributeCertificate) {
                throw new AnnotatedException("No CRLs found for issuer \"" + ((X509AttributeCertificate) obj).getIssuer().getPrincipals()[0] + "\"");
            }
            throw new AnnotatedException("No CRLs found for issuer \"" + ((X509Certificate) obj).getIssuerX500Principal() + "\"");
        } catch (Throwable e) {
            throw new AnnotatedException("Could not get issuer information from distribution point.", e);
        }
    }

    protected static Set getDeltaCRLs(Date date, ExtendedPKIXParameters extendedPKIXParameters, X509CRL x509crl) {
        BigInteger bigInteger = null;
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            x509CRLStoreSelector.addIssuerName(getIssuerPrincipal(x509crl).getEncoded());
            try {
                ASN1Primitive extensionValue = getExtensionValue(x509crl, CRL_NUMBER);
                BigInteger positiveValue = extensionValue != null ? ASN1Integer.getInstance(extensionValue).getPositiveValue() : null;
                try {
                    byte[] extensionValue2 = x509crl.getExtensionValue(ISSUING_DISTRIBUTION_POINT);
                    if (positiveValue != null) {
                        bigInteger = positiveValue.add(BigInteger.valueOf(1));
                    }
                    x509CRLStoreSelector.setMinCRLNumber(bigInteger);
                    x509CRLStoreSelector.setIssuingDistributionPoint(extensionValue2);
                    x509CRLStoreSelector.setIssuingDistributionPointEnabled(true);
                    x509CRLStoreSelector.setMaxBaseCRLNumber(positiveValue);
                    Set<X509CRL> findCRLs = CRL_UTIL.findCRLs(x509CRLStoreSelector, extendedPKIXParameters, date);
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

    protected static X500Principal getEncodedIssuerPrincipal(Object obj) {
        return obj instanceof X509Certificate ? ((X509Certificate) obj).getIssuerX500Principal() : (X500Principal) ((X509AttributeCertificate) obj).getIssuer().getPrincipals()[0];
    }

    protected static ASN1Primitive getExtensionValue(java.security.cert.X509Extension x509Extension, String str) {
        byte[] extensionValue = x509Extension.getExtensionValue(str);
        return extensionValue == null ? null : getObject(str, extensionValue);
    }

    protected static X500Principal getIssuerPrincipal(X509CRL x509crl) {
        return x509crl.getIssuerX500Principal();
    }

    protected static PublicKey getNextWorkingKey(List list, int i) {
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
                                publicKey = KeyFactory.getInstance("DSA", BouncyCastleProvider.PROVIDER_NAME).generatePublic(new DSAPublicKeySpec(dSAPublicKey.getY(), params.getP(), params.getQ(), params.getG()));
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
        return obj instanceof X509Certificate ? ((X509Certificate) obj).getSerialNumber() : ((X509AttributeCertificate) obj).getSerialNumber();
    }

    protected static X500Principal getSubjectPrincipal(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectX500Principal();
    }

    protected static Date getValidCertDateFromValidityModel(ExtendedPKIXParameters extendedPKIXParameters, CertPath certPath, int i) {
        if (extendedPKIXParameters.getValidityModel() != 1) {
            return getValidDate(extendedPKIXParameters);
        }
        if (i <= 0) {
            return getValidDate(extendedPKIXParameters);
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

    protected static Date getValidDate(PKIXParameters pKIXParameters) {
        Date date = pKIXParameters.getDate();
        return date == null ? new Date() : date;
    }

    protected static boolean isAnyPolicy(Set set) {
        return set == null || set.contains(ANY_POLICY) || set.isEmpty();
    }

    private static boolean isDeltaCRL(X509CRL x509crl) {
        Set criticalExtensionOIDs = x509crl.getCriticalExtensionOIDs();
        return criticalExtensionOIDs == null ? false : criticalExtensionOIDs.contains(Extension.deltaCRLIndicator.getId());
    }

    static boolean isIndirectCRL(X509CRL x509crl) {
        try {
            Object extensionValue = x509crl.getExtensionValue(Extension.issuingDistributionPoint.getId());
            return extensionValue != null && IssuingDistributionPoint.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets()).isIndirectCRL();
        } catch (Exception e) {
            throw new CRLException("Exception reading IssuingDistributionPoint: " + e);
        }
    }

    protected static boolean isSelfIssued(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectDN().equals(x509Certificate.getIssuerDN());
    }

    protected static void prepareNextCertB1(int i, List[] listArr, String str, Map map, X509Certificate x509Certificate) {
        PKIXPolicyNode pKIXPolicyNode;
        boolean z;
        boolean z2 = false;
        for (PKIXPolicyNode pKIXPolicyNode2 : listArr[i]) {
            if (pKIXPolicyNode2.getValidPolicy().equals(str)) {
                pKIXPolicyNode2.setExpectedPolicies((Set) map.get(str));
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
