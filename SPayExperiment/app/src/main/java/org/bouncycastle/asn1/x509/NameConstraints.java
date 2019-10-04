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
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralSubtree;

public class NameConstraints
extends ASN1Object {
    private GeneralSubtree[] excluded;
    private GeneralSubtree[] permitted;

    private NameConstraints(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        block4 : while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    continue block4;
                }
                case 0: {
                    this.permitted = this.createArray(ASN1Sequence.getInstance(aSN1TaggedObject, false));
                    continue block4;
                }
                case 1: 
            }
            this.excluded = this.createArray(ASN1Sequence.getInstance(aSN1TaggedObject, false));
        }
    }

    public NameConstraints(GeneralSubtree[] arrgeneralSubtree, GeneralSubtree[] arrgeneralSubtree2) {
        if (arrgeneralSubtree != null) {
            this.permitted = arrgeneralSubtree;
        }
        if (arrgeneralSubtree2 != null) {
            this.excluded = arrgeneralSubtree2;
        }
    }

    private GeneralSubtree[] createArray(ASN1Sequence aSN1Sequence) {
        GeneralSubtree[] arrgeneralSubtree = new GeneralSubtree[aSN1Sequence.size()];
        for (int i2 = 0; i2 != arrgeneralSubtree.length; ++i2) {
            arrgeneralSubtree[i2] = GeneralSubtree.getInstance(aSN1Sequence.getObjectAt(i2));
        }
        return arrgeneralSubtree;
    }

    public static NameConstraints getInstance(Object object) {
        if (object instanceof NameConstraints) {
            return (NameConstraints)object;
        }
        if (object != null) {
            return new NameConstraints(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public GeneralSubtree[] getExcludedSubtrees() {
        return this.excluded;
    }

    public GeneralSubtree[] getPermittedSubtrees() {
        return this.permitted;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.permitted != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, new DERSequence(this.permitted)));
        }
        if (this.excluded != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, new DERSequence(this.excluded)));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

