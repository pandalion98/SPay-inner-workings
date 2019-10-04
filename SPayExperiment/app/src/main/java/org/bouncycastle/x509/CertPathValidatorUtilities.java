/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.KeyFactory
 *  java.security.Principal
 *  java.security.PublicKey
 *  java.security.cert.CRLException
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathValidatorException
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.CertStoreException
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateException
 *  java.security.cert.PKIXParameters
 *  java.security.cert.PolicyNode
 *  java.security.cert.PolicyQualifierInfo
 *  java.security.cert.TrustAnchor
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLEntry
 *  java.security.cert.X509CRLSelector
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.security.cert.X509Extension
 *  java.security.interfaces.DSAParams
 *  java.security.interfaces.DSAPublicKey
 *  java.security.spec.DSAPublicKeySpec
 *  java.security.spec.KeySpec
 *  java.text.ParseException
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
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
import java.security.spec.KeySpec;
import java.text.ParseException;
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
import org.bouncycastle.asn1.DERSequence;
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
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.CertStatus;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.PKIXCRLUtil;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509Store;
import org.bouncycastle.x509.X509StoreParameters;

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
        crlReasons = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    }

    CertPathValidatorUtilities() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static void addAdditionalStoreFromLocation(String string, ExtendedPKIXParameters extendedPKIXParameters) {
        if (!extendedPKIXParameters.isAdditionalLocationsEnabled()) return;
        {
            try {
                if (!string.startsWith("ldap://")) return;
                {
                    String string2;
                    String string3;
                    String string4 = string.substring(7);
                    if (string4.indexOf("/") != -1) {
                        string2 = string4.substring(string4.indexOf("/"));
                        string3 = "ldap://" + string4.substring(0, string4.indexOf("/"));
                    } else {
                        String string5;
                        string3 = string5 = "ldap://" + string4;
                        string2 = null;
                    }
                    X509LDAPCertStoreParameters x509LDAPCertStoreParameters = new X509LDAPCertStoreParameters.Builder(string3, string2).build();
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATE/LDAP", (X509StoreParameters)x509LDAPCertStoreParameters, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CRL/LDAP", (X509StoreParameters)x509LDAPCertStoreParameters, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("ATTRIBUTECERTIFICATE/LDAP", (X509StoreParameters)x509LDAPCertStoreParameters, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATEPAIR/LDAP", (X509StoreParameters)x509LDAPCertStoreParameters, "BC"));
                    return;
                }
            }
            catch (Exception exception) {
                throw new RuntimeException("Exception adding X.509 stores.");
            }
        }
    }

    protected static void addAdditionalStoresFromAltNames(X509Certificate x509Certificate, ExtendedPKIXParameters extendedPKIXParameters) {
        if (x509Certificate.getIssuerAlternativeNames() != null) {
            for (List list : x509Certificate.getIssuerAlternativeNames()) {
                if (!list.get(0).equals((Object)Integers.valueOf(6))) continue;
                CertPathValidatorUtilities.addAdditionalStoreFromLocation((String)list.get(1), extendedPKIXParameters);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static void addAdditionalStoresFromCRLDistributionPoint(CRLDistPoint cRLDistPoint, ExtendedPKIXParameters extendedPKIXParameters) {
        if (cRLDistPoint != null) {
            DistributionPoint[] arrdistributionPoint;
            try {
                arrdistributionPoint = cRLDistPoint.getDistributionPoints();
            }
            catch (Exception exception) {
                throw new AnnotatedException("Distribution points could not be read.", exception);
            }
            for (int i = 0; i < arrdistributionPoint.length; ++i) {
                DistributionPointName distributionPointName = arrdistributionPoint[i].getDistributionPoint();
                if (distributionPointName == null || distributionPointName.getType() != 0) continue;
                GeneralName[] arrgeneralName = GeneralNames.getInstance(distributionPointName.getName()).getNames();
                for (int j = 0; j < arrgeneralName.length; ++j) {
                    if (arrgeneralName[j].getTagNo() != 6) continue;
                    CertPathValidatorUtilities.addAdditionalStoreFromLocation(DERIA5String.getInstance(arrgeneralName[j].getName()).getString(), extendedPKIXParameters);
                }
            }
        }
    }

    protected static Collection findCertificates(PKIXCertStoreSelector pKIXCertStoreSelector, List list) {
        HashSet hashSet = new HashSet();
        for (Object object : list) {
            if (object instanceof Store) {
                Store store = (Store)object;
                try {
                    hashSet.addAll(store.getMatches(pKIXCertStoreSelector));
                    continue;
                }
                catch (StoreException storeException) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", (Throwable)storeException);
                }
            }
            CertStore certStore = (CertStore)object;
            try {
                hashSet.addAll(PKIXCertStoreSelector.getCertificates(pKIXCertStoreSelector, certStore));
            }
            catch (CertStoreException certStoreException) {
                throw new AnnotatedException("Problem while picking certificates from certificate store.", certStoreException);
            }
        }
        return hashSet;
    }

    protected static Collection findCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector, List list) {
        HashSet hashSet = new HashSet();
        for (Object object : list) {
            if (!(object instanceof X509Store)) continue;
            X509Store x509Store = (X509Store)object;
            try {
                hashSet.addAll(x509Store.getMatches(x509AttributeCertStoreSelector));
            }
            catch (StoreException storeException) {
                throw new AnnotatedException("Problem while picking certificates from X.509 store.", (Throwable)storeException);
            }
        }
        return hashSet;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static Collection findCertificates(X509CertStoreSelector var0, List var1_1) {
        var2_2 = new HashSet();
        var3_3 = var1_1.iterator();
        var4_4 = new CertificateFactory();
        do {
            block11 : {
                if (var3_3.hasNext() == false) return var2_2;
                var5_5 = var3_3.next();
                if (!(var5_5 instanceof Store)) ** GOTO lbl11
                var9_7 = (Store)var5_5;
                break block11;
lbl11: // 1 sources:
                var6_6 = (CertStore)var5_5;
                try {
                    var2_2.addAll(var6_6.getCertificates((CertSelector)var0));
                    continue;
                }
                catch (CertStoreException var7_13) {
                    throw new AnnotatedException("Problem while picking certificates from certificate store.", var7_13);
                }
            }
            var13_8 = var9_7.getMatches(var0).iterator();
            do {
                if (!var13_8.hasNext()) ** break;
                var14_9 = var13_8.next();
                if (var14_9 instanceof Encodable) {
                    var2_2.add((Object)var4_4.engineGenerateCertificate((InputStream)new ByteArrayInputStream(((Encodable)var14_9).getEncoded())));
                    continue;
                }
                if (var14_9 instanceof Certificate == false) throw new AnnotatedException("Unknown object found in certificate store.");
                var2_2.add(var14_9);
            } while (true);
            break;
        } while (true);
        catch (StoreException var12_10) {
            throw new AnnotatedException("Problem while picking certificates from X.509 store.", (Throwable)var12_10);
        }
        catch (IOException var11_11) {
            throw new AnnotatedException("Problem while extracting certificates from X.509 store.", var11_11);
        }
        catch (CertificateException var10_12) {
            throw new AnnotatedException("Problem while extracting certificates from X.509 store.", var10_12);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    static Collection findIssuerCerts(X509Certificate x509Certificate, List list, List list2) {
        Iterator iterator;
        X509CertSelector x509CertSelector = new X509CertSelector();
        x509CertSelector.setSubject(x509Certificate.getIssuerX500Principal().getEncoded());
        PKIXCertStoreSelector<? extends Certificate> pKIXCertStoreSelector = new PKIXCertStoreSelector.Builder((CertSelector)x509CertSelector).build();
        HashSet hashSet = new HashSet();
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, list));
            arrayList.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, list2));
            iterator = arrayList.iterator();
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Issuer certificate cannot be searched.", (Throwable)annotatedException);
        }
        while (iterator.hasNext()) {
            hashSet.add((Object)((X509Certificate)iterator.next()));
        }
        return hashSet;
        catch (IOException iOException) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate could not be set.", iOException);
        }
    }

    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set) {
        return CertPathValidatorUtilities.findTrustAnchor(x509Certificate, set, null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static TrustAnchor findTrustAnchor(X509Certificate x509Certificate, Set set, String string) {
        X509CertSelector x509CertSelector = new X509CertSelector();
        X500Principal x500Principal = CertPathValidatorUtilities.getEncodedIssuerPrincipal((Object)x509Certificate);
        try {}
        catch (IOException iOException) {
            throw new AnnotatedException("Cannot set subject search criteria for trust anchor.", iOException);
        }
        x509CertSelector.setSubject(x500Principal.getEncoded());
        Iterator iterator = set.iterator();
        Exception exception = null;
        PublicKey publicKey = null;
        TrustAnchor trustAnchor = null;
        do {
            TrustAnchor trustAnchor2;
            PublicKey publicKey2;
            block17 : {
                block13 : {
                    block15 : {
                        block16 : {
                            TrustAnchor trustAnchor3;
                            block12 : {
                                block14 : {
                                    if (!iterator.hasNext() || trustAnchor != null) break block13;
                                    trustAnchor3 = (TrustAnchor)iterator.next();
                                    if (trustAnchor3.getTrustedCert() == null) break block14;
                                    if (x509CertSelector.match((Certificate)trustAnchor3.getTrustedCert())) {
                                        PublicKey publicKey3 = trustAnchor3.getTrustedCert().getPublicKey();
                                        trustAnchor2 = trustAnchor3;
                                        publicKey2 = publicKey3;
                                    } else {
                                        publicKey2 = publicKey;
                                        trustAnchor2 = null;
                                    }
                                    break block15;
                                }
                                if (trustAnchor3.getCAName() == null || trustAnchor3.getCAPublicKey() == null) break block16;
                                try {
                                    if (x500Principal.equals((Object)new X500Principal(trustAnchor3.getCAName()))) {
                                        PublicKey publicKey4;
                                        publicKey = publicKey4 = trustAnchor3.getCAPublicKey();
                                        break block12;
                                    }
                                    trustAnchor3 = null;
                                }
                                catch (IllegalArgumentException illegalArgumentException) {
                                    publicKey2 = publicKey;
                                    trustAnchor2 = null;
                                }
                            }
                            PublicKey publicKey5 = publicKey;
                            trustAnchor2 = trustAnchor3;
                            publicKey2 = publicKey5;
                            break block15;
                            break block15;
                        }
                        publicKey2 = publicKey;
                        trustAnchor2 = null;
                    }
                    if (publicKey2 != null) {
                        try {
                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate, publicKey2, string);
                            PublicKey publicKey6 = publicKey2;
                            trustAnchor = trustAnchor2;
                            publicKey = publicKey6;
                        }
                        catch (Exception exception2) {
                            exception = exception2;
                            trustAnchor = null;
                            publicKey = null;
                        }
                        continue;
                    }
                    break block17;
                }
                if (trustAnchor == null && exception != null) {
                    throw new AnnotatedException("TrustAnchor found but certificate validation failed.", exception);
                }
                return trustAnchor;
            }
            PublicKey publicKey7 = publicKey2;
            trustAnchor = trustAnchor2;
            publicKey = publicKey7;
        } while (true);
    }

    protected static AlgorithmIdentifier getAlgorithmIdentifier(PublicKey publicKey) {
        try {
            AlgorithmIdentifier algorithmIdentifier = SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getAlgorithmId();
            return algorithmIdentifier;
        }
        catch (Exception exception) {
            throw new ExtCertPathValidatorException("Subject public key cannot be decoded.", exception);
        }
    }

    protected static void getCRLIssuersFromDistributionPoint(DistributionPoint distributionPoint, Collection collection, X509CRLSelector x509CRLSelector, ExtendedPKIXParameters extendedPKIXParameters) {
        ArrayList arrayList = new ArrayList();
        if (distributionPoint.getCRLIssuer() != null) {
            GeneralName[] arrgeneralName = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < arrgeneralName.length; ++i) {
                if (arrgeneralName[i].getTagNo() != 4) continue;
                try {
                    arrayList.add((Object)new X500Principal(arrgeneralName[i].getName().toASN1Primitive().getEncoded()));
                }
                catch (IOException iOException) {
                    throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", iOException);
                }
            }
        } else {
            if (distributionPoint.getDistributionPoint() == null) {
                throw new AnnotatedException("CRL issuer is omitted from distribution point but no distributionPoint field present.");
            }
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                arrayList.add((Object)((X500Principal)iterator.next()));
            }
        }
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            try {
                x509CRLSelector.addIssuerName(((X500Principal)iterator.next()).getEncoded());
            }
            catch (IOException iOException) {
                throw new AnnotatedException("Cannot decode CRL issuer information.", iOException);
            }
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void getCertStatus(Date date, X509CRL x509CRL, Object object, CertStatus certStatus) {
        X509CRLEntry x509CRLEntry;
        block12 : {
            block11 : {
                try {
                    boolean bl = CertPathValidatorUtilities.isIndirectCRL(x509CRL);
                    if (!bl) break block11;
                }
                catch (CRLException cRLException) {
                    throw new AnnotatedException("Failed check for indirect CRL.", cRLException);
                }
                X509CRLEntry x509CRLEntry2 = x509CRL.getRevokedCertificate(CertPathValidatorUtilities.getSerialNumber(object));
                if (x509CRLEntry2 == null) {
                    return;
                }
                X500Principal x500Principal = x509CRLEntry2.getCertificateIssuer();
                if (x500Principal == null) {
                    x500Principal = CertPathValidatorUtilities.getIssuerPrincipal(x509CRL);
                }
                if (!CertPathValidatorUtilities.getEncodedIssuerPrincipal(object).equals((Object)x500Principal)) return;
                x509CRLEntry = x509CRLEntry2;
                break block12;
            }
            if (!CertPathValidatorUtilities.getEncodedIssuerPrincipal(object).equals((Object)CertPathValidatorUtilities.getIssuerPrincipal(x509CRL))) return;
            x509CRLEntry = x509CRL.getRevokedCertificate(CertPathValidatorUtilities.getSerialNumber(object));
            if (x509CRLEntry == null) {
                return;
            }
        }
        boolean bl = x509CRLEntry.hasExtensions();
        ASN1Enumerated aSN1Enumerated = null;
        if (bl) {
            ASN1Enumerated aSN1Enumerated2;
            aSN1Enumerated = aSN1Enumerated2 = ASN1Enumerated.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509CRLEntry, org.bouncycastle.asn1.x509.X509Extension.reasonCode.getId()));
        }
        if (date.getTime() < x509CRLEntry.getRevocationDate().getTime() && aSN1Enumerated != null && aSN1Enumerated.getValue().intValue() != 0 && aSN1Enumerated.getValue().intValue() != 1 && aSN1Enumerated.getValue().intValue() != 2) {
            if (aSN1Enumerated.getValue().intValue() != 8) return;
        }
        if (aSN1Enumerated != null) {
            certStatus.setCertStatus(aSN1Enumerated.getValue().intValue());
        } else {
            certStatus.setCertStatus(0);
        }
        certStatus.setRevocationDate(x509CRLEntry.getRevocationDate());
        return;
        catch (Exception exception) {
            throw new AnnotatedException("Reason code CRL entry extension could not be decoded.", exception);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static Set getCompleteCRLs(DistributionPoint distributionPoint, Object object, Date date, ExtendedPKIXParameters extendedPKIXParameters) {
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            HashSet hashSet = new HashSet();
            if (object instanceof X509AttributeCertificate) {
                hashSet.add((Object)((X509AttributeCertificate)object).getIssuer().getPrincipals()[0]);
            } else {
                hashSet.add((Object)CertPathValidatorUtilities.getEncodedIssuerPrincipal(object));
            }
            CertPathValidatorUtilities.getCRLIssuersFromDistributionPoint(distributionPoint, (Collection)hashSet, x509CRLStoreSelector, extendedPKIXParameters);
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Could not get issuer information from distribution point.", (Throwable)annotatedException);
        }
        if (object instanceof X509Certificate) {
            x509CRLStoreSelector.setCertificateChecking((X509Certificate)object);
        } else if (object instanceof X509AttributeCertificate) {
            x509CRLStoreSelector.setAttrCertificateChecking((X509AttributeCertificate)object);
        }
        x509CRLStoreSelector.setCompleteCRLEnabled(true);
        Set set = CRL_UTIL.findCRLs(x509CRLStoreSelector, extendedPKIXParameters, date);
        if (!set.isEmpty()) {
            return set;
        }
        if (object instanceof X509AttributeCertificate) {
            X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)object;
            throw new AnnotatedException("No CRLs found for issuer \"" + (Object)x509AttributeCertificate.getIssuer().getPrincipals()[0] + "\"");
        }
        X509Certificate x509Certificate = (X509Certificate)object;
        throw new AnnotatedException("No CRLs found for issuer \"" + (Object)x509Certificate.getIssuerX500Principal() + "\"");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static Set getDeltaCRLs(Date date, ExtendedPKIXParameters extendedPKIXParameters, X509CRL x509CRL) {
        byte[] arrby;
        BigInteger bigInteger;
        BigInteger bigInteger2;
        X509CRLStoreSelector x509CRLStoreSelector;
        block9 : {
            block8 : {
                x509CRLStoreSelector = new X509CRLStoreSelector();
                try {
                    x509CRLStoreSelector.addIssuerName(CertPathValidatorUtilities.getIssuerPrincipal(x509CRL).getEncoded());
                }
                catch (IOException iOException) {
                    throw new AnnotatedException("Cannot extract issuer from CRL.", iOException);
                }
                try {
                    BigInteger bigInteger3;
                    ASN1Primitive aSN1Primitive = CertPathValidatorUtilities.getExtensionValue((X509Extension)x509CRL, CRL_NUMBER);
                    bigInteger2 = aSN1Primitive != null ? (bigInteger3 = ASN1Integer.getInstance(aSN1Primitive).getPositiveValue()) : null;
                }
                catch (Exception exception) {
                    throw new AnnotatedException("CRL number extension could not be extracted from CRL.", exception);
                }
                try {
                    arrby = x509CRL.getExtensionValue(ISSUING_DISTRIBUTION_POINT);
                    bigInteger = null;
                    if (bigInteger2 != null) break block8;
                    break block9;
                }
                catch (Exception exception) {
                    throw new AnnotatedException("Issuing distribution point extension value could not be read.", exception);
                }
            }
            bigInteger = bigInteger2.add(BigInteger.valueOf((long)1L));
        }
        x509CRLStoreSelector.setMinCRLNumber(bigInteger);
        x509CRLStoreSelector.setIssuingDistributionPoint(arrby);
        x509CRLStoreSelector.setIssuingDistributionPointEnabled(true);
        x509CRLStoreSelector.setMaxBaseCRLNumber(bigInteger2);
        Set set = CRL_UTIL.findCRLs(x509CRLStoreSelector, extendedPKIXParameters, date);
        HashSet hashSet = new HashSet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            X509CRL x509CRL2 = (X509CRL)iterator.next();
            if (!CertPathValidatorUtilities.isDeltaCRL(x509CRL2)) continue;
            hashSet.add((Object)x509CRL2);
        }
        return hashSet;
    }

    protected static X500Principal getEncodedIssuerPrincipal(Object object) {
        if (object instanceof X509Certificate) {
            return ((X509Certificate)object).getIssuerX500Principal();
        }
        return (X500Principal)((X509AttributeCertificate)object).getIssuer().getPrincipals()[0];
    }

    protected static ASN1Primitive getExtensionValue(X509Extension x509Extension, String string) {
        byte[] arrby = x509Extension.getExtensionValue(string);
        if (arrby == null) {
            return null;
        }
        return CertPathValidatorUtilities.getObject(string, arrby);
    }

    protected static X500Principal getIssuerPrincipal(X509CRL x509CRL) {
        return x509CRL.getIssuerX500Principal();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static PublicKey getNextWorkingKey(List list, int n) {
        DSAPublicKey dSAPublicKey;
        PublicKey publicKey;
        block6 : {
            publicKey = ((Certificate)list.get(n)).getPublicKey();
            if (!(publicKey instanceof DSAPublicKey)) {
                return publicKey;
            }
            if ((publicKey = (DSAPublicKey)publicKey).getParams() != null) return publicKey;
            int n2 = n + 1;
            while (n2 < list.size()) {
                PublicKey publicKey2 = ((X509Certificate)list.get(n2)).getPublicKey();
                if (!(publicKey2 instanceof DSAPublicKey)) {
                    throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
                }
                dSAPublicKey = (DSAPublicKey)publicKey2;
                if (dSAPublicKey.getParams() == null) {
                    ++n2;
                    continue;
                }
                break block6;
            }
            throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
        }
        DSAParams dSAParams = dSAPublicKey.getParams();
        DSAPublicKeySpec dSAPublicKeySpec = new DSAPublicKeySpec(publicKey.getY(), dSAParams.getP(), dSAParams.getQ(), dSAParams.getG());
        try {
            return KeyFactory.getInstance((String)"DSA", (String)"BC").generatePublic((KeySpec)dSAPublicKeySpec);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static ASN1Primitive getObject(String string, byte[] arrby) {
        try {
            ASN1Primitive aSN1Primitive = new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(arrby).readObject()).getOctets()).readObject();
            return aSN1Primitive;
        }
        catch (Exception exception) {
            throw new AnnotatedException("exception processing extension " + string, exception);
        }
    }

    protected static final Set getQualifierSet(ASN1Sequence aSN1Sequence) {
        HashSet hashSet = new HashSet();
        if (aSN1Sequence == null) {
            return hashSet;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ASN1OutputStream aSN1OutputStream = new ASN1OutputStream((OutputStream)byteArrayOutputStream);
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            try {
                aSN1OutputStream.writeObject((ASN1Encodable)enumeration.nextElement());
                hashSet.add((Object)new PolicyQualifierInfo(byteArrayOutputStream.toByteArray()));
            }
            catch (IOException iOException) {
                throw new ExtCertPathValidatorException("Policy qualifier info cannot be decoded.", iOException);
            }
            byteArrayOutputStream.reset();
        }
        return hashSet;
    }

    private static BigInteger getSerialNumber(Object object) {
        if (object instanceof X509Certificate) {
            return ((X509Certificate)object).getSerialNumber();
        }
        return ((X509AttributeCertificate)object).getSerialNumber();
    }

    protected static X500Principal getSubjectPrincipal(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectX500Principal();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static Date getValidCertDateFromValidityModel(ExtendedPKIXParameters extendedPKIXParameters, CertPath certPath, int n) {
        ASN1GeneralizedTime aSN1GeneralizedTime;
        if (extendedPKIXParameters.getValidityModel() != 1) return CertPathValidatorUtilities.getValidDate(extendedPKIXParameters);
        if (n <= 0) {
            return CertPathValidatorUtilities.getValidDate(extendedPKIXParameters);
        }
        if (n - 1 != 0) return ((X509Certificate)certPath.getCertificates().get(n - 1)).getNotBefore();
        byte[] arrby = ((X509Certificate)certPath.getCertificates().get(n - 1)).getExtensionValue(ISISMTTObjectIdentifiers.id_isismtt_at_dateOfCertGen.getId());
        ASN1GeneralizedTime aSN1GeneralizedTime2 = arrby != null ? (aSN1GeneralizedTime = ASN1GeneralizedTime.getInstance(ASN1Primitive.fromByteArray(arrby))) : null;
        if (aSN1GeneralizedTime2 == null) return ((X509Certificate)certPath.getCertificates().get(n - 1)).getNotBefore();
        try {
            return aSN1GeneralizedTime2.getDate();
        }
        catch (ParseException parseException) {
            throw new AnnotatedException("Date from date of cert gen extension could not be parsed.", parseException);
        }
        catch (IOException iOException) {
            throw new AnnotatedException("Date of cert gen extension could not be read.");
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new AnnotatedException("Date of cert gen extension could not be read.");
        }
    }

    protected static Date getValidDate(PKIXParameters pKIXParameters) {
        Date date = pKIXParameters.getDate();
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    protected static boolean isAnyPolicy(Set set) {
        return set == null || set.contains((Object)ANY_POLICY) || set.isEmpty();
    }

    private static boolean isDeltaCRL(X509CRL x509CRL) {
        Set set = x509CRL.getCriticalExtensionOIDs();
        if (set == null) {
            return false;
        }
        return set.contains((Object)Extension.deltaCRLIndicator.getId());
    }

    static boolean isIndirectCRL(X509CRL x509CRL) {
        block3 : {
            byte[] arrby;
            try {
                arrby = x509CRL.getExtensionValue(Extension.issuingDistributionPoint.getId());
                if (arrby == null) break block3;
            }
            catch (Exception exception) {
                throw new CRLException("Exception reading IssuingDistributionPoint: " + (Object)((Object)exception));
            }
            boolean bl = IssuingDistributionPoint.getInstance(ASN1OctetString.getInstance(arrby).getOctets()).isIndirectCRL();
            if (!bl) break block3;
            return true;
        }
        return false;
    }

    protected static boolean isSelfIssued(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectDN().equals((Object)x509Certificate.getIssuerDN());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void prepareNextCertB1(int var0, List[] var1_1, String var2_2, Map var3_3, X509Certificate var4_4) {
        block11 : {
            block10 : {
                for (PKIXPolicyNode var23_6 : var1_1[var0]) {
                    if (!var23_6.getValidPolicy().equals((Object)var2_2)) continue;
                    var23_6.setExpectedPolicies((Set)var3_3.get((Object)var2_2));
                    var6_7 = true;
                    break block10;
                }
                var6_7 = false;
            }
            if (var6_7 != false) return;
            var7_8 = var1_1[var0].iterator();
            do {
                if (var7_8.hasNext() == false) return;
            } while (!"2.5.29.32.0".equals((Object)(var8_9 = (PKIXPolicyNode)var7_8.next()).getValidPolicy()));
            var10_10 = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var4_4, CertPathValidatorUtilities.CERTIFICATE_POLICIES));
            var11_11 = var10_10.getObjects();
            do {
                var12_12 = var11_11.hasMoreElements();
                var13_13 = null;
                if (!var12_12) break block11;
                ** try [egrp 1[TRYBLOCK] [1 : 149->161)] { 
lbl21: // 1 sources:
            } while (!"2.5.29.32.0".equals((Object)(var20_14 = PolicyInformation.getInstance(var11_11.nextElement())).getPolicyIdentifier().getId()));
            try {
                var13_13 = var22_15 = CertPathValidatorUtilities.getQualifierSet(var20_14.getPolicyQualifiers());
            }
            catch (CertPathValidatorException var21_22) {
                throw new ExtCertPathValidatorException("Policy qualifier info set could not be built.", var21_22);
            }
        }
        var14_16 = var4_4.getCriticalExtensionOIDs();
        var15_17 = false;
        if (var14_16 != null) {
            var15_17 = var4_4.getCriticalExtensionOIDs().contains((Object)CertPathValidatorUtilities.CERTIFICATE_POLICIES);
        }
        if ("2.5.29.32.0".equals((Object)(var16_18 = (PKIXPolicyNode)var8_9.getParent()).getValidPolicy()) == false) return;
        var17_19 = new PKIXPolicyNode((List)new ArrayList(), var0, (Set)var3_3.get((Object)var2_2), var16_18, var13_13, var2_2, var15_17);
        var16_18.addChild(var17_19);
        var1_1[var0].add((Object)var17_19);
        return;
        catch (Exception var9_20) {
            throw new AnnotatedException("Certificate policies cannot be decoded.", var9_20);
        }
lbl38: // 1 sources:
        catch (Exception var19_21) {
            throw new AnnotatedException("Policy information cannot be decoded.", var19_21);
        }
    }

    protected static PKIXPolicyNode prepareNextCertB2(int n, List[] arrlist, String string, PKIXPolicyNode pKIXPolicyNode) {
        Iterator iterator = arrlist[n].iterator();
        do {
            PKIXPolicyNode pKIXPolicyNode2;
            if (iterator.hasNext()) {
                PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode)iterator.next();
                if (!pKIXPolicyNode3.getValidPolicy().equals((Object)string)) continue;
                ((PKIXPolicyNode)pKIXPolicyNode3.getParent()).removeChild(pKIXPolicyNode3);
                iterator.remove();
                pKIXPolicyNode2 = pKIXPolicyNode;
                block1 : for (int i = n - 1; i >= 0; --i) {
                    List list = arrlist[i];
                    PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNode2;
                    int n2 = 0;
                    do {
                        PKIXPolicyNode pKIXPolicyNode5;
                        if (n2 >= list.size() || !(pKIXPolicyNode5 = (PKIXPolicyNode)list.get(n2)).hasChildren() && (pKIXPolicyNode4 = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode4, arrlist, pKIXPolicyNode5)) == null) {
                            pKIXPolicyNode2 = pKIXPolicyNode4;
                            continue block1;
                        }
                        ++n2;
                    } while (true);
                }
            } else {
                return pKIXPolicyNode;
            }
            pKIXPolicyNode = pKIXPolicyNode2;
        } while (true);
    }

    protected static boolean processCertD1i(int n, List[] arrlist, ASN1ObjectIdentifier aSN1ObjectIdentifier, Set set) {
        List list = arrlist[n - 1];
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = list.size();
                    bl = false;
                    if (n2 >= n3) break block3;
                    PKIXPolicyNode pKIXPolicyNode = (PKIXPolicyNode)list.get(n2);
                    if (!pKIXPolicyNode.getExpectedPolicies().contains((Object)aSN1ObjectIdentifier.getId())) break block4;
                    HashSet hashSet = new HashSet();
                    hashSet.add((Object)aSN1ObjectIdentifier.getId());
                    PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode((List)new ArrayList(), n, (Set)hashSet, pKIXPolicyNode, set, aSN1ObjectIdentifier.getId(), false);
                    pKIXPolicyNode.addChild(pKIXPolicyNode2);
                    arrlist[n].add((Object)pKIXPolicyNode2);
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    protected static void processCertD1ii(int n, List[] arrlist, ASN1ObjectIdentifier aSN1ObjectIdentifier, Set set) {
        List list = arrlist[n - 1];
        int n2 = 0;
        do {
            block4 : {
                block3 : {
                    if (n2 >= list.size()) break block3;
                    PKIXPolicyNode pKIXPolicyNode = (PKIXPolicyNode)list.get(n2);
                    if (!ANY_POLICY.equals((Object)pKIXPolicyNode.getValidPolicy())) break block4;
                    HashSet hashSet = new HashSet();
                    hashSet.add((Object)aSN1ObjectIdentifier.getId());
                    PKIXPolicyNode pKIXPolicyNode2 = new PKIXPolicyNode((List)new ArrayList(), n, (Set)hashSet, pKIXPolicyNode, set, aSN1ObjectIdentifier.getId(), false);
                    pKIXPolicyNode.addChild(pKIXPolicyNode2);
                    arrlist[n].add((Object)pKIXPolicyNode2);
                }
                return;
            }
            ++n2;
        } while (true);
    }

    protected static PKIXPolicyNode removePolicyNode(PKIXPolicyNode pKIXPolicyNode, List[] arrlist, PKIXPolicyNode pKIXPolicyNode2) {
        PKIXPolicyNode pKIXPolicyNode3 = (PKIXPolicyNode)pKIXPolicyNode2.getParent();
        if (pKIXPolicyNode == null) {
            return null;
        }
        if (pKIXPolicyNode3 == null) {
            for (int i = 0; i < arrlist.length; ++i) {
                arrlist[i] = new ArrayList();
            }
            return null;
        }
        pKIXPolicyNode3.removeChild(pKIXPolicyNode2);
        CertPathValidatorUtilities.removePolicyNodeRecurse(arrlist, pKIXPolicyNode2);
        return pKIXPolicyNode;
    }

    private static void removePolicyNodeRecurse(List[] arrlist, PKIXPolicyNode pKIXPolicyNode) {
        arrlist[pKIXPolicyNode.getDepth()].remove((Object)pKIXPolicyNode);
        if (pKIXPolicyNode.hasChildren()) {
            Iterator iterator = pKIXPolicyNode.getChildren();
            while (iterator.hasNext()) {
                CertPathValidatorUtilities.removePolicyNodeRecurse(arrlist, (PKIXPolicyNode)iterator.next());
            }
        }
    }

    protected static void verifyX509Certificate(X509Certificate x509Certificate, PublicKey publicKey, String string) {
        if (string == null) {
            x509Certificate.verify(publicKey);
            return;
        }
        x509Certificate.verify(publicKey, string);
    }
}

