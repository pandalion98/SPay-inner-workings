/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Target;
import org.bouncycastle.asn1.x509.Targets;

public class TargetInformation
extends ASN1Object {
    private ASN1Sequence targets;

    private TargetInformation(ASN1Sequence aSN1Sequence) {
        this.targets = aSN1Sequence;
    }

    public TargetInformation(Targets targets) {
        this.targets = new DERSequence(targets);
    }

    public TargetInformation(Target[] arrtarget) {
        this(new Targets(arrtarget));
    }

    public static TargetInformation getInstance(Object object) {
        if (object instanceof TargetInformation) {
            return (TargetInformation)object;
        }
        if (object != null) {
            return new TargetInformation(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public Targets[] getTargetsObjects() {
        Targets[] arrtargets = new Targets[this.targets.size()];
        int n2 = 0;
        Enumeration enumeration = this.targets.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arrtargets[n2] = Targets.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arrtargets;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.targets;
    }
}

