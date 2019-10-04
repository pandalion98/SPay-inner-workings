/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.cert.CertPathBuilderException
 *  java.security.cert.CertPathBuilderResult
 *  java.security.cert.CertPathBuilderSpi
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertStore
 *  java.security.cert.PKIXBuilderParameters
 *  java.security.cert.PKIXParameters
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import java.security.cert.CertStore;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;
import org.bouncycastle.jcajce.PKIXExtendedBuilderParameters;
import org.bouncycastle.jcajce.PKIXExtendedParameters;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.CertPathValidatorUtilities;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PKIXCertPathBuilderSpi
extends CertPathBuilderSpi {
    private Exception certPathException;

    /*
     * Exception decompiling
     */
    protected CertPathBuilderResult build(X509Certificate var1_1, PKIXExtendedBuilderParameters var2_2, List var3_3) {
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
        if (certPathParameters instanceof PKIXBuilderParameters) {
            PKIXExtendedBuilderParameters.Builder builder;
            PKIXExtendedParameters.Builder builder2 = new PKIXExtendedParameters.Builder((PKIXParameters)((PKIXBuilderParameters)certPathParameters));
            if (certPathParameters instanceof ExtendedPKIXParameters) {
                ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)certPathParameters;
                Iterator iterator = extendedPKIXBuilderParameters.getAdditionalStores().iterator();
                while (iterator.hasNext()) {
                    builder2.addCertificateStore((PKIXCertStore)iterator.next());
                }
                builder = new PKIXExtendedBuilderParameters.Builder(builder2.build());
                builder.addExcludedCerts((Set<X509Certificate>)extendedPKIXBuilderParameters.getExcludedCerts());
                builder.setMaxPathLength(extendedPKIXBuilderParameters.getMaxPathLength());
            } else {
                builder = new PKIXExtendedBuilderParameters.Builder((PKIXBuilderParameters)certPathParameters);
            }
            pKIXExtendedBuilderParameters = builder.build();
        } else {
            pKIXExtendedBuilderParameters = (PKIXExtendedBuilderParameters)certPathParameters;
        }
        ArrayList arrayList = new ArrayList();
        PKIXCertStoreSelector pKIXCertStoreSelector = pKIXExtendedBuilderParameters.getBaseParameters().getTargetConstraints();
        try {
            collection = CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, pKIXExtendedBuilderParameters.getBaseParameters().getCertificateStores());
            collection.addAll(CertPathValidatorUtilities.findCertificates(pKIXCertStoreSelector, pKIXExtendedBuilderParameters.getBaseParameters().getCertStores()));
        }
        catch (AnnotatedException annotatedException) {
            throw new ExtCertPathBuilderException("Error finding target certificate.", (Throwable)annotatedException);
        }
        if (collection.isEmpty()) {
            throw new CertPathBuilderException("No certificate found matching targetContraints.");
        }
        CertPathBuilderResult certPathBuilderResult = null;
        Iterator iterator = collection.iterator();
        while (iterator.hasNext() && certPathBuilderResult == null) {
            certPathBuilderResult = this.build((X509Certificate)iterator.next(), pKIXExtendedBuilderParameters, (List)arrayList);
        }
        if (certPathBuilderResult == null && this.certPathException != null) {
            if (this.certPathException instanceof AnnotatedException) {
                throw new CertPathBuilderException(this.certPathException.getMessage(), this.certPathException.getCause());
            }
            throw new CertPathBuilderException("Possible certificate chain could not be validated.", (Throwable)this.certPathException);
        }
        if (certPathBuilderResult == null && this.certPathException == null) {
            throw new CertPathBuilderException("Unable to find certificate chain.");
        }
        return certPathBuilderResult;
    }
}

