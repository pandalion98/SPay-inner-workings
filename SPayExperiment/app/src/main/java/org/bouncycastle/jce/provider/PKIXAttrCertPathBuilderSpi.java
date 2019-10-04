/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.Principal
 *  java.security.cert.CertPathBuilderException
 *  java.security.cert.CertPathBuilderResult
 *  java.security.cert.CertPathBuilderSpi
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.Certificate
 *  java.security.cert.PKIXBuilderParameters
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Principal;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CertStoreSelector;

public class PKIXAttrCertPathBuilderSpi
extends CertPathBuilderSpi {
    private Exception certPathException;

    /*
     * Exception decompiling
     */
    private CertPathBuilderResult build(X509AttributeCertificate var1_1, X509Certificate var2_2, PKIXExtendedBuilderParameters var3_3, List var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 5[TRYBLOCK]
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

    protected static Collection findCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector, List list) {
        HashSet hashSet = new HashSet();
        for (Object object : list) {
            if (!(object instanceof Store)) continue;
            Store store = (Store)object;
            try {
                hashSet.addAll(store.getMatches(x509AttributeCertStoreSelector));
            }
            catch (StoreException storeException) {
                throw new AnnotatedException("Problem while picking certificates from X.509 store.", (Throwable)storeException);
            }
        }
        return hashSet;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CertPathBuilderResult engineBuild(CertPathParameters certPathParameters) {
        PKIXExtendedBuilderParameters pKIXExtendedBuilderParameters;
        Collection collection;
        if (!(certPathParameters instanceof PKIXBuilderParameters || certPathParameters instanceof ExtendedPKIXBuilderParameters || certPathParameters instanceof PKIXExtendedBuilderParameters)) {
            throw new InvalidAlgorithmParameterException("Parameters must be an instance of " + PKIXBuilderParameters.class.getName() + " or " + PKIXExtendedBuilderParameters.class.getName() + ".");
        }
        ArrayList arrayList = new ArrayList();
        if (certPathParameters instanceof PKIXBuilderParameters) {
            ArrayList arrayList2;
            PKIXExtendedBuilderParameters.Builder builder = new PKIXExtendedBuilderParameters.Builder((PKIXBuilderParameters)certPathParameters);
            if (certPathParameters instanceof ExtendedPKIXParameters) {
                ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)certPathParameters;
                builder.addExcludedCerts((Set<X509Certificate>)extendedPKIXBuilderParameters.getExcludedCerts());
                builder.setMaxPathLength(extendedPKIXBuilderParameters.getMaxPathLength());
                arrayList2 = extendedPKIXBuilderParameters.getStores();
            } else {
                arrayList2 = arrayList;
            }
            pKIXExtendedBuilderParameters = builder.build();
            arrayList = arrayList2;
        } else {
            pKIXExtendedBuilderParameters = (PKIXExtendedBuilderParameters)certPathParameters;
        }
        ArrayList arrayList3 = new ArrayList();
        PKIXCertStoreSelector pKIXCertStoreSelector = pKIXExtendedBuilderParameters.getBaseParameters().getTargetConstraints();
        if (!(pKIXCertStoreSelector instanceof X509AttributeCertStoreSelector)) {
            throw new CertPathBuilderException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + this.getClass().getName() + " class.");
        }
        try {
            collection = PKIXAttrCertPathBuilderSpi.findCertificates((X509AttributeCertStoreSelector)((Object)pKIXCertStoreSelector), (List)arrayList);
        }
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathBuilderException("Error finding target attribute certificate.", (Throwable)annotatedException);
        }
        if (collection.isEmpty()) {
            throw new CertPathBuilderException("No attribute certificate found matching targetContraints.");
        }
        CertPathBuilderResult certPathBuilderResult = null;
        Iterator iterator = collection.iterator();
        do {
            X509CertStoreSelector x509CertStoreSelector;
            HashSet hashSet;
            X509AttributeCertificate x509AttributeCertificate;
            Principal[] arrprincipal;
            if (iterator.hasNext() && certPathBuilderResult == null) {
                x509AttributeCertificate = (X509AttributeCertificate)iterator.next();
                x509CertStoreSelector = new X509CertStoreSelector();
                arrprincipal = x509AttributeCertificate.getIssuer().getPrincipals();
                hashSet = new HashSet();
            } else {
                if (certPathBuilderResult == null && this.certPathException != null) {
                    throw new ExtCertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
                }
                if (certPathBuilderResult == null && this.certPathException == null) {
                    throw new CertPathBuilderException("Unable to find certificate chain.");
                }
                return certPathBuilderResult;
            }
            for (int i = 0; i < arrprincipal.length; ++i) {
                try {
                    if (arrprincipal[i] instanceof X500Principal) {
                        x509CertStoreSelector.setSubject(((X500Principal)arrprincipal[i]).getEncoded());
                    }
                    PKIXCertStoreSelector<? extends Certificate> pKIXCertStoreSelector2 = new PKIXCertStoreSelector.Builder((CertSelector)x509CertStoreSelector).build();
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector2, pKIXExtendedBuilderParameters.getBaseParameters().getCertStores()));
                    hashSet.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector2, pKIXExtendedBuilderParameters.getBaseParameters().getCertificateStores()));
                    continue;
                }
                catch (AnnotatedException annotatedException) {
                    throw new ExtCertPathBuilderException("Public key certificate for attribute certificate cannot be searched.", (Throwable)annotatedException);
                }
                catch (IOException iOException) {
                    throw new ExtCertPathBuilderException("cannot encode X500Principal.", iOException);
                }
            }
            if (hashSet.isEmpty()) {
                throw new CertPathBuilderException("Public key certificate for attribute certificate cannot be found.");
            }
            Iterator iterator2 = hashSet.iterator();
            CertPathBuilderResult certPathBuilderResult2 = certPathBuilderResult;
            while (iterator2.hasNext() && certPathBuilderResult2 == null) {
                certPathBuilderResult2 = this.build(x509AttributeCertificate, (X509Certificate)iterator2.next(), pKIXExtendedBuilderParameters, (List)arrayList3);
            }
            certPathBuilderResult = certPathBuilderResult2;
        } while (true);
    }
}

