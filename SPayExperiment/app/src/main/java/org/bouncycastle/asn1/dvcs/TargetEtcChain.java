/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.asn1.dvcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.dvcs.CertEtcToken;
import org.bouncycastle.asn1.dvcs.PathProcInput;

public class TargetEtcChain
extends ASN1Object {
    private ASN1Sequence chain;
    private PathProcInput pathProcInput;
    private CertEtcToken target;

    /*
     * Exception decompiling
     */
    private TargetEtcChain(ASN1Sequence var1_1) {
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

    public TargetEtcChain(CertEtcToken certEtcToken) {
        this(certEtcToken, null, null);
    }

    public TargetEtcChain(CertEtcToken certEtcToken, PathProcInput pathProcInput) {
        this(certEtcToken, null, pathProcInput);
    }

    public TargetEtcChain(CertEtcToken certEtcToken, CertEtcToken[] arrcertEtcToken) {
        this(certEtcToken, arrcertEtcToken, null);
    }

    public TargetEtcChain(CertEtcToken certEtcToken, CertEtcToken[] arrcertEtcToken, PathProcInput pathProcInput) {
        this.target = certEtcToken;
        if (arrcertEtcToken != null) {
            this.chain = new DERSequence(arrcertEtcToken);
        }
        this.pathProcInput = pathProcInput;
    }

    public static TargetEtcChain[] arrayFromSequence(ASN1Sequence aSN1Sequence) {
        TargetEtcChain[] arrtargetEtcChain = new TargetEtcChain[aSN1Sequence.size()];
        for (int i2 = 0; i2 != arrtargetEtcChain.length; ++i2) {
            arrtargetEtcChain[i2] = TargetEtcChain.getInstance(aSN1Sequence.getObjectAt(i2));
        }
        return arrtargetEtcChain;
    }

    public static TargetEtcChain getInstance(Object object) {
        if (object instanceof TargetEtcChain) {
            return (TargetEtcChain)object;
        }
        if (object != null) {
            return new TargetEtcChain(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static TargetEtcChain getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return TargetEtcChain.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    private void setChain(ASN1Sequence aSN1Sequence) {
        this.chain = aSN1Sequence;
    }

    private void setPathProcInput(PathProcInput pathProcInput) {
        this.pathProcInput = pathProcInput;
    }

    public CertEtcToken[] getChain() {
        if (this.chain != null) {
            return CertEtcToken.arrayFromSequence(this.chain);
        }
        return null;
    }

    public PathProcInput getPathProcInput() {
        return this.pathProcInput;
    }

    public CertEtcToken getTarget() {
        return this.target;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.target);
        if (this.chain != null) {
            aSN1EncodableVector.add(this.chain);
        }
        if (this.pathProcInput != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.pathProcInput));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("TargetEtcChain {\n");
        stringBuffer.append("target: " + this.target + "\n");
        if (this.chain != null) {
            stringBuffer.append("chain: " + this.chain + "\n");
        }
        if (this.pathProcInput != null) {
            stringBuffer.append("pathProcInput: " + this.pathProcInput + "\n");
        }
        stringBuffer.append("}\n");
        return stringBuffer.toString();
    }
}

