/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.IndexOutOfBoundsException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.net.HttpURLConnection
 *  java.net.InetAddress
 *  java.net.URL
 *  java.net.URLConnection
 *  java.security.GeneralSecurityException
 *  java.security.PublicKey
 *  java.security.SignatureException
 *  java.security.cert.CRL
 *  java.security.cert.CertPath
 *  java.security.cert.CertPathValidatorException
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateFactory
 *  java.security.cert.CertificateNotYetValidException
 *  java.security.cert.PKIXCertPathChecker
 *  java.security.cert.PKIXParameters
 *  java.security.cert.PolicyNode
 *  java.security.cert.TrustAnchor
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLEntry
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.security.cert.X509Extension
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Date
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 *  java.util.Vector
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.qualified.Iso4217CurrencyCode;
import org.bouncycastle.asn1.x509.qualified.MonetaryValue;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocaleString;
import org.bouncycastle.i18n.filter.TrustedInput;
import org.bouncycastle.i18n.filter.UntrustedInput;
import org.bouncycastle.i18n.filter.UntrustedUrlInput;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.util.Integers;
import org.bouncycastle.x509.CertPathReviewerException;
import org.bouncycastle.x509.CertPathValidatorUtilities;
import org.bouncycastle.x509.PKIXCRLUtil;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class PKIXCertPathReviewer
extends CertPathValidatorUtilities {
    private static final String AUTH_INFO_ACCESS;
    private static final String CRL_DIST_POINTS;
    private static final String QC_STATEMENT;
    private static final String RESOURCE_NAME = "org.bouncycastle.x509.CertPathReviewerMessages";
    protected CertPath certPath;
    protected List certs;
    protected List[] errors;
    private boolean initialized;
    protected int n;
    protected List[] notifications;
    protected PKIXParameters pkixParams;
    protected PolicyNode policyTree;
    protected PublicKey subjectPublicKey;
    protected TrustAnchor trustAnchor;
    protected Date validDate;

    static {
        QC_STATEMENT = X509Extensions.QCStatements.getId();
        CRL_DIST_POINTS = X509Extensions.CRLDistributionPoints.getId();
        AUTH_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
    }

    public PKIXCertPathReviewer() {
    }

    public PKIXCertPathReviewer(CertPath certPath, PKIXParameters pKIXParameters) {
        this.init(certPath, pKIXParameters);
    }

    private String IPtoString(byte[] arrby) {
        try {
            String string = InetAddress.getByAddress((byte[])arrby).getHostAddress();
            return string;
        }
        catch (Exception exception) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i != arrby.length; ++i) {
                stringBuffer.append(Integer.toHexString((int)(255 & arrby[i])));
                stringBuffer.append(' ');
            }
            return stringBuffer.toString();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void checkCriticalExtensions() {
        int n;
        List list = this.pkixParams.getCertPathCheckers();
        Iterator iterator = list.iterator();
        try {
            try {
                while (iterator.hasNext()) {
                    ((PKIXCertPathChecker)iterator.next()).init(false);
                }
            }
            catch (CertPathValidatorException certPathValidatorException) {
                Object[] arrobject = new Object[]{certPathValidatorException.getMessage(), certPathValidatorException, certPathValidatorException.getClass().getName()};
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathCheckerError", arrobject), certPathValidatorException);
            }
            n = -1 + this.certs.size();
        }
        catch (CertPathReviewerException certPathReviewerException) {
            this.addError(certPathReviewerException.getErrorMessage(), certPathReviewerException.getIndex());
            return;
        }
        while (n >= 0) {
            X509Certificate x509Certificate = (X509Certificate)this.certs.get(n);
            Set set = x509Certificate.getCriticalExtensionOIDs();
            if (set != null && !set.isEmpty()) {
                boolean bl;
                set.remove((Object)KEY_USAGE);
                set.remove((Object)CERTIFICATE_POLICIES);
                set.remove((Object)POLICY_MAPPINGS);
                set.remove((Object)INHIBIT_ANY_POLICY);
                set.remove((Object)ISSUING_DISTRIBUTION_POINT);
                set.remove((Object)DELTA_CRL_INDICATOR);
                set.remove((Object)POLICY_CONSTRAINTS);
                set.remove((Object)BASIC_CONSTRAINTS);
                set.remove((Object)SUBJECT_ALTERNATIVE_NAME);
                set.remove((Object)NAME_CONSTRAINTS);
                if (set.contains((Object)QC_STATEMENT) && this.processQcStatements(x509Certificate, n)) {
                    set.remove((Object)QC_STATEMENT);
                }
                Iterator iterator2 = list.iterator();
                while (bl = iterator2.hasNext()) {
                    try {
                        ((PKIXCertPathChecker)iterator2.next()).check((Certificate)x509Certificate, (Collection)set);
                    }
                    catch (CertPathValidatorException certPathValidatorException) {
                        Object[] arrobject = new Object[]{certPathValidatorException.getMessage(), certPathValidatorException, certPathValidatorException.getClass().getName()};
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.criticalExtensionError", arrobject), certPathValidatorException.getCause(), this.certPath, n);
                    }
                }
                if (!set.isEmpty()) {
                    Iterator iterator3 = set.iterator();
                    while (iterator3.hasNext()) {
                        Object[] arrobject = new Object[]{new ASN1ObjectIdentifier((String)iterator3.next())};
                        this.addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknownCriticalExt", arrobject), n);
                    }
                }
            }
            --n;
        }
    }

    /*
     * Exception decompiling
     */
    private void checkNameConstraints() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void checkPathLength() {
        int n = this.n;
        int n2 = -1 + this.certs.size();
        int n3 = n;
        int n4 = 0;
        do {
            BigInteger bigInteger;
            int n5;
            BasicConstraints basicConstraints;
            int n6;
            int n7;
            if (n2 <= 0) {
                Object[] arrobject = new Object[]{Integers.valueOf(n4)};
                this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.totalPathLength", arrobject));
                return;
            }
            this.n - n2;
            X509Certificate x509Certificate = (X509Certificate)this.certs.get(n2);
            if (!PKIXCertPathReviewer.isSelfIssued(x509Certificate)) {
                if (n3 <= 0) {
                    this.addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pathLenghtExtended"));
                }
                int n8 = n3 - 1;
                n5 = n4 + 1;
                n7 = n8;
            } else {
                int n9 = n4;
                n7 = n3;
                n5 = n9;
            }
            try {
                BasicConstraints basicConstraints2;
                basicConstraints = basicConstraints2 = BasicConstraints.getInstance(PKIXCertPathReviewer.getExtensionValue((X509Extension)x509Certificate, BASIC_CONSTRAINTS));
            }
            catch (AnnotatedException annotatedException) {
                this.addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.processLengthConstError"), n2);
                basicConstraints = null;
            }
            if (basicConstraints == null || (bigInteger = basicConstraints.getPathLenConstraint()) == null || (n6 = bigInteger.intValue()) >= n7) {
                n6 = n7;
            }
            --n2;
            n4 = n5;
            n3 = n6;
        } while (true);
    }

    /*
     * Exception decompiling
     */
    private void checkPolicy() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 3[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void checkSignatures() {
        block70 : {
            block68 : {
                block63 : {
                    block62 : {
                        var1_1 = null;
                        var2_2 = new Object[]{new TrustedInput((Object)this.validDate), new TrustedInput((Object)new Date())};
                        this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certPathValidDate", var2_2));
                        var70_3 = (X509Certificate)this.certs.get(-1 + this.certs.size());
                        var71_4 = this.getTrustAnchors(var70_3, this.pkixParams.getTrustAnchors());
                        if (var71_4.size() > 1) {
                            var79_5 = new Object[]{Integers.valueOf(var71_4.size()), new UntrustedInput((Object)var70_3.getIssuerX500Principal())};
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.conflictingTrustAnchors", var79_5));
                            var72_6 = null;
                            break block62;
                        }
                        if (var71_4.isEmpty()) {
                            var78_12 = new Object[]{new UntrustedInput((Object)var70_3.getIssuerX500Principal()), Integers.valueOf(this.pkixParams.getTrustAnchors().size())};
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noTrustAnchorFound", var78_12));
                            var72_6 = null;
                            break block62;
                        }
                        var72_6 = (TrustAnchor)var71_4.iterator().next();
                        try {
                            var74_8 = var72_6.getTrustedCert() != null ? (var77_11 = var72_6.getTrustedCert().getPublicKey()) : (var73_7 = var72_6.getCAPublicKey());
                            try {
                                CertPathValidatorUtilities.verifyX509Certificate(var70_3, var74_8, this.pkixParams.getSigProvider());
                            }
                            catch (SignatureException var76_10) {
                                this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustButInvalidCert"));
                            }
                        }
                        catch (CertPathReviewerException var3_15) {
                            block67 : {
                                var4_17 = var72_6;
                                break block67;
                                catch (Throwable var68_19) {}
                                ** GOTO lbl-1000
                                catch (Throwable var68_20) {
                                    var1_1 = var72_6;
                                }
lbl-1000: // 2 sources:
                                {
                                    var69_21 = new Object[]{new UntrustedInput(var68_18.getMessage()), new UntrustedInput(var68_18)};
                                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.unknown", var69_21));
                                    var5_13 = var1_1;
                                    break block63;
                                }
                                catch (CertPathReviewerException var3_16) {
                                    var4_17 = null;
                                }
                            }
                            this.addError(var3_14.getErrorMessage());
                            var5_13 = var4_17;
                            break block63;
                        }
                        catch (Exception var75_9) {}
                    }
                    var5_13 = var72_6;
                }
                if (var5_13 == null) break block68;
                var62_22 = var5_13.getTrustedCert();
                if (var62_22 == null) ** GOTO lbl53
                try {
                    block69 : {
                        var63_24 = var67_23 = PKIXCertPathReviewer.getSubjectPrincipal(var62_22);
                        break block69;
lbl53: // 1 sources:
                        var63_24 = new X500Principal(var5_13.getCAName());
                    }
                    var6_25 = var63_24;
                }
                catch (IllegalArgumentException var65_26) {
                    var66_27 = new Object[]{new UntrustedInput(var5_13.getCAName())};
                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustDNInvalid", var66_27));
                    var6_25 = null;
                }
                if (var62_22 != null && (var64_28 = var62_22.getKeyUsage()) != null && !var64_28[5]) {
                    this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustKeyUsage"));
                }
                break block70;
            }
            var6_25 = null;
        }
        var7_29 = null;
        var8_30 = null;
        if (var5_13 != null) {
            var7_29 = var5_13.getTrustedCert();
            var8_30 = var7_29 != null ? var7_29.getPublicKey() : var5_13.getCAPublicKey();
            try {
                var59_31 = PKIXCertPathReviewer.getAlgorithmIdentifier(var8_30);
                var59_31.getObjectId();
                var59_31.getParameters();
            }
            catch (CertPathValidatorException var58_32) {
                this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustPubKeyError"));
            }
        }
        var9_33 = -1 + this.certs.size();
        var10_34 = var7_29;
        var11_35 = var6_25;
        var12_36 = var8_30;
        do {
            block66 : {
                if (var9_33 < 0) {
                    this.trustAnchor = var5_13;
                    this.subjectPublicKey = var12_36;
                    return;
                }
                var13_37 = this.n - var9_33;
                var14_38 = (X509Certificate)this.certs.get(var9_33);
                if (var12_36 != null) {
                    try {
                        CertPathValidatorUtilities.verifyX509Certificate(var14_38, var12_36, this.pkixParams.getSigProvider());
                    }
                    catch (GeneralSecurityException var56_78) {
                        var57_79 = new Object[]{var56_78.getMessage(), var56_78, var56_78.getClass().getName()};
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.signatureNotVerified", var57_79), var9_33);
                    }
                } else if (PKIXCertPathReviewer.isSelfIssued(var14_38)) {
                    try {
                        CertPathValidatorUtilities.verifyX509Certificate(var14_38, var14_38.getPublicKey(), this.pkixParams.getSigProvider());
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"), var9_33);
                    }
                    catch (GeneralSecurityException var54_76) {
                        var55_77 = new Object[]{var54_76.getMessage(), var54_76, var54_76.getClass().getName()};
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.signatureNotVerified", var55_77), var9_33);
                    }
                } else {
                    var15_39 = new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.NoIssuerPublicKey");
                    var16_40 = var14_38.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
                    if (var16_40 != null) {
                        try {
                            var49_71 = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(var16_40));
                            var50_72 = var49_71.getAuthorityCertIssuer();
                            if (var50_72 != null) {
                                var51_73 = var50_72.getNames()[0];
                                var52_74 = var49_71.getAuthorityCertSerialNumber();
                                if (var52_74 != null) {
                                    var53_75 = new Object[]{new LocaleString("org.bouncycastle.x509.CertPathReviewerMessages", "missingIssuer"), " \"", var51_73, "\" ", new LocaleString("org.bouncycastle.x509.CertPathReviewerMessages", "missingSerial"), " ", var52_74};
                                    var15_39.setExtraArguments(var53_75);
                                }
                            }
                        }
                        catch (IOException var48_70) {}
                    }
                    this.addError(var15_39, var9_33);
                }
                try {
                    var14_38.checkValidity(this.validDate);
                }
                catch (CertificateNotYetValidException var46_68) {
                    var47_69 = new Object[]{new TrustedInput((Object)var14_38.getNotBefore())};
                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certificateNotYetValid", var47_69), var9_33);
                }
                catch (CertificateExpiredException var17_41) {
                    var18_42 = new Object[]{new TrustedInput((Object)var14_38.getNotAfter())};
                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certificateExpired", var18_42), var9_33);
                }
                if (this.pkixParams.isRevocationEnabled()) {
                    try {
                        var44_66 = PKIXCertPathReviewer.getExtensionValue((X509Extension)var14_38, PKIXCertPathReviewer.CRL_DIST_POINTS);
                        var32_54 = null;
                        if (var44_66 != null) {
                            var32_54 = var45_67 = CRLDistPoint.getInstance(var44_66);
                        }
                    }
                    catch (AnnotatedException var31_53) {
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlDistPtExtError"), var9_33);
                        var32_54 = null;
                    }
                    try {
                        var42_64 = PKIXCertPathReviewer.getExtensionValue((X509Extension)var14_38, PKIXCertPathReviewer.AUTH_INFO_ACCESS);
                        var34_56 = null;
                        if (var42_64 != null) {
                            var34_56 = var43_65 = AuthorityInformationAccess.getInstance(var42_64);
                        }
                    }
                    catch (AnnotatedException var33_55) {
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlAuthInfoAccError"), var9_33);
                        var34_56 = null;
                    }
                    var35_57 = this.getCRLDistUrls(var32_54);
                    var36_58 = this.getOCSPUrls(var34_56);
                    var37_59 = var35_57.iterator();
                    while (var37_59.hasNext()) {
                        var41_63 = new Object[]{new UntrustedUrlInput(var37_59.next())};
                        this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlDistPoint", var41_63), var9_33);
                    }
                    var38_60 = var36_58.iterator();
                    while (var38_60.hasNext()) {
                        var40_62 = new Object[]{new UntrustedUrlInput(var38_60.next())};
                        this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.ocspLocation", var40_62), var9_33);
                    }
                    try {
                        this.checkRevocation(this.pkixParams, var14_38, this.validDate, var10_34, var12_36, var35_57, var36_58, var9_33);
                    }
                    catch (CertPathReviewerException var39_61) {
                        this.addError(var39_61.getErrorMessage(), var9_33);
                    }
                }
                if (var11_35 != null && !var14_38.getIssuerX500Principal().equals((Object)var11_35)) {
                    var30_52 = new Object[]{var11_35.getName(), var14_38.getIssuerX500Principal().getName()};
                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certWrongIssuer", var30_52), var9_33);
                }
                if (var13_37 != this.n) {
                    block65 : {
                        if (var14_38 != null && var14_38.getVersion() == 1) {
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCACert"), var9_33);
                        }
                        try {
                            var29_51 = BasicConstraints.getInstance(PKIXCertPathReviewer.getExtensionValue((X509Extension)var14_38, PKIXCertPathReviewer.BASIC_CONSTRAINTS));
                            if (var29_51 != null) {
                                if (!var29_51.isCA()) {
                                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCACert"), var9_33);
                                }
                                break block65;
                            }
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noBasicConstraints"), var9_33);
                        }
                        catch (AnnotatedException var27_49) {
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.errorProcesingBC"), var9_33);
                        }
                    }
                    if ((var28_50 = var14_38.getKeyUsage()) != null && !var28_50[5]) {
                        this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCertSign"), var9_33);
                    }
                }
                var19_43 = var14_38.getSubjectX500Principal();
                var21_45 = var22_46 = PKIXCertPathReviewer.getNextWorkingKey(this.certs, var9_33);
                var24_48 = PKIXCertPathReviewer.getAlgorithmIdentifier(var21_45);
                var24_48.getObjectId();
                var24_48.getParameters();
                break block66;
                {
                    catch (CertPathValidatorException var23_47) {}
                }
                catch (CertPathValidatorException var20_44) {
                    var21_45 = var12_36;
                    this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.pubKeyError"), var9_33);
                }
            }
            --var9_33;
            var10_34 = var14_38;
            var11_35 = var19_43;
            var12_36 = var21_45;
        } while (true);
    }

    private X509CRL getCRL(String string) {
        try {
            URL uRL = new URL(string);
            if (uRL.getProtocol().equals((Object)"http") || uRL.getProtocol().equals((Object)"https")) {
                HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {
                    return (X509CRL)CertificateFactory.getInstance((String)"X.509", (String)"BC").generateCRL(httpURLConnection.getInputStream());
                }
                throw new Exception(httpURLConnection.getResponseMessage());
            }
        }
        catch (Exception exception) {
            Object[] arrobject = new Object[]{new UntrustedInput(string), exception.getMessage(), exception, exception.getClass().getName()};
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.loadCrlDistPointError", arrobject));
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean processQcStatements(X509Certificate var1_1, int var2_2) {
        block7 : {
            block6 : {
                block8 : {
                    block10 : {
                        try {
                            var4_3 = (ASN1Sequence)PKIXCertPathReviewer.getExtensionValue((X509Extension)var1_1, PKIXCertPathReviewer.QC_STATEMENT);
                            var5_4 = 0;
                            var6_5 = false;
lbl5: // 2 sources:
                            do {
                                if (var5_4 >= var4_3.size()) break block6;
                                var7_6 = QCStatement.getInstance(var4_3.getObjectAt(var5_4));
                                if (QCStatement.id_etsi_qcs_QcCompliance.equals(var7_6.getStatementId())) {
                                    this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcEuCompliance"), var2_2);
                                    break block7;
                                }
                                if (QCStatement.id_qcs_pkixQCSyntax_v1.equals(var7_6.getStatementId())) break block7;
                                if (QCStatement.id_etsi_qcs_QcSSCD.equals(var7_6.getStatementId())) {
                                    this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcSSCD"), var2_2);
                                    break block7;
                                }
                                if (!QCStatement.id_etsi_qcs_LimiteValue.equals(var7_6.getStatementId())) break block8;
                                var9_8 = MonetaryValue.getInstance(var7_6.getStatementInfo());
                                var9_8.getCurrency();
                                var11_9 = var9_8.getAmount().doubleValue() * Math.pow((double)10.0, (double)var9_8.getExponent().doubleValue());
                                if (!var9_8.getCurrency().isAlphabetic()) break;
                                var15_12 = new Object[]{var9_8.getCurrency().getAlphabetic(), new TrustedInput((Object)new Double(var11_9)), var9_8};
                                var14_11 = new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueAlpha", var15_12);
                                break block10;
                                break;
                            } while (true);
                        }
                        catch (AnnotatedException var3_13) {
                            this.addError(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcStatementExtError"), var2_2);
                            return false;
                        }
                        var13_10 = new Object[]{Integers.valueOf(var9_8.getCurrency().getNumeric()), new TrustedInput((Object)new Double(var11_9)), var9_8};
                        var14_11 = new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueNum", var13_10);
                    }
                    this.addNotification(var14_11, var2_2);
                    break block7;
                }
                var8_7 = new Object[]{var7_6.getStatementId(), new UntrustedInput(var7_6)};
                this.addNotification(new ErrorBundle("org.bouncycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcUnknownStatement", var8_7), var2_2);
                var6_5 = true;
                break block7;
            }
            if (var6_5 != false) return false;
            return true;
        }
        ++var5_4;
        ** while (true)
    }

    protected void addError(ErrorBundle errorBundle) {
        this.errors[0].add((Object)errorBundle);
    }

    protected void addError(ErrorBundle errorBundle, int n) {
        if (n < -1 || n >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.errors[n + 1].add((Object)errorBundle);
    }

    protected void addNotification(ErrorBundle errorBundle) {
        this.notifications[0].add((Object)errorBundle);
    }

    protected void addNotification(ErrorBundle errorBundle, int n) {
        if (n < -1 || n >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.notifications[n + 1].add((Object)errorBundle);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected void checkCRLs(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, int n) {
        boolean bl;
        block55 : {
            BasicConstraints basicConstraints;
            ASN1Primitive aSN1Primitive;
            block52 : {
                boolean bl2;
                block54 : {
                    ASN1Primitive aSN1Primitive2;
                    X509CRL x509CRL;
                    Iterator iterator;
                    block58 : {
                        block56 : {
                            X509CRLEntry x509CRLEntry;
                            LocaleString localeString;
                            block57 : {
                                String string;
                                block51 : {
                                    boolean[] arrbl;
                                    block50 : {
                                        X509CRL x509CRL2;
                                        boolean bl3;
                                        block49 : {
                                            Iterator iterator2;
                                            block47 : {
                                                X509CRLStoreSelector x509CRLStoreSelector;
                                                ArrayList arrayList;
                                                block48 : {
                                                    x509CRLStoreSelector = new X509CRLStoreSelector();
                                                    x509CRLStoreSelector.addIssuerName(PKIXCertPathReviewer.getEncodedIssuerPrincipal((Object)x509Certificate).getEncoded());
                                                    x509CRLStoreSelector.setCertificateChecking(x509Certificate);
                                                    Set set = CRL_UTIL.findCRLs(x509CRLStoreSelector, pKIXParameters);
                                                    iterator2 = set.iterator();
                                                    if (!set.isEmpty()) break block47;
                                                    Iterator iterator3 = CRL_UTIL.findCRLs(new X509CRLStoreSelector(), pKIXParameters).iterator();
                                                    arrayList = new ArrayList();
                                                    while (iterator3.hasNext()) {
                                                        arrayList.add((Object)((X509CRL)iterator3.next()).getIssuerX500Principal());
                                                    }
                                                    break block48;
                                                    catch (IOException iOException) {
                                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), iOException);
                                                    }
                                                }
                                                try {
                                                    int n2 = arrayList.size();
                                                    Object[] arrobject = new Object[]{new UntrustedInput((Object)x509CRLStoreSelector.getIssuerNames()), new UntrustedInput((Object)arrayList), Integers.valueOf(n2)};
                                                    this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlInCertstore", arrobject), n);
                                                }
                                                catch (AnnotatedException annotatedException) {
                                                    Object[] arrobject = new Object[]{annotatedException.getCause().getMessage(), annotatedException.getCause(), annotatedException.getCause().getClass().getName()};
                                                    this.addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError", arrobject), n);
                                                    iterator2 = new ArrayList().iterator();
                                                }
                                            }
                                            X509CRL x509CRL3 = null;
                                            while (iterator2.hasNext()) {
                                                x509CRL3 = (X509CRL)iterator2.next();
                                                if (x509CRL3.getNextUpdate() == null || pKIXParameters.getDate().before(x509CRL3.getNextUpdate())) {
                                                    Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRL3.getThisUpdate()), new TrustedInput((Object)x509CRL3.getNextUpdate())};
                                                    this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localValidCRL", arrobject), n);
                                                    x509CRL2 = x509CRL3;
                                                    bl3 = true;
                                                    break block49;
                                                }
                                                Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRL3.getThisUpdate()), new TrustedInput((Object)x509CRL3.getNextUpdate())};
                                                this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localInvalidCRL", arrobject), n);
                                            }
                                            x509CRL2 = x509CRL3;
                                            bl3 = false;
                                        }
                                        if (bl3) {
                                            x509CRL = x509CRL2;
                                            bl = bl3;
                                        } else {
                                            Iterator iterator4 = vector.iterator();
                                            boolean bl4 = bl3;
                                            while (iterator4.hasNext()) {
                                                try {
                                                    String string2 = (String)iterator4.next();
                                                    X509CRL x509CRL4 = this.getCRL(string2);
                                                    if (x509CRL4 == null) continue;
                                                    if (!x509Certificate.getIssuerX500Principal().equals((Object)x509CRL4.getIssuerX500Principal())) {
                                                        Object[] arrobject = new Object[]{new UntrustedInput(x509CRL4.getIssuerX500Principal().getName()), new UntrustedInput(x509Certificate.getIssuerX500Principal().getName()), new UntrustedUrlInput(string2)};
                                                        this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineCRLWrongCA", arrobject), n);
                                                    }
                                                    if (x509CRL4.getNextUpdate() == null || this.pkixParams.getDate().before(x509CRL4.getNextUpdate())) {
                                                        bl4 = true;
                                                        Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRL4.getThisUpdate()), new TrustedInput((Object)x509CRL4.getNextUpdate()), new UntrustedUrlInput(string2)};
                                                        this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineValidCRL", arrobject), n);
                                                        bl = bl4;
                                                        x509CRL = x509CRL4;
                                                        break block50;
                                                    }
                                                    Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRL4.getThisUpdate()), new TrustedInput((Object)x509CRL4.getNextUpdate()), new UntrustedUrlInput(string2)};
                                                    this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineInvalidCRL", arrobject), n);
                                                }
                                                catch (CertPathReviewerException certPathReviewerException) {
                                                    boolean bl5 = bl4;
                                                    this.addNotification(certPathReviewerException.getErrorMessage(), n);
                                                    bl4 = bl5;
                                                }
                                            }
                                            bl = bl4;
                                            x509CRL = x509CRL2;
                                        }
                                    }
                                    if (x509CRL == null) break block55;
                                    if (x509Certificate2 != null && (arrbl = x509Certificate2.getKeyUsage()) != null) {
                                        if (arrbl.length < 7) throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlSigningPermited"));
                                        if (!arrbl[6]) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlSigningPermited"));
                                        }
                                    }
                                    if (publicKey == null) throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNoIssuerPublicKey"));
                                    try {
                                        x509CRL.verify(publicKey, "BC");
                                    }
                                    catch (Exception exception) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlVerifyFailed"), exception);
                                    }
                                    x509CRLEntry = x509CRL.getRevokedCertificate(x509Certificate.getSerialNumber());
                                    if (x509CRLEntry == null) break block56;
                                    boolean bl6 = x509CRLEntry.hasExtensions();
                                    string = null;
                                    if (bl6) {
                                        ASN1Enumerated aSN1Enumerated = ASN1Enumerated.getInstance(PKIXCertPathReviewer.getExtensionValue((X509Extension)x509CRLEntry, X509Extensions.ReasonCode.getId()));
                                        string = null;
                                        if (aSN1Enumerated == null) break block51;
                                        string = crlReasons[aSN1Enumerated.getValue().intValue()];
                                    }
                                }
                                if (string == null) {
                                    string = crlReasons[7];
                                }
                                localeString = new LocaleString(RESOURCE_NAME, string);
                                if (!date.before(x509CRLEntry.getRevocationDate())) {
                                    Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRLEntry.getRevocationDate()), localeString};
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certRevoked", arrobject));
                                }
                                break block57;
                                catch (AnnotatedException annotatedException) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlReasonExtError"), (Throwable)((Object)annotatedException));
                                }
                            }
                            Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRLEntry.getRevocationDate()), localeString};
                            this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.revokedAfterValidation", arrobject), n);
                            break block58;
                        }
                        this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notRevoked"), n);
                    }
                    if (x509CRL.getNextUpdate() != null && x509CRL.getNextUpdate().before(this.pkixParams.getDate())) {
                        Object[] arrobject = new Object[]{new TrustedInput((Object)x509CRL.getNextUpdate())};
                        this.addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlUpdateAvailable", arrobject), n);
                    }
                    try {
                        aSN1Primitive = PKIXCertPathReviewer.getExtensionValue((X509Extension)x509CRL, ISSUING_DISTRIBUTION_POINT);
                    }
                    catch (AnnotatedException annotatedException) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"));
                    }
                    try {
                        aSN1Primitive2 = PKIXCertPathReviewer.getExtensionValue((X509Extension)x509CRL, DELTA_CRL_INDICATOR);
                        if (aSN1Primitive2 == null) break block52;
                    }
                    catch (AnnotatedException annotatedException) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.deltaCrlExtError"));
                    }
                    X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
                    try {
                        x509CRLStoreSelector.addIssuerName(PKIXCertPathReviewer.getIssuerPrincipal(x509CRL).getEncoded());
                    }
                    catch (IOException iOException) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), iOException);
                    }
                    x509CRLStoreSelector.setMinCRLNumber(((ASN1Integer)aSN1Primitive2).getPositiveValue());
                    try {
                        x509CRLStoreSelector.setMaxCRLNumber(((ASN1Integer)PKIXCertPathReviewer.getExtensionValue((X509Extension)x509CRL, CRL_NUMBER)).getPositiveValue().subtract(BigInteger.valueOf((long)1L)));
                    }
                    catch (AnnotatedException annotatedException) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNbrExtError"), (Throwable)((Object)annotatedException));
                    }
                    try {
                        iterator = CRL_UTIL.findCRLs(x509CRLStoreSelector, pKIXParameters).iterator();
                    }
                    catch (AnnotatedException annotatedException) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError"), (Throwable)((Object)annotatedException));
                    }
                    while (iterator.hasNext()) {
                        ASN1Primitive aSN1Primitive3;
                        block53 : {
                            X509CRL x509CRL5 = (X509CRL)iterator.next();
                            try {
                                aSN1Primitive3 = PKIXCertPathReviewer.getExtensionValue((X509Extension)x509CRL5, ISSUING_DISTRIBUTION_POINT);
                                if (aSN1Primitive != null) break block53;
                                if (aSN1Primitive3 != null) continue;
                                bl2 = true;
                                break block54;
                            }
                            catch (AnnotatedException annotatedException) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"), (Throwable)((Object)annotatedException));
                            }
                        }
                        if (!aSN1Primitive.equals(aSN1Primitive3)) continue;
                        bl2 = true;
                        break block54;
                    }
                    bl2 = false;
                }
                if (!bl2) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBaseCRL"));
                }
            }
            if (aSN1Primitive == null) break block55;
            IssuingDistributionPoint issuingDistributionPoint = IssuingDistributionPoint.getInstance(aSN1Primitive);
            try {
                basicConstraints = BasicConstraints.getInstance(PKIXCertPathReviewer.getExtensionValue((X509Extension)x509Certificate, BASIC_CONSTRAINTS));
            }
            catch (AnnotatedException annotatedException) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlBCExtError"), (Throwable)((Object)annotatedException));
            }
            if (issuingDistributionPoint.onlyContainsUserCerts() && basicConstraints != null && basicConstraints.isCA()) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyUserCert"));
            }
            if (issuingDistributionPoint.onlyContainsCACerts()) {
                if (basicConstraints == null) throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyCaCert"));
                if (!basicConstraints.isCA()) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyCaCert"));
                }
            }
            if (issuingDistributionPoint.onlyContainsAttributeCerts()) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyAttrCert"));
            }
        }
        if (bl) return;
        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidCrlFound"));
    }

    protected void checkRevocation(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, Vector vector2, int n) {
        this.checkCRLs(pKIXParameters, x509Certificate, date, x509Certificate2, publicKey, vector, n);
    }

    protected void doChecks() {
        if (!this.initialized) {
            throw new IllegalStateException("Object not initialized. Call init() first.");
        }
        if (this.notifications == null) {
            this.notifications = new List[1 + this.n];
            this.errors = new List[1 + this.n];
            for (int i = 0; i < this.notifications.length; ++i) {
                this.notifications[i] = new ArrayList();
                this.errors[i] = new ArrayList();
            }
            this.checkSignatures();
            this.checkNameConstraints();
            this.checkPathLength();
            this.checkPolicy();
            this.checkCriticalExtensions();
        }
    }

    protected Vector getCRLDistUrls(CRLDistPoint cRLDistPoint) {
        Vector vector = new Vector();
        if (cRLDistPoint != null) {
            DistributionPoint[] arrdistributionPoint = cRLDistPoint.getDistributionPoints();
            for (int i = 0; i < arrdistributionPoint.length; ++i) {
                DistributionPointName distributionPointName = arrdistributionPoint[i].getDistributionPoint();
                if (distributionPointName.getType() != 0) continue;
                GeneralName[] arrgeneralName = GeneralNames.getInstance(distributionPointName.getName()).getNames();
                for (int j = 0; j < arrgeneralName.length; ++j) {
                    if (arrgeneralName[j].getTagNo() != 6) continue;
                    vector.add((Object)((DERIA5String)arrgeneralName[j].getName()).getString());
                }
            }
        }
        return vector;
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getCertPathSize() {
        return this.n;
    }

    public List getErrors(int n) {
        this.doChecks();
        return this.errors[n + 1];
    }

    public List[] getErrors() {
        this.doChecks();
        return this.errors;
    }

    public List getNotifications(int n) {
        this.doChecks();
        return this.notifications[n + 1];
    }

    public List[] getNotifications() {
        this.doChecks();
        return this.notifications;
    }

    protected Vector getOCSPUrls(AuthorityInformationAccess authorityInformationAccess) {
        Vector vector = new Vector();
        if (authorityInformationAccess != null) {
            AccessDescription[] arraccessDescription = authorityInformationAccess.getAccessDescriptions();
            for (int i = 0; i < arraccessDescription.length; ++i) {
                GeneralName generalName;
                if (!arraccessDescription[i].getAccessMethod().equals(AccessDescription.id_ad_ocsp) || (generalName = arraccessDescription[i].getAccessLocation()).getTagNo() != 6) continue;
                vector.add((Object)((DERIA5String)generalName.getName()).getString());
            }
        }
        return vector;
    }

    public PolicyNode getPolicyTree() {
        this.doChecks();
        return this.policyTree;
    }

    public PublicKey getSubjectPublicKey() {
        this.doChecks();
        return this.subjectPublicKey;
    }

    public TrustAnchor getTrustAnchor() {
        this.doChecks();
        return this.trustAnchor;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected Collection getTrustAnchors(X509Certificate x509Certificate, Set set) {
        ArrayList arrayList = new ArrayList();
        Iterator iterator = set.iterator();
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PKIXCertPathReviewer.getEncodedIssuerPrincipal((Object)x509Certificate).getEncoded());
            byte[] arrby = x509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
            if (arrby != null) {
                AuthorityKeyIdentifier authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1Primitive.fromByteArray(((ASN1OctetString)ASN1Primitive.fromByteArray(arrby)).getOctets()));
                x509CertSelector.setSerialNumber(authorityKeyIdentifier.getAuthorityCertSerialNumber());
                byte[] arrby2 = authorityKeyIdentifier.getKeyIdentifier();
                if (arrby2 != null) {
                    x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(arrby2).getEncoded());
                }
            }
        }
        catch (IOException iOException) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustAnchorIssuerError"));
        }
        while (iterator.hasNext()) {
            TrustAnchor trustAnchor = (TrustAnchor)iterator.next();
            if (trustAnchor.getTrustedCert() != null) {
                if (!x509CertSelector.match((Certificate)trustAnchor.getTrustedCert())) continue;
                arrayList.add((Object)trustAnchor);
                continue;
            }
            if (trustAnchor.getCAName() == null || trustAnchor.getCAPublicKey() == null || !PKIXCertPathReviewer.getEncodedIssuerPrincipal((Object)x509Certificate).equals((Object)new X500Principal(trustAnchor.getCAName()))) continue;
            arrayList.add((Object)trustAnchor);
        }
        return arrayList;
    }

    public void init(CertPath certPath, PKIXParameters pKIXParameters) {
        if (this.initialized) {
            throw new IllegalStateException("object is already initialized!");
        }
        this.initialized = true;
        if (certPath == null) {
            throw new NullPointerException("certPath was null");
        }
        this.certPath = certPath;
        this.certs = certPath.getCertificates();
        this.n = this.certs.size();
        if (this.certs.isEmpty()) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.emptyCertPath"));
        }
        this.pkixParams = (PKIXParameters)pKIXParameters.clone();
        this.validDate = PKIXCertPathReviewer.getValidDate(this.pkixParams);
        this.notifications = null;
        this.errors = null;
        this.trustAnchor = null;
        this.subjectPublicKey = null;
        this.policyTree = null;
    }

    public boolean isValidCertPath() {
        this.doChecks();
        for (int i = 0; i < this.errors.length; ++i) {
            if (this.errors[i].isEmpty()) continue;
            return false;
        }
        return true;
    }
}

