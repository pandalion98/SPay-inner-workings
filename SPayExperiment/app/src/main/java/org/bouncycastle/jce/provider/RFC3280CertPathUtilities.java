/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.GeneralSecurityException
 *  java.security.PublicKey
 *  java.security.cert.CRL
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathBuilderException
 *  java.security.cert.CertPathBuilderResult
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertPathValidatorException
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateNotYetValidException
 *  java.security.cert.PKIXCertPathChecker
 *  java.security.cert.PolicyNode
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLSelector
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.security.cert.X509Extension
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.TimeZone
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PolicyNode;
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
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
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
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCRLStoreSelector;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.jce.provider.CertStatus;
import org.bouncycastle.jce.provider.PKIXCRLUtil;
import org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidator;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidatorException;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.jce.provider.PrincipalUtils;
import org.bouncycastle.jce.provider.ReasonsMask;
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
        crlReasons = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    }

    RFC3280CertPathUtilities() {
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static void checkCRL(DistributionPoint distributionPoint, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, CertStatus certStatus, ReasonsMask reasonsMask, List list, JcaJceHelper jcaJceHelper) {
        Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        Iterator iterator = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, (Object)x509Certificate, date2, pKIXExtendedParameters).iterator();
        AnnotatedException annotatedException = null;
        boolean bl = false;
        do {
            if (iterator.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                Set set;
                X509CRL x509CRL = (X509CRL)iterator.next();
                ReasonsMask reasonsMask2 = RFC3280CertPathUtilities.processCRLD(x509CRL, distributionPoint);
                if (!reasonsMask2.hasNewReasons(reasonsMask)) continue;
                PublicKey publicKey2 = RFC3280CertPathUtilities.processCRLG(x509CRL, RFC3280CertPathUtilities.processCRLF(x509CRL, (Object)x509Certificate, x509Certificate2, publicKey, pKIXExtendedParameters, list, jcaJceHelper));
                Date date3 = pKIXExtendedParameters.getDate() != null ? pKIXExtendedParameters.getDate() : date2;
                boolean bl2 = pKIXExtendedParameters.isUseDeltasEnabled();
                X509CRL x509CRL2 = null;
                if (bl2) {
                    x509CRL2 = RFC3280CertPathUtilities.processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date3, x509CRL, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()), publicKey2);
                }
                if (pKIXExtendedParameters.getValidityModel() != 1 && x509Certificate.getNotAfter().getTime() < x509CRL.getThisUpdate().getTime()) {
                    throw new AnnotatedException("No valid CRL for current time found.");
                }
                RFC3280CertPathUtilities.processCRLB1(distributionPoint, (Object)x509Certificate, x509CRL);
                RFC3280CertPathUtilities.processCRLB2(distributionPoint, (Object)x509Certificate, x509CRL);
                RFC3280CertPathUtilities.processCRLC(x509CRL2, x509CRL, pKIXExtendedParameters);
                RFC3280CertPathUtilities.processCRLI(date, x509CRL2, (Object)x509Certificate, certStatus, pKIXExtendedParameters);
                RFC3280CertPathUtilities.processCRLJ(date, x509CRL, (Object)x509Certificate, certStatus);
                if (certStatus.getCertStatus() == 8) {
                    certStatus.setCertStatus(11);
                }
                reasonsMask.addReasons(reasonsMask2);
                Set set2 = x509CRL.getCriticalExtensionOIDs();
                if (set2 != null) {
                    HashSet hashSet = new HashSet((Collection)set2);
                    hashSet.remove((Object)Extension.issuingDistributionPoint.getId());
                    hashSet.remove((Object)Extension.deltaCRLIndicator.getId());
                    if (!hashSet.isEmpty()) {
                        throw new AnnotatedException("CRL contains unsupported critical extensions.");
                    }
                }
                if (x509CRL2 != null && (set = x509CRL2.getCriticalExtensionOIDs()) != null) {
                    HashSet hashSet = new HashSet((Collection)set);
                    hashSet.remove((Object)Extension.issuingDistributionPoint.getId());
                    hashSet.remove((Object)Extension.deltaCRLIndicator.getId());
                    if (!hashSet.isEmpty()) {
                        throw new AnnotatedException("Delta CRL contains unsupported critical extension.");
                    }
                }
                bl = true;
                continue;
            }
            if (bl) return;
            throw annotatedException;
            catch (AnnotatedException annotatedException2) {
                annotatedException = annotatedException2;
                continue;
            }
            break;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    protected static void checkCRLs(PKIXExtendedParameters var0, X509Certificate var1_1, Date var2_2, X509Certificate var3_3, PublicKey var4_4, List var5_5, JcaJceHelper var6_6) {
        block26 : {
            block25 : {
                block24 : {
                    var8_7 = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var1_1, RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS));
                    var9_8 = new PKIXExtendedParameters.Builder(var0);
                    try {
                        var11_9 = CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(var8_7, var0.getNamedCRLStoreMap()).iterator();
                        while (var11_9.hasNext()) {
                            var9_8.addCRLStore((PKIXCRLStore)var11_9.next());
                        }
                    }
                    catch (AnnotatedException var10_10) {
                        throw new AnnotatedException("No additional CRL locations could be decoded from CRL distribution point extension.", (Throwable)var10_10);
                    }
                    catch (Exception var7_11) {
                        throw new AnnotatedException("CRL distribution point extension could not be read.", var7_11);
                    }
                    var12_12 = new CertStatus();
                    var13_13 = new ReasonsMask();
                    var14_14 = var9_8.build();
                    var15_15 = false;
                    var16_16 = null;
                    if (var8_7 != null) {
                        try {
                            var23_17 = var8_7.getDistributionPoints();
                            var15_15 = false;
                            var16_16 = null;
                            ** if (var23_17 == null) goto lbl-1000
                        }
                        catch (Exception var22_22) {
                            throw new AnnotatedException("Distribution points could not be read.", var22_22);
                        }
lbl-1000: // 2 sources:
                        {
                            block14 : for (var24_18 = 0; var24_18 < var23_17.length && var12_12.getCertStatus() == 11 && !var13_13.isAllReasons(); ++var24_18) {
                                RFC3280CertPathUtilities.checkCRL(var23_17[var24_18], var14_14, var1_1, var2_2, var3_3, var4_4, var12_12, var13_13, var5_5, var6_6);
                                var27_21 = true;
                                var26_20 = var16_16;
lbl30: // 2 sources:
                                do {
                                    var15_15 = var27_21;
                                    var16_16 = var26_20;
                                    continue block14;
                                    break;
                                } while (true);
                            }
                        }
lbl-1000: // 2 sources:
                        {
                            break block24;
                        }
                        catch (AnnotatedException var25_19) {
                            var26_20 = var25_19;
                            var27_21 = var15_15;
                            ** continue;
                        }
                    }
                }
                if (var12_12.getCertStatus() == 11 && !var13_13.isAllReasons()) {
                    var21_23 = new ASN1InputStream(PrincipalUtils.getEncodedIssuerPrincipal((Object)var1_1).getEncoded()).readObject();
                    RFC3280CertPathUtilities.checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, var21_23))), null, null), (PKIXExtendedParameters)var0.clone(), var1_1, var2_2, var3_3, var4_4, var12_12, var13_13, var5_5, var6_6);
                    var15_15 = true;
                }
lbl48: // 3 sources:
                while (!var15_15) {
                    if (var16_16 instanceof AnnotatedException) {
                        throw var16_16;
                    }
                    break block25;
                }
                {
                    break block26;
                    catch (Exception var20_24) {
                        try {
                            throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", var20_24);
                        }
                        catch (AnnotatedException var19_25) {
                            var16_16 = var19_25;
                        }
                    }
                }
                ** GOTO lbl48
            }
            throw new AnnotatedException("No valid CRL found.", (Throwable)var16_16);
        }
        if (var12_12.getCertStatus() != 11) {
            var17_26 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            var17_26.setTimeZone(TimeZone.getTimeZone((String)"UTC"));
            var18_27 = "Certificate revocation after " + var17_26.format(var12_12.getRevocationDate());
            throw new AnnotatedException(var18_27 + ", reason: " + RFC3280CertPathUtilities.crlReasons[var12_12.getCertStatus()]);
        }
        if (!var13_13.isAllReasons() && var12_12.getCertStatus() == 11) {
            var12_12.setCertStatus(12);
        }
        if (var12_12.getCertStatus() == 12) {
            throw new AnnotatedException("Certificate status could not be determined.");
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    protected static PKIXPolicyNode prepareCertB(CertPath var0, int var1_1, List[] var2_2, PKIXPolicyNode var3_3, int var4_4) {
        block25 : {
            block26 : {
                block22 : {
                    block27 : {
                        block23 : {
                            var5_5 = var0.getCertificates();
                            var6_6 = (X509Certificate)var5_5.get(var1_1);
                            var7_7 = var5_5.size() - var1_1;
                            try {
                                var9_8 = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var6_6, RFC3280CertPathUtilities.POLICY_MAPPINGS));
                                if (var9_8 == null) break block22;
                            }
                            catch (AnnotatedException var8_16) {
                                throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", (Throwable)var8_16, var0, var1_1);
                            }
                            var10_9 = new HashMap();
                            var11_10 = new HashSet();
                            block8 : for (var12_11 = 0; var12_11 < var9_8.size(); ++var12_11) {
                                var42_12 = (ASN1Sequence)var9_8.getObjectAt(var12_11);
                                var43_13 = ((ASN1ObjectIdentifier)var42_12.getObjectAt(0)).getId();
                                var44_14 = ((ASN1ObjectIdentifier)var42_12.getObjectAt(1)).getId();
                                if (!var10_9.containsKey((Object)var43_13)) {
                                    var45_15 = new HashSet();
                                    var45_15.add((Object)var44_14);
                                    var10_9.put((Object)var43_13, (Object)var45_15);
                                    var11_10.add((Object)var43_13);
lbl18: // 2 sources:
                                    continue block8;
                                }
                                break block23;
                            }
                            break block27;
                        }
                        ((Set)var10_9.get((Object)var43_13)).add((Object)var44_14);
                        ** while (true)
                    }
                    block10 : for (String var14_18 : var11_10) {
                        if (var4_4 > 0) {
                            for (PKIXPolicyNode var41_41 : var2_2[var7_7]) {
                                if (!var41_41.getValidPolicy().equals((Object)var14_18)) continue;
                                var41_41.expectedPolicies = (Set)var10_9.get((Object)var14_18);
                                var24_28 = true;
lbl34: // 2 sources:
                                while (!var24_28) {
                                    for (PKIXPolicyNode var26_30 : var2_2[var7_7]) {
                                        block24 : {
                                            if (!"2.5.29.32.0".equals((Object)var26_30.getValidPolicy())) continue;
                                            var28_31 = (ASN1Sequence)CertPathValidatorUtilities.getExtensionValue((X509Extension)var6_6, RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                            var29_32 = var28_31.getObjects();
                                            do {
                                                var30_33 = var29_32.hasMoreElements();
                                                var31_34 = null;
                                                if (!var30_33) break block24;
                                                ** try [egrp 2[TRYBLOCK] [2 : 408->420)] { 
lbl45: // 1 sources:
                                            } while (!"2.5.29.32.0".equals((Object)(var38_39 = PolicyInformation.getInstance(var29_32.nextElement())).getPolicyIdentifier().getId()));
                                            try {
                                                var31_34 = var40_40 = CertPathValidatorUtilities.getQualifierSet(var38_39.getPolicyQualifiers());
                                            }
                                            catch (CertPathValidatorException var39_44) {
                                                throw new ExtCertPathValidatorException("Policy qualifier info set could not be decoded.", var39_44, var0, var1_1);
                                            }
                                        }
                                        var32_35 = var6_6.getCriticalExtensionOIDs();
                                        var33_36 = false;
                                        if (var32_35 != null) {
                                            var33_36 = var6_6.getCriticalExtensionOIDs().contains((Object)RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                        }
                                        if (!"2.5.29.32.0".equals((Object)(var34_37 = (PKIXPolicyNode)var26_30.getParent()).getValidPolicy())) continue block10;
                                        var35_38 = new PKIXPolicyNode((List)new ArrayList(), var7_7, (Set)var10_9.get((Object)var14_18), var34_37, var31_34, var14_18, var33_36);
                                        var34_37.addChild(var35_38);
                                        var2_2[var7_7].add((Object)var35_38);
                                        continue block10;
                                        catch (AnnotatedException var27_42) {
                                            throw new ExtCertPathValidatorException("Certificate policies extension could not be decoded.", (Throwable)var27_42, var0, var1_1);
                                        }
lbl62: // 1 sources:
                                        catch (Exception var37_43) {
                                            throw new CertPathValidatorException("Policy information could not be decoded.", (Throwable)var37_43, var0, var1_1);
                                        }
                                    }
                                    continue block10;
                                }
                                continue block10;
                            }
                            break block25;
                        }
                        if (var4_4 > 0) continue;
                        var15_19 = var2_2[var7_7].iterator();
lbl69: // 3 sources:
                        while (var15_19.hasNext()) {
                            var16_20 = (PKIXPolicyNode)var15_19.next();
                            if (!var16_20.getValidPolicy().equals((Object)var14_18)) continue;
                            ((PKIXPolicyNode)var16_20.getParent()).removeChild(var16_20);
                            var15_19.remove();
                            var18_22 = var3_3;
                            block16 : for (var17_21 = var7_7 - 1; var17_21 >= 0; --var17_21) {
                                var19_23 = var2_2[var17_21];
                                var20_24 = var18_22;
                                var21_25 = 0;
                                do {
                                    if (var21_25 >= var19_23.size() || !(var22_26 = (PKIXPolicyNode)var19_23.get(var21_25)).hasChildren() && (var20_24 = CertPathValidatorUtilities.removePolicyNode(var20_24, var2_2, var22_26)) == null) {
                                        var18_22 = var20_24;
                                        continue block16;
                                    }
                                    ++var21_25;
                                } while (true);
                            }
                            break block26;
                        }
                    }
                }
                return var3_3;
            }
            var3_3 = var18_22;
            ** GOTO lbl69
        }
        var24_28 = false;
        ** GOTO lbl34
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static void prepareNextCertA(CertPath certPath, int n) {
        block6 : {
            ASN1Sequence aSN1Sequence;
            X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
            try {
                aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, POLICY_MAPPINGS));
                if (aSN1Sequence == null) break block6;
            }
            catch (AnnotatedException annotatedException) {
                throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", (Throwable)annotatedException, certPath, n);
            }
            for (int i = 0; i < aSN1Sequence.size(); ++i) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier;
                ASN1ObjectIdentifier aSN1ObjectIdentifier2;
                try {
                    ASN1Sequence aSN1Sequence2 = DERSequence.getInstance(aSN1Sequence.getObjectAt(i));
                    aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(aSN1Sequence2.getObjectAt(0));
                    aSN1ObjectIdentifier2 = ASN1ObjectIdentifier.getInstance(aSN1Sequence2.getObjectAt(1));
                }
                catch (Exception exception) {
                    throw new ExtCertPathValidatorException("Policy mappings extension contents could not be decoded.", exception, certPath, n);
                }
                if (ANY_POLICY.equals((Object)aSN1ObjectIdentifier.getId())) {
                    throw new CertPathValidatorException("IssuerDomainPolicy is anyPolicy", null, certPath, n);
                }
                if (!ANY_POLICY.equals((Object)aSN1ObjectIdentifier2.getId())) continue;
                throw new CertPathValidatorException("SubjectDomainPolicy is anyPolicy,", null, certPath, n);
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
    protected static void prepareNextCertG(CertPath certPath, int n, PKIXNameConstraintValidator pKIXNameConstraintValidator) {
        NameConstraints nameConstraints;
        block8 : {
            X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
            try {
                NameConstraints nameConstraints2;
                ASN1Sequence aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, NAME_CONSTRAINTS));
                nameConstraints = aSN1Sequence != null ? (nameConstraints2 = NameConstraints.getInstance(aSN1Sequence)) : null;
            }
            catch (Exception exception) {
                throw new ExtCertPathValidatorException("Name constraints extension could not be decoded.", exception, certPath, n);
            }
            if (nameConstraints == null) return;
            GeneralSubtree[] arrgeneralSubtree2 = nameConstraints.getPermittedSubtrees();
            if (arrgeneralSubtree2 != null) {
                pKIXNameConstraintValidator.intersectPermittedSubtree(arrgeneralSubtree2);
            }
            break block8;
            catch (Exception exception) {
                throw new ExtCertPathValidatorException("Permitted subtrees cannot be build from name constraints extension.", exception, certPath, n);
            }
        }
        GeneralSubtree[] arrgeneralSubtree = nameConstraints.getExcludedSubtrees();
        if (arrgeneralSubtree == null) return;
        int n2 = 0;
        while (n2 != arrgeneralSubtree.length) {
            try {
                pKIXNameConstraintValidator.addExcludedSubtree(arrgeneralSubtree[n2]);
                ++n2;
            }
            catch (Exception exception) {
                throw new ExtCertPathValidatorException("Excluded subtrees cannot be build from name constraints extension.", exception, certPath, n);
            }
        }
    }

    protected static int prepareNextCertH1(CertPath certPath, int n, int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            --n2;
        }
        return n2;
    }

    protected static int prepareNextCertH2(CertPath certPath, int n, int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            --n2;
        }
        return n2;
    }

    protected static int prepareNextCertH3(CertPath certPath, int n, int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            --n2;
        }
        return n2;
    }

    /*
     * Loose catch block
     * Enabled aggressive exception aggregation
     */
    protected static int prepareNextCertI1(CertPath certPath, int n, int n2) {
        block5 : {
            ASN1Sequence aSN1Sequence;
            X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
            try {
                aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, POLICY_CONSTRAINTS));
                if (aSN1Sequence == null) break block5;
            }
            catch (Exception exception) {
                throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", exception, certPath, n);
            }
            Enumeration enumeration = aSN1Sequence.getObjects();
            while (enumeration.hasMoreElements()) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
                if (aSN1TaggedObject.getTagNo() != 0) continue;
                int n3 = ASN1Integer.getInstance(aSN1TaggedObject, false).getValue().intValue();
                if (n3 >= n2) break;
                n2 = n3;
                break;
            }
        }
        return n2;
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", illegalArgumentException, certPath, n);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive exception aggregation
     */
    protected static int prepareNextCertI2(CertPath certPath, int n, int n2) {
        block5 : {
            ASN1Sequence aSN1Sequence;
            X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
            try {
                aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, POLICY_CONSTRAINTS));
                if (aSN1Sequence == null) break block5;
            }
            catch (Exception exception) {
                throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", exception, certPath, n);
            }
            Enumeration enumeration = aSN1Sequence.getObjects();
            while (enumeration.hasMoreElements()) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
                if (aSN1TaggedObject.getTagNo() != 1) continue;
                int n3 = ASN1Integer.getInstance(aSN1TaggedObject, false).getValue().intValue();
                if (n3 >= n2) break;
                n2 = n3;
                break;
            }
        }
        return n2;
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", illegalArgumentException, certPath, n);
        }
    }

    protected static int prepareNextCertJ(CertPath certPath, int n, int n2) {
        ASN1Integer aSN1Integer;
        int n3;
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            aSN1Integer = ASN1Integer.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, INHIBIT_ANY_POLICY));
        }
        catch (Exception exception) {
            throw new ExtCertPathValidatorException("Inhibit any-policy extension cannot be decoded.", exception, certPath, n);
        }
        if (aSN1Integer != null && (n3 = aSN1Integer.getValue().intValue()) < n2) {
            n2 = n3;
        }
        return n2;
    }

    protected static void prepareNextCertK(CertPath certPath, int n) {
        block4 : {
            block3 : {
                BasicConstraints basicConstraints;
                X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
                try {
                    basicConstraints = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, BASIC_CONSTRAINTS));
                    if (basicConstraints == null) break block3;
                }
                catch (Exception exception) {
                    throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", exception, certPath, n);
                }
                if (!basicConstraints.isCA()) {
                    throw new CertPathValidatorException("Not a CA certificate");
                }
                break block4;
            }
            throw new CertPathValidatorException("Intermediate certificate lacks BasicConstraints");
        }
    }

    protected static int prepareNextCertL(CertPath certPath, int n, int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n))) {
            if (n2 <= 0) {
                throw new ExtCertPathValidatorException("Max path length not greater than zero", null, certPath, n);
            }
            --n2;
        }
        return n2;
    }

    protected static int prepareNextCertM(CertPath certPath, int n, int n2) {
        BigInteger bigInteger;
        int n3;
        BasicConstraints basicConstraints;
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            basicConstraints = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, BASIC_CONSTRAINTS));
        }
        catch (Exception exception) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", exception, certPath, n);
        }
        if (basicConstraints != null && (bigInteger = basicConstraints.getPathLenConstraint()) != null && (n3 = bigInteger.intValue()) < n2) {
            n2 = n3;
        }
        return n2;
    }

    protected static void prepareNextCertN(CertPath certPath, int n) {
        boolean[] arrbl = ((X509Certificate)certPath.getCertificates().get(n)).getKeyUsage();
        if (arrbl != null && !arrbl[5]) {
            throw new ExtCertPathValidatorException("Issuer certificate keyusage extension is critical and does not permit key signing.", null, certPath, n);
        }
    }

    protected static void prepareNextCertO(CertPath certPath, int n, Set set, List list) {
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                ((PKIXCertPathChecker)iterator.next()).check((Certificate)x509Certificate, (Collection)set);
            }
            catch (CertPathValidatorException certPathValidatorException) {
                throw new CertPathValidatorException(certPathValidatorException.getMessage(), certPathValidatorException.getCause(), certPath, n);
            }
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension: " + (Object)set, null, certPath, n);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static Set processCRLA1i(Date date, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, X509CRL x509CRL) {
        HashSet hashSet;
        CRLDistPoint cRLDistPoint;
        block8 : {
            hashSet = new HashSet();
            if (!pKIXExtendedParameters.isUseDeltasEnabled()) return hashSet;
            try {
                CRLDistPoint cRLDistPoint2;
                cRLDistPoint = cRLDistPoint2 = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, FRESHEST_CRL));
                if (cRLDistPoint != null) break block8;
            }
            catch (AnnotatedException annotatedException) {
                throw new AnnotatedException("Freshest CRL extension could not be decoded from certificate.", (Throwable)annotatedException);
            }
            try {
                CRLDistPoint cRLDistPoint3;
                cRLDistPoint = cRLDistPoint3 = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509CRL, FRESHEST_CRL));
            }
            catch (AnnotatedException annotatedException) {
                throw new AnnotatedException("Freshest CRL extension could not be decoded from CRL.", (Throwable)annotatedException);
            }
        }
        if (cRLDistPoint == null) return hashSet;
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(pKIXExtendedParameters.getCRLStores());
        try {
            arrayList.addAll(CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(cRLDistPoint, pKIXExtendedParameters.getNamedCRLStoreMap()));
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("No new delta CRL locations could be added from Freshest CRL extension.", (Throwable)annotatedException);
        }
        try {
            hashSet.addAll((Collection)CertPathValidatorUtilities.getDeltaCRLs(date, x509CRL, pKIXExtendedParameters.getCertStores(), (List<PKIXCRLStore>)arrayList));
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Exception obtaining delta CRLs.", (Throwable)annotatedException);
        }
        return hashSet;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static Set[] processCRLA1ii(Date date, PKIXExtendedParameters pKIXExtendedParameters, X509Certificate x509Certificate, X509CRL x509CRL) {
        HashSet hashSet = new HashSet();
        X509CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.setCertificateChecking(x509Certificate);
        try {
            x509CRLSelector.addIssuerName(PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded());
        }
        catch (IOException iOException) {
            throw new AnnotatedException("Cannot extract issuer from CRL." + (Object)((Object)iOException), iOException);
        }
        PKIXCRLStoreSelector<? extends CRL> pKIXCRLStoreSelector = new PKIXCRLStoreSelector.Builder((CRLSelector)x509CRLSelector).setCompleteCRLEnabled(true).build();
        if (pKIXExtendedParameters.getDate() != null) {
            date = pKIXExtendedParameters.getDate();
        }
        Set set = CRL_UTIL.findCRLs(pKIXCRLStoreSelector, date, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores());
        if (!pKIXExtendedParameters.isUseDeltasEnabled()) return new Set[]{set, hashSet};
        try {
            hashSet.addAll((Collection)CertPathValidatorUtilities.getDeltaCRLs(date, x509CRL, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()));
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Exception obtaining delta CRLs.", (Throwable)annotatedException);
        }
        return new Set[]{set, hashSet};
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static void processCRLB1(DistributionPoint distributionPoint, Object object, X509CRL x509CRL) {
        boolean bl;
        block10 : {
            boolean bl2;
            byte[] arrby;
            GeneralName[] arrgeneralName;
            block9 : {
                block8 : {
                    ASN1Primitive aSN1Primitive = CertPathValidatorUtilities.getExtensionValue((X509Extension)x509CRL, ISSUING_DISTRIBUTION_POINT);
                    bl2 = aSN1Primitive != null && IssuingDistributionPoint.getInstance(aSN1Primitive).isIndirectCRL();
                    try {
                        arrby = PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded();
                    }
                    catch (IOException iOException) {
                        throw new AnnotatedException("Exception encoding CRL issuer: " + iOException.getMessage(), iOException);
                    }
                    if (distributionPoint.getCRLIssuer() == null) break block8;
                    arrgeneralName = distributionPoint.getCRLIssuer().getNames();
                    bl = false;
                    break block9;
                }
                if (PrincipalUtils.getIssuerPrincipal(x509CRL).equals(PrincipalUtils.getEncodedIssuerPrincipal(object))) {
                    return;
                }
                bl = false;
                break block10;
            }
            for (int i = 0; i < arrgeneralName.length; ++i) {
                if (arrgeneralName[i].getTagNo() != 4) continue;
                try {
                    boolean bl3 = Arrays.areEqual(arrgeneralName[i].getName().toASN1Primitive().getEncoded(), arrby);
                    if (!bl3) continue;
                    bl = true;
                }
                catch (IOException iOException) {
                    throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", iOException);
                }
            }
            if (bl && !bl2) {
                throw new AnnotatedException("Distribution point contains cRLIssuer field but CRL is not indirect.");
            }
            if (!bl) {
                throw new AnnotatedException("CRL issuer of CRL does not match CRL issuer of distribution point.");
            }
        }
        if (bl) return;
        throw new AnnotatedException("Cannot find matching CRL issuer for certificate.");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void processCRLB2(DistributionPoint var0, Object var1_1, X509CRL var2_2) {
        block42 : {
            block31 : {
                block37 : {
                    block36 : {
                        block35 : {
                            block33 : {
                                block34 : {
                                    try {
                                        var4_3 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var2_2, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
                                        if (var4_3 == null) return;
                                    }
                                    catch (Exception var3_8) {
                                        throw new AnnotatedException("Issuing distribution point extension could not be decoded.", var3_8);
                                    }
                                    if (var4_3.getDistributionPoint() == null) break block31;
                                    var7_4 = IssuingDistributionPoint.getInstance(var4_3).getDistributionPoint();
                                    var8_5 = new ArrayList();
                                    if (var7_4.getType() == 0) {
                                        var29_6 = GeneralNames.getInstance(var7_4.getName()).getNames();
                                        for (var30_7 = 0; var30_7 < var29_6.length; ++var30_7) {
                                            var8_5.add((Object)var29_6[var30_7]);
                                        }
                                    }
                                    if (var7_4.getType() == 1) {
                                        var9_9 = new ASN1EncodableVector();
                                        try {
                                            var11_10 = ASN1Sequence.getInstance(PrincipalUtils.getIssuerPrincipal(var2_2)).getObjects();
                                            while (var11_10.hasMoreElements()) {
                                                var9_9.add((ASN1Encodable)var11_10.nextElement());
                                            }
                                        }
                                        catch (Exception var10_11) {
                                            throw new AnnotatedException("Could not read CRL issuer.", var10_11);
                                        }
                                        var9_9.add(var7_4.getName());
                                        var8_5.add((Object)new GeneralName(X500Name.getInstance(new DERSequence(var9_9))));
                                    }
                                    if (var0.getDistributionPoint() == null) break block33;
                                    var17_12 = var0.getDistributionPoint();
                                    var18_13 = var17_12.getType();
                                    var19_14 = null;
                                    if (var18_13 == 0) {
                                        var19_14 = GeneralNames.getInstance(var17_12.getName()).getNames();
                                    }
                                    if (var17_12.getType() != 1) break block34;
                                    if (var0.getCRLIssuer() != null) {
                                        var20_15 = var0.getCRLIssuer().getNames();
                                    } else {
                                        var24_16 = new GeneralName[1];
                                        var24_16[0] = new GeneralName(X500Name.getInstance(PrincipalUtils.getEncodedIssuerPrincipal(var1_1).getEncoded()));
                                        var20_15 = var24_16;
                                    }
                                    break block35;
                                    catch (Exception var25_20) {
                                        throw new AnnotatedException("Could not read certificate issuer.", var25_20);
                                    }
                                }
                                var20_15 = var19_14;
                                break block36;
                            }
                            if (var0.getCRLIssuer() == null) {
                                throw new AnnotatedException("Either the cRLIssuer or the distributionPoint field must be contained in DistributionPoint.");
                            }
                            break block37;
                        }
                        for (var26_17 = 0; var26_17 < var20_15.length; ++var26_17) {
                            var27_18 = ASN1Sequence.getInstance(var20_15[var26_17].getName().toASN1Primitive()).getObjects();
                            var28_19 = new ASN1EncodableVector();
                            while (var27_18.hasMoreElements()) {
                                var28_19.add((ASN1Encodable)var27_18.nextElement());
                            }
                            var28_19.add(var17_12.getName());
                            var20_15[var26_17] = new GeneralName(X500Name.getInstance(new DERSequence(var28_19)));
                        }
                    }
                    var21_21 = false;
                    if (var20_15 == null) ** GOTO lbl72
                    var22_22 = 0;
                    do {
                        block39 : {
                            block38 : {
                                var23_23 = var20_15.length;
                                var21_21 = false;
                                if (var22_22 >= var23_23) break block38;
                                if (!var8_5.contains((Object)var20_15[var22_22])) break block39;
                                var21_21 = true;
                            }
                            if (!var21_21) {
                                throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                            }
                            break block31;
                        }
                        ++var22_22;
                    } while (true);
                }
                var13_24 = var0.getCRLIssuer().getNames();
                var14_25 = 0;
                do {
                    block41 : {
                        block40 : {
                            var15_26 = var13_24.length;
                            var16_27 = false;
                            if (var14_25 >= var15_26) break block40;
                            if (!var8_5.contains((Object)var13_24[var14_25])) break block41;
                            var16_27 = true;
                        }
                        if (var16_27) break;
                        throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                    }
                    ++var14_25;
                } while (true);
            }
            try {
                var6_28 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var1_1, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
            }
            catch (Exception var5_29) {
                throw new AnnotatedException("Basic constraints extension could not be decoded.", var5_29);
            }
            if (!(var1_1 instanceof X509Certificate)) break block42;
            if (var4_3.onlyContainsUserCerts() && var6_28 != null && var6_28.isCA()) {
                throw new AnnotatedException("CA Cert CRL only contains user certificates.");
            }
            if (var4_3.onlyContainsCACerts()) {
                if (var6_28 == null) throw new AnnotatedException("End CRL only contains CA certificates.");
                if (!var6_28.isCA()) {
                    throw new AnnotatedException("End CRL only contains CA certificates.");
                }
            }
        }
        if (var4_3.onlyContainsAttributeCerts() == false) return;
        throw new AnnotatedException("onlyContainsAttributeCerts boolean is asserted.");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void processCRLC(X509CRL var0, X509CRL var1_1, PKIXExtendedParameters var2_2) {
        block13 : {
            var3_3 = true;
            if (var0 == null) {
                return;
            }
            try {
                var5_4 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var1_1, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
            }
            catch (Exception var4_5) {
                throw new AnnotatedException("Issuing distribution point extension could not be decoded.", var4_5);
            }
            if (var2_2.isUseDeltasEnabled() == false) return;
            if (!PrincipalUtils.getIssuerPrincipal(var0).equals(PrincipalUtils.getIssuerPrincipal(var1_1))) {
                throw new AnnotatedException("Complete CRL issuer does not match delta CRL issuer.");
            }
            try {
                var7_6 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var0, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
                if (var5_4 != null ? var5_4.equals(var7_6) != false : var7_6 == null) break block13;
            }
            catch (Exception var6_7) {
                throw new AnnotatedException("Issuing distribution point extension from delta CRL could not be decoded.", var6_7);
            }
            var3_3 = false;
        }
        if (!var3_3) {
            throw new AnnotatedException("Issuing distribution point extension from delta CRL and complete CRL does not match.");
        }
        try {
            var9_8 = CertPathValidatorUtilities.getExtensionValue((X509Extension)var1_1, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
        }
        catch (AnnotatedException var8_10) {
            throw new AnnotatedException("Authority key identifier extension could not be extracted from complete CRL.", (Throwable)var8_10);
        }
        try {
            var11_9 = CertPathValidatorUtilities.getExtensionValue((X509Extension)var0, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
            ** if (var9_8 != null) goto lbl-1000
        }
        catch (AnnotatedException var10_11) {
            throw new AnnotatedException("Authority key identifier extension could not be extracted from delta CRL.", (Throwable)var10_11);
        }
lbl-1000: // 1 sources:
        {
            throw new AnnotatedException("CRL authority key identifier is null.");
        }
lbl-1000: // 1 sources:
        {
        }
        if (var11_9 == null) {
            throw new AnnotatedException("Delta CRL authority key identifier is null.");
        }
        if (var9_8.equals(var11_9) != false) return;
        throw new AnnotatedException("Delta CRL authority key identifier does not match complete CRL authority key identifier.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static ReasonsMask processCRLD(X509CRL x509CRL, DistributionPoint distributionPoint) {
        ReasonsMask reasonsMask;
        IssuingDistributionPoint issuingDistributionPoint;
        try {
            issuingDistributionPoint = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509CRL, ISSUING_DISTRIBUTION_POINT));
        }
        catch (Exception exception) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", exception);
        }
        if (issuingDistributionPoint != null && issuingDistributionPoint.getOnlySomeReasons() != null && distributionPoint.getReasons() != null) {
            return new ReasonsMask(distributionPoint.getReasons()).intersect(new ReasonsMask(issuingDistributionPoint.getOnlySomeReasons()));
        }
        if ((issuingDistributionPoint == null || issuingDistributionPoint.getOnlySomeReasons() == null) && distributionPoint.getReasons() == null) {
            return ReasonsMask.allReasons;
        }
        ReasonsMask reasonsMask2 = distributionPoint.getReasons() == null ? ReasonsMask.allReasons : new ReasonsMask(distributionPoint.getReasons());
        if (issuingDistributionPoint == null) {
            reasonsMask = ReasonsMask.allReasons;
            return reasonsMask2.intersect(reasonsMask);
        }
        reasonsMask = new ReasonsMask(issuingDistributionPoint.getOnlySomeReasons());
        return reasonsMask2.intersect(reasonsMask);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static Set processCRLF(X509CRL x509CRL, Object object, X509Certificate x509Certificate, PublicKey publicKey, PKIXExtendedParameters pKIXExtendedParameters, List list, JcaJceHelper jcaJceHelper) {
        Collection collection;
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded());
        }
        catch (IOException iOException) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate for CRL could not be set.", iOException);
        }
        PKIXCertStoreSelector<? extends Certificate> pKIXCertStoreSelector = new PKIXCertStoreSelector.Builder((CertSelector)x509CertSelector).build();
        try {
            collection = CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, pKIXExtendedParameters.getCertificateStores());
            collection.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, pKIXExtendedParameters.getCertStores()));
        }
        catch (AnnotatedException annotatedException) {
            throw new AnnotatedException("Issuer certificate for CRL cannot be searched.", (Throwable)annotatedException);
        }
        collection.add((Object)x509Certificate);
        Iterator iterator = collection.iterator();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (iterator.hasNext()) {
            X509Certificate x509Certificate2 = (X509Certificate)iterator.next();
            if (x509Certificate2.equals((Object)x509Certificate)) {
                arrayList.add((Object)x509Certificate2);
                arrayList2.add((Object)publicKey);
                continue;
            }
            try {
                PKIXCertPathBuilderSpi pKIXCertPathBuilderSpi = new PKIXCertPathBuilderSpi();
                X509CertSelector x509CertSelector2 = new X509CertSelector();
                x509CertSelector2.setCertificate(x509Certificate2);
                PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder(pKIXExtendedParameters).setTargetConstraints(new PKIXCertStoreSelector.Builder((CertSelector)x509CertSelector2).build());
                if (list.contains((Object)x509Certificate2)) {
                    builder.setRevocationEnabled(false);
                } else {
                    builder.setRevocationEnabled(true);
                }
                List list2 = pKIXCertPathBuilderSpi.engineBuild(new PKIXExtendedBuilderParameters.Builder(builder.build()).build()).getCertPath().getCertificates();
                arrayList.add((Object)x509Certificate2);
                arrayList2.add((Object)CertPathValidatorUtilities.getNextWorkingKey(list2, 0, jcaJceHelper));
            }
            catch (CertPathBuilderException certPathBuilderException) {
                throw new AnnotatedException("CertPath for CRL signer failed to validate.", certPathBuilderException);
            }
            catch (CertPathValidatorException certPathValidatorException) {
                throw new AnnotatedException("Public key of issuer certificate of CRL could not be retrieved.", certPathValidatorException);
            }
            catch (Exception exception) {
                throw new AnnotatedException(exception.getMessage());
            }
        }
        HashSet hashSet = new HashSet();
        AnnotatedException annotatedException = null;
        for (int i = 0; i < arrayList.size(); ++i) {
            AnnotatedException annotatedException2;
            boolean[] arrbl = ((X509Certificate)arrayList.get(i)).getKeyUsage();
            if (!(arrbl == null || arrbl.length >= 7 && arrbl[6])) {
                annotatedException2 = new AnnotatedException("Issuer certificate key usage extension does not permit CRL signing.");
            } else {
                hashSet.add(arrayList2.get(i));
                annotatedException2 = annotatedException;
            }
            annotatedException = annotatedException2;
        }
        if (hashSet.isEmpty() && annotatedException == null) {
            throw new AnnotatedException("Cannot find a valid issuer certificate.");
        }
        if (hashSet.isEmpty() && annotatedException != null) {
            throw annotatedException;
        }
        return hashSet;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static PublicKey processCRLG(X509CRL x509CRL, Set set) {
        Throwable throwable = null;
        for (PublicKey publicKey : set) {
            try {
                x509CRL.verify(publicKey);
                return publicKey;
            }
            catch (Exception exception) {
            }
        }
        throw new AnnotatedException("Cannot verify CRL.", throwable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static X509CRL processCRLH(Set set, PublicKey publicKey) {
        Iterator iterator = set.iterator();
        Throwable throwable = null;
        do {
            if (!iterator.hasNext()) {
                if (throwable == null) return null;
                throw new AnnotatedException("Cannot verify delta CRL.", throwable);
            }
            X509CRL x509CRL = (X509CRL)iterator.next();
            try {
                x509CRL.verify(publicKey);
                return x509CRL;
            }
            catch (Exception exception) {
                continue;
            }
            break;
        } while (true);
    }

    protected static void processCRLI(Date date, X509CRL x509CRL, Object object, CertStatus certStatus, PKIXExtendedParameters pKIXExtendedParameters) {
        if (pKIXExtendedParameters.isUseDeltasEnabled() && x509CRL != null) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, object, certStatus);
        }
    }

    protected static void processCRLJ(Date date, X509CRL x509CRL, Object object, CertStatus certStatus) {
        if (certStatus.getCertStatus() == 11) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, object, certStatus);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void processCertA(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters, int n, PublicKey publicKey, boolean bl, X500Name x500Name, X509Certificate x509Certificate, JcaJceHelper jcaJceHelper) {
        AnnotatedException annotatedException42222;
        AnnotatedException annotatedException2;
        block10 : {
            List list = certPath.getCertificates();
            X509Certificate x509Certificate2 = (X509Certificate)list.get(n);
            if (!bl) {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, publicKey, pKIXExtendedParameters.getSigProvider());
            }
            try {
                x509Certificate2.checkValidity(CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, certPath, n));
            }
            catch (CertificateExpiredException certificateExpiredException) {
                throw new ExtCertPathValidatorException("Could not validate certificate: " + certificateExpiredException.getMessage(), certificateExpiredException, certPath, n);
            }
            catch (CertificateNotYetValidException certificateNotYetValidException) {
                throw new ExtCertPathValidatorException("Could not validate certificate: " + certificateNotYetValidException.getMessage(), certificateNotYetValidException, certPath, n);
            }
            catch (AnnotatedException annotatedException3) {
                throw new ExtCertPathValidatorException("Could not validate time of certificate.", (Throwable)annotatedException3, certPath, n);
            }
            if (pKIXExtendedParameters.isRevocationEnabled()) {
                RFC3280CertPathUtilities.checkCRLs(pKIXExtendedParameters, x509Certificate2, CertPathValidatorUtilities.getValidCertDateFromValidityModel(pKIXExtendedParameters, certPath, n), x509Certificate, publicKey, list, jcaJceHelper);
            }
            if (PrincipalUtils.getEncodedIssuerPrincipal((Object)x509Certificate2).equals(x500Name)) return;
            throw new ExtCertPathValidatorException("IssuerName(" + PrincipalUtils.getEncodedIssuerPrincipal((Object)x509Certificate2) + ") does not match SubjectName(" + x500Name + ") of signing certificate.", null, certPath, n);
            catch (GeneralSecurityException generalSecurityException) {
                throw new ExtCertPathValidatorException("Could not validate certificate signature.", generalSecurityException, certPath, n);
            }
            catch (AnnotatedException annotatedException42222) {
                if (annotatedException42222.getCause() == null) break block10;
                annotatedException2 = annotatedException42222.getCause();
                throw new ExtCertPathValidatorException(annotatedException42222.getMessage(), (Throwable)annotatedException2, certPath, n);
            }
        }
        annotatedException2 = annotatedException42222;
        throw new ExtCertPathValidatorException(annotatedException42222.getMessage(), (Throwable)annotatedException2, certPath, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected static void processCertBC(CertPath certPath, int n, PKIXNameConstraintValidator pKIXNameConstraintValidator) {
        block15 : {
            GeneralNames generalNames;
            ASN1Sequence aSN1Sequence;
            int n2 = 0;
            List list = certPath.getCertificates();
            X509Certificate x509Certificate = (X509Certificate)list.get(n);
            int n3 = list.size();
            int n4 = n3 - n;
            if (CertPathValidatorUtilities.isSelfIssued(x509Certificate) && n4 < n3) break block15;
            X500Name x500Name = PrincipalUtils.getSubjectPrincipal(x509Certificate);
            try {
                aSN1Sequence = DERSequence.getInstance(x500Name.getEncoded());
            }
            catch (Exception exception) {
                throw new CertPathValidatorException("Exception extracting subject name when checking subtrees.", (Throwable)exception, certPath, n);
            }
            try {
                pKIXNameConstraintValidator.checkPermittedDN(aSN1Sequence);
                pKIXNameConstraintValidator.checkExcludedDN(aSN1Sequence);
            }
            catch (PKIXNameConstraintValidatorException pKIXNameConstraintValidatorException) {
                throw new CertPathValidatorException("Subtree check for certificate subject failed.", (Throwable)pKIXNameConstraintValidatorException, certPath, n);
            }
            try {
                generalNames = GeneralNames.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, SUBJECT_ALTERNATIVE_NAME));
            }
            catch (Exception exception) {
                throw new CertPathValidatorException("Subject alternative name extension could not be decoded.", (Throwable)exception, certPath, n);
            }
            RDN[] arrrDN = X500Name.getInstance(aSN1Sequence).getRDNs(BCStyle.EmailAddress);
            for (int i = 0; i != arrrDN.length; ++i) {
                GeneralName generalName = new GeneralName(1, ((ASN1String)((Object)arrrDN[i].getFirst().getValue())).getString());
                try {
                    pKIXNameConstraintValidator.checkPermitted(generalName);
                    pKIXNameConstraintValidator.checkExcluded(generalName);
                    continue;
                }
                catch (PKIXNameConstraintValidatorException pKIXNameConstraintValidatorException) {
                    throw new CertPathValidatorException("Subtree check for certificate subject alternative email failed.", (Throwable)pKIXNameConstraintValidatorException, certPath, n);
                }
            }
            if (generalNames != null) {
                GeneralName[] arrgeneralName;
                try {
                    arrgeneralName = generalNames.getNames();
                }
                catch (Exception exception) {
                    throw new CertPathValidatorException("Subject alternative name contents could not be decoded.", (Throwable)exception, certPath, n);
                }
                while (n2 < arrgeneralName.length) {
                    try {
                        pKIXNameConstraintValidator.checkPermitted(arrgeneralName[n2]);
                        pKIXNameConstraintValidator.checkExcluded(arrgeneralName[n2]);
                        ++n2;
                    }
                    catch (PKIXNameConstraintValidatorException pKIXNameConstraintValidatorException) {
                        throw new CertPathValidatorException("Subtree check for certificate subject alternative name failed.", (Throwable)pKIXNameConstraintValidatorException, certPath, n);
                    }
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static PKIXPolicyNode processCertD(CertPath var0, int var1_1, Set var2_2, PKIXPolicyNode var3_3, List[] var4_4, int var5_5) {
        block20 : {
            var6_6 = var0.getCertificates();
            var7_7 = (X509Certificate)var6_6.get(var1_1);
            var8_8 = var6_6.size();
            var9_9 = var8_8 - var1_1;
            try {
                var11_10 = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)var7_7, RFC3280CertPathUtilities.CERTIFICATE_POLICIES));
                if (var11_10 == null) return null;
                if (var3_3 == null) return null;
            }
            catch (AnnotatedException var10_16) {
                throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", (Throwable)var10_16, var0, var1_1);
            }
            var13_11 = var11_10.getObjects();
            var14_12 = new HashSet();
            while (var13_11.hasMoreElements()) {
                var46_13 = PolicyInformation.getInstance(var13_11.nextElement());
                var47_14 = var46_13.getPolicyIdentifier();
                var14_12.add((Object)var47_14.getId());
                if ("2.5.29.32.0".equals((Object)var47_14.getId())) continue;
                ** try [egrp 1[TRYBLOCK] [1 : 127->137)] { 
lbl17: // 1 sources:
                var50_15 = CertPathValidatorUtilities.getQualifierSet(var46_13.getPolicyQualifiers());
                if (CertPathValidatorUtilities.processCertD1i(var9_9, var4_4, var47_14, var50_15)) continue;
                CertPathValidatorUtilities.processCertD1ii(var9_9, var4_4, var47_14, var50_15);
            }
            break block20;
lbl24: // 1 sources:
            catch (CertPathValidatorException var49_17) {
                throw new ExtCertPathValidatorException("Policy qualifier info set could not be build.", var49_17, var0, var1_1);
            }
        }
        if (var2_2.isEmpty() || var2_2.contains((Object)"2.5.29.32.0")) {
            var2_2.clear();
            var2_2.addAll((Collection)var14_12);
        } else {
            var41_18 = var2_2.iterator();
            var42_19 = new HashSet();
            while (var41_18.hasNext()) {
                var44_20 = var41_18.next();
                if (!var14_12.contains(var44_20)) continue;
                var42_19.add(var44_20);
            }
            var2_2.clear();
            var2_2.addAll((Collection)var42_19);
        }
        if (var5_5 > 0 || var9_9 < var8_8 && CertPathValidatorUtilities.isSelfIssued(var7_7)) {
            var16_21 = var11_10.getObjects();
            while (var16_21.hasMoreElements()) {
                var26_22 = PolicyInformation.getInstance(var16_21.nextElement());
                if (!"2.5.29.32.0".equals((Object)var26_22.getPolicyIdentifier().getId())) continue;
                var27_23 = CertPathValidatorUtilities.getQualifierSet(var26_22.getPolicyQualifiers());
                var28_24 = var4_4[var9_9 - 1];
                block7 : for (var29_25 = 0; var29_25 < var28_24.size(); ++var29_25) {
                    var30_26 = (PKIXPolicyNode)var28_24.get(var29_25);
                    var31_27 = var30_26.getExpectedPolicies().iterator();
                    do {
                        if (!var31_27.hasNext()) continue block7;
                        var32_28 = var31_27.next();
                        if (var32_28 instanceof String) {
                            var33_29 = (String)var32_28;
                        } else {
                            if (!(var32_28 instanceof ASN1ObjectIdentifier)) continue;
                            var33_29 = ((ASN1ObjectIdentifier)var32_28).getId();
                        }
                        var34_30 = var30_26.getChildren();
                        var35_31 = false;
                        while (var34_30.hasNext()) {
                            var40_34 = var33_29.equals((Object)((PKIXPolicyNode)var34_30.next()).getValidPolicy()) != false ? true : var35_31;
                            var35_31 = var40_34;
                        }
                        if (var35_31) continue;
                        var36_32 = new HashSet();
                        var36_32.add((Object)var33_29);
                        var38_33 = new PKIXPolicyNode((List)new ArrayList(), var9_9, (Set)var36_32, var30_26, var27_23, var33_29, false);
                        var30_26.addChild(var38_33);
                        var4_4[var9_9].add((Object)var38_33);
                    } while (true);
                }
            }
        }
        var17_35 = var9_9 - 1;
        var12_36 = var3_3;
        block10 : do {
            if (var17_35 < 0) {
                var18_41 = var7_7.getCriticalExtensionOIDs();
                if (var18_41 == null) return var12_36;
                var19_42 = var18_41.contains((Object)RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                var20_43 = var4_4[var9_9];
                var21_44 = 0;
                while (var21_44 < var20_43.size()) {
                    ((PKIXPolicyNode)var20_43.get(var21_44)).setCritical(var19_42);
                    ++var21_44;
                }
                return var12_36;
            }
            var22_37 = var4_4[var17_35];
            var23_38 = var12_36;
            var24_39 = 0;
            do {
                if (var24_39 >= var22_37.size() || !(var25_40 = (PKIXPolicyNode)var22_37.get(var24_39)).hasChildren() && (var23_38 = CertPathValidatorUtilities.removePolicyNode(var23_38, var4_4, var25_40)) == null) {
                    --var17_35;
                    var12_36 = var23_38;
                    continue block10;
                }
                ++var24_39;
            } while (true);
            break;
        } while (true);
    }

    protected static PKIXPolicyNode processCertE(CertPath certPath, int n, PKIXPolicyNode pKIXPolicyNode) {
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            ASN1Sequence aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, CERTIFICATE_POLICIES));
            if (aSN1Sequence == null) {
                pKIXPolicyNode = null;
            }
            return pKIXPolicyNode;
        }
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", (Throwable)annotatedException, certPath, n);
        }
    }

    protected static void processCertF(CertPath certPath, int n, PKIXPolicyNode pKIXPolicyNode, int n2) {
        if (n2 <= 0 && pKIXPolicyNode == null) {
            throw new ExtCertPathValidatorException("No valid policy tree found when one expected.", null, certPath, n);
        }
    }

    protected static int wrapupCertA(int n, X509Certificate x509Certificate) {
        if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate) && n != 0) {
            --n;
        }
        return n;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static int wrapupCertB(CertPath certPath, int n, int n2) {
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {}
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathValidatorException("Policy constraints could not be decoded.", (Throwable)annotatedException, certPath, n);
        }
        ASN1Sequence aSN1Sequence = DERSequence.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)x509Certificate, POLICY_CONSTRAINTS));
        if (aSN1Sequence == null) return n2;
        Enumeration enumeration = aSN1Sequence.getObjects();
        block7 : while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)enumeration.nextElement();
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    continue block7;
                }
                case 0: 
            }
            int n3 = ASN1Integer.getInstance(aSN1TaggedObject, false).getValue().intValue();
            if (n3 == 0) return 0;
        }
        return n2;
        catch (Exception exception) {
            throw new ExtCertPathValidatorException("Policy constraints requireExplicitPolicy field could not be decoded.", exception, certPath, n);
        }
    }

    protected static void wrapupCertF(CertPath certPath, int n, List list, Set set) {
        X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                ((PKIXCertPathChecker)iterator.next()).check((Certificate)x509Certificate, (Collection)set);
            }
            catch (CertPathValidatorException certPathValidatorException) {
                throw new ExtCertPathValidatorException("Additional certificate path checker failed.", certPathValidatorException, certPath, n);
            }
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension: " + (Object)set, null, certPath, n);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static PKIXPolicyNode wrapupCertG(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters, Set set, int n, List[] arrlist, PKIXPolicyNode pKIXPolicyNode, Set set2) {
        int n2 = certPath.getCertificates().size();
        if (pKIXPolicyNode == null) {
            if (!pKIXExtendedParameters.isExplicitPolicyRequired()) return null;
            throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, n);
        }
        if (CertPathValidatorUtilities.isAnyPolicy(set)) {
            if (!pKIXExtendedParameters.isExplicitPolicyRequired()) return pKIXPolicyNode;
            if (set2.isEmpty()) {
                throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, n);
            }
            HashSet hashSet = new HashSet();
            for (int i = 0; i < arrlist.length; ++i) {
                List list = arrlist[i];
                for (int j = 0; j < list.size(); ++j) {
                    PKIXPolicyNode pKIXPolicyNode2 = (PKIXPolicyNode)list.get(j);
                    if (!ANY_POLICY.equals((Object)pKIXPolicyNode2.getValidPolicy())) continue;
                    Iterator iterator = pKIXPolicyNode2.getChildren();
                    while (iterator.hasNext()) {
                        hashSet.add(iterator.next());
                    }
                }
            }
            Iterator iterator = hashSet.iterator();
            while (iterator.hasNext()) {
                if (set2.contains((Object)((PKIXPolicyNode)iterator.next()).getValidPolicy())) continue;
            }
            if (pKIXPolicyNode == null) return pKIXPolicyNode;
            int n3 = n2 - 1;
            PKIXPolicyNode pKIXPolicyNode3 = pKIXPolicyNode;
            while (n3 >= 0) {
                List list = arrlist[n3];
                PKIXPolicyNode pKIXPolicyNode4 = pKIXPolicyNode3;
                for (int i = 0; i < list.size(); ++i) {
                    PKIXPolicyNode pKIXPolicyNode5 = (PKIXPolicyNode)list.get(i);
                    if (pKIXPolicyNode5.hasChildren()) continue;
                    pKIXPolicyNode4 = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode4, arrlist, pKIXPolicyNode5);
                }
                --n3;
                pKIXPolicyNode3 = pKIXPolicyNode4;
            }
            return pKIXPolicyNode3;
        }
        HashSet hashSet = new HashSet();
        for (int i = 0; i < arrlist.length; ++i) {
            List list = arrlist[i];
            for (int j = 0; j < list.size(); ++j) {
                PKIXPolicyNode pKIXPolicyNode6 = (PKIXPolicyNode)list.get(j);
                if (!ANY_POLICY.equals((Object)pKIXPolicyNode6.getValidPolicy())) continue;
                Iterator iterator = pKIXPolicyNode6.getChildren();
                while (iterator.hasNext()) {
                    PKIXPolicyNode pKIXPolicyNode7 = (PKIXPolicyNode)iterator.next();
                    if (ANY_POLICY.equals((Object)pKIXPolicyNode7.getValidPolicy())) continue;
                    hashSet.add((Object)pKIXPolicyNode7);
                }
            }
        }
        for (PKIXPolicyNode pKIXPolicyNode8 : hashSet) {
            if (set.contains((Object)pKIXPolicyNode8.getValidPolicy())) continue;
            pKIXPolicyNode = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode, arrlist, pKIXPolicyNode8);
        }
        if (pKIXPolicyNode == null) return pKIXPolicyNode;
        int n4 = n2 - 1;
        PKIXPolicyNode pKIXPolicyNode9 = pKIXPolicyNode;
        while (n4 >= 0) {
            List list = arrlist[n4];
            PKIXPolicyNode pKIXPolicyNode10 = pKIXPolicyNode9;
            for (int i = 0; i < list.size(); ++i) {
                PKIXPolicyNode pKIXPolicyNode11 = (PKIXPolicyNode)list.get(i);
                if (pKIXPolicyNode11.hasChildren()) continue;
                pKIXPolicyNode10 = CertPathValidatorUtilities.removePolicyNode(pKIXPolicyNode10, arrlist, pKIXPolicyNode11);
            }
            --n4;
            pKIXPolicyNode9 = pKIXPolicyNode10;
        }
        return pKIXPolicyNode9;
    }
}

