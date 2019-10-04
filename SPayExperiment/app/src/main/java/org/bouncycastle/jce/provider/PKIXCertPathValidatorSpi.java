/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.PublicKey
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertPathValidatorException
 *  java.security.cert.CertPathValidatorResult
 *  java.security.cert.CertPathValidatorSpi
 *  java.security.cert.Certificate
 *  java.security.cert.PKIXCertPathChecker
 *  java.security.cert.PKIXCertPathValidatorResult
 *  java.security.cert.PKIXParameters
 *  java.security.cert.PolicyNode
 *  java.security.cert.TrustAnchor
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidator;
import org.bouncycastle.jce.provider.PKIXPolicyNode;
import org.bouncycastle.jce.provider.PrincipalUtils;
import org.bouncycastle.jce.provider.RFC3280CertPathUtilities;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PKIXCertPathValidatorSpi
extends CertPathValidatorSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public CertPathValidatorResult engineValidate(CertPath var1_1, CertPathParameters var2_2) {
        block26 : {
            block25 : {
                if (!(var2_2 instanceof CertPathParameters)) {
                    throw new InvalidAlgorithmParameterException("Parameters must be a " + PKIXParameters.class.getName() + " instance.");
                }
                if (var2_2 instanceof PKIXParameters) {
                    var3_3 = new PKIXExtendedParameters.Builder((PKIXParameters)var2_2);
                    if (var2_2 instanceof ExtendedPKIXParameters) {
                        var92_4 = (ExtendedPKIXParameters)var2_2;
                        var3_3.setUseDeltasEnabled(var92_4.isUseDeltasEnabled());
                        var3_3.setValidityModel(var92_4.getValidityModel());
                    }
                    var4_5 = var3_3.build();
                } else {
                    var4_5 = var2_2 instanceof PKIXExtendedBuilderParameters != false ? ((PKIXExtendedBuilderParameters)var2_2).getBaseParameters() : (PKIXExtendedParameters)var2_2;
                }
                if (var4_5.getTrustAnchors() == null) {
                    throw new InvalidAlgorithmParameterException("trustAnchors is null, this is not allowed for certification path validation.");
                }
                var5_6 = var1_1.getCertificates();
                var6_7 = var5_6.size();
                if (var5_6.isEmpty()) {
                    throw new CertPathValidatorException("Certification path is empty.", null, var1_1, 0);
                }
                var7_8 = var4_5.getInitialPolicies();
                try {
                    var9_9 = CertPathValidatorUtilities.findTrustAnchor((X509Certificate)var5_6.get(-1 + var5_6.size()), var4_5.getTrustAnchors(), var4_5.getSigProvider());
                    ** if (var9_9 != null) goto lbl-1000
                }
                catch (AnnotatedException var8_10) {
                    throw new CertPathValidatorException(var8_10.getMessage(), (Throwable)var8_10, var1_1, -1 + var5_6.size());
                }
lbl-1000: // 1 sources:
                {
                    throw new CertPathValidatorException("Trust anchor for certification path not found.", null, var1_1, -1);
                }
lbl-1000: // 1 sources:
                {
                }
                var10_11 = new PKIXExtendedParameters.Builder(var4_5).setTrustAnchor(var9_9).build();
                var11_12 = new ArrayList[var6_7 + 1];
                for (var12_13 = 0; var12_13 < var11_12.length; ++var12_13) {
                    var11_12[var12_13] = new ArrayList();
                }
                var13_14 = new HashSet();
                var13_14.add((Object)"2.5.29.32.0");
                var15_15 = new PKIXPolicyNode((List)new ArrayList(), 0, (Set)var13_14, null, (Set)new HashSet(), "2.5.29.32.0", false);
                var11_12[0].add((Object)var15_15);
                var17_16 = new PKIXNameConstraintValidator();
                var18_17 = new HashSet();
                var19_18 = var10_11.isExplicitPolicyRequired() != false ? 0 : var6_7 + 1;
                var20_19 = var10_11.isAnyPolicyInhibited() != false ? 0 : var6_7 + 1;
                var21_20 = var10_11.isPolicyMappingInhibited() != false ? 0 : var6_7 + 1;
                var22_21 = var9_9.getTrustedCert();
                if (var22_21 == null) ** GOTO lbl44
                break block25;
lbl44: // 1 sources:
                var24_26 = PrincipalUtils.getCA(var9_9);
                var26_24 = var25_27 = var9_9.getCAPublicKey();
                var27_25 = var24_26;
            }
            var90_22 = PrincipalUtils.getSubjectPrincipal(var22_21);
            var26_24 = var91_23 = var22_21.getPublicKey();
            var27_25 = var90_22;
            try {
                var29_28 = CertPathValidatorUtilities.getAlgorithmIdentifier(var26_24);
            }
            catch (CertPathValidatorException var28_30) {
                throw new ExtCertPathValidatorException("Algorithm identifier of public key of trust anchor could not be read.", var28_30, var1_1, -1);
            }
            var29_28.getAlgorithm();
            var29_28.getParameters();
            if (var10_11.getTargetConstraints() != null && !var10_11.getTargetConstraints().match((Certificate)((X509Certificate)var5_6.get(0)))) {
                throw new ExtCertPathValidatorException("Target certificate in certification path does not match targetConstraints.", null, var1_1, 0);
            }
            break block26;
            catch (IllegalArgumentException var23_29) {
                throw new ExtCertPathValidatorException("Subject of trust anchor could not be (re)encoded.", var23_29, var1_1, -1);
            }
        }
        var32_31 = var10_11.getCertPathCheckers();
        var33_32 = var32_31.iterator();
        while (var33_32.hasNext()) {
            ((PKIXCertPathChecker)var33_32.next()).init(false);
        }
        var34_33 = null;
        var35_34 = -1 + var5_6.size();
        var36_35 = var6_7;
        var37_36 = var21_20;
        var38_37 = var20_19;
        var39_38 = var19_18;
        var40_39 = var15_15;
        var41_40 = var26_24;
        var42_41 = var35_34;
        var43_42 = var22_21;
        do {
            block29 : {
                block28 : {
                    block27 : {
                        if (var42_41 < 0) break block27;
                        var60_43 = var6_7 - var42_41;
                        var34_33 = (X509Certificate)var5_6.get(var42_41);
                        var61_44 = var42_41 == -1 + var5_6.size();
                        RFC3280CertPathUtilities.processCertA(var1_1, var10_11, var42_41, var41_40, var61_44, var27_25, var43_42, this.helper);
                        RFC3280CertPathUtilities.processCertBC(var1_1, var42_41, var17_16);
                        var62_45 = RFC3280CertPathUtilities.processCertE(var1_1, var42_41, RFC3280CertPathUtilities.processCertD(var1_1, var42_41, (Set)var18_17, var40_39, (List[])var11_12, var38_37));
                        RFC3280CertPathUtilities.processCertF(var1_1, var42_41, var62_45, var39_38);
                        if (var60_43 == var6_7) break block28;
                        if (var34_33 != null && var34_33.getVersion() == 1) {
                            throw new CertPathValidatorException("Version 1 certificates can't be used as CA ones.", null, var1_1, var42_41);
                        }
                        RFC3280CertPathUtilities.prepareNextCertA(var1_1, var42_41);
                        var65_48 = RFC3280CertPathUtilities.prepareCertB(var1_1, var42_41, (List[])var11_12, var62_45, var37_36);
                        RFC3280CertPathUtilities.prepareNextCertG(var1_1, var42_41, var17_16);
                        var67_50 = RFC3280CertPathUtilities.prepareNextCertH1(var1_1, var42_41, var39_38);
                        var68_51 = RFC3280CertPathUtilities.prepareNextCertH2(var1_1, var42_41, var37_36);
                        var69_52 = RFC3280CertPathUtilities.prepareNextCertH3(var1_1, var42_41, var38_37);
                        var70_53 = RFC3280CertPathUtilities.prepareNextCertI1(var1_1, var42_41, var67_50);
                        var71_54 = RFC3280CertPathUtilities.prepareNextCertI2(var1_1, var42_41, var68_51);
                        var38_37 = RFC3280CertPathUtilities.prepareNextCertJ(var1_1, var42_41, var69_52);
                        RFC3280CertPathUtilities.prepareNextCertK(var1_1, var42_41);
                        var72_55 = RFC3280CertPathUtilities.prepareNextCertM(var1_1, var42_41, RFC3280CertPathUtilities.prepareNextCertL(var1_1, var42_41, var36_35));
                        RFC3280CertPathUtilities.prepareNextCertN(var1_1, var42_41);
                        var73_56 = var34_33.getCriticalExtensionOIDs();
                        if (var73_56 != null) {
                            var74_57 = new HashSet((Collection)var73_56);
                            var74_57.remove((Object)RFC3280CertPathUtilities.KEY_USAGE);
                            var74_57.remove((Object)RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                            var74_57.remove((Object)RFC3280CertPathUtilities.POLICY_MAPPINGS);
                            var74_57.remove((Object)RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                            var74_57.remove((Object)RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                            var74_57.remove((Object)RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                            var74_57.remove((Object)RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                            var74_57.remove((Object)RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                            var74_57.remove((Object)RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                            var74_57.remove((Object)RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                        } else {
                            var74_57 = new HashSet();
                        }
                        RFC3280CertPathUtilities.prepareNextCertO(var1_1, var42_41, (Set)var74_57, var32_31);
                        var27_25 = PrincipalUtils.getSubjectPrincipal(var34_33);
                        try {
                            var41_40 = var86_58 = CertPathValidatorUtilities.getNextWorkingKey(var1_1.getCertificates(), var42_41, this.helper);
                        }
                        catch (CertPathValidatorException var85_60) {
                            throw new CertPathValidatorException("Next working key could not be retrieved.", (Throwable)var85_60, var1_1, var42_41);
                        }
                        var87_59 = CertPathValidatorUtilities.getAlgorithmIdentifier(var41_40);
                        var87_59.getAlgorithm();
                        var87_59.getParameters();
                        var66_49 = var72_55;
                        var63_46 = var71_54;
                        var64_47 = var70_53;
                        var43_42 = var34_33;
                        break block29;
                    }
                    var44_61 = RFC3280CertPathUtilities.wrapupCertA(var39_38, var34_33);
                    var45_62 = RFC3280CertPathUtilities.wrapupCertB(var1_1, var42_41 + 1, var44_61);
                    var46_63 = var34_33.getCriticalExtensionOIDs();
                    if (var46_63 != null) {
                        var47_64 = new HashSet((Collection)var46_63);
                        var47_64.remove((Object)RFC3280CertPathUtilities.KEY_USAGE);
                        var47_64.remove((Object)RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                        var47_64.remove((Object)RFC3280CertPathUtilities.POLICY_MAPPINGS);
                        var47_64.remove((Object)RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                        var47_64.remove((Object)RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                        var47_64.remove((Object)RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                        var47_64.remove((Object)RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                        var47_64.remove((Object)RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                        var47_64.remove((Object)RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                        var47_64.remove((Object)RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                        var47_64.remove((Object)RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS);
                    } else {
                        var47_64 = new HashSet();
                    }
                    RFC3280CertPathUtilities.wrapupCertF(var1_1, var42_41 + 1, var32_31, (Set)var47_64);
                    var59_65 = RFC3280CertPathUtilities.wrapupCertG(var1_1, var10_11, var7_8, var42_41 + 1, (List[])var11_12, var40_39, (Set)var18_17);
                    if (var45_62 > 0) return new PKIXCertPathValidatorResult(var9_9, (PolicyNode)var59_65, var34_33.getPublicKey());
                    if (var59_65 == null) throw new CertPathValidatorException("Path processing failed on policy.", null, var1_1, var42_41);
                    return new PKIXCertPathValidatorResult(var9_9, (PolicyNode)var59_65, var34_33.getPublicKey());
                }
                var63_46 = var37_36;
                var64_47 = var39_38;
                var65_48 = var62_45;
                var66_49 = var36_35;
            }
            --var42_41;
            var36_35 = var66_49;
            var37_36 = var63_46;
            var39_38 = var64_47;
            var40_39 = var65_48;
        } while (true);
    }
}

