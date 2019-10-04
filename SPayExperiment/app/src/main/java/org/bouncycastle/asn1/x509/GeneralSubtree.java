/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;

public class GeneralSubtree
extends ASN1Object {
    private static final BigInteger ZERO = BigInteger.valueOf((long)0L);
    private GeneralName base;
    private ASN1Integer maximum;
    private ASN1Integer minimum;

    /*
     * Exception decompiling
     */
    private GeneralSubtree(ASN1Sequence var1_1) {
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

    public GeneralSubtree(GeneralName generalName) {
        this(generalName, null, null);
    }

    public GeneralSubtree(GeneralName generalName, BigInteger bigInteger, BigInteger bigInteger2) {
        this.base = generalName;
        if (bigInteger2 != null) {
            this.maximum = new ASN1Integer(bigInteger2);
        }
        if (bigInteger == null) {
            this.minimum = null;
            return;
        }
        this.minimum = new ASN1Integer(bigInteger);
    }

    public static GeneralSubtree getInstance(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof GeneralSubtree) {
            return (GeneralSubtree)object;
        }
        return new GeneralSubtree(ASN1Sequence.getInstance(object));
    }

    public static GeneralSubtree getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return new GeneralSubtree(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public GeneralName getBase() {
        return this.base;
    }

    public BigInteger getMaximum() {
        if (this.maximum == null) {
            return null;
        }
        return this.maximum.getValue();
    }

    public BigInteger getMinimum() {
        if (this.minimum == null) {
            return ZERO;
        }
        return this.minimum.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.base);
        if (this.minimum != null && !this.minimum.getValue().equals((Object)ZERO)) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.minimum));
        }
        if (this.maximum != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.maximum));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

