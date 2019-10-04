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

public class Targets
extends ASN1Object {
    private ASN1Sequence targets;

    private Targets(ASN1Sequence aSN1Sequence) {
        this.targets = aSN1Sequence;
    }

    public Targets(Target[] arrtarget) {
        this.targets = new DERSequence(arrtarget);
    }

    public static Targets getInstance(Object object) {
        if (object instanceof Targets) {
            return (Targets)object;
        }
        if (object != null) {
            return new Targets(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public Target[] getTargets() {
        Target[] arrtarget = new Target[this.targets.size()];
        int n2 = 0;
        Enumeration enumeration = this.targets.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arrtarget[n2] = Target.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arrtarget;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.targets;
    }
}

