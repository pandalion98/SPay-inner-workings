/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class OriginatorInfo
extends ASN1Object {
    private ASN1Set certs;
    private ASN1Set crls;

    /*
     * Exception decompiling
     */
    private OriginatorInfo(ASN1Sequence var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:478)
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:61)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:372)
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

    public OriginatorInfo(ASN1Set aSN1Set, ASN1Set aSN1Set2) {
        this.certs = aSN1Set;
        this.crls = aSN1Set2;
    }

    public static OriginatorInfo getInstance(Object object) {
        if (object instanceof OriginatorInfo) {
            return (OriginatorInfo)object;
        }
        if (object != null) {
            return new OriginatorInfo(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static OriginatorInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return OriginatorInfo.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1Set getCRLs() {
        return this.crls;
    }

    public ASN1Set getCertificates() {
        return this.certs;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.certs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.certs));
        }
        if (this.crls != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

