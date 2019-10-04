/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.NoSuchAlgorithmException
 *  java.security.NoSuchProviderException
 *  java.security.Principal
 *  java.security.PublicKey
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathBuilder
 *  java.security.cert.CertPathBuilderException
 *  java.security.cert.CertPathBuilderResult
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertPathValidator
 *  java.security.cert.CertPathValidatorException
 *  java.security.cert.CertPathValidatorResult
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateNotYetValidException
 *  java.security.cert.TrustAnchor
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.x509.TargetInformation;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.jce.provider.CertStatus;
import org.bouncycastle.jce.provider.RFC3280CertPathUtilities;
import org.bouncycastle.jce.provider.ReasonsMask;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.PKIXAttrCertChecker;
import org.bouncycastle.x509.X509Attribute;
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
        for (String string : set) {
            if (x509AttributeCertificate.getAttributes(string) == null) continue;
            throw new CertPathValidatorException("Attribute certificate contains prohibited attribute: " + string + ".");
        }
        for (String string : set2) {
            if (x509AttributeCertificate.getAttributes(string) != null) continue;
            throw new CertPathValidatorException("Attribute certificate does not contain necessary attribute: " + string + ".");
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static void checkCRL(DistributionPoint distributionPoint, X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters, Date date, X509Certificate x509Certificate, CertStatus certStatus, ReasonsMask reasonsMask, List list, JcaJceHelper jcaJceHelper) {
        if (x509AttributeCertificate.getExtensionValue(X509Extensions.NoRevAvail.getId()) != null) {
            return;
        }
        Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        Iterator iterator = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509AttributeCertificate, date2, pKIXExtendedParameters).iterator();
        AnnotatedException annotatedException = null;
        boolean bl = false;
        do {
            if (iterator.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                X509CRL x509CRL = (X509CRL)iterator.next();
                ReasonsMask reasonsMask2 = RFC3280CertPathUtilities.processCRLD(x509CRL, distributionPoint);
                if (!reasonsMask2.hasNewReasons(reasonsMask)) continue;
                PublicKey publicKey = RFC3280CertPathUtilities.processCRLG(x509CRL, RFC3280CertPathUtilities.processCRLF(x509CRL, x509AttributeCertificate, null, null, pKIXExtendedParameters, list, jcaJceHelper));
                boolean bl2 = pKIXExtendedParameters.isUseDeltasEnabled();
                X509CRL x509CRL2 = null;
                if (bl2) {
                    x509CRL2 = RFC3280CertPathUtilities.processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date2, x509CRL, pKIXExtendedParameters.getCertStores(), pKIXExtendedParameters.getCRLStores()), publicKey);
                }
                if (pKIXExtendedParameters.getValidityModel() != 1 && x509AttributeCertificate.getNotAfter().getTime() < x509CRL.getThisUpdate().getTime()) {
                    throw new AnnotatedException("No valid CRL for current time found.");
                }
                RFC3280CertPathUtilities.processCRLB1(distributionPoint, x509AttributeCertificate, x509CRL);
                RFC3280CertPathUtilities.processCRLB2(distributionPoint, x509AttributeCertificate, x509CRL);
                RFC3280CertPathUtilities.processCRLC(x509CRL2, x509CRL, pKIXExtendedParameters);
                RFC3280CertPathUtilities.processCRLI(date, x509CRL2, x509AttributeCertificate, certStatus, pKIXExtendedParameters);
                RFC3280CertPathUtilities.processCRLJ(date, x509CRL, x509AttributeCertificate, certStatus);
                if (certStatus.getCertStatus() == 8) {
                    certStatus.setCertStatus(11);
                }
                reasonsMask.addReasons(reasonsMask2);
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
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static void checkCRLs(X509AttributeCertificate var0, PKIXExtendedParameters var1_1, X509Certificate var2_2, Date var3_3, List var4_4, JcaJceHelper var5_5) {
        block29 : {
            block27 : {
                block28 : {
                    if (!var1_1.isRevocationEnabled()) return;
                    if (var0.getExtensionValue(RFC3281CertPathUtilities.NO_REV_AVAIL) != null) break block27;
                    var7_6 = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(var0, RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS));
                    var8_7 = new ArrayList();
                    try {
                        var8_7.addAll(CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(var7_6, var1_1.getNamedCRLStoreMap()));
                    }
                    catch (AnnotatedException var9_11) {
                        throw new CertPathValidatorException("No additional CRL locations could be decoded from CRL distribution point extension.", (Throwable)var9_11);
                    }
                    var11_8 = new PKIXExtendedParameters.Builder(var1_1);
                    var12_9 = var8_7.iterator();
                    while (var12_9.hasNext()) {
                        var11_8.addCRLStore((PKIXCRLStore)var8_7);
                    }
                    break block28;
                    catch (AnnotatedException var6_10) {
                        throw new CertPathValidatorException("CRL distribution point extension could not be read.", (Throwable)var6_10);
                    }
                }
                var13_12 = var11_8.build();
                var14_13 = new CertStatus();
                var15_14 = new ReasonsMask();
                if (var7_6 == null) break block29;
                try {
                    var23_15 = var7_6.getDistributionPoints();
                    var24_16 = 0;
                    var16_17 = false;
                }
                catch (Exception var22_23) {
                    throw new ExtCertPathValidatorException("Distribution points could not be read.", var22_23);
                }
                do {
                    var26_19 = var23_15.length;
                    var17_18 = null;
                    ** if (var24_16 >= var26_19) goto lbl-1000
lbl-1000: // 1 sources:
                    {
                        var27_20 = var14_13.getCertStatus();
                        var17_18 = null;
                        ** if (var27_20 != 11) goto lbl-1000
lbl-1000: // 1 sources:
                        {
                            var28_21 = var15_14.isAllReasons();
                            var17_18 = null;
                            ** if (var28_21) goto lbl-1000
lbl-1000: // 1 sources:
                            {
                                var29_22 = (PKIXExtendedParameters)var13_12.clone();
                                RFC3281CertPathUtilities.checkCRL(var23_15[var24_16], var0, var29_22, var3_3, var2_2, var14_13, var15_14, var4_4, var5_5);
                                var16_17 = true;
                                ++var24_16;
                                continue;
                            }
                        }
                    }
lbl-1000: // 3 sources:
                    {
                    }
                    break;
                } while (true);
                catch (AnnotatedException var25_24) {
                    var17_18 = new AnnotatedException("No valid CRL for distribution point found.", (Throwable)var25_24);
                }
lbl51: // 3 sources:
                do {
                    block26 : {
                        if (var14_13.getCertStatus() == 11 && !var15_14.isAllReasons()) {
                            var21_25 = new ASN1InputStream(((X500Principal)var0.getIssuer().getPrincipals()[0]).getEncoded()).readObject();
                            RFC3281CertPathUtilities.checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, var21_25))), null, null), var0, (PKIXExtendedParameters)var13_12.clone(), var3_3, var2_2, var14_13, var15_14, var4_4, var5_5);
                            var16_17 = true;
                        }
lbl58: // 3 sources:
                        while (!var16_17) {
                            throw new ExtCertPathValidatorException("No valid CRL found.", (Throwable)var17_18);
                        }
                        {
                            break block26;
                            catch (Exception var20_26) {
                                try {
                                    throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", var20_26);
                                }
                                catch (AnnotatedException var19_27) {
                                    var17_18 = new AnnotatedException("No valid CRL for distribution point found.", (Throwable)var19_27);
                                }
                            }
                        }
                        ** GOTO lbl58
                    }
                    if (var14_13.getCertStatus() != 11) {
                        var18_28 = "Attribute certificate revocation after " + (Object)var14_13.getRevocationDate();
                        throw new CertPathValidatorException(var18_28 + ", reason: " + RFC3280CertPathUtilities.crlReasons[var14_13.getCertStatus()]);
                    }
                    if (!var15_14.isAllReasons() && var14_13.getCertStatus() == 11) {
                        var14_13.setCertStatus(12);
                    }
                    if (var14_13.getCertStatus() != 12) return;
                    throw new CertPathValidatorException("Attribute certificate status could not be determined.");
                    break;
                } while (true);
            }
            if (var0.getExtensionValue(RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS) == null && var0.getExtensionValue(RFC3281CertPathUtilities.AUTHORITY_INFO_ACCESS) == null) return;
            throw new CertPathValidatorException("No rev avail extension is set, but also an AC revocation pointer.");
        }
        var16_17 = false;
        var17_18 = null;
        ** while (true)
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static CertPath processAttrCert1(X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters) {
        int n = 0;
        HashSet hashSet = new HashSet();
        if (x509AttributeCertificate.getHolder().getIssuer() != null) {
            X509CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setSerialNumber(x509AttributeCertificate.getHolder().getSerialNumber());
            Principal[] arrprincipal = x509AttributeCertificate.getHolder().getIssuer();
            for (int i = 0; i < arrprincipal.length; ++i) {
                try {
                    if (arrprincipal[i] instanceof X500Principal) {
                        x509CertSelector.setIssuer(((X500Principal)arrprincipal[i]).getEncoded());
                    }
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder((CertSelector)x509CertSelector).build(), pKIXExtendedParameters.getCertStores()));
                    continue;
                }
                catch (AnnotatedException annotatedException) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", (Throwable)annotatedException);
                }
                catch (IOException iOException) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", iOException);
                }
            }
            if (hashSet.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in base certificate ID for attribute certificate cannot be found.");
            }
        }
        if (x509AttributeCertificate.getHolder().getEntityNames() != null) {
            X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            Principal[] arrprincipal = x509AttributeCertificate.getHolder().getEntityNames();
            while (n < arrprincipal.length) {
                try {
                    if (arrprincipal[n] instanceof X500Principal) {
                        x509CertStoreSelector.setIssuer(((X500Principal)arrprincipal[n]).getEncoded());
                    }
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder((CertSelector)x509CertStoreSelector).build(), pKIXExtendedParameters.getCertStores()));
                    ++n;
                }
                catch (AnnotatedException annotatedException) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", (Throwable)annotatedException);
                }
                catch (IOException iOException) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", iOException);
                }
            }
            if (hashSet.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in entity name for attribute certificate cannot be found.");
            }
        }
        PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder(pKIXExtendedParameters);
        Iterator iterator = hashSet.iterator();
        ExtCertPathValidatorException extCertPathValidatorException = null;
        CertPathBuilderResult certPathBuilderResult = null;
        do {
            if (!iterator.hasNext()) {
                if (extCertPathValidatorException == null) return certPathBuilderResult.getCertPath();
                throw extCertPathValidatorException;
            }
            X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            x509CertStoreSelector.setCertificate((X509Certificate)iterator.next());
            builder.setTargetConstraints(new PKIXCertStoreSelector.Builder((CertSelector)x509CertStoreSelector).build());
            CertPathBuilder certPathBuilder = CertPathBuilder.getInstance((String)"PKIX", (String)"BC");
            CertPathBuilderResult certPathBuilderResult2 = certPathBuilder.build((CertPathParameters)new PKIXExtendedBuilderParameters.Builder(builder.build()).build());
            ExtCertPathValidatorException extCertPathValidatorException2 = extCertPathValidatorException;
            CertPathBuilderResult certPathBuilderResult3 = certPathBuilderResult2;
            ExtCertPathValidatorException extCertPathValidatorException3 = extCertPathValidatorException2;
            catch (NoSuchProviderException noSuchProviderException) {
                throw new ExtCertPathValidatorException("Support class could not be created.", noSuchProviderException);
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new ExtCertPathValidatorException("Support class could not be created.", noSuchAlgorithmException);
            }
            catch (CertPathBuilderException certPathBuilderException) {
                extCertPathValidatorException3 = new ExtCertPathValidatorException("Certification path for public key certificate of attribute certificate could not be build.", certPathBuilderException);
                certPathBuilderResult3 = certPathBuilderResult;
            }
            certPathBuilderResult = certPathBuilderResult3;
            extCertPathValidatorException = extCertPathValidatorException3;
        } while (true);
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new RuntimeException(invalidAlgorithmParameterException.getMessage());
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static CertPathValidatorResult processAttrCert2(CertPath certPath, PKIXExtendedParameters pKIXExtendedParameters) {
        CertPathValidator certPathValidator = CertPathValidator.getInstance((String)"PKIX", (String)"BC");
        return certPathValidator.validate(certPath, (CertPathParameters)pKIXExtendedParameters);
        catch (NoSuchProviderException noSuchProviderException) {
            throw new ExtCertPathValidatorException("Support class could not be created.", noSuchProviderException);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new ExtCertPathValidatorException("Support class could not be created.", noSuchAlgorithmException);
        }
        catch (CertPathValidatorException certPathValidatorException) {
            throw new ExtCertPathValidatorException("Certification path for issuer certificate of attribute certificate could not be validated.", certPathValidatorException);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new RuntimeException(invalidAlgorithmParameterException.getMessage());
        }
    }

    protected static void processAttrCert3(X509Certificate x509Certificate, PKIXExtendedParameters pKIXExtendedParameters) {
        if (x509Certificate.getKeyUsage() != null && !x509Certificate.getKeyUsage()[0] && !x509Certificate.getKeyUsage()[1]) {
            throw new CertPathValidatorException("Attribute certificate issuer public key cannot be used to validate digital signatures.");
        }
        if (x509Certificate.getBasicConstraints() != -1) {
            throw new CertPathValidatorException("Attribute certificate issuer is also a public key certificate issuer.");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected static void processAttrCert4(X509Certificate x509Certificate, Set set) {
        Iterator iterator = set.iterator();
        boolean bl = false;
        while (iterator.hasNext()) {
            TrustAnchor trustAnchor = (TrustAnchor)iterator.next();
            boolean bl2 = x509Certificate.getSubjectX500Principal().getName("RFC2253").equals((Object)trustAnchor.getCAName()) || x509Certificate.equals((Object)trustAnchor.getTrustedCert()) ? true : bl;
            bl = bl2;
        }
        if (!bl) {
            throw new CertPathValidatorException("Attribute certificate issuer is not directly trusted.");
        }
    }

    protected static void processAttrCert5(X509AttributeCertificate x509AttributeCertificate, PKIXExtendedParameters pKIXExtendedParameters) {
        try {
            x509AttributeCertificate.checkValidity(CertPathValidatorUtilities.getValidDate(pKIXExtendedParameters));
            return;
        }
        catch (CertificateExpiredException certificateExpiredException) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", certificateExpiredException);
        }
        catch (CertificateNotYetValidException certificateNotYetValidException) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", certificateNotYetValidException);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected static void processAttrCert7(X509AttributeCertificate x509AttributeCertificate, CertPath certPath, CertPath certPath2, PKIXExtendedParameters pKIXExtendedParameters, Set set) {
        Set set2 = x509AttributeCertificate.getCriticalExtensionOIDs();
        if (set2.contains((Object)TARGET_INFORMATION)) {
            TargetInformation.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, TARGET_INFORMATION));
        }
        set2.remove((Object)TARGET_INFORMATION);
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) {
                if (set2.isEmpty()) return;
                throw new CertPathValidatorException("Attribute certificate contains unsupported critical extensions: " + (Object)set2);
            }
            ((PKIXAttrCertChecker)iterator.next()).check(x509AttributeCertificate, certPath, certPath2, (Collection)set2);
        } while (true);
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathValidatorException("Target information extension could not be read.", (Throwable)annotatedException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ExtCertPathValidatorException("Target information extension could not be read.", illegalArgumentException);
        }
    }
}

